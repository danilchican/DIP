package com.bsuir.danilchican.network;

import java.util.Arrays;

public class TeachingObject {

    private String name;

    private double[] cluster;
    private double[] pixels;

    /**
     * Default constructor.
     *
     * @param name
     * @param pixels
     * @param cluster
     */
    TeachingObject(String name, double[] pixels, double[] cluster) {
        this.name = name;
        this.pixels = pixels;
        this.cluster = cluster;
    }

    /**
     * Get cluster index in cluster array.
     *
     * @return index
     */
    int getClusterIndex() {
        int id = 0;

        for(int i = 0; i < cluster.length; i++) {
            if(cluster[i] == 1) {
                id = i;
                break;
            }
        }

        return id;
    }

    /**
     * Get cluster data.
     *
     * @return cluster vector
     */
    double[] getCluster() {
        return cluster;
    }

    /**
     * Get pixels
     *
     * @return pixels
     */
    public double[] getPixels() {
        return pixels;
    }

    @Override
    public String toString() {
        return "TeachingObject{" +
                "name='" + name + '\'' +
                ", cluster=" + Arrays.toString(cluster) +
                '}';
    }
}
