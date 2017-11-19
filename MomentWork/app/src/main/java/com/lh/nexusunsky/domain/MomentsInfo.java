package com.lh.nexusunsky.domain;

import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.utils.StringUtil;
import com.lh.nexusunsky.item.MomentItem;

import java.util.List;

/**
 * @author: Nexusunsky
 */
public class MomentsInfo {
    private String content;
    private SenderBean sender;
    private List<ImagesBean> images;
    private List<CommentsBean> comments;

    public int getMomentType() {
        if (!StringUtil.noEmpty(content) && sender == null && images == null && comments == null) {
            Logger.e("MomentsInfo Error !");
            return MomentItem.Type.EMPTY_CONTENT;
        }

        if (images != null && !images.isEmpty()) {
            return MomentItem.Type.MULTI_IMAGES;
        }
        return MomentItem.Type.TEXT_ONLY;
    }

    public String getContent() {
        return getResult(this.content);
    }

    public SenderBean getSender() {
        return sender;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    private String getResult(String input) {
        if (StringUtil.noEmpty(input)) {
            return input;
        }
        return "";
    }
}
