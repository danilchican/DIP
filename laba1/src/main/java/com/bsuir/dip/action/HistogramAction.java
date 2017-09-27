package com.bsuir.dip.action;

import com.bsuir.dip.drawing.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import static com.bsuir.dip.index.Main.window;

public class HistogramAction extends Action {

    private boolean isEmptyImage = false;

    /**
     * Execute command.
     */
    @Override
    public void execute() {
        System.out.println("Histogram action.");
        Image image = window.getImage();

        if(image == null) {
            isEmptyImage = true;

            new Dialog(
                    Alert.AlertType.WARNING, "Image not selected",
                    null, "Select image to display histogram."
            ).show();
        }
    }

    public void executeGS() {
        if(!isEmptyImage) {
            System.out.println("Execute GS");
        }
    }

    public void executeR() {
        if(!isEmptyImage) {
            System.out.println("Execute R");
        }
    }

    public void executeG() {
        if(!isEmptyImage) {
            System.out.println("Execute G");
        }
    }

    public void executeB() {
        if(!isEmptyImage) {
            System.out.println("Execute B");
        }
    }
}
