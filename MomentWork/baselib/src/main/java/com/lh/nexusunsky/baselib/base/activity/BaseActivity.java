package com.lh.nexusunsky.baselib.base.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lh.nexusunsky.baselib.base.context.AppContext;
import com.lh.nexusunsky.baselib.helper.PermissionHelper;
import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.mvp.BasePresenter;
import com.lh.nexusunsky.baselib.network.EasyHttp;

/**
 * @author Nexusunsky
 */
public abstract class BaseActivity extends AppCompatActivity {

    private PermissionHelper mPermissionHelper;
    protected boolean isAppInBackground = false;
    protected BasePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("当前打开 :  " + this.getClass().getSimpleName());
        if (mPermissionHelper == null) {
            mPermissionHelper = new PermissionHelper(this);
        }
        final Intent intent = getIntent();
        if (intent != null) {
            parseIntentInfo(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.handlePermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected PermissionHelper getPermissionHelper() {
        return mPermissionHelper;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (AppContext.isAppBackground()) {
            isAppInBackground = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAppInBackground) {
            isAppInBackground = false;
        }
    }

    /**
     * run in {@link BaseActivity#onCreate(Bundle)} but before {@link AppCompatActivity#setContentView(int)}
     * <p>
     * <p>
     * 如果有intent，则需要处理这个intent（该方法在onCreate里面执行，但在setContentView之前调用）
     *
     * @param intent
     * @return false:关闭activity
     */
    public abstract void parseIntentInfo(Intent intent);

    protected <T extends View> T findView(@IdRes int id) {
        return (T) super.findViewById(id);
    }


    public Activity getActivity() {
        return BaseActivity.this;
    }

    /**
     * 隐藏状态栏
     * <p>
     * 在setContentView前调用
     */
    protected void hideStatusBar() {
        final int sdkVer = Build.VERSION.SDK_INT;
        if (sdkVer < 16) {
            //4.0及一下
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

    @Override
    public void onDestroy() {
        EasyHttp.getHttp().cancel(this);
        super.onDestroy();
        release();
        detach();
    }

    private void release() {
        if (mPermissionHelper != null) {
            mPermissionHelper.handleDestroy();
        }
        mPermissionHelper = null;
    }

    private void detach() {
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
    }
}
