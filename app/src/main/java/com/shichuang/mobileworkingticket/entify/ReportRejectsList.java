package com.shichuang.mobileworkingticket.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class ReportRejectsList {
    private List<ReportRejectsModel> rows;
    private int recordCount;

    public List<ReportRejectsModel> getRows() {
        return rows;
    }

    public void setRows(List<ReportRejectsModel> rows) {
        this.rows = rows;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public static class ReportRejectsModel{
        private int id;
        @SerializedName("cause_content")
        private String causeContent;
        @SerializedName("scrap_count")
        private int scrapCount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCauseContent() {
            return causeContent;
        }

        public void setCauseContent(String causeContent) {
            this.causeContent = causeContent;
        }

        public int getScrapCount() {
            return scrapCount;
        }

        public void setScrapCount(int scrapCount) {
            this.scrapCount = scrapCount;
        }
    }
}
