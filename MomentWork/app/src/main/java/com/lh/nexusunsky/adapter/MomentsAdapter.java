package com.lh.nexusunsky.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.lh.nexusunsky.baselib.base.adapter.BaseRecyclerViewAdapter;
import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.domain.MomentsInfo;
import com.lh.nexusunsky.impl.MomentPresenter;
import com.lh.nexusunsky.item.moments.BaseMomentItem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nexusunsky
 */
public class MomentsAdapter extends BaseRecyclerViewAdapter<MomentsInfo> {

    private SparseArray<ItemType> typeArray;
    private MomentPresenter momentPresenter;

    private MomentsAdapter(@NonNull Context context, @NonNull List<MomentsInfo> datas) {
        super(context, datas);
    }

    private MomentsAdapter(Builder builder) {
        this(builder.context, builder.datas);
        this.typeArray = builder.typeArray;
        this.momentPresenter = builder.momentPresenter;
    }

    @Override
    protected int getViewType(int position, @NonNull MomentsInfo data) {
        return data.getMomentType();
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return 0;
    }

    @Override
    protected BaseMomentItem getViewHolder(ViewGroup parent, View rootView, int viewType) {
        ItemType itemType = typeArray.get(viewType);
        if (itemType != null) {
            BaseMomentItem item = createCircleViewHolder(context, parent, itemType);
            if (item != null) {
                item.setPresenter(momentPresenter);
            }
            return item;
        }
        return null;
    }


    public static final class Builder<T> {
        private Context context;
        private SparseArray<ItemType> typeArray = new SparseArray<>();
        private List<T> datas;
        private MomentPresenter momentPresenter;

        public Builder(Context context) {
            this.context = context;
            datas = new ArrayList<>();
        }

        public Builder<T> addType(Class<? extends BaseMomentItem> aClass, int viewType, int layoutResId) {
            final ItemType info = new ItemType();
            info.holderClass = aClass;
            info.viewType = viewType;
            info.layoutResID = layoutResId;
            typeArray.put(viewType, info);
            return this;
        }

        public Builder<T> setData(List<T> datas) {
            this.datas = datas;
            return this;
        }

        public Builder<T> setPresenter(MomentPresenter presenter) {
            this.momentPresenter = presenter;
            return this;
        }

        @NonNull
        public MomentsAdapter build() {
            return new MomentsAdapter(this);
        }
    }

    /**
     * vh的信息类
     */
    private static final class ItemType {
        Class<? extends BaseMomentItem> holderClass;
        int viewType;
        int layoutResID;
    }

    @Nullable
    private BaseMomentItem createCircleViewHolder(Context context, ViewGroup viewGroup, ItemType info) {
        if (info == null) {
            throw new NullPointerException("No Item Match!");
        }
        Class<? extends BaseMomentItem> className = info.holderClass;
        Logger.i("class  >>>  " + className);
        Constructor constructor = null;
        try {
            constructor = className.getConstructor(Context.class, ViewGroup.class, int.class);
            return (BaseMomentItem) constructor.newInstance(context, viewGroup, info.layoutResID);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException
                | NullPointerException e) {
            Logger.e(e);
        }
        return null;
    }
}
