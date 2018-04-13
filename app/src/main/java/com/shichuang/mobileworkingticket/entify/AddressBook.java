package com.shichuang.mobileworkingticket.entify;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class AddressBook {
    private List<AddressBookModel> userRows;

    public List<AddressBookModel> getUserRows() {
        return userRows;
    }

    public void setUserRows(List<AddressBookModel> userRows) {
        this.userRows = userRows;
    }

    public static class AddressBookModel implements MultiItemEntity {
        public static final int LETTER = 0x11;
        public static final int USER = 0x12;

        public AddressBookModel(){
        }

        public AddressBookModel(int itemType) {
            this.itemType = itemType;
        }

        private int itemType;
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
        @SerializedName("is_equal_dept")
        private int isEqualDept;   // 是否本部门 1=本部门 2=不是本部门
        private String index;  // 首字母索引

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public static int getLETTER() {
            return LETTER;
        }

        public static int getUSER() {
            return USER;
        }

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

        public int getIsEqualDept() {
            return isEqualDept;
        }

        public void setIsEqualDept(int isEqualDept) {
            this.isEqualDept = isEqualDept;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }
    }
}
