package com.lh.nexusunsky.baselib.helper.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.lh.nexusunsky.baselib.R;
import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.utils.AppFilePathUtil;


/**
 * @author Nexusunsky
 */
public class GlobalGlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //解决setTag问题
        ViewTarget.setTagId(R.id.glide_tag_id);
        //磁盘缓存
        builder.setDiskCache(new DiskLruCacheFactory(AppFilePathUtil.getAppCachePath(), 50 * 1024 * 1024));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        //内存缓存
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        //设置比默认大小大1.5倍的缓存和图片池大小
        int customMemoryCacheSize = (int) (1.5 * defaultMemoryCacheSize);
        int customBitmapPoolSize = defaultBitmapPoolSize;


        Logger.i("poolSize", "bitmapPoolSize >>>>>   "
                + android.text.format.Formatter.formatFileSize(context, customBitmapPoolSize)
                + "          memorySize>>>>>>>>   " +
                android.text.format.Formatter.formatFileSize(context, customMemoryCacheSize));

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
