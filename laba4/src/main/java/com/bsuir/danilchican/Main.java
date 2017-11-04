package com.bsuir.danilchican;

import com.bsuir.danilchican.image.ImageConverter;
import com.bsuir.danilchican.image.ImageLoader;
import com.bsuir.danilchican.network.NeuralNetwork;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;

public class Main {

    static {
        OpenCV.loadLocally();
    }

    public static final String RESOURCES_INDEX = "resources";
    public static final String RES_IMAGES_INDEX = "images";
    public static final String RES_EXAMPLES_INDEX = "tests";

    private static final int NEURONS = 3;
    private static final int PIXELS_PER_IMAGE = 10 * 10;
    private static final int EPOCH_NUM = 1000;

    public static void main(String[] args) {
        /* Neurons count should be less than images count! */
        NeuralNetwork network = new NeuralNetwork(NEURONS, PIXELS_PER_IMAGE, EPOCH_NUM);

        network.loadImages();
//        network.teach();
//
//        Mat testImage = ImageLoader.load(RESOURCES_INDEX + "/" + RES_EXAMPLES_INDEX + "/m_20.bmp");
//        network.verify(ImageConverter.convertToPixels(testImage));
    }
}
