package com.bsuir.dip.action;

import com.bsuir.dip.drawing.Dialog;
import com.bsuir.dip.image.Image;
import com.bsuir.dip.image.ImageConverter;
import com.bsuir.dip.type.Channel;
import javafx.scene.control.Alert;

import static com.bsuir.dip.index.Main.window;

public class HistogramAction extends Action {

    private boolean isEmptyImage = false;

    private Image image;

    /**
     * Execute command.
     */
    @Override
    public void execute() {
        System.out.println("Histogram action.");
        Image image = window.getImage();

        if (image == null) {
            isEmptyImage = true;

            new Dialog(
                    Alert.AlertType.WARNING, "Image not selected",
                    null, "Select image to display histogram."
            ).show();
        } else {
            this.image = image;
        }
    }

    public void executeGS() {
        System.out.println("Execute show histogram by channel: GS");

        if (!isEmptyImage) {
            if (!image.isGrayScale()) {
                image = ImageConverter.convertToGS(image);
            }

            image.showHistogram(Channel.ALL);
        }
    }

    public void executeByChannel(Channel channel) {
        System.out.println("Execute show histogram by channel: " + channel);

        if (!isEmptyImage) {
            if (image.isGrayScale()) {
                new Dialog(
                        Alert.AlertType.ERROR, "Error",
                        null, "Image is not in RGB colors."
                ).show();

                return;
            }

            image.showHistogram(channel);
        }
    }
}
