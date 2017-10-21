package com.bsuir.dip.action;

import com.bsuir.dip.image.Image;
import com.bsuir.dip.image.ImageConverter;
import com.bsuir.dip.index.Main;

public class TranslationAction extends Action {

    public void executeGS() {
        System.out.println("Execute translating to GrayScale.");

        if (!isEmptyImage) {
            if (!image.isGrayScale()) {
                Main.window.setLastImage(ImageConverter.convertToGS(new Image(image.getImg().clone())));
            }

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
