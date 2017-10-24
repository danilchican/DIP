package com.bsuir.danilchican.image;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;

public final class ImageLoader {

    /**
     * Load image from file to Mat
     *
     * @param path to load
     * @return image like Mat format
     * @see Mat
     */
    public static Mat load(String path) {
        return Imgcodecs.imread(path);
    }

    /**
     * Save image from Mat to .jpeg format.
     *
     * @param path  to save
     * @param image data to save
     * @return result of saving
     * @see Image
     */
    public static boolean save(String path, Image image) {
        MatOfInt quality = new MatOfInt(Imgcodecs.CV_IMWRITE_JPEG_QUALITY, 200);
        return Imgcodecs.imwrite(path, image.getImg(), quality);
    }
}
