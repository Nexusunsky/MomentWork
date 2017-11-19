package com.lh.nexusunsky.activity;

import android.content.Intent;
import android.os.Bundle;

import com.lh.nexusunsky.adapter.MomentsAdapter;
import com.lh.nexusunsky.baselib.base.activity.NavigateActivity;
import com.lh.nexusunsky.baselib.base.context.AppContext;
import com.lh.nexusunsky.baselib.ui.widget.NavigationBar;
import com.lh.nexusunsky.baselib.ui.widget.pullrecyclerview.MomentLayout;
import com.lh.nexusunsky.baselib.ui.widget.pullrecyclerview.listener.OnInteractListener;
import com.lh.nexusunsky.baselib.ui.widget.pullrecyclerview.mode.Mode;
import com.lh.nexusunsky.baselib.utils.MessageHelper;
import com.lh.nexusunsky.domain.MineInfo;
import com.lh.nexusunsky.domain.MomentsInfo;
import com.lh.nexusunsky.impl.MomentPresenter;
import com.lh.nexusunsky.item.moments.EmptyMomentItem;
import com.lh.nexusunsky.item.HostItem;
import com.lh.nexusunsky.item.moments.MomentItem;
import com.lh.nexusunsky.item.moments.MultiTypeMomentsItem;
import com.lh.nexusunsky.item.moments.TextOnlyMomentItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nexusunsky
 */
public class MomentIndexActivity extends NavigateActivity implements MomentPresenter.MomentView, OnInteractListener {

    private static final String TAG = MomentIndexActivity.class.getSimpleName();
    private static final int MIN_INTERVAL = 2000;
    private MomentLayout layoutMoment;
    private HostItem mHostItem;
    private MomentsAdapter adapter;
    private long lastClickBackTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attach();
    }

    private void attach() {
        presenter = new MomentPresenter(MomentIndexActivity.this);
    }

    private void setNavigation() {
        setTitle(getString(R.string.moment_title));
        setTitleMode(NavigationBar.MODE_BOTH);
        setTitleRightIcon(R.drawable.ic_camera);
        setTitleLeftText(getString(R.string.moment_left));
        setTitleLeftIcon(R.drawable.back_left);
    }

    @Override
    public void onTitleDoubleClick() {
        super.onTitleDoubleClick();
        if (layoutMoment != null) {
            int firstVisibleItemPos = layoutMoment.findFirstVisibleItemPosition();
            layoutMoment.getRecyclerView().smoothScrollToPosition(0);
            if (firstVisibleItemPos > 1) {
                layoutMoment.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutMoment.autoRefresh();
                    }
                }, 200);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastClickBackTime > MIN_INTERVAL) {
            MessageHelper.showMessage(getString(R.string.quite_tips));
            lastClickBackTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void initView() {
        setNavigation();
        mHostItem = new HostItem(this);
        layoutMoment = (MomentLayout) findViewById(R.id.rv_moment);
        layoutMoment.setInteractListener(this);
        layoutMoment.addHeaderView(mHostItem.getView());
        MomentsAdapter.Builder<MomentsInfo> builder = new MomentsAdapter.Builder<>(this);
        builder.setData(new ArrayList<MomentsInfo>())
                .addType(EmptyMomentItem.class, MomentItem.Type.EMPTY_CONTENT, R.layout.moments_empty_content)
                .addType(MultiTypeMomentsItem.class, MomentItem.Type.MULTI_IMAGES, R.layout.moments_multi_image)
                .addType(TextOnlyMomentItem.class, MomentItem.Type.TEXT_ONLY, R.layout.moments_only_text);
        adapter = builder.build();
        layoutMoment.setAdapter(adapter);
        layoutMoment.autoRefresh();
    }

    @Override
    public void onRefresh() {
        presenter.presenting();
    }

    @Override
    public void onLoadMore() {
        ((MomentPresenter) presenter).loadMore();
    }

    @Override
    public void refreshMineInfo(MineInfo info) {
        mHostItem.loadMineInfo(info);
    }

    @Override
    public void refreshMoments(List<MomentsInfo> infos) {
        loadDataComplete();
        adapter.updateData(infos);
        layoutMoment.setMode(Mode.BOTH);
    }

    @Override
    public void loadMomentsCache(List<MomentsInfo> infos) {
        adapter.updateData(infos);
    }

    @Override
    public void loadMoreMoments(List<MomentsInfo> infos) {
        loadDataComplete();
        adapter.addDatas(infos);
    }

    @Override
    public void loadDataComplete() {
        layoutMoment.compelete();
    }

    @Override
    public void loadDataFinished() {
        MessageHelper.showMessage(getString(R.string.double_click));
        loadDataComplete();
        AppContext.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutMoment.setMode(Mode.REFRESH);
            }
        }, MomentLayout.MID_DELAY_MILLIS);
    }

    @Override
    public void parseIntentInfo(Intent intent) {
        //Empty Body
    }

    @Override
    public void initListener() {
        //Empty Body
    }

    @Override
    public void bindDataToView(Object data) {
        //Empty Body
    }

    @Override
    public void showLoading() {
        //Empty Body
    }

    @Override
    public void dismissLoading() {
        //Empty Body
    }

    @Override
    public void showError(String e) {
        //Empty Body
    }

    @Override
    public void showEmpty() {
        //Empty Body
    }
}
