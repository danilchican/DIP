package com.bsuir.danilchican.util;

public class ClusterIncrementator {
    private static int id;

    public static double[] nextCluster() {
        double[] cluster = new double[5];
        cluster[id++] = 1;

        return cluster;
    }
}
