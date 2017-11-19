package com.lh.nexusunsky.baselib.ui.widget.photo.adapter.observer;

import java.util.ArrayList;

/**
 * @author Nexusunsky
 */
public abstract class PhotoImageObservable<T> {

    protected final ArrayList<T> mObservers = new ArrayList<T>();

    public void registerObserver(T observer) {
        if (observer == null) {
            throw new IllegalArgumentException("观察者为空");
        }
        synchronized (mObservers) {
            if (mObservers.contains(observer)) {
                throw new IllegalStateException("观察者已经存在");
            }
            mObservers.add(observer);
        }
    }

    public void unregisterObserver(T observer) {
        if (observer == null) {
            throw new IllegalArgumentException("观察者为空");
        }
        synchronized (mObservers) {
            int index = mObservers.indexOf(observer);
            if (index == -1) {
                throw new IllegalStateException("观察者已经存在");
            }
            mObservers.remove(index);
        }
    }

    public void unregisterAll() {
        synchronized (mObservers) {
            mObservers.clear();
        }
    }
}
