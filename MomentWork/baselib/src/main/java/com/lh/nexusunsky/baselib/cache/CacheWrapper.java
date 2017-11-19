package com.lh.nexusunsky.baselib.cache;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.lh.nexusunsky.baselib.base.context.AppContext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nexusunsky
 */
public class CacheWrapper {
    private static final String TAG = CacheWrapper.class.getSimpleName();
    private static LinkedHashMap<String, CacheWrapper> preWrappers = new LinkedHashMap<String, CacheWrapper>();
    private static final String NEXUSUNSKY_SP = "nexusunsky_sp";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private CacheWrapper(final Context context, String preferenceName) {
        mPreferences = context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public static CacheWrapper defaultWrapper() {
        return instance(AppContext.getAppInstance(), NEXUSUNSKY_SP);
    }

    public static CacheWrapper instance(final Context context, String preferenceName) {
        if (context == null) {
            return null;
        }
        CacheWrapper wrapper = preWrappers.get(preferenceName);
        if (wrapper != null) {
            return wrapper;
        }

        synchronized (TAG) {
            if (null == wrapper) {
                wrapper = new CacheWrapper(context, preferenceName);
                preWrappers.put(preferenceName, wrapper);
            }
        }

        return wrapper;
    }

    public Map<String, ?> getAll() {
        return mPreferences.getAll();
    }

    public boolean contains(String key) {
        return mPreferences.contains(key);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return mPreferences.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return mPreferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return mPreferences.getLong(key, defValue);
    }

    public String getString(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

    public void putBoolean(String key, boolean b) {
        mEditor.putBoolean(key, b);
        mEditor.apply();
    }

    public void putInt(String key, int i) {
        mEditor.putInt(key, i);
        mEditor.apply();
    }

    public void putFloat(String key, float f) {
        mEditor.putFloat(key, f);
        mEditor.apply();
    }

    public void putLong(String key, long l) {
        mEditor.putLong(key, l);
        mEditor.apply();
    }

    public void putString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public void removeKey(String key) {
        mEditor.remove(key);
        mEditor.apply();
    }

    public void removeKeyList(List<String> keyList) {
        if (keyList == null || keyList.isEmpty()) {
            return;
        }
        for (String key : keyList) {
            mEditor.remove(key);
        }
        mEditor.commit();
    }

    public void clear() {
        mEditor.clear();
    }
}
