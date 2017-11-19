package com.lh.nexusunsky.baselib.ui.widget;

import android.content.Context;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lh.nexusunsky.baselib.R;
import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.utils.UIHelper;
import com.lh.nexusunsky.baselib.utils.ViewUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * @author Nexusunsky
 */
public class NavigationBar extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {

    private LinearLayout llLeft;
    private ImageView ivLeft;
    private TextView txLeft;
    private LinearLayout llRight;
    private ImageView ivRight;
    private TextView txRight;
    private TextView tvTitle;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_LEFT, MODE_RIGHT, MODE_BOTH, MODE_TITLE})
    public @interface TitleBarMode {
    }

    public static final int MODE_LEFT = 0x11;
    public static final int MODE_RIGHT = 0x12;
    public static final int MODE_BOTH = 0x13;
    public static final int MODE_TITLE = 0x14;

    private int currentMode = MODE_LEFT;

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        View.inflate(context, R.layout.widget_title_bar, this);
        llLeft = (LinearLayout) findViewById(R.id.ll_title_bar_left);
        ivLeft = (ImageView) findViewById(R.id.ic_title_bar_left);
        txLeft = (TextView) findViewById(R.id.tx_title_bar_left);

        llRight = (LinearLayout) findViewById(R.id.ll_title_bar_right);
        ivRight = (ImageView) findViewById(R.id.ic_title_bar_right);
        txRight = (TextView) findViewById(R.id.tx_title_bar_right);

        tvTitle = (TextView) findViewById(R.id.tx_title);
        this.setBackgroundColor(UIHelper.getResourceColor(R.color.action_bar_bg));

        llRight.setOnClickListener(null);
        llRight.setVisibility(INVISIBLE);
        llLeft.setOnClickListener(this);

    }

    public int getTitleBarMode() {
        return currentMode;
    }

    public void setTitleBarMode(@TitleBarMode int currentMode) {
        if (this.currentMode == currentMode)
            return;
        this.currentMode = currentMode;
        switch (currentMode) {
            case MODE_LEFT:
                llLeft.setVisibility(VISIBLE);
                llRight.setOnClickListener(null);
                llRight.setOnLongClickListener(null);
                llRight.setVisibility(INVISIBLE);
                llLeft.setOnClickListener(this);
                llLeft.setOnLongClickListener(this);
                break;
            case MODE_RIGHT:
                llRight.setVisibility(VISIBLE);
                llLeft.setOnClickListener(null);
                llLeft.setOnLongClickListener(null);
                llLeft.setVisibility(INVISIBLE);
                llRight.setOnClickListener(this);
                llRight.setOnLongClickListener(this);
                break;
            case MODE_BOTH:
                llLeft.setVisibility(VISIBLE);
                llRight.setVisibility(VISIBLE);
                ViewUtil.setViewsClickListener(this, llLeft, llRight);
                llLeft.setOnLongClickListener(this);
                llRight.setOnLongClickListener(this);
                break;
            case MODE_TITLE:
                llLeft.setVisibility(GONE);
                llRight.setVisibility(GONE);
                llLeft.setOnClickListener(null);
                llRight.setOnClickListener(null);
                break;
            default:
                break;
        }
    }

    public void setTitleBarBackground(int color) {
        if (color != -1) {
            this.setBackgroundColor(color);
        }
    }

    public TextView getTitleView() {
        return tvTitle;
    }

    public void setTvTitle(String titleStr) {
        if (TextUtils.isEmpty(titleStr)) {
            tvTitle.setText("");
        } else {
            tvTitle.setText(titleStr);
        }
    }

    public void setTitle(int stringResid) {
        if (stringResid > 0) {
            tvTitle.setText(stringResid);
        }
    }

    public void setLeftIcon(int resid) {
        try {
            ivLeft.setImageResource(resid);
            setShowLeftIcon(resid != 0);
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    public void setRightIcon(int resid) {
        try {
            ivRight.setImageResource(resid);
            setShowRightIcon(resid != 0);
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    public void setLeftText(int resid) {
        if (resid > 0) {
            txLeft.setText(resid);
        }
    }

    public void setLeftText(String leftText) {
        if (TextUtils.isEmpty(leftText)) {
            txLeft.setText("");
            txLeft.setVisibility(GONE);
        } else {
            txLeft.setText(leftText);
        }
    }

    public void setLeftTextColor(int color) {
        txLeft.setTextColor(color);
    }

    public void setRightTextColor(int color) {
        txRight.setTextColor(color);
    }


    public void setRightText(int resid) {
        if (resid > 0) {
            if (txRight.getVisibility() != VISIBLE) {
                txRight.setVisibility(VISIBLE);
            }
            txRight.setText(resid);
        }
    }

    public void setRightText(String leftText) {
        if (TextUtils.isEmpty(leftText)) {
            txRight.setText("");
            txRight.setVisibility(GONE);
        } else {
            if (txRight.getVisibility() != VISIBLE) {
                txRight.setVisibility(VISIBLE);
            }
            txRight.setText(leftText);
        }
    }

    public void setShowLeftIcon(boolean isShow) {
        ivLeft.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setShowRightIcon(boolean isShow) {
        ivRight.setVisibility(isShow ? VISIBLE : GONE);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_bar_left) {
            if (onTitleBarClickListener != null) {
                onTitleBarClickListener.onLeftClick(llLeft, false);
            }

        } else if (i == R.id.ll_title_bar_right) {
            if (onTitleBarClickListener != null) {
                onTitleBarClickListener.onRightClick(llLeft, false);
            }

        }
    }


    @Override
    public boolean onLongClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_bar_left) {
            if (onTitleBarClickListener != null) {
                return onTitleBarClickListener.onLeftClick(llLeft, true);
            }

        } else if (i == R.id.ll_title_bar_right) {
            if (onTitleBarClickListener != null) {
                return onTitleBarClickListener.onRightClick(llLeft, true);
            }

        }
        return false;
    }

    private OnTitleBarClickListener onTitleBarClickListener;

    public OnTitleBarClickListener getOnTitleBarClickListener() {
        return onTitleBarClickListener;
    }

    public void setOnTitleBarClickListener(OnTitleBarClickListener onTitleBarClickListener) {
        this.onTitleBarClickListener = onTitleBarClickListener;
    }

    public interface OnTitleBarClickListener {
        boolean onLeftClick(View v, boolean isLongClick);

        boolean onRightClick(View v, boolean isLongClick);
    }
}
