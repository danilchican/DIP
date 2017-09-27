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
import javafx.util.converter.NumberStringConverter;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.io.File;

public class Window {

    final private Stage stage;

    private static final String WINDOW_TITLE = "DIP Lab1";

    private static final String OPEN_FILE_BTN_TITLE = "Choose image...";
    private static final String SAVE_FILE_BTN_TITLE = "Save image";
    private static final String EXECUTE_BTN_TITLE = "Execute";
    private static final String SHOW_IMAGE_BTN_TITLE = "Show in Window";

    private static final String FILE_PREFIX = "file:///";
    private static final String DEFAULT_BG_IMAGE_PATH = "image_bg.jpg";

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int LAYOUT_PADDING = 10;
    private static final int MAX_PREVIEW_WIDTH = 300;

    private final SplitPane mainLayout;

    private final VBox leftBar;
    private final VBox rightBar;

    private final FileChooser fileChooser;

    private final Button openFileBtn;
    private final Button saveFileBtn;
    private final Button execTranslateBtn;
    private final Button showImageBtn;

    private final ImageView imageView;

    private final Label optionsLabel;
    private final Label histogramLabel;
    private final Label translateLabel;

    private Label leftThresholdLabel;
    private Label rightThresholdLabel;

    private TextField leftLineThreshold;
    private TextField rightLineThreshold;

    private final ComboBox optionsBox;
    private final ComboBox histogramsBox;
    private final ComboBox translationsBox;

    private Image image;
    private Image lastImage;

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
        execTranslateBtn = new Button(EXECUTE_BTN_TITLE);
        showImageBtn = new Button(SHOW_IMAGE_BTN_TITLE);

        leftLineThreshold = new TextField();
        rightLineThreshold = new TextField();

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
        optionsBox.setDisable(true);
        histogramsBox.setDisable(true);
        translationsBox.setDisable(true);
        saveFileBtn.setDisable(true);
        showImageBtn.setDisable(true);

        leftLineThreshold.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        rightLineThreshold.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));

        leftLineThreshold.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                leftLineThreshold.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        rightLineThreshold.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                rightLineThreshold.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

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

        rightBar.getChildren().addAll(imageView, openFileBtn, saveFileBtn, showImageBtn);
        mainLayout.getItems().addAll(leftBar, rightBar);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setLastImage(Image lastImage) {
        this.lastImage = lastImage;
    }

    public Image getLastImage() {
        return lastImage;
    }

    private void setActions() {
        openFileBtn.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(stage);

            if (file != null && file.exists()) {
                javafx.scene.image.Image displayingImage = new javafx.scene.image.Image(FILE_PREFIX + file.getAbsolutePath());
                Mat pixs = ImageLoader.load(file.getAbsolutePath());

                this.lastImage = new Image(pixs);
                this.image = new Image(pixs.clone());

                if (displayingImage.getWidth() > MAX_PREVIEW_WIDTH) {
                    imageView.setFitWidth(MAX_PREVIEW_WIDTH);
                }

                imageView.setImage(displayingImage);
                optionsBox.setDisable(false);
                saveFileBtn.setDisable(false);
                showImageBtn.setDisable(false);
            }
        });

        showImageBtn.setOnAction(event -> {
            getLastImage().show();
        });

        saveFileBtn.setOnAction(event -> {
            image = lastImage;

            if (image == null) {
                new Dialog(
                        Alert.AlertType.ERROR, "Error",
                        null, "Image is not selected."
                ).show();

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

                    clearTranslationsObject();
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

                    clearTranslationsObject();
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

    public void replaceImage() {
        if(getLastImage() != null && getImage() != getLastImage()) {
            MatOfByte byteMat = new MatOfByte();
            Imgcodecs.imencode(".jpg", getLastImage().getImg(), byteMat);
            javafx.scene.image.Image displayingImage = new javafx.scene.image.Image(new ByteArrayInputStream(byteMat.toArray()));

            if (displayingImage.getWidth() > MAX_PREVIEW_WIDTH) {
                imageView.setFitWidth(MAX_PREVIEW_WIDTH);
            }

            imageView.setImage(displayingImage);
            System.out.println("Image replaced.");
        }
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
                clearTranslationsObject();
                break;
            case GRAYSCALE:
                clearTranslationsObject();
                action.executeGS();
                break;
            case BIN_PREPARING:
                clearTranslationsObject();
                leftThresholdLabel = new Label("Threshold:");
                leftBar.getChildren().addAll(leftThresholdLabel, leftLineThreshold, execTranslateBtn);

                actionBinPrepExecute();
                break;
            case PREPARING:
                clearTranslationsObject();
                leftThresholdLabel = new Label("Left threshold:");
                rightThresholdLabel = new Label("Right threshold:");

                leftBar.getChildren().addAll(leftThresholdLabel, leftLineThreshold,
                        rightThresholdLabel, rightLineThreshold, execTranslateBtn);
                actionPrepExecute();
                break;
            case SOBIEL:
                clearTranslationsObject();
                action.executeSobiel();
                break;
            default:
                throw new IllegalArgumentException("Translation item was not found");
        }
    }

    private void clearTranslationsObject() {
        leftBar.getChildren().removeAll(leftThresholdLabel, leftLineThreshold);
        leftBar.getChildren().removeAll(rightThresholdLabel, rightLineThreshold, execTranslateBtn);
    }

    private void actionBinPrepExecute() {
        execTranslateBtn.setOnAction(event -> {
            TranslationAction action = new TranslationAction();
            int value = Integer.parseInt(leftLineThreshold.getText());

            action.execute();
            action.executeBinPreparing(value);
        });
    }

    private void actionPrepExecute() {
        execTranslateBtn.setOnAction(event -> {
            TranslationAction action = new TranslationAction();
            int left = Integer.parseInt(leftLineThreshold.getText());
            int right = Integer.parseInt(rightLineThreshold.getText());

            action.execute();
            action.executePreparing(left, right);
        });
    }
}
