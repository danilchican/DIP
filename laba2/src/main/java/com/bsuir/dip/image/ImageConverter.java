package com.bsuir.dip.image;

import com.bsuir.dip.bean.DetectedItem;
import com.bsuir.dip.type.Channel;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.*;
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
