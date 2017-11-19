package com.lh.nexusunsky.baselib.base.adapter;

import android.view.View;

/**
 * @author Nexusunsky
 */
public interface OnRecyclerViewLongItemClickListener<T> {
    boolean onItemLongClick(View v, int position, T data);
}
