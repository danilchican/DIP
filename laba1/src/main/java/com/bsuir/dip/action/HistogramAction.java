package com.bsuir.dip.action;

import com.bsuir.dip.drawing.Dialog;
import com.bsuir.dip.image.ImageConverter;
import com.bsuir.dip.type.Channel;
import javafx.scene.control.Alert;

public class HistogramAction extends Action {

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
