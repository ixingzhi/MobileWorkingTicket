package com.shichuang.mobileworkingticket.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23.
 */

public class ProcessList {
    private List<ProcessInfoRows> processInfoRows;

    public List<ProcessInfoRows> getProcessInfoRows() {
        return processInfoRows;
    }

    public void setProcessInfoRows(List<ProcessInfoRows> processInfoRows) {
        this.processInfoRows = processInfoRows;
    }

    public static class ProcessInfoRows{
        private int id;
        @SerializedName("process_name")
        private String processName;

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
    }
}
