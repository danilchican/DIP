package com.bsuir.danilchican.image;

import com.bsuir.danilchican.drawing.Imshow;
import com.bsuir.danilchican.type.Channel;
import org.opencv.core.Mat;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Image {

    private Mat img;
    private String title = "No title Image";

    private int width;
    private int height;

    private int channels = 3;

    public Image(Mat img) {
        this.img = img;

        width = img.width();
        height = img.height();

        channels = img.channels();
    }

    public Image(Mat img, String title) {
        this(img);
        this.title = title;
    }

    public Mat getImg() {
        return img;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isGrayScale() {
        return channels == 1;
    }

    /**
     * Get data of concrete channel.
     *
     * @param channel
     * @return pixels
     */
    public int[] getChannel(Channel channel) {
        List<Integer> pixels = new ArrayList<>();
        int index = channel.getIndex();

        if (channel == Channel.ALL) {
            return ImageConverter.convertToPixels(this);
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels.add((int) img.get(i, j)[index]);
            }
        }

        return pixels.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Show image in window.
     */
    public void show() {
        if (!getImg().empty()) {
            Imshow showHandler = new Imshow(title);
            showHandler.setCloseOption(WindowConstants.HIDE_ON_CLOSE);
            showHandler.showImage(getImg());

            return;
        }

        System.out.println("Can't show image. Mat obj is empty!");
    }
}