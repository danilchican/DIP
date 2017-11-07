package com.bsuir.danilchican.network.layer;

import com.bsuir.danilchican.network.TeachingObject;

public class HiddenLayer extends AbstractLayer {

    public HiddenLayer(final int n, final int h) {
        super(h, n, h);
    }

    /**
     * Correct mistakes of hidden layer coefficients.
     *
     * @param beta
     * @param outputLayer
     * @param mistakeDk
     * @param yOutputLayer
     * @param gHiddenLayer
     * @param image
     */
    public void correctMistakes(final double beta, OutputLayer outputLayer, double[] mistakeDk,
                                double[] yOutputLayer, double[] gHiddenLayer, TeachingObject image) {
        double[][] outLayer = outputLayer.getLayer();
        double[] e = new double[outLayer.length];

        for (int j = 0; j < outLayer.length; j++) {
            e[j] = 0;

            for (int k = 0; k < mistakeDk.length; k++) {
                e[j] += mistakeDk[k] * yOutputLayer[k] * (1 - yOutputLayer[k]) * outLayer[j][k];
            }
        }

        this.correct(beta, e, gHiddenLayer, image);
    }

    private void correct(final double beta, double[] e, double[] gHiddenLayer, TeachingObject image) {
        double[] pixels = image.getPixels();

        for (int j = 0; j < thresholds.length; j++) {
            for (int i = 0; i < layer.length; i++) {
                layer[i][j] += beta * gHiddenLayer[j] * (1 - gHiddenLayer[j]) * e[j] * pixels[i];
            }

            thresholds[j] += beta * gHiddenLayer[j] * (1 - gHiddenLayer[j]) * e[j];
        }
    }
}
