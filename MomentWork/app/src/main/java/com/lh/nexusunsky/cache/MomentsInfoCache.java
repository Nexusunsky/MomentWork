package com.lh.nexusunsky.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lh.nexusunsky.baselib.cache.BasePreferenceProvider;
import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.utils.StringUtil;
import com.lh.nexusunsky.domain.MineInfo;
import com.lh.nexusunsky.domain.MomentsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nexusunsky
 */
public class MomentsInfoCache extends BasePreferenceProvider {

    private static final String DEF_VALUE = "";
    private static final String MOMENT_INFO = "moment_info";
    private static final String MINE_INFO = "mine_info";

    public void saveMomentInfo(String momentInfos) {
        prefWrapper().putString(MOMENT_INFO, momentInfos);
    }

    private String getMomentInfo() {
        return prefWrapper().getString(MOMENT_INFO, DEF_VALUE);
    }

    public List<MomentsInfo> loadMomentsInfoCache() {
        final String jsonString = getMomentInfo();
        List<MomentsInfo> infoList = null;
        if (jsonString.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            infoList = new Gson().fromJson(jsonString, new TypeToken<List<MomentsInfo>>() {
            }.getType());
        } catch (Exception e) {
            Logger.e(this.getClass().toString(), e);
        }
        return infoList;
    }

    public void saveMineInfo(String mineInfo) {
        prefWrapper().putString(MINE_INFO, mineInfo);
    }

    private String getMineInfo() {
        return prefWrapper().getString(MINE_INFO, DEF_VALUE);
    }

    public MineInfo loadMineInfoCache() {
        final String mineInfo = getMineInfo();
        if (!StringUtil.noEmpty(mineInfo)) {
            return null;
        }
        return new MineInfo(mineInfo);
    }

    @Override
    public String getFileName() {
        return MomentsInfoCache.class.getSimpleName().toLowerCase();
    }
}
