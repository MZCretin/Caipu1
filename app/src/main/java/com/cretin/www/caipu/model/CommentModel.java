package com.cretin.www.caipu.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by cretin on 2017/3/21.
 */

public class CommentModel extends BmobObject{
    private UserModel user;
    private String caipuId;
    private String content;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getCaipuId() {
        return caipuId;
    }

    public void setCaipuId(String caipuId) {
        this.caipuId = caipuId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
