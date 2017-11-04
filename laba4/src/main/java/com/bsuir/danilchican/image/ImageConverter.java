package com.bsuir.danilchican.image;

import org.opencv.core.Mat;

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
     * Conver image as Mat to pixels as byte array.
     *
     * @param img
     * @return bytes
     */
    public static byte[] convertToBytes(Mat img) {
        byte[] bytes = new byte[(int) (img.total() * img.channels())];

        img.get(0, 0, bytes);
        return bytes;
    }

    /**
     * Convert Mat img to pixels as int array.
     *
     * @param img
     * @return pixels
     */
    public static int[] convertToPixels(Mat img) {
        byte[] bytes = convertToBytes(img);
        return convertToPixels(bytes);
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
     * Convert image pixels from int to double array.
     *
     * @param in to convert
     * @return out array
     */
    public static double[] convertToPixelsAsDouble(int[] in) {
        double[] out = new double[in.length];

        for (int i = 0; i < in.length; i++) {
            out[i] = (double) in[i];
        }

        return out;
    }
}