package com.bsuir.danilchican;

import com.bsuir.danilchican.image.ImageConverter;
import com.bsuir.danilchican.image.ImageLoader;
import com.bsuir.danilchican.network.Perceptron;
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
    private static final String RES_EXAMPLES_INDEX = "tests";

    private static final int PIXELS_PER_IMAGE = 10 * 10;
    private static final int INPUT_NEURONS = PIXELS_PER_IMAGE;
    private static final int OUTPUT_NEURONS = 5;

    private static final double ALPHA = 1.0;
    private static final double BETA = 2.5;
    private static final double MISTAKE_DISTANCE = 0.00000001;

    public static void main(String[] args) {
        Perceptron network = new Perceptron(ALPHA, BETA, INPUT_NEURONS, OUTPUT_NEURONS, MISTAKE_DISTANCE);

        network.loadImages();
        network.teach();

        Scanner in = new Scanner(System.in);
        String cmd;

        network.showTeachingObjects();

        do {
            System.out.print("\nEnter test image name: ");
            cmd = in.nextLine();

            Mat testImage = ImageLoader.load(RESOURCES_INDEX + DELIMITER + RES_EXAMPLES_INDEX + DELIMITER + cmd + ".bmp");
            int[] pixels = ImageConverter.convertToPixels(testImage);
            double[] pixelsAsDouble = ImageConverter.convertToPixelsAsDouble(pixels);

            network.showProbabilities(pixelsAsDouble);
        } while (!"exit".equals(cmd));
    }
}
