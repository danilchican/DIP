package com.bsuir.dip.drawing;

import com.bsuir.dip.action.TranslationAction;
import com.bsuir.dip.image.Image;
import com.bsuir.dip.image.ImageLoader;
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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Vector;

public class Window {

    final private Stage stage;

    private static final String WINDOW_TITLE = "DIP Lab1";

    private static final String OPEN_FILE_BTN_TITLE = "Choose image...";
    private static final String SAVE_FILE_BTN_TITLE = "Save image";
    private static final String EXECUTE_BTN_TITLE = "Execute";
    private static final String SHOW_STATISTICS_TITLE = "Show Statistics";
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
    private final Button showStatisticsBtn;

    private final ImageView imageView;

    private final Label translateLabel;

    private Label leftThresholdLabel;
    private Label rightThresholdLabel;

    private TextField leftLineThreshold;
    private TextField rightLineThreshold;

    private final ComboBox translationsBox;

    private Image image;
    private Image lastImage;

    private JTable table;

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
        showStatisticsBtn = new Button(SHOW_STATISTICS_TITLE);

        leftLineThreshold = new TextField();
        rightLineThreshold = new TextField();

        leftBar = new VBox();
        rightBar = new VBox();
        mainLayout = new SplitPane();

        translateLabel = new Label("Translate:");

        ObservableList<String> translations = FXCollections.observableArrayList(Translation.getAsArray());

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
        translationsBox.setDisable(true);
        saveFileBtn.setDisable(true);
        showImageBtn.setDisable(true);
        showStatisticsBtn.setDisable(true);

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

        leftBar.getChildren().addAll(translateLabel, translationsBox);

        javafx.scene.image.Image image = new javafx.scene.image.Image(DEFAULT_BG_IMAGE_PATH);
        imageView.setImage(image);
        imageView.setPreserveRatio(true);

        rightBar.getChildren().addAll(imageView, openFileBtn, saveFileBtn, showImageBtn, showStatisticsBtn);
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
                translationsBox.setDisable(false);
                saveFileBtn.setDisable(false);
                showImageBtn.setDisable(false);
                showStatisticsBtn.setDisable(false);
            }
        });

        showImageBtn.setOnAction(event -> {
            getLastImage().show();
        });

        showStatisticsBtn.setOnAction(event -> {
            showStatistics();
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
        if (getLastImage() != null && getImage() != getLastImage()) {
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

    private void handleByTranslateItem(Translation item) {
        System.out.println("Selected translation item: " + item.getTitle());
        TranslationAction action = new TranslationAction();
        action.execute();

        switch (item) {
            case EMPTY:
                clearTranslationsObject();
                break;
            case COLORIZE:
                clearTranslationsObject();
                action.executeColorize();
                break;
            case CLASTERIZE:
                clearTranslationsObject();
                action.executeClasterize();
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

    private void showStatistics() {
        System.out.println("Showing statistics");
        getLastImage().calcMassCenter();

        fillTableData();

        JFrame frame = new JFrame();
        frame.add(new JScrollPane(table));

        frame.setTitle("Statistics");
        frame.pack();
        frame.setVisible(true);
    }

    private void fillTableData() {
        Vector<String> columnNames = new Vector<String>() {{
            add("#");
            add("Space");
            add("Perimeter");
            add("Compactness");
            add("MassCenter");
            add("Claster");
            add("Color");
        }};

        Vector<Vector<String>> allData = new Vector<>();
        Image im = getLastImage();

        for (int i = 0; i < im.getAreas().size(); i++) {
            Vector<String> data = new Vector<>();

            data.add(String.valueOf(i + 1));
            data.add(String.valueOf(im.getAreas().get(i).getSpace()));
            data.add(String.valueOf(im.getAreas().get(i).getPerimeter()));
            data.add(String.valueOf(im.getAreas().get(i).getCompactness()));
            data.add("x: " + String.valueOf(im.getAreas().get(i).getMassCenterX()) +
                    "; y: " + im.getAreas().get(i).getMassCenterY());
            data.add(String.valueOf(im.getAreas().get(i).getClaster()));
            data.add(String.valueOf(Image.getColorName(i)));

            allData.add(data);
        }

        table = new JTable(allData, columnNames);
        table.setRowHeight(26);
        table.setBounds(WIDTH, HEIGHT, table.getColumnCount() * 80, table.getRowCount() * 28);
    }
}
