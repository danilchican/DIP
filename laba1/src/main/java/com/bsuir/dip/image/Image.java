package com.bsuir.dip.image;

import com.bsuir.dip.action.Imshow;
import org.opencv.core.Mat;

public class Image {
    private Mat img;
    private String title = "No title Image";

    public void setImg(Mat img) {
        this.img = img;
    }

    public Mat getImg() {
        return img;
    }

    /**
     * Show image in window.
     */
    public void show() {
        if(!getImg().empty()) {
            new Imshow(title).showImage(getImg());
            return;
        }

        System.out.println("Can't show image. Mat obj is empty!");
    }
}
