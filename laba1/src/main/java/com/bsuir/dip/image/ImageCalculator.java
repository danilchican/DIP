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
}
