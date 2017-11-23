package com.lh.nexusunsky.baselib.ui.widget.pullrecyclerview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lh.nexusunsky.baselib.R;
import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.ui.widget.pullrecyclerview.listener.OnInteractListener;
import com.lh.nexusunsky.baselib.ui.widget.pullrecyclerview.mode.Mode;
import com.lh.nexusunsky.baselib.ui.widget.pullrecyclerview.mode.PullMode;
import com.lh.nexusunsky.baselib.ui.widget.pullrecyclerview.wrapperadapter.FixedViewInfo;
import com.lh.nexusunsky.baselib.ui.widget.pullrecyclerview.wrapperadapter.HeaderViewWrapperAdapter;
import com.lh.nexusunsky.baselib.utils.AnimUtils;
import com.lh.nexusunsky.baselib.utils.UIHelper;
import com.lh.nexusunsky.baselib.utils.ViewOffsetHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollStateListener;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;

import static com.lh.nexusunsky.baselib.ui.widget.pullrecyclerview.MomentLayout.Status.DEFAULT;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_BOUNCE_BACK;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_END_SIDE;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_START_SIDE;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_IDLE;

/**
 * 专为朋友圈项目定制的下拉recyclerview
 *
 * @author Nexusunsky
 */
public class MomentLayout extends FrameLayout {
    private static final String TAG = MomentLayout.class.getSimpleName();
    private static final int LEFT_MARGIN = UIHelper.dipToPx(12);
    private static final int REFRESH_POSITION = UIHelper.dipToPx(90);
    private static final int DELAY_MILLIS = 1000;
    public static final int MID_DELAY_MILLIS = 1500;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Status.DEFAULT, Status.REFRESHING})
    @interface Status {
        int DEFAULT = 0;
        int REFRESHING = 1;
    }

    @Status
    private int currentStatus;

    private PullMode pullMode;

    private Mode mode = Mode.BOTH;

    private InnerRefreshIconObserver iconObserver;

    private OnInteractListener mInteractListener;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ImageView refreshIcon;
    private PullRefreshFooter footerView;

    private boolean canPull;
    private boolean canLoadMore;


    public MomentLayout(Context context) {
        this(context, null);
    }

    public MomentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MomentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }
        GradientDrawable background = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new
                int[]{0xff323232, 0xff323232, 0xffffffff, 0xffffffff});
        setBackground(background);

        if (recyclerView == null) {
            recyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.view_recyclerview, this, false);
            recyclerView.setBackgroundColor(Color.WHITE);
            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        recyclerView.setItemAnimator(null);
        if (refreshIcon == null) {
            refreshIcon = new ImageView(context);
            refreshIcon.setBackgroundColor(Color.TRANSPARENT);
            refreshIcon.setImageResource(R.drawable.rotate_icon);
        }
        LayoutParams param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.leftMargin = LEFT_MARGIN;
        addView(recyclerView, RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
        addView(refreshIcon, param);
        iconObserver = new InnerRefreshIconObserver(refreshIcon, REFRESH_POSITION);

        footerView = new PullRefreshFooter(getContext());
        addFooterView(footerView);

        setMode(Mode.BOTH);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 2) {
            throw new IllegalStateException("CHILD COUNT MUST BE LESS THAN TWO!!!");
        }
        super.onFinishInflate();
    }

    private void setCurrentStatus(@Status int status) {
        this.currentStatus = status;
    }

    public void complete() {
        Logger.i(TAG, "complete");
        if (pullMode == PullMode.FROM_START && iconObserver != null) {
            iconObserver.catchResetEvent();
        }
        if (pullMode == PullMode.FROM_BOTTOM && footerView != null) {
            footerView.onFinish();
        }
        setCurrentStatus(DEFAULT);
    }

    public void autoRefresh() {
        if (!canRefresh() || iconObserver == null || mInteractListener == null) {
            return;
        }
        setPullMode(PullMode.FROM_START);
        setCurrentStatus(Status.REFRESHING);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                iconObserver.autoAnimateRefresh();
                mInteractListener.onRefresh();
            }
        }, DELAY_MILLIS);
    }

    boolean canRefresh() {
        return canPull && currentStatus != Status.REFRESHING && (mode == Mode.REFRESH || mode == Mode.BOTH);
    }

    boolean canLoadMore() {
        return canLoadMore && currentStatus != Status.REFRESHING && (mode == Mode.LOADMORE || mode == Mode.BOTH);
    }

    private void setPullMode(PullMode pullMode) {
        this.pullMode = pullMode;
    }

    //------------------------------------------get/set-----------------------------------------------
    public void setInteractListener(OnInteractListener interactListener) {
        this.mInteractListener = interactListener;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            if (!mHeaderViewInfos.isEmpty() || !mFooterViewInfos.isEmpty()) {
                recyclerView.setAdapter(wrapHeaderInternal(adapter, mHeaderViewInfos, mFooterViewInfos));
            } else {
                recyclerView.setAdapter(adapter);
            }
        }
        initOverScroll();
    }

    public void setCanPull(boolean canPull) {
        this.canPull = canPull;
    }

    public void setLoadMoreEnable(boolean canLoadMore) {
        if (recyclerView == null) {
            return;
        }
        this.canLoadMore = canLoadMore;
        if (canLoadMore) {
            recyclerView.addOnScrollListener(onScrollListener);
        } else {
            footerView.onFinish();
            recyclerView.removeOnScrollListener(onScrollListener);
        }
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        switch (mode) {
            case REFRESH:
                setCanPull(true);
                setLoadMoreEnable(false);
                break;
            case BOTH:
                setCanPull(true);
                setLoadMoreEnable(true);
                break;
            case LOADMORE:
                setCanPull(false);
                setLoadMoreEnable(true);
                break;
            default:
                break;
        }
    }


    private void initOverScroll() {
        IOverScrollDecor decor = new VerticalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(
                recyclerView), 2f, 1f, 2f);
        decor.setOverScrollStateListener(new IOverScrollStateListener() {
            @Override
            public void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState) {
                switch (newState) {
                    case STATE_IDLE:
                        // No over-scroll is in effect.
                        break;
                    case STATE_DRAG_START_SIDE:
                        // Dragging started at the left-end.
                        break;
                    case STATE_DRAG_END_SIDE:
                        // Dragging started at the right-end.
                        Logger.i("refreshState", "current state  >>>   " + currentStatus
                                + "   refresh mode  >>>   " + pullMode);
                        break;
                    case STATE_BOUNCE_BACK:
                        if (oldState == STATE_DRAG_START_SIDE) {
                            // Dragging stopped -- view is starting to bounce back from the *left-end* onto natural
                            // position.       d
                        } else { // i.e. (oldState == STATE_DRAG_END_SIDE)
                            // View is starting to bounce back from the *right-end*.
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        decor.setOverScrollUpdateListener(new IOverScrollUpdateListener() {
            @Override
            public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset) {
                Logger.i(TAG, "offset：" + offset);
                if (offset > 0) {
                    if (!canRefresh()) {
                        return;
                    }
                    if (offset >= REFRESH_POSITION && state == STATE_BOUNCE_BACK && currentStatus != Status.REFRESHING) {
                        setCurrentStatus(Status.REFRESHING);
                        if (mInteractListener != null) {
                            Logger.i(TAG, "onRefresh");
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mInteractListener.onRefresh();
                                }
                            }, DELAY_MILLIS);
                        }
                        setPullMode(PullMode.FROM_START);
                        iconObserver.catchRefreshEvent();
                    } else {
                        iconObserver.catchPullEvent(offset);
                    }
                } else if (offset < 0) {
                    //底部的overscroll
                }
            }
        });
    }

    /**
     * 判断recyclerview是否滑到底部
     * <p>
     * 原理：判断滑过的距离加上屏幕上的显示的区域是否比整个控件高度高
     */
    public boolean isScrollToBottom() {
        return recyclerView != null &&
                recyclerView.computeVerticalScrollExtent() +
                        recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
    }

    public int findFirstVisibleItemPosition() {
        return linearLayoutManager.findFirstVisibleItemPosition();
    }

    /**
     * scroll listener
     */
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && isScrollToBottom() && currentStatus != Status.REFRESHING) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mInteractListener.onLoadMore();
                    }
                }, DELAY_MILLIS);
                Logger.i("loadmoretag", "loadmore");
                pullMode = PullMode.FROM_BOTTOM;
                setCurrentStatus(Status.REFRESHING);
                footerView.onRefreshing();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isScrollToBottom() && canLoadMore()) {
                Logger.i("loadmoretag", "loadmore");
                setPullMode(PullMode.FROM_BOTTOM);
                setCurrentStatus(Status.REFRESHING);
                footerView.onRefreshing();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mInteractListener.onLoadMore();
                    }
                }, DELAY_MILLIS);
            }
        }
    };


    /**
     * 刷新Icon的动作观察者
     */
    private static class InnerRefreshIconObserver {
        private ViewOffsetHelper viewOffsetHelper;
        private ImageView refreshIcon;
        private final int refreshPosition;
        private RotateAnimation rotateAnimation;

        InnerRefreshIconObserver(ImageView refreshIcon, int refreshPosition) {
            this.refreshIcon = refreshIcon;
            this.refreshPosition = refreshPosition;

            viewOffsetHelper = new ViewOffsetHelper(refreshIcon);

            rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation
                    .RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(1000);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            rotateAnimation.setFillBefore(true);

        }

        void catchPullEvent(float offset) {
            float result = offset;
            Logger.i(TAG, " catchPullEvent " + result);
            if (checkHacIcon()) {
                refreshIcon.setRotation(-result * 2);
                if (result >= refreshPosition) {
                    result = refreshPosition;
                }
                viewOffsetHelper.absoluteOffsetTopAndBottom((int) result);
                adjustRefreshIconPosition();
            }
        }

        /**
         * 调整icon的位置界限
         */
        private void adjustRefreshIconPosition() {
            final float refreshIconY = refreshIcon.getY();
            final int top = refreshIcon.getTop();
            Logger.i(TAG, " adjustRefreshIconPosition Y " + refreshIconY);
            Logger.i(TAG, " adjustRefreshIconPosition Top " + top);
            if (refreshIconY < 0) {
                refreshIcon.offsetTopAndBottom(Math.abs(top));
                Logger.i(TAG, " offsetTopAndBottom " + Math.abs(top));
            } else if (refreshIconY > refreshPosition) {
                refreshIcon.offsetTopAndBottom(-(top - refreshPosition));
                Logger.i(TAG, " offsetTopAndBottom " + -(top - refreshPosition));
            }
        }

        void catchRefreshEvent() {
            if (checkHacIcon()) {
                refreshIcon.clearAnimation();
                final int refreshIconTop = refreshIcon.getTop();
                Logger.i(TAG, " refreshIcon.getTop() : " + refreshIconTop + "\n" + "refreshPosition" + refreshPosition);
                if (refreshIconTop < refreshPosition) {
                    viewOffsetHelper.absoluteOffsetTopAndBottom(refreshPosition);
                }
                refreshIcon.startAnimation(rotateAnimation);
            }
        }

        void catchResetEvent() {
            Logger.i("refreshTop", " top  >>>  " + refreshIcon.getTop());
            final ValueAnimator aFloat = ValueAnimator.ofFloat(refreshPosition, 0);
            aFloat.setInterpolator(new LinearInterpolator());
            aFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float result = (float) animation.getAnimatedValue();
                    Logger.i(" animation.getAnimatedValue() " + result);
                    catchPullEvent(result);
                }
            });
            aFloat.addListener(new AnimUtils.SimpleAnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    refreshIcon.clearAnimation();
                }
            });
            aFloat.setDuration(500);

            refreshIcon.post(new Runnable() {
                @Override
                public void run() {
                    aFloat.start();
                }
            });
        }

        private boolean checkHacIcon() {
            return refreshIcon != null;
        }

        void autoAnimateRefresh() {
            final ValueAnimator animator = ValueAnimator.ofFloat(0, refreshPosition);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float result = (float) animation.getAnimatedValue();
                    catchPullEvent(result);
                }
            });
            animator.addListener(new AnimUtils.SimpleAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    catchRefreshEvent();
                }
            });
            refreshIcon.post(new Runnable() {
                @Override
                public void run() {
                    animator.start();
                }
            });
        }
    }


    /**
     * 以下为recyclerview 的headeradapter实现方案
     * <p>
     * 以Listview的headerView和footerView为模板做出的recyclerview的header和footer
     */
    private ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    private ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<>();

    /**
     * 不完美解决方法：添加一个header，则从-2开始减1
     * header:-2~-98
     */
    private static final int ITEM_VIEW_TYPE_HEADER_START = -2;
    /**
     * 不完美解决方法：添加一个header，则从-99开始减1
     * footer:-99~-无穷
     */
    private static final int ITEM_VIEW_TYPE_FOOTER_START = -99;

    public void addHeaderView(View headerView) {
        final FixedViewInfo info = new FixedViewInfo(headerView, ITEM_VIEW_TYPE_HEADER_START - mHeaderViewInfos.size());
        if (mHeaderViewInfos.size() == Math.abs(ITEM_VIEW_TYPE_FOOTER_START - ITEM_VIEW_TYPE_HEADER_START)) {
            mHeaderViewInfos.remove(mHeaderViewInfos.size() - 1);
        }
        if (checkFixedViewInfoNotAdded(info, mHeaderViewInfos)) {
            mHeaderViewInfos.add(info);
        }
        checkAndNotifyWrappedViewAdd(recyclerView.getAdapter(), info, true);

    }

    private void checkAndNotifyWrappedViewAdd(RecyclerView.Adapter adapter, FixedViewInfo info, boolean isHeader) {
        if (adapter != null && !(adapter instanceof HeaderViewWrapperAdapter)) {
            adapter = wrapHeaderInternal(adapter);
            if (isHeader) {
                adapter.notifyItemInserted(((HeaderViewWrapperAdapter) adapter).findHeaderPosition(info.view));
            } else {
                adapter.notifyItemInserted(((HeaderViewWrapperAdapter) adapter).findFooterPosition(info.view));
            }
        }
    }

    public void addFooterView(View footerView) {
        final FixedViewInfo info = new FixedViewInfo(footerView, ITEM_VIEW_TYPE_FOOTER_START - mFooterViewInfos.size());
        if (checkFixedViewInfoNotAdded(info, mFooterViewInfos)) {
            mFooterViewInfos.add(info);
        }
        checkAndNotifyWrappedViewAdd(recyclerView.getAdapter(), info, false);
    }

    private boolean checkFixedViewInfoNotAdded(FixedViewInfo info, List<FixedViewInfo> infoList) {
        boolean result = true;
        if (info == null || infoList.isEmpty()) {
            result = true;
        } else {
            for (FixedViewInfo fixedViewInfo : infoList) {
                if (fixedViewInfo.view == info.view) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public int getHeaderViewCount() {
        return mHeaderViewInfos.size();
    }

    public int getFooterViewCount() {
        return mFooterViewInfos.size();
    }


    protected HeaderViewWrapperAdapter wrapHeaderInternal(@NonNull RecyclerView.Adapter mWrappedAdapter,
                                                          ArrayList<FixedViewInfo> mHeaderViewInfos,
                                                          ArrayList<FixedViewInfo> mFooterViewInfos) {
        return new HeaderViewWrapperAdapter(recyclerView, mWrappedAdapter, mHeaderViewInfos, mFooterViewInfos);
    }

    protected HeaderViewWrapperAdapter wrapHeaderInternal(@NonNull RecyclerView.Adapter mWrappedAdapter) {
        return wrapHeaderInternal(mWrappedAdapter, mHeaderViewInfos, mFooterViewInfos);
    }
}
