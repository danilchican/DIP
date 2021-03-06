package com.bsuir.dip.index;

import com.bsuir.dip.drawing.Window;
import javafx.application.Application;
import javafx.stage.Stage;
import nu.pattern.OpenCV;

public class Main extends Application {
    static {
        OpenCV.loadLocally();
    }

    public static Window window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        window = new Window(stage);
        window.show();
    }
}
