package com.run.running.entity.http;

import java.util.List;

/**
 * Created by zsr on 2016/6/3.
 */
public class Book {

    private String resultcode;
    private String reason;
    private Result result;

    @Override
    public String toString() {
        return "Book{" +
                "resultcode='" + resultcode + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
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


    public static class Result {
        private List<Data> data;

        @Override
        public String toString() {
            return "Result{" +
                    "data=" + data +
                    '}';
        }

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }
    }

    public static class Data {
        private String title;
        private String code;

        @Override
        public String toString() {
            return "Data{" +
                    "title='" + title + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
