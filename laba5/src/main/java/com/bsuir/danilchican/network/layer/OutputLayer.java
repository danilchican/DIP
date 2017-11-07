package com.bsuir.danilchican.network.layer;

public class OutputLayer extends AbstractLayer {

    public OutputLayer(final int h, final int m) {
        super(m, h, m);
    }

    /**
     * Correct mistakes of output layer coefficients.
     *
     * @param mistakeDk
     * @param gHiddenLayer
     * @param yOutputLayer
     * @param alpha
     */
    public void correctMistakes(double[] mistakeDk, double[] gHiddenLayer, double[] yOutputLayer, final double alpha) {
        for (int k = 0; k < mistakeDk.length; k++) {
            for (int j = 0; j < gHiddenLayer.length; j++) {
                layer[j][k] += alpha * yOutputLayer[k] * (1 - yOutputLayer[k]) * mistakeDk[k] * gHiddenLayer[j];
            }

            thresholds[k] += alpha * yOutputLayer[k] * (1 - yOutputLayer[k]) * mistakeDk[k];
        }
    }
}
