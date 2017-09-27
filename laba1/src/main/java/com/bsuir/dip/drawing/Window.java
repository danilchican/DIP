package com.bsuir.dip.drawing;

import com.bsuir.dip.action.HistogramAction;
import com.bsuir.dip.type.HistogramItem;
import com.bsuir.dip.type.Option;
import com.bsuir.dip.action.IAction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private static final String DEFAULT_BG_IMAGE_PATH = "image_bg.jpg";

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

    private final ComboBox optionsBox;
    private final ComboBox histogramsBox;
    private final ComboBox translationsBox;

    private Image image;

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

        ObservableList<String> options = FXCollections.observableArrayList(Option.getAsArray());
        ObservableList<String> histograms = FXCollections.observableArrayList(HistogramItem.getAsArray());
        ObservableList<String> translations = FXCollections.observableArrayList("", "GrayScale", "Preparing", "Sobiel operator");

        optionsBox = new ComboBox<>(options);
        optionsBox.getSelectionModel().selectFirst();

        histogramsBox = new ComboBox<>(histograms);
        translationsBox = new ComboBox<>(translations);
    }

    public void show() {
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

        Image image = new Image(DEFAULT_BG_IMAGE_PATH);
        imageView.setImage(image);
        imageView.setPreserveRatio(true);

        rightBar.getChildren().addAll(imageView, openFileBtn, saveFileBtn);
        mainLayout.getItems().addAll(leftBar, rightBar);
    }

    public Image getImage() {
        return image;
    }

    private void setActions() {
        openFileBtn.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(stage);
            // TODO change
            if (file.exists()) {
                image = new Image(FILE_PREFIX + file.getAbsolutePath());

                if (image.getWidth() > 300) {
                    imageView.setFitWidth(300);
                }

                imageView.setImage(image);
            }
        });

        optionsBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((ov, t, t1) -> {
                    if (t1 != null) {
                        int id = optionsBox.getSelectionModel().getSelectedIndex();
                        Option option = Option.findById(id);

                        this.handleByOption(option);
                    }
                });

        histogramsBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((ov, t, t1) -> {
                    if (t1 != null) {
                        int id = histogramsBox.getSelectionModel().getSelectedIndex();
                        HistogramItem item = HistogramItem.findById(id);

                        this.handleByHistogramItem(item);
                    }
                });

        translationsBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((ov, t, t1) -> {
                    if (t1 != null) {
                        int id = translationsBox.getSelectionModel().getSelectedIndex();
                        Option option = Option.findById(id);

                        this.handleByOption(option);
                    }
                });
    }

    private void handleByOption(Option option) {
        System.out.println("Selected option: " + option.getTitle());

        switch (option) {
            case VIEW_IMAGE:
                histogramsBox.setDisable(true);
                translationsBox.setDisable(true);
                break;
            case VIEW_HIST:
                histogramsBox.setDisable(false);
                translationsBox.setDisable(true);
                break;
            case TRANSLATION:
                histogramsBox.setDisable(true);
                translationsBox.setDisable(false);
                break;
            default:
                throw new IllegalArgumentException("Option was not found");
        }

        IAction action = option.getAction();

        if (action != null) {
            action.execute();
        }
    }

    private void handleByHistogramItem(HistogramItem item) {
        System.out.println("Selected histogram item: " + item.getTitle());
        HistogramAction action = new HistogramAction();
        action.execute();

        switch (item) {
            case EMPTY:
                break;
            case GRAYSCALE:
                action.executeGS();
                break;
            case R:
                action.executeR();
                break;
            case G:
                action.executeG();
                break;
            case B:
                action.executeB();
                break;
            default:
                throw new IllegalArgumentException("Histogram item was not found");
        }
    }
}
