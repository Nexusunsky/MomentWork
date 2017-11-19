package com.lh.nexusunsky.baselib.base.adapter;

import android.view.View;

/**
 * @author Nexusunsky
 */
public interface OnRecyclerViewItemClickListener<T> {
    void onItemClick(View v, int position, T data);
}
