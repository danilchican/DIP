package com.bsuir.danilchican.image;

import static com.bsuir.danilchican.network.Network.NEURONS_COUNT;

public class Calculator {

    /**
     * Multiple vec & matrix.
     *
     * @param in
     * @param vec
     * @return result vector
     */
    public int[] mul(int[][] in, int[] vec) {
        int[] out = new int[NEURONS_COUNT];

        for (int i = 0; i < NEURONS_COUNT; i++) {
            for (int j = 0; j < NEURONS_COUNT; j++) {
                out[i] += in[i][j] * vec[j];
            }
        }

        return out;
    }

    public static int[] executeActivation(int[] in) {
        int[] out = new int[NEURONS_COUNT];

        for(int i = 0; i < NEURONS_COUNT; i++) {
            out[i] = (in[i] > 0) ? 1 : -1;
        }

        return out;
    }
}
