package com.bsuir.dip.image;

import org.opencv.core.Mat;

public class Image {
    private Mat img;

    public void setImg(Mat img) {
        this.img = img;
    }

    public Mat getImg() {
        return img;
    }
}
