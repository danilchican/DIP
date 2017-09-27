package com.bsuir.dip.action;

import com.bsuir.dip.drawing.Dialog;
import com.bsuir.dip.image.Image;
import javafx.scene.control.Alert;

import static com.bsuir.dip.index.Main.window;

public abstract class Action implements IAction {
    boolean isEmptyImage = false;
    Image image;

    /**
     * Execute command.
     */
    public void execute() {
        System.out.println("Executing action.");
        Image image = window.getImage();

        if (image == null) {
            isEmptyImage = true;

            new Dialog(
                    Alert.AlertType.WARNING, "Image not selected",
                    null, "Select image to execute operation."
            ).show();
        } else {
            this.image = image;
        }
    }
}
