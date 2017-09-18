package com.bsuir.dip.index;

import com.bsuir.dip.image.Image;
import com.bsuir.dip.image.ImageLoader;
import nu.pattern.OpenCV;

public class Main {
    static {
        OpenCV.loadLocally();
    }

    public static void main(String[] args) {
        Image image = new Image();
        image.setImg(ImageLoader.load("D:/zm.jpg"));
        ImageLoader.save("D:/result.jpg", image);
    }
}
