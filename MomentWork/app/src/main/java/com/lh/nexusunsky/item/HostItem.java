package com.lh.nexusunsky.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lh.nexusunsky.activity.R;
import com.lh.nexusunsky.baselib.helper.imageloader.ImageLoadManager;
import com.lh.nexusunsky.domain.MineInfo;

/**
 * @author Nexusunsky
 */
public class HostItem {
    private View vRoot;
    private ImageView ivWall;
    private ImageView ivAvatar;
    private TextView tvUsername;

    public HostItem(Context context) {
        this.vRoot = LayoutInflater.from(context).inflate(R.layout.circle_host_header, null);
        this.tvUsername = (TextView) vRoot.findViewById(R.id.host_id);
        this.ivWall = (ImageView) vRoot.findViewById(R.id.friend_wall_pic);
        this.ivAvatar = (ImageView) vRoot.findViewById(R.id.friend_avatar);
    }

    public void loadMineInfo(MineInfo hostInfo) {
        if (hostInfo == null) {
            return;
        }
        ImageLoadManager.INSTANCE.loadImage(ivWall, hostInfo.getProfileImage());
        ImageLoadManager.INSTANCE.loadImage(ivAvatar, hostInfo.getAvatar());
        tvUsername.setText("UserName :" + hostInfo.getUsername() + '\n' + "NickName :" + hostInfo.getNick());
    }

    public View getView() {
        return vRoot;
    }
}