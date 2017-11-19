package com.lh.nexusunsky.domain;

import com.lh.nexusunsky.baselib.utils.StringUtil;

/**
 * ${TODO}
 *
 * @author: Nexusunsky on 17/11/19 00:12
 * @note:
 */
public class SenderBean {
    private String username;
    private String nick;
    private String avatar;

    public String getUsername() {
        return getResult(this.username);
    }

    public String getNick() {
        return getResult(this.nick);
    }

    public String getAvatar() {
        return getResult(this.avatar);
    }

    private String getResult(String input) {
        if (StringUtil.noEmpty(input)) {
            return input;
        }
        return "";
    }
}
