package com.run.mycook.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zsr on 2016/6/17.
 */
public class JHCook {
    private String resultcode;
    private String reason;//返回说明
    private Result result;//结果集
    private String error_code;//返回码

    @Override
    public String toString() {
        return "JHCook{" +
                "resultcode='" + resultcode + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                ", error_code='" + error_code + '\'' +
                '}';
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public static class Result {
        private List<Data> data;//菜谱详情
        private String totalNum;
        private String pn;//数据返回起始下标
        private String rn;//数据返回条数，最大30

        @Override
        public String toString() {
            return "Result{" +
                    "data=" + data +
                    ", totalNum='" + totalNum + '\'' +
                    ", pn='" + pn + '\'' +
                    ", rn='" + rn + '\'' +
                    '}';
        }

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }

        public String getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(String totalNum) {
            this.totalNum = totalNum;
        }

        public String getPn() {
            return pn;
        }

        public void setPn(String pn) {
            this.pn = pn;
        }

        public String getRn() {
            return rn;
        }

        public void setRn(String rn) {
            this.rn = rn;
        }
    }

    public static class Data implements Serializable {
        private String id;
        private String title;
        private String tags;
        private String imtro;
        private String ingredients;
        private String burden;
        private List<String> albums;
        private List<Steps> steps;

        public Data(String title, String imtro, List<String> albums, String tags) {
            this.title = title;
            this.imtro = imtro;
            this.albums = albums;
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", tags='" + tags + '\'' +
                    ", imtro='" + imtro + '\'' +
                    ", ingredients='" + ingredients + '\'' +
                    ", burden='" + burden + '\'' +
                    ", albums=" + albums +
                    ", steps=" + steps +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getImtro() {
            return imtro;
        }

        public void setImtro(String imtro) {
            this.imtro = imtro;
        }

        public String getIngredients() {
            return ingredients;
        }

        public void setIngredients(String ingredients) {
            this.ingredients = ingredients;
        }

        public String getBurden() {
            return burden;
        }

        public void setBurden(String burden) {
            this.burden = burden;
        }

        public List<String> getAlbums() {
            return albums;
        }

        public void setAlbums(List<String> albums) {
            this.albums = albums;
        }

        public List<Steps> getSteps() {
            return steps;
        }

        public void setSteps(List<Steps> steps) {
            this.steps = steps;
        }
    }

    public static class Steps implements Serializable{
        private String img;
        private String step;

        @Override
        public String toString() {
            return "Steps{" +
                    "img='" + img + '\'' +
                    ", step='" + step + '\'' +
                    '}';
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }
    }
}
