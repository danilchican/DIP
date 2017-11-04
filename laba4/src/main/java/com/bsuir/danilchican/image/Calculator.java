package com.bsuir.danilchican.image;

import static com.bsuir.danilchican.Main.NEURONS;

public class Calculator {

    /**
     * Calculate Euclidean distance.
     *
     * @param a 1st vector
     * @param b 2nd vector
     * @return distance
     */
    public static double euclideanDistance(double[] a, double[] b) {
        double sum = 0;

        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }

        return Math.sqrt(sum);
    }


    /**
     * Multiple vec & matrix.
     *
     * @param in
     * @param vec multiple vector
     * @return result vector
     */
    public static int[] mul(int[][] in, int[] vec) {
        int[] out = new int[vec.length];

        for (int i = 0; i < NEURONS; i++) {
            for (int j = 0; j < NEURONS; j++) {
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
        int[] out = new int[NEURONS];

        for (int i = 0; i < NEURONS; i++) {
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

    /**
     * Inverse preparing image
     * @param in
     * @return
     */
    public static int[] prepareInverseImage(int[] in) {
        int[] out = new int[in.length];

        for (int i = 0; i < out.length; i++) {
            out[i] = (in[i] != -1) ? 0 : 255;
        }

        return out;
    }
}
