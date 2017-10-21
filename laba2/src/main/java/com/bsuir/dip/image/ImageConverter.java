package com.bsuir.dip.image;

import com.bsuir.dip.type.Channel;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public final class ImageConverter {

    /**
     * Convert Image to pixels as bytes array.
     *
     * @param image to convert
     * @return bytes array
     */
    public static byte[] convertToBytes(Image image) {
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
    public static int[] convertToPixels(Image image) {
        byte[] bytes = convertToBytes(image);
        return convertToPixels(bytes);
    }

    /**
     * Convert image pixels to bytes array.
     *
     * @param pixels to convert
     * @return bytes array
     */
    public static byte[] convertToBytes(int[] pixels) {
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
    public static int[] convertToPixels(byte[] bytes) {
        int[] pixels = new int[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            pixels[i] = bytes[i] & 0xFF;
        }

        return pixels;
    }

    /**
     * Convert image to GrayScale.
     *
     * @param image
     * @return
     */
    public static Image convertToGS(Image image) {
        final Mat oldImage = image.getImg();
        Mat gsImage = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);

        final int RED = Channel.RED.getIndex();
        final int GREEN = Channel.GREEN.getIndex();
        final int BLUE = Channel.BLUE.getIndex();

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                double[] pixel = oldImage.get(i, j);
                double value = 0.3 * pixel[RED] + 0.59 * pixel[GREEN] + 0.11 * pixel[BLUE];

                gsImage.put(i, j, value);
            }
        }

        return new Image(gsImage);
    }

    /**
     * Convert image to luminance.
     *
     * @param image
     * @return image
     */
    public static int[][] convertToLuminance(Image image) {
        final Mat oldImage = image.getImg();
        int[][] pixels = new int[image.getHeight()][image.getWidth()];

        final int RED = Channel.RED.getIndex();
        final int GREEN = Channel.GREEN.getIndex();
        final int BLUE = Channel.BLUE.getIndex();

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                double[] pixel = oldImage.get(i, j);
                double luminance = (0.2126f * pixel[RED] + 0.7152f * pixel[GREEN] + 0.0722f * pixel[BLUE]) / 255;

                pixels[i][j] = (luminance < 0.7) ? 0 : 1;
            }
        }

        return pixels;
    }
}
