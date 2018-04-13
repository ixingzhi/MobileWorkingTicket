package com.shichuang.mobileworkingticket.entify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/3/22.
 */

public class PersonalDetails {
    private PersonalDetailsModel userRow;

    public PersonalDetailsModel getUserRow() {
        return userRow;
    }

    public void setUserRow(PersonalDetailsModel userRow) {
        this.userRow = userRow;
    }

    public static class PersonalDetailsModel{
        private int id;
        @SerializedName("nick_name")
        private String nickName;
        @SerializedName("head_portrait")
        private String headPortrait;
        @SerializedName("phone_num")
        private String phoneNum;
        @SerializedName("position_name")
        private String positionName;
        @SerializedName("dept_name")
        private String deptName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadPortrait() {
            return headPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            this.headPortrait = headPortrait;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }
    }
}
