package com.bsuir.dip.action;

import com.bsuir.dip.image.Image;
import com.bsuir.dip.image.ImageConverter;

public class TranslationAction extends Action {

    public void executeGS() {
        System.out.println("Execute translating to GrayScale.");

        if (!isEmptyImage) {
            if (!image.isGrayScale()) {
                image = ImageConverter.convertToGS(image);
            }

            image.show();
        }
    }

    public void executePreparing() {
        System.out.println("Execute preparing.");

        if (!isEmptyImage) {
            // TODO Change
            image = ImageConverter.convertToGS(image);
            image.show();
        }
    }

    public void executeBinPreparing() {
        System.out.println("Execute preparing.");

        if (!isEmptyImage) {
            // TODO Change
            image = ImageConverter.convertToGS(image);
            image.show();
        }
    }

    public void executeSobiel() {
        System.out.println("Execute Sobiel.");

        Image img = new Image(image.getImg().clone());
        img.execSobel();
        img.show();
    }
}
