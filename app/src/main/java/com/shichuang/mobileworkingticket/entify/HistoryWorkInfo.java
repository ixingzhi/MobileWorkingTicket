package com.shichuang.mobileworkingticket.entify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/3/26.
 */

public class HistoryWorkInfo {
    private HistoryWorkInfoModel row;

    public HistoryWorkInfoModel getRow() {
        return row;
    }

    public void setRow(HistoryWorkInfoModel row) {
        this.row = row;
    }

    public static class HistoryWorkInfoModel {
        @SerializedName("sum_complete_count")
        private String sumCompleteCount;
        @SerializedName("sum_working_hours")
        private String sumWorkingHours;

        public String getSumCompleteCount() {
            return sumCompleteCount;
        }

        public void setSumCompleteCount(String sumCompleteCount) {
            this.sumCompleteCount = sumCompleteCount;
        }

        public String getSumWorkingHours() {
            return sumWorkingHours;
        }

        public void setSumWorkingHours(String sumWorkingHours) {
            this.sumWorkingHours = sumWorkingHours;
        }
    }
}
