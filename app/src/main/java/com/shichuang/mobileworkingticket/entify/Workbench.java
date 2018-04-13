package com.shichuang.mobileworkingticket.entify;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2018/2/27.
 */

public class Workbench implements MultiItemEntity {
    public static final int HEADER = 0X11;
    public static final int BODY = 0X12;

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
