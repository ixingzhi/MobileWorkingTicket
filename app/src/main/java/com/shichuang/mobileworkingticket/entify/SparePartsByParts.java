package com.shichuang.mobileworkingticket.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/28.
 */

public class SparePartsByParts {
    private StatisticsModel statisticsRow;
    private DataRows dataRows;

    public StatisticsModel getStatisticsRow() {
        return statisticsRow;
    }

    public void setStatisticsRow(StatisticsModel statisticsRow) {
        this.statisticsRow = statisticsRow;
    }

    public DataRows getDataRows() {
        return dataRows;
    }

    public void setDataRows(DataRows dataRows) {
        this.dataRows = dataRows;
    }

    public static class StatisticsModel{
        @SerializedName("sum_completed_number")
        private int sumCompletedNumber;
        @SerializedName("working_hours")
        private int workingHours;

        public int getSumCompletedNumber() {
            return sumCompletedNumber;
        }

        public void setSumCompletedNumber(int sumCompletedNumber) {
            this.sumCompletedNumber = sumCompletedNumber;
        }

        public int getWorkingHours() {
            return workingHours;
        }

        public void setWorkingHours(int workingHours) {
            this.workingHours = workingHours;
        }
    }

    public static class DataRows{
        private List<Rows> rows;
        private int recordCount;

        public List<Rows> getRows() {
            return rows;
        }

        public void setRows(List<Rows> rows) {
            this.rows = rows;
        }

        public int getRecordCount() {
            return recordCount;
        }

        public void setRecordCount(int recordCount) {
            this.recordCount = recordCount;
        }

        public static class Rows{
            @SerializedName("row_number")
            private int rowNumber;
            @SerializedName("work_ticket_num")
            private String workTicketNum;
            @SerializedName("work_order_no")
            private String workOrderNo;
            @SerializedName("parts_drawing_no")
            private String partsDrawingNo;
            @SerializedName("parts_name")
            private String partsName;
            private String component;
            @SerializedName("add_time")
            private String addTime;
            @SerializedName("finish_count")
            private double finishCount;
            @SerializedName("all_count")
            private double allCount;

            public int getRowNumber() {
                return rowNumber;
            }

            public void setRowNumber(int rowNumber) {
                this.rowNumber = rowNumber;
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

            public String getComponent() {
                return component;
            }

            public void setComponent(String component) {
                this.component = component;
            }

            public String getAddTime() {
                return addTime;
            }

            public void setAddTime(String addTime) {
                this.addTime = addTime;
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
}
