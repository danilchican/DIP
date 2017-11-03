package com.bsuir.dip.image;

import com.bsuir.dip.bean.DetectedItem;
import com.bsuir.dip.drawing.Imshow;
import com.bsuir.dip.drawing.ImageHistogram;
import com.bsuir.dip.type.Channel;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bsuir.dip.drawing.ImageHistogram.INTERVAL_COUNT;
import static com.bsuir.dip.drawing.ImageHistogram.X_AXIS_NAME;
import static com.bsuir.dip.drawing.ImageHistogram.Y_AXIS_NAME;

public class Image {

    private Mat img;
    private String title = "No title Image";

    private int width;
    private int height;

    private int channels = 3;

    private int[][] pixels;

    private List<DetectedItem> areas;
    private Map<Integer, DetectedItem> areasMap;

    private static boolean flag = true;








    public Image(Mat img) {
        this.img = img;

        width = img.width();
        height = img.height();

        channels = img.channels();

        areas = new ArrayList<>();
        areasMap = new HashMap<>();

        if(!flag) pixels = ImageConverter.medianFilter(this);

        pixels = ImageConverter.convertToLuminance(this);

        this.modifyLabel();
        this.calcSpace();
        this.calcPerimeter();
        this.calcCompactness();
    }

    public Image(Mat img, String title) {
        this(img);
        this.title = title;
    }

    int[][] getPixels() {
        return pixels;
    }

    public List<DetectedItem> getAreas() {
        return areas;
    }

    Map<Integer, DetectedItem> getAreasMap() {
        return areasMap;
    }

    public Mat getImg() {
        return img;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    boolean isGrayScale() {
        return channels == 1;
    }

    /**
     * Get data of concrete channel.
     *
     * @param channel
     * @return pixels
     */
    private int[] getChannel(Channel channel) {
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

    /**
     * Execute binary preparing.
     *
     * @param threshold
     */
    public void execBinPreparing(final int threshold) {
        final int[] pixels = ImageConverter.convertToPixels(this);
        final int[] binary = ImageCalculator.calcBinaryPreparing(pixels, threshold);

        this.img.put(0, 0, ImageConverter.convertToBytes(binary));
    }

    /**
     * Execute preparing.
     *
     * @param left
     * @param right
     */
    public void execPreparing(final int left, final int right) {
        final int[] pixels = ImageConverter.convertToPixels(this);
        final int[] binary = ImageCalculator.calcPreparing(pixels, left, right);

        this.img.put(0, 0, ImageConverter.convertToBytes(binary));
    }

    /**
     * Execute Sobel operator filter.
     */
    public void execSobel() {
        final int[] sobel = ImageCalculator.calcSobel(this);

        this.img.put(0, 0, ImageConverter.convertToBytes(sobel));
    }

    /**
     * Show image histogram.
     */
    public void showHistogram(Channel channel) {
        HashMap<String, List<Integer>> map = this.calcImageHistogram(channel);

        ImageHistogram histogram = new ImageHistogram(title, map.get(X_AXIS_NAME), map.get(Y_AXIS_NAME));
        histogram.show();
    }

    /**
     * Clasterize all objects.
     */
    public void clasterize() {
        int c1 = areas.get(0).getSpace();
        int c2 = areas.get(5).getSpace();

        int prevC1;
        int prevC2;

        List<DetectedItem> Claster1 = new ArrayList<>();
        List<DetectedItem> Claster2 = new ArrayList<>();

        do {
            Claster1.clear();
            Claster2.clear();

            for (DetectedItem area : areas) {
                int diff1 = Math.abs(c1 - area.getSpace());
                int diff2 = Math.abs(c2 - area.getSpace());

                if (diff1 < diff2) {
                    Claster1.add(area);
                } else {
                    Claster2.add(area);
                }
            }

            prevC1 = c1;
            prevC2 = c2;

            c1 = 0;
            c2 = 0;

            for (DetectedItem clast1 : Claster1) {
                c1 += clast1.getSpace();
            }

            for (DetectedItem clast2 : Claster2) {
                c2 += clast2.getSpace();
            }

            c1 = Claster1.size() == 0 ? 0 : c1 / Claster1.size();
            c2 = Claster2.size() == 0 ? 0 : c2 / Claster2.size();
        } while (c1 != prevC1 && c2 != prevC2);

        for (DetectedItem area : areas) {
            if (Claster1.contains(area)) {
                area.setClaster(1);
            }
            if (Claster2.contains(area)) {
                area.setClaster(2);
            }
        }
    }

    /**
     * Calculate mass center.
     */
    public void calcMassCenter() {
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                if (pixels[i][j] != 0) {
                    DetectedItem obj = areasMap.get(pixels[i][j]);
                    obj.setMassCenterX(obj.getMassCenterX() + j);
                    obj.setMassCenterY(obj.getMassCenterY() + i);
                }
            }
        }
        for (Map.Entry<Integer, DetectedItem> entry : areasMap.entrySet()) {
            entry.getValue().setMassCenterX(entry.getValue().getMassCenterX() / entry.getValue().getSpace());
            entry.getValue().setMassCenterY(entry.getValue().getMassCenterY() / entry.getValue().getSpace());
        }
        for (Map.Entry<Integer, DetectedItem> entry : areasMap.entrySet()) {
            img.put(entry.getValue().getMassCenterY(), entry.getValue().getMassCenterX(), Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue());
        }
    }

