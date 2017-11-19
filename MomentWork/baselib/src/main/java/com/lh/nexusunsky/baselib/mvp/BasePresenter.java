package com.lh.nexusunsky.baselib.mvp;

import android.support.annotation.UiThread;

import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.mvp.contract.IModel;
import com.lh.nexusunsky.baselib.mvp.contract.IView;

import java.lang.ref.WeakReference;

/**
 * @author Nexusunsky
 */
public abstract class BasePresenter<V extends IView, M extends IModel> {
    private static final String TAG = BasePresenter.class.getSimpleName();
    protected M dataModel;
    private WeakReference<V> vHolder;

    public BasePresenter(V view) {
        attachView(view);
        initHost();
        bindDataSource();
    }

    protected abstract void bindDataSource();

    public abstract void presenting();

    @UiThread
    public void attachView(V view) {
        Logger.d("#attachView", toString());
        vHolder = new WeakReference<>(view);
    }

    private void initHost() {
        final V viewRoot = getView();
        if (viewRoot != null) {
            viewRoot.initView();
            viewRoot.initListener();
        }
    }

    @UiThread
    public void detachView() {
        Logger.d("#detachView", toString());
        if (vHolder == null) {
            return;
        }
        vHolder.clear();
        vHolder = null;
    }

    public boolean isViewAttached() {
        Logger.d("#isViewAttached", toString());
        return getView() != null;
    }

    private V getView() {
        Logger.d("#getView", toString());
        if (vHolder != null) {
            return vHolder.get();
        }
        return null;
    }

    protected V checkThenGet() {
        try {
            checkViewAttached();
        } catch (ViewNotAttachedException e) {
            Logger.e(TAG, e);
        }
        return getView();
    }

    private void checkViewAttached() throws ViewNotAttachedException {
        if (!isViewAttached()) {
            throw new ViewNotAttachedException();
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    private static class ViewNotAttachedException extends Exception {
        private ViewNotAttachedException() {
            super("The IPresenter`s attached View(IView) is losing, When requesting data to the IPresenter");
        }
    }
}