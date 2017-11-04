package com.bsuir.danilchican.network;

import com.bsuir.danilchican.Main;
import com.bsuir.danilchican.image.Calculator;
import com.bsuir.danilchican.image.ImageConverter;
import com.bsuir.danilchican.image.ImageLoader;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static com.bsuir.danilchican.Main.DELIMITER;
import static com.bsuir.danilchican.image.Calculator.normalize;

public class NeuralNetwork {

    private final int neurons;
    private final int epochNum;
    private final int pixelsPerImage;
    private final double learnSpeed;
    private static final double EXP = 0.00001;

    private Map<String, int[]> images;

    private double[][] weights;
    private int[] neuronWins;

    /**
     * Constructor with args.
     *
     * @param neurons        count of neurons
     * @param pixelsPerImage count of image's pixels
     * @param epochNum       max epochs
     * @param learnSpeed     learning speed (BETA)
     */
    public NeuralNetwork(int neurons, int pixelsPerImage, int epochNum, double learnSpeed) {
        this.neurons = neurons;
        this.pixelsPerImage = pixelsPerImage;
        this.epochNum = epochNum;
        this.learnSpeed = learnSpeed;

        this.images = new HashMap<>();

        this.neuronWins = new int[neurons];
        this.weights = new double[neurons][pixelsPerImage];

        this.fillWeights();
    }

    public void loadImages() {
        File file = new File(Main.RESOURCES_INDEX);
        String path = file.getAbsolutePath() + DELIMITER + Main.RES_IMAGES_INDEX;

        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile).forEach(p -> {
                Mat tempImg = ImageLoader.load(Main.RESOURCES_INDEX + DELIMITER + Main.RES_IMAGES_INDEX + DELIMITER + p.getFileName().toString());
                images.put(p.getFileName().toString(), ImageConverter.convertToPixels(tempImg));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Learn network.
     */
    public void learn() {
        for (int epoch = 0; epoch < epochNum; epoch++) { // learn iteration
            double maxEuclideanDistance = 0;

            for (Map.Entry<String, int[]> image : images.entrySet()) {
                double[] pixels = ImageConverter.convertToPixelsAsDouble(image.getValue());
                pixels = normalize(pixels);

                int vinJ = 0;
                double minValue = 0;

                boolean firstStep = true;

                for (int j = 0; j < weights.length; j++) {
                    double sum = Calculator.euclideanDistance(pixels, weights[j]) * neuronWins[j];

                    if (minValue > sum || firstStep) {
                        minValue = sum;
                        vinJ = j;
                        firstStep = false;
                    }
                }

                neuronWins[vinJ]++;

                /* Change weights by formula */
                changeWeights(vinJ, pixels);

                double currentEuclideanDistance = Calculator.euclideanDistance(pixels, weights[vinJ]);

                if (currentEuclideanDistance > maxEuclideanDistance) {
                    maxEuclideanDistance = currentEuclideanDistance;
                }
            }

            if (maxEuclideanDistance < EXP) {
                System.out.println("Max Euclidean distance < exponent : " + maxEuclideanDistance + " < " + EXP);
                break;
            }
        }
    }

    private void fillWeights() {
        for (int i = 0; i < neurons; i++) {
            for (int j = 0; j < pixelsPerImage; j++) {
                weights[i][j] = new Random().nextDouble();
            }

            weights[i] = normalize(weights[i]);
            neuronWins[i] = 1;
        }
    }

    private void changeWeights(int j, double[] pixels) {
        for (int i = 0; i < pixelsPerImage; i++) {
            weights[j][i] = weights[j][i] + learnSpeed * (pixels[i] - weights[j][i]);
        }

        weights[j] = normalize(weights[j]);
    }
}