    /**
     * Get color name.
     *
     * @param n
     * @return color name
     */
    public static String getColorName(int n) {
        switch (n) {
            case 0:
                return "orange";
            case 1:
                return "red";
            case 2:
                return "blue";
            case 3:
                return "yellow";
            case 4:
                return "magenta";
            case 5:
                return "cyan";
            case 6:
                return "gray";
            case 7:
                return "darkGray";
            case 8:
                return "pink";
            default:
                return "green";
        }
    }

    private void modifyLabel() {
        int lbl = 2;

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                fillLabel(i, j, lbl++);
            }
        }
    }

    private void calcSpace() {
        Map<Integer, Integer> tempMap = new HashMap<>();

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                if (pixels[i][j] != 0) {
                    if (tempMap.containsKey(pixels[i][j])) {
                        tempMap.put(pixels[i][j], tempMap.get(pixels[i][j]) + 1);
                    } else {
                        tempMap.put(pixels[i][j], 1);
                    }
                }
            }
        }

        int i = 0;

        for (Map.Entry<Integer, Integer> entry : tempMap.entrySet()) {
            areas.add(new DetectedItem(entry.getKey(), entry.getValue()));
            areasMap.put(entry.getKey(), areas.get(i));
            i++;
        }

        System.out.println("Areas size: " + areas.size());
    }

    private void calcPerimeter() {
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                if (pixels[i][j] != 0) {
                    if (i + 1 < pixels.length) {
                        if (pixels[i + 1][j] == 0) {
                            areasMap.get(pixels[i][j]).setPerimeter(areasMap.get(pixels[i][j]).getPerimeter() + 1);
                            continue;
                        }
                    }

                    if (j + 1 < pixels[i].length) {
                        if (pixels[i][j + 1] == 0) {
                            areasMap.get(pixels[i][j]).setPerimeter(areasMap.get(pixels[i][j]).getPerimeter() + 1);
                            continue;
                        }
                    }

                    if (j > 0) {
                        if (pixels[i][j - 1] == 0) {
                            areasMap.get(pixels[i][j]).setPerimeter(areasMap.get(pixels[i][j]).getPerimeter() + 1);
                            continue;
                        }
                    }

                    if (i > 0) {
                        if (pixels[i - 1][j] == 0) {
                            areasMap.get(pixels[i][j]).setPerimeter(areasMap.get(pixels[i][j]).getPerimeter() + 1);
                        }
                    }
                }
            }
        }
    }

    private void calcCompactness() {
        for (Map.Entry<Integer, DetectedItem> entry : areasMap.entrySet()) {
            float compactness = (float) Math.pow(entry.getValue().getPerimeter(), 2) / entry.getValue().getSpace();
            entry.getValue().setCompactness(compactness);
        }
    }

    private void fillLabel(int x, int y, int lbl) {
        if (pixels[x][y] == 1) {
            pixels[x][y] = lbl;

            if (x > 0) {
                fillLabel(x - 1, y, lbl);
            }

            if (x < pixels.length - 1) {
                fillLabel(x + 1, y, lbl);
            }

            if (y > 0) {
                fillLabel(x, y - 1, lbl);
            }

            if (y < pixels[x].length) {
                fillLabel(x, y + 1, lbl);
            }
        }
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
