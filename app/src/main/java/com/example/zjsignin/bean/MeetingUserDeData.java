package com.example.zjsignin.bean;

import java.io.Serializable;

/**
 * author : LiuJie
 * date   : 2023/2/240:16
 */

public class MeetingUserDeData implements Serializable {

private String name = "";
private String corporateName = "";
private String avatar = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
