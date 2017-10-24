package com.bsuir.danilchican.network;

import com.bsuir.danilchican.image.ImageConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Network {

    /**
     * Matrix size.
     */
    public static final int NEURONS_COUNT = 10;

    private int[][] teachMatrix = new int[NEURONS_COUNT][NEURONS_COUNT];
    private int[][] resultMatrix = new int[NEURONS_COUNT][NEURONS_COUNT];

    private Map<String, int[]> images = new HashMap<>();
    private Map<String, int[][]> imagesMatrix = new HashMap<>();

    public Network() {
        init();
    }

    public void loadImages() {
        File file = new File("resources");
        String path = file.getAbsolutePath() + "/images";

        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile).forEach(p -> {
                try {
                    images.put(p.getFileName().toString(), ImageConverter.convertToPixels(Files.readAllBytes(p)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void teach() {
        initImagesPixels();
        initImagesMatrix();
    }

    /**
     * Fill images matrix.
     */
    public void initImagesPixels() {
        for (Map.Entry<String, int[]> image : images.entrySet()) {
            int[] pixels = image.getValue();

            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = (pixels[i] == 0) ? -1 : 1;
            }
        }
    }

    /**
     * Setting up start values to teaching matrix.
     */
    private void init() {
        for (int i = 0; i < NEURONS_COUNT; i++) {
            for (int j = 0; j < NEURONS_COUNT; j++) {
                teachMatrix[i][j] = (i != j) ? 1 : 0;
            }
        }
    }

    private void initImagesMatrix() {
        for (Map.Entry<String, int[]> image : images.entrySet()) {
            final int[][] imgMatrixItem = this.calcImageMatrix(image.getValue()); // do for each other image

            imagesMatrix.put(image.getKey(), imgMatrixItem);
            this.updateResultMatrix(imgMatrixItem);
        }
    }

    private int[][] calcImageMatrix(int[] inMatrix) {
        int[][] outMatrix = new int[NEURONS_COUNT][NEURONS_COUNT];

        for (int i = 0; i < NEURONS_COUNT; i++) {
            for (int j = 0; j < NEURONS_COUNT; j++) {
                outMatrix[i][j] = (i != j) ? inMatrix[i] * inMatrix[j] : 0;
            }
        }


        return outMatrix;
    }

    private void updateResultMatrix(int[][] m) {
        for (int i = 0; i < NEURONS_COUNT; i++) {
            for (int j = 0; j < NEURONS_COUNT; j++) {
                resultMatrix[i][j] += m[i][j];
            }
        }
    }
}
