package com.shichuang.mobileworkingticket.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23.
 */

public class WorkingTicketDetails {
    private List<WorkFlowModels> workFlowModels;
    private List<MemberRosModel> memberRos;
    private TicketRowModel ticketRow;
    private List<CauseAnalysisModel> causeAnalysisModels;

    public List<WorkFlowModels> getWorkFlowModels() {
        return workFlowModels;
    }

    public void setWorkFlowModels(List<WorkFlowModels> workFlowModels) {
        this.workFlowModels = workFlowModels;
    }

    public List<MemberRosModel> getMemberRos() {
        return memberRos;
    }

    public void setMemberRos(List<MemberRosModel> memberRos) {
        this.memberRos = memberRos;
    }

    public TicketRowModel getTicketRow() {
        return ticketRow;
    }

    public void setTicketRow(TicketRowModel ticketRow) {
        this.ticketRow = ticketRow;
    }

    public List<CauseAnalysisModel> getCauseAnalysisModels() {
        return causeAnalysisModels;
    }

    public void setCauseAnalysisModels(List<CauseAnalysisModel> causeAnalysisModels) {
        this.causeAnalysisModels = causeAnalysisModels;
    }

    public static class WorkFlowModels {
        private int id;
        @SerializedName("ticket_id")
        private int ticketId;
        @SerializedName("flow_content")
        private String flowContent;
        @SerializedName("add_time")
        private String addTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTicketId() {
            return ticketId;
        }

        public void setTicketId(int ticketId) {
            this.ticketId = ticketId;
        }

        public String getFlowContent() {
            return flowContent;
        }

        public void setFlowContent(String flowContent) {
            this.flowContent = flowContent;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }
    }

    public static class MemberRosModel {
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
        @SerializedName("user_type_of_work")
        private int userTypeOfWork;  // 计划员=1，调度员=2，发料员=3，工序组长=4，检验员=5，工序组员=6，发运员=7
        @SerializedName("process_name")
        private String processName;

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

        public int getUserTypeOfWork() {
            return userTypeOfWork;
        }

        public void setUserTypeOfWork(int userTypeOfWork) {
            this.userTypeOfWork = userTypeOfWork;
        }

        public String getProcessName() {
            return processName;
        }

        public void setProcessName(String processName) {
            this.processName = processName;
        }
    }

    public static class TicketRowModel {
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
        @SerializedName("brand_no")
        private String brandNo;
        private String specifications;
        @SerializedName("overall_dimensions")
        private String overallDimensions;
        @SerializedName("parts_name")
        private String partsName;
        @SerializedName("each_number")
        private int eachNumber;
        @SerializedName("plan_number")
        private int planNumber;
        @SerializedName("complete_count")
        private int completeCount;
        private int priority;    // 优先级 1=紧急 2=普通
        @SerializedName("work_ticket_state")
        private String workTicketState;
        @SerializedName("reference_drawings")
        private String referenceDrawings;
        @SerializedName("add_time")
        private String addTime;
        @SerializedName("workshop_name")
        private String workshopName;
        @SerializedName("operation_display")
        private int operationDisplay;  // 操作显示 2=发料 3=分配 4=生产作业未开始 5=生产作业进行中 6=质量检查
        @SerializedName("process_state")
        private String processState;
        @SerializedName("process_name")
        private String processName;
        @SerializedName("now_process_id")
        private int nowProcessId;

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

        public String getBrandNo() {
            return brandNo;
        }

        public void setBrandNo(String brandNo) {
            this.brandNo = brandNo;
        }

        public String getSpecifications() {
            return specifications;
        }

        public void setSpecifications(String specifications) {
            this.specifications = specifications;
        }

        public String getOverallDimensions() {
            return overallDimensions;
        }

        public void setOverallDimensions(String overallDimensions) {
            this.overallDimensions = overallDimensions;
        }

        public String getPartsName() {
            return partsName;
        }

        public void setPartsName(String partsName) {
            this.partsName = partsName;
        }

        public int getEachNumber() {
            return eachNumber;
        }

        public void setEachNumber(int eachNumber) {
            this.eachNumber = eachNumber;
        }

        public int getPlanNumber() {
            return planNumber;
        }

        public void setPlanNumber(int planNumber) {
            this.planNumber = planNumber;
        }

        public int getCompleteCount() {
            return completeCount;
        }

        public void setCompleteCount(int completeCount) {
            this.completeCount = completeCount;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getWorkTicketState() {
            return workTicketState;
        }

        public void setWorkTicketState(String workTicketState) {
            this.workTicketState = workTicketState;
        }

        public String getReferenceDrawings() {
            return referenceDrawings;
        }

        public void setReferenceDrawings(String referenceDrawings) {
            this.referenceDrawings = referenceDrawings;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getWorkshopName() {
            return workshopName;
        }

        public void setWorkshopName(String workshopName) {
            this.workshopName = workshopName;
        }

        public int getOperationDisplay() {
            return operationDisplay;
        }

        public void setOperationDisplay(int operationDisplay) {
            this.operationDisplay = operationDisplay;
        }

        public String getProcessState() {
            return processState;
        }

        public void setProcessState(String processState) {
            this.processState = processState;
        }

        public String getProcessName() {
            return processName;
        }

        public void setProcessName(String processName) {
            this.processName = processName;
        }

        public int getNowProcessId() {
            return nowProcessId;
        }

        public void setNowProcessId(int nowProcessId) {
            this.nowProcessId = nowProcessId;
        }
    }

    public static class CauseAnalysisModel{
        private int id;
        @SerializedName("process_info_id")
        private int processInfoId;
        @SerializedName("cause_content")
        private String causeContent;
        @SerializedName("is_enable")
        private int isEnable;
        private int sort;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProcessInfoId() {
            return processInfoId;
        }

        public void setProcessInfoId(int processInfoId) {
            this.processInfoId = processInfoId;
        }

        public String getCauseContent() {
            return causeContent;
        }

        public void setCauseContent(String causeContent) {
            this.causeContent = causeContent;
        }

        public int getIsEnable() {
            return isEnable;
        }

        public void setIsEnable(int isEnable) {
            this.isEnable = isEnable;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
    }
}
