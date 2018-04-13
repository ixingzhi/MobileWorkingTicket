package com.shichuang.mobileworkingticket.entify;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class Workshop {

    private List<WorkshopModel> workshopInfoModels;

    public List<WorkshopModel> getWorkshopInfoModels() {
        return workshopInfoModels;
    }

    public void setWorkshopInfoModels(List<WorkshopModel> workshopInfoModels) {
        this.workshopInfoModels = workshopInfoModels;
    }

    public static class WorkshopModel {
        private int id;
        private String name;  // 车间名
        private String describe;  // 车间描述

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }
    }
}
