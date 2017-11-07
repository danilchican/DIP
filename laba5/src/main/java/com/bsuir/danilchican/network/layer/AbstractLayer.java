package com.bsuir.danilchican.network.layer;

import java.util.Random;

public abstract class AbstractLayer {

    /**
     * Data of layer.
     */
    double[][] layer;

    private final int layerWidth;
    private final int layerHeight;

    /**
     * Thresholds for Q or T.
     */
    double[] thresholds;

    /**
     * Default constructor.
     *
     * @param thresholdsSize
     * @param layerWidth
     * @param layerHeight
     */
    AbstractLayer(final int thresholdsSize, final int layerWidth, final int layerHeight) {
        this.thresholds = new double[thresholdsSize];
        this.layerWidth = layerWidth;
        this.layerHeight = layerHeight;

        this.layer = new double[layerWidth][layerHeight];
        this.fill();
    }

    public double[] getThresholds() {
        return thresholds;
    }

    public double[][] getLayer() {
        return layer;
    }

    private void fill() {
        Random rand = new Random();

        for (int i = 0; i < layerWidth; i++) {
            for (int j = 0; j < layerHeight; j++) {
                layer[i][j] = rand.nextDouble() * 2 - 1;
            }
        }

        for (int i = 0; i < thresholds.length; i++) {
            thresholds[i] = rand.nextDouble() * 2 - 1;
        }
    }
}
