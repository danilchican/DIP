package com.bsuir.danilchican;

import com.bsuir.danilchican.image.ImageConverter;
import com.bsuir.danilchican.image.ImageLoader;
import com.bsuir.danilchican.network.NeuralNetwork;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;

import java.io.File;
import java.util.Scanner;

public class Main {

    static {
        OpenCV.loadLocally();
    }

    public static final String DELIMITER = File.separator;

    public static final String RESOURCES_INDEX = "resources";
    public static final String RES_IMAGES_INDEX = "images";
    public static final String RES_EXAMPLES_INDEX = "tests";

    public static final int NEURONS = 5;
    private static final int PIXELS_PER_IMAGE = 10 * 10;
    private static final int EPOCH_NUM = 1_000_000;
    private static final double LEARN_SPEED = 0.01;

    public static void main(String[] args) {
        /* Neurons count should be less than images count! */
        NeuralNetwork network = new NeuralNetwork(NEURONS, PIXELS_PER_IMAGE, EPOCH_NUM, LEARN_SPEED);

        network.loadImages();
        network.learn();
        network.showLearnedData();

        Scanner in = new Scanner(System.in);
        String cmd;

        do {
            System.out.print("\nEnter test image name: ");
            cmd = in.nextLine();

            Mat testImage = ImageLoader.load(RESOURCES_INDEX + DELIMITER + RES_EXAMPLES_INDEX + DELIMITER + cmd + ".bmp");
            int[] pixels = ImageConverter.convertToPixels(testImage);

            int clusterIndex = network.findClusterIndex(ImageConverter.convertToPixelsAsDouble(pixels));
            System.out.println("\ntest '" + cmd + ".bmp" + "': cluster = " + clusterIndex);
        } while (!"exit".equals(cmd));
    }
}
