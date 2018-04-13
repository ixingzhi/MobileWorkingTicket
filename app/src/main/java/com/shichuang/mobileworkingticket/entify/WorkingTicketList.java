package com.shichuang.mobileworkingticket.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23.
 */

public class WorkingTicketList {
    private List<WorkingTicketListModel> rows;
    private int recordCount;

    public List<WorkingTicketListModel> getRows() {
        return rows;
    }

    public void setRows(List<WorkingTicketListModel> rows) {
        this.rows = rows;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public static class WorkingTicketListModel {
        private int id;
        @SerializedName("work_ticket_num")
        private String workTicketNum;
        @SerializedName("work_order_no")
        private String workOrderNo;
        @SerializedName("product_drawing_no")
        private String productDrawingNo;
        private String component;
        @SerializedName("parts_drawing_no")
        private String partsDrawingNo;
        @SerializedName("parts_name")
        private String partsName;
        private int priority;   // 优先级 1=高 2=中 3=低
        @SerializedName("process_state")
        private int processState;  // 工票工序状态 1=待激活，2=领料确认 ，3=分配组员，4=生产作业,5=质量检查，6=已完成
        @SerializedName("add_time")
        private String addTime;
        @SerializedName("release_user_name")
        private String releaseUserName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getWorkTicketNum() {
            return workTicketNum;
        }

        public void setWorkTicketNum(String workTicketNum) {
            this.workTicketNum = workTicketNum;
        }

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

        public String getPartsDrawingNo() {
            return partsDrawingNo;
        }

        public void setPartsDrawingNo(String partsDrawingNo) {
            this.partsDrawingNo = partsDrawingNo;
        }

        public String getPartsName() {
            return partsName;
        }

        public void setPartsName(String partsName) {
            this.partsName = partsName;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getProcessState() {
            return processState;
        }

        public void setProcessState(int processState) {
            this.processState = processState;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getReleaseUserName() {
            return releaseUserName;
        }

        public void setReleaseUserName(String releaseUserName) {
            this.releaseUserName = releaseUserName;
        }
    }
}
