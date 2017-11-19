package com.lh.nexusunsky.item;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * @author Nexusunsky
 * @created at   17/11/19 上午1:55
 */
public interface MomentItem<T> {

    void diffChildItemType(@NonNull View rootView);

    void bindChildItemData(@NonNull final T data, int position, int viewType);

    interface Type {
        int EMPTY_CONTENT = 0;
        int TEXT_ONLY = 1;
        int MULTI_IMAGES = 2;
    }
}
