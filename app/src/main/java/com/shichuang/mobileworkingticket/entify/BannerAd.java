package com.shichuang.mobileworkingticket.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class BannerAd {
    private List<AdvPicsModels> advPicsModels;

    public List<AdvPicsModels> getAdvPicsModels() {
        return advPicsModels;
    }

    public void setAdvPicsModels(List<AdvPicsModels> advPicsModels) {
        this.advPicsModels = advPicsModels;
    }

    public static class AdvPicsModels{
        private int id;
        @SerializedName("show_location")
        private int showLocation;
        @SerializedName("adv_title")
        private String advTitle;
        @SerializedName("cover_pics")
        private String coverPics;
        @SerializedName("lik_url")
        private String likUrl;
        @SerializedName("sort_id")
        private int sortId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getShowLocation() {
            return showLocation;
        }

        public void setShowLocation(int showLocation) {
            this.showLocation = showLocation;
        }

        public String getAdvTitle() {
            return advTitle;
        }

        public void setAdvTitle(String advTitle) {
            this.advTitle = advTitle;
        }

        public String getCoverPics() {
            return coverPics;
        }

        public void setCoverPics(String coverPics) {
            this.coverPics = coverPics;
        }

        public String getLikUrl() {
            return likUrl;
        }

        public void setLikUrl(String likUrl) {
            this.likUrl = likUrl;
        }

        public int getSortId() {
            return sortId;
        }

        public void setSortId(int sortId) {
            this.sortId = sortId;
        }
    }
}
