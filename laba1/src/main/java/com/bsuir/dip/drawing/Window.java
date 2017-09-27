package com.bsuir.dip.drawing;

import com.bsuir.dip.action.HistogramAction;
import com.bsuir.dip.action.TranslationAction;
import com.bsuir.dip.image.Image;
import com.bsuir.dip.image.ImageLoader;
import com.bsuir.dip.type.Channel;
import com.bsuir.dip.type.HistogramItem;
import com.bsuir.dip.type.Option;
import com.bsuir.dip.action.IAction;
import com.bsuir.dip.type.Translation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private static final int MAX_PREVIEW_WIDTH = 300;

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
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

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
        ObservableList<String> translations = FXCollections.observableArrayList(Translation.getAsArray());

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
        saveFileBtn.setDisable(true);

        mainLayout.setOrientation(Orientation.HORIZONTAL);
        mainLayout.setDividerPositions(0.5);

        leftBar.setPadding(new Insets(LAYOUT_PADDING));
        leftBar.setSpacing(5);

        rightBar.setPadding(new Insets(LAYOUT_PADDING));
        rightBar.setSpacing(10);

        leftBar.getChildren().addAll(optionsLabel, optionsBox);
        leftBar.getChildren().addAll(histogramLabel, histogramsBox);
        leftBar.getChildren().addAll(translateLabel, translationsBox);

        javafx.scene.image.Image image = new javafx.scene.image.Image(DEFAULT_BG_IMAGE_PATH);
        imageView.setImage(image);
        imageView.setPreserveRatio(true);

        rightBar.getChildren().addAll(imageView, openFileBtn, saveFileBtn);
        mainLayout.getItems().addAll(leftBar, rightBar);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    private void setActions() {
        openFileBtn.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(stage);

            if (file != null && file.exists()) {
                javafx.scene.image.Image displayingImage = new javafx.scene.image.Image(FILE_PREFIX + file.getAbsolutePath());
                this.image = new Image(ImageLoader.load(file.getAbsolutePath()));

                if (displayingImage.getWidth() > MAX_PREVIEW_WIDTH) {
                    imageView.setFitWidth(MAX_PREVIEW_WIDTH);
                }

                imageView.setImage(displayingImage);
                saveFileBtn.setDisable(false);
            }
        });

        saveFileBtn.setOnAction(event -> {
            saveFileBtn.setDisable(true);

            if (image == null) {
                new Dialog(
                        Alert.AlertType.ERROR, "Error",
                        null, "Image is not selected."
                ).show();

                saveFileBtn.setDisable(false);
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(SAVE_FILE_BTN_TITLE);
            fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("Image (*.jpg, *.png)", "*.jpg", "*.png"));

            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                ImageLoader.save(file.getAbsolutePath(), image);

                new Dialog(
                        Alert.AlertType.INFORMATION, "Success",
                        null, "Image has been saved."
                ).show();
            }

            saveFileBtn.setDisable(false);
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
                        Translation item = Translation.findById(id);

                        this.handleByTranslateItem(item);
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
            case RGB:
                action.executeByChannel(Channel.ALL);
                break;
            case R:
                action.executeByChannel(Channel.RED);
                break;
            case G:
                action.executeByChannel(Channel.GREEN);
                break;
            case B:
                action.executeByChannel(Channel.BLUE);
                break;
            default:
                throw new IllegalArgumentException("Histogram item was not found");
        }
    }

    private void handleByTranslateItem(Translation item) {
        System.out.println("Selected translation item: " + item.getTitle());
        TranslationAction action = new TranslationAction();
        action.execute();

        switch (item) {
            case EMPTY:
                break;
            case GRAYSCALE:
                action.executeGS();
                break;
            case BIN_PREPARING:
                action.executeBinPreparing();
                break;
            case PREPARING:
                action.executePreparing();
                break;
            case SOBIEL:
                action.executeSobiel();
                break;
            default:
                throw new IllegalArgumentException("Translation item was not found");
        }
    }
}
