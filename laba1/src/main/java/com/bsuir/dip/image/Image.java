package com.bsuir.dip.image;

import com.bsuir.dip.drawing.Imshow;
import com.bsuir.dip.drawing.ImageHistogram;
import com.bsuir.dip.type.Channel;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bsuir.dip.drawing.ImageHistogram.INTERVAL_COUNT;
import static com.bsuir.dip.drawing.ImageHistogram.X_AXIS_NAME;
import static com.bsuir.dip.drawing.ImageHistogram.Y_AXIS_NAME;

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
            new Imshow(title).showImage(getImg());
            return;
        }

        System.out.println("Can't show image. Mat obj is empty!");
    }

    public void execBinPreparing(int threshold) {
        int[] pixels = ImageConverter.convertToPixels(this);
        int[] binary = ImageCalculator.calcBinaryPreparing(pixels, threshold);

        this.img.put(0, 0, ImageConverter.convertToBytes(binary));
    }

    /**
     * Show image histogram.
     */
    public void showHistogram(Channel channel) {
        HashMap<String, List<Integer>> map = this.calcImageHistogram(channel);

        ImageHistogram histogram = new ImageHistogram(title, map.get(X_AXIS_NAME), map.get(Y_AXIS_NAME));
        histogram.show();
    }

    private HashMap<String, List<Integer>> calcImageHistogram(Channel channel) {
        int[] pixels = getChannel(channel);
        int[] histData = ImageCalculator.calcChannelHist(pixels);

        HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>() {{
            put(X_AXIS_NAME, new ArrayList<>());
            put(Y_AXIS_NAME, new ArrayList<>());
        }};

        final int startValue = 0;

        map.get(X_AXIS_NAME).add(startValue);
        map.get(Y_AXIS_NAME).add(startValue);

        for (int i = 0; i < INTERVAL_COUNT - 1; i++) {
            map.get(X_AXIS_NAME).add(i + 1);
            map.get(Y_AXIS_NAME).add(startValue);
        }


        for (int i = 0; i < histData.length; i++) {
            map.get(Y_AXIS_NAME).set(i, histData[i]);
        }

        System.out.println("Histogram calculated");
        return map;
    }
}
