package com.bsuir.danilchican;

import com.bsuir.danilchican.image.ImageConverter;
import com.bsuir.danilchican.image.ImageLoader;
import com.bsuir.danilchican.network.Network;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;

public class App {

    static {
        OpenCV.loadLocally();
    }

    public static final String RESOURCES_INDEX = "resources";
    public static final String RES_IMAGES_INDEX = "images";
    public static final String RES_EXAMPLES_INDEX = "tests";

    public static void main(String[] args) {
        Network network = new Network();

        network.loadImages();
        network.teach();

        Mat testImage = ImageLoader.load(RESOURCES_INDEX + "/" + RES_EXAMPLES_INDEX + "/m_20.bmp");
        network.verify(ImageConverter.convertToPixels(testImage));
    }
}
