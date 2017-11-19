package com.lh.nexusunsky.item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.lh.nexusunsky.domain.MomentsInfo;


/**
 * @author Nexusunsky
 */
public class EmptyMomentItem extends BaseMomentItem {

    public EmptyMomentItem(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
    }

    @Override
    public void diffChildItemType(@NonNull View rootView) {

    }

    @Override
    public void bindChildItemData(@NonNull MomentsInfo data, int position, int viewType) {

    }
}
