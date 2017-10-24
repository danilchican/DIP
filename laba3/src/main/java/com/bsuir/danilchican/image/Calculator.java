package com.bsuir.danilchican.image;

import static com.bsuir.danilchican.network.Network.NEURONS_COUNT;

public class Calculator {

    /**
     * Multiple vec & matrix.
     *
     * @param in
     * @param vec multiple vector
     * @return result vector
     */
    public static int[] mul(int[][] in, int[] vec) {
        int[] out = new int[vec.length];

        for (int i = 0; i < NEURONS_COUNT; i++) {
            for (int j = 0; j < NEURONS_COUNT; j++) {
                out[i] += in[i][j] * vec[j];
            }
        }

        return out;
    }

    /**
     * Execute activation function.
     *
     * @param in vector
     * @return result vector
     */
    public static int[] executeActivation(int[] in) {
        int[] out = new int[NEURONS_COUNT];

        for (int i = 0; i < NEURONS_COUNT; i++) {
            out[i] = (in[i] > 0) ? 1 : -1;
        }

        return out;
    }

    /**
     * Prepare image.
     *
     * @param in
     * @return prepared image
     */
    public static int[] prepareImage(int[] in) {
        int[] out = new int[in.length];

        for (int i = 0; i < out.length; i++) {
            out[i] = (in[i] != 0) ? -1 : 1;
        }

        return out;
    }

    public static int[] prepareInverseImage(int[] in) {
        int[] out = new int[in.length];

        for (int i = 0; i < out.length; i++) {
            out[i] = (in[i] != -1) ? 0 : 255;
        }

        return out;
    }
}
