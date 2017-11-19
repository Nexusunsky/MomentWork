package com.lh.nexusunsky.baselib.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lh.nexusunsky.baselib.R;
import com.lh.nexusunsky.baselib.log.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nexusunsky
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder<T>> {

    private static final String TAG = "BaseRecyclerViewAdapter";
    protected Context context;
    protected List<T> datas;
    protected LayoutInflater mInflater;

    private OnRecyclerViewItemClickListener<T> itemClick;
    private OnRecyclerViewLongItemClickListener<T> itemLongClick;

    public BaseRecyclerViewAdapter(@NonNull Context context, @NonNull List<T> datas) {
        this.context = context;
        this.datas = new ArrayList<>(datas);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position, datas.get(position));
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder holder;
        if (getLayoutResId(viewType) != 0) {
            View rootView = mInflater.inflate(getLayoutResId(viewType), parent, false);
            holder = getViewHolder(parent, rootView, viewType);
        } else {
            holder = getViewHolder(parent, null, viewType);
        }
        setUpItemEvent(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        T data = datas.get(position);
        holder.itemView.setTag(R.id.recycler_view_tag, data);
        holder.onBindData(data, position);
        bindHolderWithData(holder, data, position);
    }

    private void setUpItemEvent(final BaseRecyclerViewHolder holder) {
        if (itemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这个获取位置的方法，防止添加删除导致位置不变
                    int layoutPosition = holder.getAdapterPosition();
                    itemClick.onItemClick(holder.itemView, layoutPosition, datas.get(layoutPosition));
                }
            });
        }
        if (itemLongClick != null) {
            //longclick
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int layoutPosition = holder.getAdapterPosition();
                    itemLongClick.onItemLongClick(holder.itemView, layoutPosition, datas.get(layoutPosition));
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void updateData(List<T> datas) {
        if (this.datas != null) {
            this.datas.clear();
            this.datas.addAll(datas);
        } else {
            this.datas = datas;
        }
        notifyDataSetChanged();
    }

    public void addDatas(List<T> datas) {
        if (!datas.isEmpty()) {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public List<T> getDatas() {
        return datas;
    }

    public void addDataInPosition(int pos, @NonNull T data) {
        if (datas != null) {
            datas.add(pos, data);
            notifyItemInserted(pos);
        }
    }

    public void addData(@NonNull T data) {
        if (datas != null) {
            datas.add(data);
            notifyItemInserted(datas.size() - 1);
        }
    }

    public void deleteDataInPosition(int pos) {
        if (datas != null && datas.size() > pos) {
            datas.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public T findDataInPosition(int pos) {
        if (pos < 0 || pos > datas.size()) {
            Log.e(TAG, "这个position他喵咪的太强大了，我hold不住");
            return null;
        }
        return datas.get(pos);
    }

    protected abstract int getViewType(int position, @NonNull T data);

    protected abstract int getLayoutResId(int viewType);

    protected abstract BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType);

    /**
     * 提供子类扩展业务
     */
    protected void bindHolderWithData(BaseRecyclerViewHolder<T> holder, T data, int position) {
        Logger.d(this.getClass().toString());
    }

    public OnRecyclerViewItemClickListener<T> getItemClick() {
        return itemClick;
    }

    public void setItemClick(OnRecyclerViewItemClickListener<T> itemClick) {
        this.itemClick = itemClick;
    }

    public OnRecyclerViewLongItemClickListener<T> getItemLongClick() {
        return itemLongClick;
    }

    public void setItemLongClick(OnRecyclerViewLongItemClickListener<T> itemLongClick) {
        this.itemLongClick = itemLongClick;
    }
}
