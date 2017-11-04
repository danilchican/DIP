package com.bsuir.danilchican.network;

import com.bsuir.danilchican.Main;
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

public class NeuralNetwork {

    private final int neurons;
    private final int epochNum;

    private final int pixelsPerImage;

    private static final double BETA = 0.01;

    private Map<String, int[]> images;

    private double[][] w;
    private int[] winClasters;

    public NeuralNetwork(int neurons, int pixelsPerImage, int epochNum) {
        this.neurons = neurons;
        this.pixelsPerImage = pixelsPerImage;
        this.epochNum = epochNum;

        this.images = new HashMap<>();

        this.winClasters = new int[neurons];
        this.w = new double[neurons][pixelsPerImage];

        this.fillWeights();
    }

    public void loadImages() {
        File file = new File(Main.RESOURCES_INDEX);
        String path = file.getAbsolutePath() + "/" + Main.RES_IMAGES_INDEX;

        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile).forEach(p -> {
                Mat tempImg = ImageLoader.load(Main.RESOURCES_INDEX + "/" + Main.RES_IMAGES_INDEX + "/" + p.getFileName().toString());
                images.put(p.getFileName().toString(), ImageConverter.convertToPixels(tempImg));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Vector normalization.
     *
     * @param in source vector
     * @return normalized vector
     */
    public double[] normalize(double[] in) {
        double[] out = new double[in.length];
        double inSum = Arrays.stream(in).sum();

        for (int i = 0; i < out.length; i++) {
            out[i] = in[i] / inSum;
        }

        return out;
    }

    private void fillWeights() {
        for (int i = 0; i < neurons; i++) {
            for (int j = 0; j < pixelsPerImage; j++) {
                w[i][j] = new Random().nextDouble();
            }

            w[i] = normalize(w[i]);
            winClasters[i] = 1;
        }
    }
}
