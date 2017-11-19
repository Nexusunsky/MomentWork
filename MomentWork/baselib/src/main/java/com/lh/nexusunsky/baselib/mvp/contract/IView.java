package com.lh.nexusunsky.baselib.mvp.contract;

import android.support.annotation.UiThread;

/**
 * @author Nexusunsky
 */
public interface IView<M> {

    @UiThread
    void initView();

    @UiThread
    void initListener();

    @UiThread
    void bindDataToView(M data);

    @UiThread
    void showLoading();

    @UiThread
    void dismissLoading();

    @UiThread
    void showError(String e);

    @UiThread
    void showEmpty();
}
