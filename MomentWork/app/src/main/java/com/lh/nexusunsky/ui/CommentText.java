package com.lh.nexusunsky.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.domain.CommentsBean;


/**
 * @author Nexusunsky
 */
public class CommentText extends TextView {
    private static final String TAG = CommentText.class.getSimpleName();
    private static final int TEXT_SIZE = 14;
    private static final String SAID = " . said : ";

    public CommentText(Context context) {
        this(context, null);
    }

    public CommentText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMovementMethod(LinkMovementMethod.getInstance());
        this.setHighlightColor(0x00000000);
        setTextSize(TEXT_SIZE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommentText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setMovementMethod(LinkMovementMethod.getInstance());
        this.setHighlightColor(0x00000000);
        setTextSize(TEXT_SIZE);
    }

    public void setCommentText(CommentsBean info) {
        if (info == null) {
            return;
        }
        try {
            setTag(info);
            final String content = info.getSender().getNick() + SAID + info.getContent();
            setText(content);
        } catch (NullPointerException e) {
            Logger.e(TAG, e);
        }
    }
}
