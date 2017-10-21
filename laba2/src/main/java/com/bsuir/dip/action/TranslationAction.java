package com.bsuir.dip.action;

import com.bsuir.dip.image.Image;
import com.bsuir.dip.image.ImageConverter;
import com.bsuir.dip.index.Main;

public class TranslationAction extends Action {

    public void executeColorize() {
        System.out.println("Execute colorizing image.");

        if (!isEmptyImage) {
            Main.window.setLastImage(new Image(image.getImg().clone()));
            Image image = ImageConverter.colorizeImage(Main.window.getLastImage());

            Main.window.setLastImage(image);
            Main.window.replaceImage();
        }
    }

    public void executeClasterize() {
        System.out.println("Execute clasterizing image.");

        if (!isEmptyImage) {
            Main.window.setLastImage(new Image(image.getImg().clone()));
            Main.window.getLastImage().clasterize();

            Image image = ImageConverter.colorizeImageClasters(Main.window.getLastImage());

            Main.window.setLastImage(image);
            Main.window.replaceImage();
        }
    }

    public void executePreparing(final int leftThreshold, final int rightThreshold) {
        System.out.println("Execute preparing.");

        if (!isEmptyImage) {
            Main.window.setLastImage(new Image(image.getImg().clone()));
            Main.window.getLastImage().execPreparing(leftThreshold, rightThreshold);

            Main.window.replaceImage();
        }
    }

    public void executeBinPreparing(int threshold) {
        System.out.println("Execute preparing.");

        if (!isEmptyImage) {
            Main.window.setLastImage(new Image(image.getImg().clone()));
            Main.window.getLastImage().execBinPreparing(threshold);

            Main.window.replaceImage();
        }
    }

    public void executeSobiel() {
        System.out.println("Execute Sobiel.");

        if (!isEmptyImage) {
            Main.window.setLastImage(new Image(image.getImg().clone()));
            Main.window.getLastImage().execSobel();

            Main.window.replaceImage();
        }
    }
}
