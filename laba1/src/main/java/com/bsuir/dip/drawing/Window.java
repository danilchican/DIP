package com.bsuir.dip.drawing;

import com.bsuir.dip.Option;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Window {

    final private Stage stage;

    private static final String WINDOW_TITLE = "DIP Lab1";
    private static final String OPEN_FILE_BTN_TITLE = "Choose image...";
    private static final String SAVE_FILE_BTN_TITLE = "Save image";
    private static final String FILE_PREFIX = "file:///";

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int LAYOUT_PADDING = 10;

    private final VBox leftBar;
    private final VBox rightBar;
    private final SplitPane mainLayout;

    private final FileChooser fileChooser;
    private final Button openFileBtn;
    private final Button saveFileBtn;

    private final ImageView imageView;

    private final Label optionsLabel;
    private final Label histogramLabel;
    private final Label translateLabel;

    private ObservableList<String> options;
    private ObservableList<String> histograms;
    private ObservableList<String> translations;

    private final ComboBox optionsBox;
    private final ComboBox histogramsBox;
    private final ComboBox translationsBox;

    public Window(Stage stage) {
        this.stage = stage;
        fileChooser = new FileChooser();
        imageView = new ImageView();

        openFileBtn = new Button(OPEN_FILE_BTN_TITLE);
        saveFileBtn = new Button(SAVE_FILE_BTN_TITLE);

        leftBar = new VBox();
        rightBar = new VBox();
        mainLayout = new SplitPane();

        optionsLabel = new Label("Options:");
        histogramLabel = new Label("Histogram:");
        translateLabel = new Label("Translate:");

        options = FXCollections.observableArrayList(Option.getAsArray());
        histograms = FXCollections.observableArrayList("", "GrayScale", "R", "G", "B");
        translations = FXCollections.observableArrayList("", "GrayScale", "Preparing", "Sobiel operator");

        optionsBox = new ComboBox<>(options);
        optionsBox.getSelectionModel().selectFirst();

        histogramsBox = new ComboBox<>(histograms);
        translationsBox = new ComboBox<>(translations);
    }

    public void draw() {
        stage.setTitle(WINDOW_TITLE);

        this.fillScene();
        this.setActions();

        StackPane root = new StackPane();
        root.getChildren().addAll(mainLayout);
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.setResizable(false);
        stage.show();
    }

    private void fillScene() {
        histogramsBox.setDisable(true);
        translationsBox.setDisable(true);

        mainLayout.setOrientation(Orientation.HORIZONTAL);
        mainLayout.setDividerPositions(0.5);

        leftBar.setPadding(new Insets(LAYOUT_PADDING));
        leftBar.setSpacing(5);

        rightBar.setPadding(new Insets(LAYOUT_PADDING));
        rightBar.setSpacing(10);

        leftBar.getChildren().addAll(optionsLabel, optionsBox);
        leftBar.getChildren().addAll(histogramLabel, histogramsBox);
        leftBar.getChildren().addAll(translateLabel, translationsBox);

        Image image = new Image(FILE_PREFIX + "D:/VLAD/image_bg.jpg");
        imageView.setImage(image);
        imageView.setPreserveRatio(true);

        rightBar.getChildren().addAll(imageView, openFileBtn, saveFileBtn);
        mainLayout.getItems().addAll(leftBar, rightBar);
    }

    private void setActions() {
        openFileBtn.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(stage);

            if (file.exists()) {
                Image image = new Image(FILE_PREFIX + file.getAbsolutePath());

                if (image.getWidth() > 300) {
                    imageView.setFitWidth(300);
                }

                imageView.setImage(image);
            }
        });

        optionsBox.getSelectionModel().selectedItemProperty().addListener((ov, t, t1) -> {
            if (t1 != null) {
                int id = optionsBox.getSelectionModel().getSelectedIndex();
                Option option = Option.findById(id);
            }
        });
    }
}
