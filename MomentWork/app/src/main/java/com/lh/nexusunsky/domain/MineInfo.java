package com.lh.nexusunsky.domain;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lh.nexusunsky.baselib.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nexusunsky
 */
public class MineInfo {
    private Map<String, String> infoPair;

    public MineInfo(String jsonPair) {
        infoPair = new HashMap<>();
        final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        final Map<String, String> pair = gson.fromJson(jsonPair, new TypeToken<Map<String, String>>() {
        }.getType());
        infoPair.putAll(pair);
    }

    private interface KeyConstant {
        String PROFILE_IMAGE = "profile-image";
        String AVATAR = "avatar";
        String NICK = "nick";
        String USERNAME = "username";
    }

    public String getProfileImage() {
        return parseValue(KeyConstant.PROFILE_IMAGE);
    }

    public String getNick() {
        return parseValue(KeyConstant.NICK);
    }

    public String getAvatar() {
        return parseValue(KeyConstant.AVATAR);
    }

    public String getUsername() {
        return parseValue(KeyConstant.USERNAME);
    }

    @NonNull
    private String parseValue(final String key) {
        final String result = infoPair.get(key);
        if (infoPair.size() == 0 || !StringUtil.noEmpty(result)) {
            return "";
        }
        return result;
    }
}
