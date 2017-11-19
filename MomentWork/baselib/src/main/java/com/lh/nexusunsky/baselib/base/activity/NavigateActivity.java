package com.lh.nexusunsky.baselib.base.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.lh.nexusunsky.baselib.R;
import com.lh.nexusunsky.baselib.ui.listener.MultiClickListener;
import com.lh.nexusunsky.baselib.ui.widget.NavigationBar;


/**
 * @author Nexusunsky
 */
public abstract class NavigateActivity extends BaseActivity {
    protected NavigationBar mNavigationBar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initNavigation();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initNavigation();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initNavigation();
    }

    private void initNavigation() {
        if (mNavigationBar == null) {
            mNavigationBar = (NavigationBar) findViewById(R.id.title_bar_view);
        }
        if (mNavigationBar != null) {
            mNavigationBar.setOnClickListener(new MultiClickListener() {
                @Override
                public void onSingleClick() {
                    onTitleSingleClick();
                }

                @Override
                public void onDoubleClick() {
                    onTitleDoubleClick();
                }
            });
            mNavigationBar.setOnTitleBarClickListener(onTitleClickListener);
        }
    }

    private NavigationBar.OnTitleBarClickListener onTitleClickListener = new NavigationBar.OnTitleBarClickListener() {

        @Override
        public boolean onLeftClick(View v, boolean isLongClick) {
            if (!isLongClick) {
                onTitleLeftClick();
                return false;
            } else {
                return onTitleLeftLongClick();
            }
        }

        @Override
        public boolean onRightClick(View v, boolean isLongClick) {
            if (!isLongClick) {
                onTitleRightClick();
                return false;
            } else {
                return onTitleRightLongClick();
            }
        }
    };

    public boolean onTitleLeftLongClick() {
        return false;
    }

    public boolean onTitleRightLongClick() {
        return false;
    }

    public void onTitleLeftClick() {
        finish();
    }

    public void onTitleRightClick() {
    }

    public void onTitleDoubleClick() {
    }

    public void onTitleSingleClick() {
    }

    @Override
    public void setTitle(int resId) {
        if (mNavigationBar != null && resId != 0) {
            mNavigationBar.setTitle(resId);
        }
    }

    public void setTitle(String title) {
        if (mNavigationBar != null && !TextUtils.isEmpty(title)) {
            mNavigationBar.setTvTitle(title);
        }
    }

    public void setTitleMode(@NavigationBar.TitleBarMode int mode) {
        if (mNavigationBar != null) {
            mNavigationBar.setTitleBarMode(mode);
        }
    }

    public void setTitleRightText(String text) {
        if (mNavigationBar != null) {
            mNavigationBar.setRightText(text);
        }
    }

    public void setTitleRightIcon(int resid) {
        if (mNavigationBar != null) {
            mNavigationBar.setRightIcon(resid);
        }
    }

    public void setTitleLeftText(String text) {
        if (mNavigationBar != null) {
            mNavigationBar.setLeftText(text);
        }
    }

    public void setTitleLeftIcon(int resid) {
        if (mNavigationBar != null) {
            mNavigationBar.setLeftIcon(resid);
        }
    }

    public void setLeftTextColor(int color) {
        if (mNavigationBar != null) {
            mNavigationBar.setLeftTextColor(color);
        }
    }

    public void setRightTextColor(int color) {
        if (mNavigationBar != null) {
            mNavigationBar.setRightTextColor(color);
        }
    }

    public void setTitleBarBackground(int color) {
        if (mNavigationBar != null) {
            mNavigationBar.setTitleBarBackground(color);
        }
    }


    public String getBarTitle() {
        if (mNavigationBar != null) {
            return mNavigationBar.getTitleView().getText().toString();
        }
        return null;
    }
}