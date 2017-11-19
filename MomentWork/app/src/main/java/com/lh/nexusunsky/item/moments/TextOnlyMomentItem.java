package com.lh.nexusunsky.item.moments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.lh.nexusunsky.domain.MomentsInfo;


/**
 * @author Nexusunsky
 */
public class TextOnlyMomentItem extends BaseMomentItem {
    public TextOnlyMomentItem(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
    }

    @Override
    public void diffChildItemType(@NonNull View rootView) {

    }

    @Override
    public void bindChildItemData(@NonNull MomentsInfo data, int position, int viewType) {

    }
}
