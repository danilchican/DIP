package com.bsuir.danilchican.network;

import com.bsuir.danilchican.image.Calculator;
import com.bsuir.danilchican.image.ImageConverter;
import com.bsuir.danilchican.image.ImageLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static com.bsuir.danilchican.App.RESOURCES_INDEX;
import static com.bsuir.danilchican.App.RES_EXAMPLES_INDEX;
import static com.bsuir.danilchican.App.RES_IMAGES_INDEX;

public class Network {

    /**
     * Matrix size.
     */
    public static final int NEURONS_COUNT = 100;

    private int[][] teachMatrix = new int[NEURONS_COUNT][NEURONS_COUNT];

    private Map<String, int[]> images = new HashMap<>();
    private Map<String, int[][]> imagesMatrix = new HashMap<>();

    public void loadImages() {
        File file = new File(RESOURCES_INDEX);
        String path = file.getAbsolutePath() + "/" + RES_IMAGES_INDEX;

        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile).forEach(p -> {
                Mat tempImg = ImageLoader.load(RESOURCES_INDEX + "/" + RES_IMAGES_INDEX + "/" + p.getFileName().toString());
                images.put(p.getFileName().toString(), ImageConverter.convertToPixels(tempImg));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Teach neural network.
     */
    public void teach() {
        initImagesPixels();
        initImagesMatrix();
    }

    /**
     * Verify image by neural network.
     *
     * @param in image pixels
     */
    public void verify(int[] in) {
        int[] pixels = Calculator.prepareImage(in);
        int[] midState;

        do {
            midState = Arrays.copyOf(pixels, pixels.length);

            pixels = Calculator.mul(teachMatrix, pixels);
            pixels = Calculator.executeActivation(pixels);
        } while (!Arrays.equals(pixels, midState));

        for (Map.Entry<String, int[]> image : images.entrySet()) {
            if (Arrays.equals(image.getValue(), pixels)) {
                System.out.println("Founded image: " + image.getKey());
                break;
            }
        }

        pixels = Calculator.prepareInverseImage(pixels);

        Mat img = new Mat(NEURONS_COUNT / 10, NEURONS_COUNT / 10, CvType.CV_8UC1);
        img.put(0, 0, ImageConverter.convertToBytes(pixels));

        ImageLoader.save("resources/res.bmp", img, Imgcodecs.CV_IMWRITE_PAM_FORMAT_BLACKANDWHITE);
    }

    /**
     * Fill images matrix.
     */
    private void initImagesPixels() {
        for (Map.Entry<String, int[]> image : images.entrySet()) {
            int[] pixels = Calculator.prepareImage(image.getValue());
            images.put(image.getKey(), pixels);
        }
    }

    private void initImagesMatrix() {
        for (Map.Entry<String, int[]> image : images.entrySet()) {
            int[] temp = Arrays.copyOf(image.getValue(), image.getValue().length);
            final int[][] imgMatrixItem = this.calcImageMatrix(temp); // do for each other image

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
                teachMatrix[i][j] += m[i][j];
            }
        }
    }
}
