package com.bsuir.dip.index;

import com.bsuir.dip.drawing.Window;
import com.bsuir.dip.image.Image;
import com.bsuir.dip.image.ImageConverter;
import com.bsuir.dip.image.ImageLoader;
import com.bsuir.dip.type.Channel;
import javafx.application.Application;
import javafx.stage.Stage;
import nu.pattern.OpenCV;

public class Main extends Application {
    static {
        OpenCV.loadLocally();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        //Window window = new Window(stage);
        //window.show();

        Image image = new Image(ImageLoader.load("D:/test.png"));

        //image.execBinPreparing(130);
        //image.execPreparing(80, 130);

        Image gs = ImageConverter.convertToGS(image);

        gs.execSobel();
        gs.show();
        //image.showHistogram(Channel.ALL);

//        Image gs = ImageConverter.convertToGS(image);
//        gs.execBinPreparing(50);
//        gs.show();
//        gs.showHistogram(Channel.ALL);
//
//        ImageLoader.save("D:/gs.jpg", gs);
    }
}
