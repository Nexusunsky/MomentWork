package com.lh.nexusunsky.baselib.cache;

import com.lh.nexusunsky.baselib.base.context.AppContext;

import java.util.List;
import java.util.Map;

/**
 * @author Nexusunsky
 */
public abstract class BasePreferenceProvider {

    public abstract String getFileName();

    protected CacheWrapper mCacheWrapper;

    public BasePreferenceProvider() {
        initPreWrapper();
    }

    private void initPreWrapper() {
        mCacheWrapper = CacheWrapper.instance(AppContext.getAppContext(), getFileName());
    }

    protected final synchronized CacheWrapper prefWrapper() {
        return mCacheWrapper;
    }

    public void putString(String key, String value) {
        if (prefWrapper() == null) {
            return;
        }
        prefWrapper().putString(key, value);
    }

    public String getString(String key, String defaultValue) {
        if (prefWrapper() == null) {
            return defaultValue;
        }
        return prefWrapper().getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        if (prefWrapper() == null) {
            return;
        }
        prefWrapper().putInt(key, value);
    }

    public int getInt(String key, int defaultValue) {
        if (prefWrapper() == null) {
            return defaultValue;
        }
        return prefWrapper().getInt(key, defaultValue);
    }

    public void putLong(String key, long defaultValue) {
        if (prefWrapper() == null) {
            return;
        }
        prefWrapper().putLong(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        if (prefWrapper() == null) {
            return defaultValue;
        }
        return prefWrapper().getLong(key, defaultValue);
    }

    public void putBoolean(String key, boolean defaultValue) {
        if (prefWrapper() == null) {
            return;
        }
        prefWrapper().putBoolean(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (prefWrapper() == null) {
            return defaultValue;
        }
        return prefWrapper().getBoolean(key, defaultValue);
    }

    public void remove(String key) {
        if (prefWrapper() == null) {
            return;
        }
        prefWrapper().removeKey(key);
    }

    public void removeList(List<String> keyList) {
        if (prefWrapper() == null) {
            return;
        }
        prefWrapper().removeKeyList(keyList);
    }

    public Map<String, ? extends Object> getAll() {
        if (prefWrapper() == null) {
            return null;
        }
        return prefWrapper().getAll();
    }

    public void clear() {
        if (prefWrapper() == null) {
            return;
        }
        prefWrapper().clear();
    }
}
