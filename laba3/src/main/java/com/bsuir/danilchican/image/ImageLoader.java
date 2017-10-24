package com.bsuir.danilchican.image;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public final class ImageLoader {

    /**
     * Load image from file to Mat
     *
     * @param path to load
     * @return image like Mat format
     * @see Mat
     */
    public static Mat load(String path) {
        return Imgcodecs.imread(path, Imgcodecs.IMREAD_GRAYSCALE);
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

    public static boolean save(String path, Mat img, int type) {
        if(type == -101) {
            type = Imgcodecs.CV_IMWRITE_JPEG_QUALITY;
        }
        MatOfInt quality = new MatOfInt(type, 200);
        return Imgcodecs.imwrite(path, img, quality);
    }
}
