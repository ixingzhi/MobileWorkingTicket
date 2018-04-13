package com.shichuang.mobileworkingticket.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class DepartmentList {
    public List<Department> deptRows;
    public List<DepartmentUser> userRows;

    public static class Department {
        private int id;
        private String name;
        @SerializedName("user_count")
        private int userCount;
        @SerializedName("has_child")
        private int hasChild;  // 是否有下级部门 >0就是有 =0没有

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUserCount() {
            return userCount;
        }

        public void setUserCount(int userCount) {
            this.userCount = userCount;
        }

        public int getHasChild() {
            return hasChild;
        }

        public void setHasChild(int hasChild) {
            this.hasChild = hasChild;
        }
    }

    public static class DepartmentUser {
        private int id;
        @SerializedName("phone_num")
        private String phoneNum;
        @SerializedName("nick_name")
        private String nickName;
        @SerializedName("head_portrait")
        private String headPortrait;
        @SerializedName("position_name")
        private String positionName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
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

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }
    }
}
