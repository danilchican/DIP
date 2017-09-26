package com.bsuir.dip.image;

public class ImageCalculator {

    private static final int HIST_WIDTH = 256;

    /**
     * Calculate histogram data.
     *
     * @param pixels
     * @return
     */
    public static int[] calcChannelHist(int[] pixels) {
        int[] hist = new int[HIST_WIDTH];

        for (int pixel : pixels) {
            hist[pixel] += 1;
        }

        return hist;
    }

    /**
     * Binary preparing.
     *
     * @param pixels
     * @param threshold
     * @return pixels
     */
    public static int[] calcBinaryPreparing(final int[] pixels, final int threshold) {
        int[] output = new int[pixels.length];

        for (int i = 0; i < pixels.length; i++) {
            output[i] = (pixels[i] >= threshold) ? 255 : 0;
        }

        return output;
    }

    /**
     * Preparing.
     *
     * @param pixels
     * @param left
     * @param right
     * @return pixels
     */
    public static int[] calcPreparing(final int[] pixels, final int left, final int right) {
        int[] output = new int[pixels.length];

        for (int i = 0; i < pixels.length; i++) {
            output[i] = (pixels[i] >= left && pixels[i] <= right) ? 255 : 0;
        }

        return output;
    }

}
