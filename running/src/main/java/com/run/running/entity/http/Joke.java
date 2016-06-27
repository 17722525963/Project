package com.run.running.entity.http;

import java.util.List;

/**
 * Created by zsr on 2016/6/7.
 */
public class Joke {

    private String error_code;
    private String reason;
    private Result result;

    @Override
    public String toString() {
        return "Joke{" +
                "error_code='" + error_code + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
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
        private List<String> data;

        @Override
        public String toString() {
            return "Result{" +
                    "data=" + data +
                    '}';
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }

}
