package com.shichuang.mobileworkingticket.entify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/3/21.
 */

public class Token {
    private String token;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("type_of_work")
    private int typeWork; //工种 计划员=1，调度员=2，发料员=3，工序组长=4，检验员=5，工序组员=6，发运员=7

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTypeWork() {
        return typeWork;
    }

    public void setTypeWork(int typeWork) {
        this.typeWork = typeWork;
    }
}
