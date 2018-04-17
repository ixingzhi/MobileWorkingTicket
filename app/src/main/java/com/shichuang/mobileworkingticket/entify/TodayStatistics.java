package com.shichuang.mobileworkingticket.entify;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */

public class TodayStatistics {
    private List<TodayStatisticsModel> statistics;

    public List<TodayStatisticsModel> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<TodayStatisticsModel> statistics) {
        this.statistics = statistics;
    }

    public static class TodayStatisticsModel{
        private String count;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
