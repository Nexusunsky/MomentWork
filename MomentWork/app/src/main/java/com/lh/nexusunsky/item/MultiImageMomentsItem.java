package com.lh.nexusunsky.item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lh.nexusunsky.activity.R;
import com.lh.nexusunsky.baselib.helper.imageloader.ImageLoadManager;
import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.ui.widget.ForceClickImageView;
import com.lh.nexusunsky.baselib.ui.widget.photo.PhotoContents;
import com.lh.nexusunsky.baselib.ui.widget.photo.adapter.PhotoContentsBaseAdapter;
import com.lh.nexusunsky.domain.ImagesBean;
import com.lh.nexusunsky.domain.MomentsInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Nexusunsky
 */
public class MultiImageMomentsItem extends BaseMomentItem {

    private PhotoContents imageContainer;
    private InnerContainerAdapter adapter;

    public MultiImageMomentsItem(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
    }

    @Override
    public void diffChildItemType(@NonNull View rootView) {
        imageContainer = (PhotoContents) findView(imageContainer, R.id.circle_image_container);
    }

    @Override
    public void bindChildItemData(@NonNull MomentsInfo data, int position, int viewType) {
        final List<String> urls = getPhotoUrls(data);
        if (adapter == null) {
            adapter = new InnerContainerAdapter(getContext(), urls);
            imageContainer.setAdapter(adapter);
        } else {
            Logger.i("update image" + data.getSender().getNick() + " : " + urls.size());
            adapter.updateData(urls);
        }
    }

    @NonNull
    private List<String> getPhotoUrls(@NonNull MomentsInfo data) {
        final List<ImagesBean> images = data.getImages();
        final List<String> urls = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            urls.add(images.get(i).getUrl());
        }
        return urls;
    }

    private static class InnerContainerAdapter extends PhotoContentsBaseAdapter {
        private Context context;
        private List<String> datas;

        InnerContainerAdapter(Context context, List<String> datas) {
            this.context = context;
            this.datas = new ArrayList<>();
            this.datas.addAll(datas);
        }

        @Override
        public ImageView onCreateView(ImageView convertView, ViewGroup parent, int position) {
            if (convertView == null) {
                convertView = new ForceClickImageView(context);
                convertView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            return convertView;
        }

        @Override
        public void onBindData(int position, @NonNull ImageView convertView) {
            ImageLoadManager.INSTANCE.loadImage(convertView, datas.get(position));
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        public void updateData(List<String> datas) {
            this.datas.clear();
            this.datas.addAll(datas);
            notifyDataChanged();
        }
    }
}
