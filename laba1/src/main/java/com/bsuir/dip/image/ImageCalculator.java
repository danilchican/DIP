package com.bsuir.dip.image;

import java.util.Arrays;

public class ImageCalculator {

    private static final int HIST_SIZE = 256;

    /**
     * Calculate histogram data.
     *
     * @param pixels
     * @return
     */
    public static int[] calcChannelHist(int[] pixels) {
        int[] hist = new int[HIST_SIZE];

        for (int pixel : pixels) {
            hist[pixel] += 1;
        }

        return hist;
    }
}
