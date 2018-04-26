package com.shichuang.mobileworkingticket.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class PartsByProductList {
    private List<PartsByProductModel> rows;
    private int recordCount;

    public List<PartsByProductModel> getRows() {
        return rows;
    }

    public void setRows(List<PartsByProductModel> rows) {
        this.rows = rows;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public static class PartsByProductModel{
        @SerializedName("work_order_no")
        private String workOrderNo;
        @SerializedName("product_drawing_no")
        private String productDrawingNo;
        private String component;
        @SerializedName("finish_count")
        private double finishCount;
        @SerializedName("all_count")
        private double allCount;

        public String getWorkOrderNo() {
            return workOrderNo;
        }

        public void setWorkOrderNo(String workOrderNo) {
            this.workOrderNo = workOrderNo;
        }

        public String getProductDrawingNo() {
            return productDrawingNo;
        }

        public void setProductDrawingNo(String productDrawingNo) {
            this.productDrawingNo = productDrawingNo;
        }

        public String getComponent() {
            return component;
        }

        public void setComponent(String component) {
            this.component = component;
        }

        public double getFinishCount() {
            return finishCount;
        }

        public void setFinishCount(double finishCount) {
            this.finishCount = finishCount;
        }

        public double getAllCount() {
            return allCount;
        }

        public void setAllCount(double allCount) {
            this.allCount = allCount;
        }
    }
}
