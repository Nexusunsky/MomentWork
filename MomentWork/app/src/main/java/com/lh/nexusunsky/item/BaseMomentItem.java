package com.lh.nexusunsky.item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lh.nexusunsky.activity.R;
import com.lh.nexusunsky.baselib.base.adapter.BaseRecyclerViewHolder;
import com.lh.nexusunsky.baselib.base.context.AppContext;
import com.lh.nexusunsky.baselib.helper.imageloader.ImageLoadManager;
import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.ui.widget.ExpandInfoOnClickLayout;
import com.lh.nexusunsky.baselib.utils.StringUtil;
import com.lh.nexusunsky.baselib.utils.UIHelper;
import com.lh.nexusunsky.baselib.utils.ViewUtil;
import com.lh.nexusunsky.domain.CommentsBean;
import com.lh.nexusunsky.domain.MomentsInfo;
import com.lh.nexusunsky.domain.SenderBean;
import com.lh.nexusunsky.impl.MomentPresenter;
import com.lh.nexusunsky.ui.CommentText;

import java.util.List;


/**
 * @author Nexusunsky
 */
public abstract class BaseMomentItem extends BaseRecyclerViewHolder<MomentsInfo> implements MomentItem<MomentsInfo> {
    private static final int VERTICAL_OFFSET = UIHelper.dipToPx(8f);
    private static final int HORIZON_OFFSET = UIHelper.dipToPx(3f);

    /**
     * Header
     */
    private ImageView ivAvatar;
    private TextView tvNick;
    private ExpandInfoOnClickLayout exLayoutMoment;

    /**
     * Footer
     */
    private View line;
    private LinearLayout commentRoot;
    private LinearLayout eachComment;

    /**
     * Content
     */
    private LinearLayout contentLayout;

    private MomentPresenter momentPresenter;
    private int itemPosition;
    private MomentsInfo momentsInfo;

    public void setPresenter(MomentPresenter momentPresenter) {
        this.momentPresenter = momentPresenter;
    }

    public MomentPresenter getPresenter() {
        return momentPresenter;
    }

    public BaseMomentItem(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
        diffChildItemType(itemView);
        initCommonHeader();
        initCommonBottom();
        initCommontContent();
    }

    private void initCommontContent() {
        contentLayout = (LinearLayout) findView(contentLayout, R.id.content);
    }

    private void initCommonBottom() {
        line = findView(line, R.id.divider);
        commentRoot = (LinearLayout) findView(commentRoot, R.id.comment_praise_layout);
        eachComment = (LinearLayout) findView(eachComment, R.id.comment_layout);
    }

    private void initCommonHeader() {
        ivAvatar = (ImageView) findView(ivAvatar, R.id.avatar);
        tvNick = (TextView) findView(tvNick, R.id.nick);
        exLayoutMoment = (ExpandInfoOnClickLayout) findView(exLayoutMoment, R.id.item_text_field);
        exLayoutMoment.setOnStateKeyGenerateListener(new ExpandInfoOnClickLayout.OnStateKeyGenerateListener() {
            @Override
            public int onStateKeyGenerated(int originKey) {
                return originKey + itemPosition;
            }
        });
    }

    @Override
    public void onBindData(MomentsInfo data, int position) {
        if (data == null) {
            Logger.e("Empty data");
            findView(exLayoutMoment, R.id.item_text_field);
            exLayoutMoment.setText("Empty data");
            return;
        }
        this.momentsInfo = data;
        this.itemPosition = position;
        bindCommonData(data);
        bindChildItemData(data, position, getViewType());
    }

    private void bindCommonData(MomentsInfo info) {
        setCommonHeader(info);
        setCommonBottom(info);
    }

    private void setCommonBottom(MomentsInfo info) {
        boolean needCommentData = setComment(info.getComments());
        eachComment.setVisibility(needCommentData ? View.VISIBLE : View.GONE);
        line.setVisibility(needCommentData ? View.VISIBLE : View.GONE);
        commentRoot.setVisibility(needCommentData ? View.VISIBLE : View.GONE);
    }

    private void setCommonHeader(MomentsInfo info) {
        final SenderBean sender = info.getSender();
        if (sender != null) {
            ImageLoadManager.INSTANCE.loadImage(ivAvatar, sender.getAvatar());
            tvNick.setText(sender.getNick());
        }
        final boolean noEmpty = StringUtil.noEmpty(info.getContent());
        ViewUtil.setViewsVisible(noEmpty ? View.VISIBLE : View.GONE, exLayoutMoment);
        if (noEmpty) {
            exLayoutMoment.setText(info.getContent());
        }
    }

    private boolean setComment(List<CommentsBean> commentList) {
        if (commentList == null || commentList.isEmpty()) {
            return false;
        }
        final int childCount = eachComment.getChildCount();
        if (childCount < commentList.size()) {
            int subCount = commentList.size() - childCount;
            for (int i = 0; i < subCount; i++) {
                CommentText comment = new CommentText(getContext());
                comment.setPadding(VERTICAL_OFFSET, HORIZON_OFFSET, VERTICAL_OFFSET, HORIZON_OFFSET);
                comment.setLineSpacing(4, 1);
                comment.setBackgroundDrawable(AppContext.getResource().getDrawable(R.drawable.common_selector));
                eachComment.addView(comment);
            }
        } else if (childCount > commentList.size()) {
            eachComment.removeViews(commentList.size(), childCount - commentList.size());
        }
        for (int n = 0; n < commentList.size(); n++) {
            CommentText commentWidget = (CommentText) eachComment.getChildAt(n);
            if (commentWidget != null) {
                commentWidget.setCommentText(commentList.get(n));
            }
        }
        return true;
    }

    final View findView(View view, int resid) {
        if (resid > 0 && itemView != null && view == null) {
            return itemView.findViewById(resid);
        }
        return view;
    }
}
