package com.bsuir.dip.image;

import com.bsuir.dip.bean.DetectedItem;
import com.bsuir.dip.type.Channel;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class ImageConverter {

    /**
     * Convert Image to pixels as bytes array.
     *
     * @param image to convert
     * @return bytes array
     */
    private static byte[] convertToBytes(Image image) {
        Mat img = image.getImg();
        byte[] bytes = new byte[(int) (img.total() * img.channels())];

        img.get(0, 0, bytes);
        return bytes;
    }

    /**
     * Convert Image to pixels as int array.
     *
     * @param image to convert
     * @return pixels array
     */
    static int[] convertToPixels(Image image) {
        byte[] bytes = convertToBytes(image);
        return convertToPixels(bytes);
    }

    /**
     * Convert image pixels to bytes array.
     *
     * @param pixels to convert
     * @return bytes array
     */
    static byte[] convertToBytes(int[] pixels) {
        byte[] bytes = new byte[pixels.length];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) pixels[i];
        }

        return bytes;
    }

    /**
     * Convert image bytes to pixels as int array.
     *
     * @param bytes to convert
     * @return pixels array
     */
    private static int[] convertToPixels(byte[] bytes) {
        int[] pixels = new int[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            pixels[i] = bytes[i] & 0xFF;
        }

        return pixels;
    }

    /**
     * Convert image to luminance.
     *
     * @param image
     * @return image
     */
    static int[][] convertToLuminance(Image image) {
        final Mat oldImage = image.getImg();
        int[][] pixels = new int[image.getWidth()][image.getHeight()];

        final int RED = Channel.RED.getIndex();
        final int GREEN = Channel.GREEN.getIndex();
        final int BLUE = Channel.BLUE.getIndex();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                double[] pixel = oldImage.get(j, i);
                float luminance = (0.2126f * (float) pixel[RED]
                        + 0.7152f * (float) pixel[GREEN]
                        + 0.0722f * (float) pixel[BLUE]) / 255;

                pixels[i][j] = (luminance < 0.7) ? 0 : 1;
            }
        }

        return pixels;
    }

    static int[][] medianFilter(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();

        final Mat oldImage = image.getImg();

        int[][] pixels = new int[image.getWidth()][image.getHeight()];

        final int RED = Channel.RED.getIndex();
        final int GREEN = Channel.GREEN.getIndex();
        final int BLUE = Channel.BLUE.getIndex();

        ArrayList<Integer> red = new ArrayList();
        ArrayList<Integer> green = new ArrayList();
        ArrayList<Integer> blue = new ArrayList();

        Mat mat = image.getImg().clone();

        for (int i = 3; i < width - 3; i++) {
            for (int j = 3; j < height - 3; j++) {
                red.clear();
                green.clear();
                blue.clear();

                double[] pixel_1 = oldImage.get(j - 2, i - 2);
                double[] pixel_2 = oldImage.get(j, i - 2);
                double[] pixel_3 = oldImage.get(j + 2, i - 2);
                double[] pixel_4 = oldImage.get(j - 2, i);
                double[] pixel_5 = oldImage.get(j + 2, i);
                double[] pixel_6 = oldImage.get(j - 2, i + 2);
                double[] pixel_7 = oldImage.get(j, i + 2);
                double[] pixel_8 = oldImage.get(j + 2, i + 2);

                red.add((int) pixel_1[RED]);
                red.add((int) pixel_2[RED]);
                red.add((int) pixel_3[RED]);
                red.add((int) pixel_4[RED]);
                red.add((int) pixel_5[RED]);
                red.add((int) pixel_6[RED]);
                red.add((int) pixel_7[RED]);
                red.add((int) pixel_8[RED]);

                double[] pixel_11 = oldImage.get(j - 3, i - 3);
                double[] pixel_21 = oldImage.get(j, i - 3);
                double[] pixel_31 = oldImage.get(j + 3, i - 3);
                double[] pixel_41 = oldImage.get(j - 3, i);
                double[] pixel_51 = oldImage.get(j + 3, i);
                double[] pixel_61 = oldImage.get(j - 3, i + 3);
                double[] pixel_71 = oldImage.get(j, i + 3);
                double[] pixel_81 = oldImage.get(j + 3, i + 3);

                red.add((int) pixel_11[RED]);
                red.add((int) pixel_21[RED]);
                red.add((int) pixel_31[RED]);
                red.add((int) pixel_41[RED]);
                red.add((int) pixel_51[RED]);
                red.add((int) pixel_61[RED]);
                red.add((int) pixel_71[RED]);
                red.add((int) pixel_81[RED]);

                double[] pixel_12 = oldImage.get(j - 1, i - 1);
                double[] pixel_22 = oldImage.get(j, i - 1);
                double[] pixel_32 = oldImage.get(j + 1, i - 1);
                double[] pixel_42 = oldImage.get(j - 1, i);
                double[] pixel_52 = oldImage.get(j, i);
                double[] pixel_62 = oldImage.get(j + 1, i);
                double[] pixel_72 = oldImage.get(j - 1, i + 1);
                double[] pixel_82 = oldImage.get(j, i + 1);
                double[] pixel_92 = oldImage.get(j + 1, i + 1);

                red.add((int) pixel_12[RED]);
                red.add((int) pixel_22[RED]);
                red.add((int) pixel_32[RED]);
                red.add((int) pixel_42[RED]);
                red.add((int) pixel_52[RED]);
                red.add((int) pixel_62[RED]);
                red.add((int) pixel_72[RED]);
                red.add((int) pixel_82[RED]);
                red.add((int) pixel_92[RED]);

                Collections.sort(red);

                green.add((int) pixel_12[GREEN]);
                green.add((int) pixel_22[GREEN]);
                green.add((int) pixel_32[GREEN]);
                green.add((int) pixel_42[GREEN]);
                green.add((int) pixel_52[GREEN]);
                green.add((int) pixel_62[GREEN]);
                green.add((int) pixel_72[GREEN]);
                green.add((int) pixel_82[GREEN]);
                green.add((int) pixel_92[GREEN]);

                green.add((int) pixel_1[GREEN]);
                green.add((int) pixel_2[GREEN]);
                green.add((int) pixel_3[GREEN]);
                green.add((int) pixel_4[GREEN]);
                green.add((int) pixel_5[GREEN]);
                green.add((int) pixel_6[GREEN]);
                green.add((int) pixel_7[GREEN]);
                green.add((int) pixel_8[GREEN]);

                green.add((int) pixel_11[GREEN]);
                green.add((int) pixel_21[GREEN]);
                green.add((int) pixel_31[GREEN]);
                green.add((int) pixel_41[GREEN]);
                green.add((int) pixel_51[GREEN]);
                green.add((int) pixel_61[GREEN]);
                green.add((int) pixel_71[GREEN]);
                green.add((int) pixel_81[GREEN]);

                Collections.sort(green);

                blue.add((int) pixel_12[BLUE]);
                blue.add((int) pixel_22[BLUE]);
                blue.add((int) pixel_32[BLUE]);
                blue.add((int) pixel_42[BLUE]);
                blue.add((int) pixel_52[BLUE]);
                blue.add((int) pixel_62[BLUE]);
                blue.add((int) pixel_72[BLUE]);
                blue.add((int) pixel_82[BLUE]);
                blue.add((int) pixel_92[BLUE]);

                blue.add((int) pixel_1[BLUE]);
                blue.add((int) pixel_2[BLUE]);
                blue.add((int) pixel_3[BLUE]);
                blue.add((int) pixel_4[BLUE]);
                blue.add((int) pixel_5[BLUE]);
                blue.add((int) pixel_6[BLUE]);
                blue.add((int) pixel_7[BLUE]);
                blue.add((int) pixel_8[BLUE]);

                blue.add((int) pixel_11[BLUE]);
                blue.add((int) pixel_21[BLUE]);
                blue.add((int) pixel_31[BLUE]);
                blue.add((int) pixel_41[BLUE]);
                blue.add((int) pixel_51[BLUE]);
                blue.add((int) pixel_61[BLUE]);
                blue.add((int) pixel_71[BLUE]);
                blue.add((int) pixel_81[BLUE]);

                Collections.sort(blue);

                Color color = new Color(red.get(12), green.get(12), blue.get(12));
                mat.put(j, i, color.getRed(), color.getGreen(), color.getBlue());
            }
        }

        byte[] bytes = new byte[(int) (mat.total() * mat.channels())];
        mat.get(0, 0, bytes);

        int[] pixs = convertToPixels(bytes);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = pixs[i * width + j];
            }
        }

        return pixels;
    }

    /**
     * Colorize image.
     *
     * @param image
     * @return image
     */
    public static Image colorizeImage(Image image) {
        int[][] pixels = image.getPixels();

        List<DetectedItem> areas = image.getAreas();
        Map<Integer, DetectedItem> areasMap = image.getAreasMap();

        Mat mat = image.getImg().clone();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (pixels[i][j] != 0) {
                    int n = areas.indexOf(areasMap.get(pixels[i][j]));
                    double[] pixel = getRGB(n);

                    mat.put(j, i, pixel);
                }
            }
        }

        return new Image(mat);
    }

    /**
     * Colorize image clasters.
     *
     * @param image
     * @return image
     */
    public static Image colorizeImageClasters(Image image) {
        int[][] pixels = image.getPixels();

        Map<Integer, DetectedItem> areasMap = image.getAreasMap();
        Mat mat = image.getImg().clone();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int pix = pixels[i][j];

                if (pix != 0) {
                    int index = areasMap.get(pix).getClaster();

                    double[] pixel = getRGB(index);
                    mat.put(j, i, pixel);
                }
            }
        }

        return new Image(mat);
    }

    private static double[] getRGB(int n) {
        Color color;

        switch (n) {
            case 0:
                color = Color.orange;
                break;
            case 1:
                color = Color.red;
                break;
            case 2:
                color = Color.blue;
                break;
            case 3:
                color = Color.yellow;
                break;
            case 4:
                color = Color.magenta;
                break;
            case 5:
                color = Color.cyan;
                break;
            case 6:
                color = Color.gray;
                break;
            case 7:
                color = Color.darkGray;
                break;
            case 8:
                color = Color.pink;
                break;
            default:
                color = Color.green;
        }

        return new double[]{color.getRed(), color.getGreen(), color.getBlue()};
    }
}
