package com.bsuir.danilchican.image;

import java.util.Arrays;

public class Calculator {

    /**
     * Normalize vector.
     *
     * @param in input vector
     * @return output normalized vector
     */
    public static double[] normalize(double[] in) {
        double[] out = new double[in.length];
        double inSum = Arrays.stream(in).sum();

        for (int i = 0; i < out.length; i++) {
            out[i] = in[i] / inSum;
        }

        return out;
    }

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
     * Prepare image.
     *
     * @param in
     * @return prepared image
     */
    public static double[] prepareImage(double[] in) {
        double[] out = new double[in.length];

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
