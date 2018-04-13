package com.shichuang.mobileworkingticket.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class ReportProcessList {
    private List<ReportProcessModel> rows;
    private int recordCount;

    public List<ReportProcessModel> getRows() {
        return rows;
    }

    public void setRows(List<ReportProcessModel> rows) {
        this.rows = rows;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public static class ReportProcessModel{
        private int id;
        @SerializedName("process_name")
        private String processName;
        @SerializedName("sum_complete_count")
        private int sumCompleteCount;
        @SerializedName("sum_unfinished_count")
        private int sumUnfinishedCount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProcessName() {
            return processName;
        }

        public void setProcessName(String processName) {
            this.processName = processName;
        }

        public int getSumCompleteCount() {
            return sumCompleteCount;
        }

        public void setSumCompleteCount(int sumCompleteCount) {
            this.sumCompleteCount = sumCompleteCount;
        }

        public int getSumUnfinishedCount() {
            return sumUnfinishedCount;
        }

        public void setSumUnfinishedCount(int sumUnfinishedCount) {
            this.sumUnfinishedCount = sumUnfinishedCount;
        }
    }
}
