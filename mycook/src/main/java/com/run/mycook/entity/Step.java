package com.run.mycook.entity;

/**
 * Created by zsr on 2016/6/18.
 */
public class Step {

    private String img;
    private String step;

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

    @Override
    public String toString() {
        return "Step{" +
                "img='" + img + '\'' +
                ", step='" + step + '\'' +
                '}';
    }
}
