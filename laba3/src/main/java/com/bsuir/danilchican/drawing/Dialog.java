package com.bsuir.danilchican.drawing;

import javafx.scene.control.Alert;

public class Dialog {

    private Alert alert;

    public Dialog(Alert.AlertType type, String title, String headerTitle, String text) {
        alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(headerTitle);
        alert.setContentText(text);
    }

    public void show() {
        alert.showAndWait();
    }
}
