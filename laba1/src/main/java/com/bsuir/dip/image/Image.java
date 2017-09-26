package com.bsuir.dip.image;

import com.bsuir.dip.drawing.Imshow;
import com.bsuir.dip.drawing.ImageHistogram;
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

    private static final int CHANNELS = 3;

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
        if (!getImg().empty()) {
            new Imshow(title).showImage(getImg());
            return;
        }

        System.out.println("Can't show image. Mat obj is empty!");
    }

    /**
     * Show image histogram.
     */
    public void showHistogram() {
        HashMap<String, List<Integer>> map = this.calcImageHistogram();

        ImageHistogram histogram = new ImageHistogram(title, map.get(X_AXIS_NAME), map.get(Y_AXIS_NAME));
        histogram.show();
    }

    private HashMap<String, List<Integer>> calcImageHistogram() {
        int[] pixels = ImageConverter.convertToPixels(this);

        HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>() {{
            put(X_AXIS_NAME, new ArrayList<>());
            put(Y_AXIS_NAME, new ArrayList<>());
        }};

        final int startValue = 0;

        int frequency = startValue;
        int xMin = pixels[0];
        int xMax = xMin;

        for (int pixel : pixels) {
            if (xMin > pixel) {
                xMin = pixel;
            }

            if (xMax < pixel) {
                xMax = pixel;
            }
        }

        map.get(X_AXIS_NAME).add(startValue);
        map.get(Y_AXIS_NAME).add(startValue);

        for (int i = 0; i < INTERVAL_COUNT - 1; i++) {
            map.get(X_AXIS_NAME).add(i + 1);
            map.get(Y_AXIS_NAME).add(startValue);
        }

        for (int i = 0; i < INTERVAL_COUNT - 1; i++) {
            for (int pixel : pixels) {
                if (pixel >= map.get(X_AXIS_NAME).get(i)
                        && pixel < map.get(X_AXIS_NAME).get(i + 1)) {
                    map.get(Y_AXIS_NAME).set(i, map.get(Y_AXIS_NAME).get(i) + 1);
                }

                if (frequency < map.get(Y_AXIS_NAME).get(i)) {
                    frequency = map.get(Y_AXIS_NAME).get(i);
                }
            }

            map.get(Y_AXIS_NAME).set(i, map.get(Y_AXIS_NAME).get(i) / pixels.length);
        }

        System.out.println("Histogram calculated");
        return map;
    }
}
