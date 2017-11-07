package com.bsuir.danilchican.network;

import com.bsuir.danilchican.Main;
import com.bsuir.danilchican.image.Calculator;
import com.bsuir.danilchican.image.ImageConverter;
import com.bsuir.danilchican.image.ImageLoader;
import com.bsuir.danilchican.network.layer.HiddenLayer;
import com.bsuir.danilchican.network.layer.OutputLayer;
import com.bsuir.danilchican.util.ClusterIncrementator;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.bsuir.danilchican.Main.DELIMITER;

public class Perceptron {

    private static final int ATTEMPTS = 100_000;

    /**
     * Learn speed.
     */
    private final double alpha;
    private final double beta;

    /**
     * Max mistake value for teaching.
     */
    private final double maxMistakeValue;

    /**
     * Count of inputs/neurons of 1st (distribution) layer.
     */
    private final int n;

    /**
     * Count neurons of 2nd hidden layer.
     */
    private final int h;

    /**
     * Count neurons of 3rd (output) layer.
     */
    private final int m;

    /**
     * Massive of thresholds for hidden & output layers.
     */
    private HiddenLayer hiddenLayer;
    private OutputLayer outputLayer;

    /**
     * Teaching objects.
     */
    private List<TeachingObject> teachingObjects;

    /**
     * Default constructor.
     *
     * @param alpha learn speed hidden layer
     * @param beta  learn speed output layer
     * @param n     inputs/neurons of 1st layer
     * @param m     neurons of 3rd layer
     */
    public Perceptron(final double alpha, final double beta, final int n, final int m, final double maxMistakeValue) {
        this.alpha = alpha;
        this.beta = beta;

        this.n = n;
        this.m = m;
        this.h = (m + n) / 2;
        this.maxMistakeValue = maxMistakeValue;

        this.hiddenLayer = new HiddenLayer(n, h);
        this.outputLayer = new OutputLayer(h, m);

        this.teachingObjects = new ArrayList<>();
    }

    /**
     * Load images to list.
     */
    public void loadImages() {
        File file = new File(Main.RESOURCES_INDEX);
        String path = file.getAbsolutePath() + DELIMITER + Main.RES_IMAGES_INDEX;

        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile).forEach(p -> {
                Mat tempImg = ImageLoader.load(Main.RESOURCES_INDEX + DELIMITER
                        + Main.RES_IMAGES_INDEX + DELIMITER
                        + p.getFileName().toString());

                int[] pixels = ImageConverter.convertToPixels(tempImg);
                double[] pixelsAsDouble = ImageConverter.convertToPixelsAsDouble(pixels);

                this.addForTeaching(p.getFileName().toString(), pixelsAsDouble, ClusterIncrementator.nextCluster());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Teach neural network.
     */
    public void teach() {
        double minMistakeValue;
        int attempts = 0;

        do {
            minMistakeValue = 0;

            for (TeachingObject image : teachingObjects) {
                double[] gHiddenLayer = calculateHiddenOutputs(image.getPixels(), hiddenLayer.getThresholds(), hiddenLayer.getLayer());
                double[] yOutputLayer = calculateOutOutputs(gHiddenLayer, outputLayer.getThresholds(), outputLayer.getLayer());

                double[] mistakeDk = getDk(image.getCluster(), yOutputLayer);

                int idx = image.getClusterIndex();

                if (mistakeDk[idx] > minMistakeValue) {
                    minMistakeValue = mistakeDk[idx];
                }

                outputLayer.correctMistakes(mistakeDk, gHiddenLayer, yOutputLayer, alpha);
                hiddenLayer.correctMistakes(beta, outputLayer, mistakeDk, yOutputLayer, gHiddenLayer, image);
            }

            if (++attempts == ATTEMPTS) {
                break;
            }

        } while (minMistakeValue > maxMistakeValue);
    }

    /**
     * Show teaching objects.
     */
    public void showTeachingObjects() {
        System.out.println();

        for(TeachingObject object : teachingObjects) {
            System.out.println(object);
        }
    }

    /**
     * Show probabilities of test images.
     *
     * @param testVector
     */
    public void showProbabilities(double[] testVector) {
        double[] hiddenLayer = calculateHiddenOutputs(testVector, this.hiddenLayer.getThresholds(), this.hiddenLayer.getLayer());
        double[] output = calculateOutOutputs(hiddenLayer, outputLayer.getThresholds(), outputLayer.getLayer());

        for (double item : output) {
            double probability = (1 - item) * 100;
            System.out.print(String.format("%.5f", probability) + "%, ");
        }
    }

    /**
     * Calculate mistake of neurons[k] of output layer.
     *
     * @param yr cluster data
     * @param y  output layer
     * @return mistake
     */
    private double[] getDk(double[] yr, double[] y) {
        double[] d = new double[y.length];

        for (int k = 0; k < y.length; k++) {
            d[k] = yr[k] - y[k];
        }

        return d;
    }

    private double[] calculateHiddenOutputs(double[] x, double[] q, double[][] v) {
        return calculateOutputs(x, q, v);
    }

    private double[] calculateOutOutputs(double[] x, double[] t, double[][] w) {
        return calculateOutputs(x, t, w);
    }

    private double[] calculateOutputs(double[] x, double[] qt, double[][] vw) {
        double[] output = new double[qt.length];

        for (int j = 0; j < qt.length; j++) {
            double summary = 0;

            for (int i = 0; i < x.length; i++) {
                summary += vw[i][j] * x[i];
            }

            output[j] = sigmaFunction(summary + qt[j]);
        }

        return output;
    }

    private double sigmaFunction(double input) {
        return 1.0 / (1.0 + Math.exp(-1.0 * input));
    }

    private void addForTeaching(String name, double[] inputs, double[] cluster) {
        inputs = Calculator.prepareImage(inputs);

        for (int i = 0; i < n; i++) {
            inputs[i] = inputs[i] == -1 ? 0 : 1;
        }

        teachingObjects.add(new TeachingObject(name, inputs, cluster));
    }
}
