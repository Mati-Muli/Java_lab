package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Window;
import java.io.File;

public class AppController {

    @FXML private Button load, save, execute, rotateLeft, rotateRight;
    @FXML private ChoiceBox<String> operationChoiceBox;
    @FXML private ImageView originalImageView, processedImageView;

    private Image currentImage;
    private boolean isImageProcessed = false;

    @FXML
    public void initialize() {
        operationChoiceBox.getItems().addAll("Skala szarości", "Negatyw", "Skalowanie", "Progowanie", "Konturowanie");
        setControlsDisabled(true);

        load.setOnAction(e -> handleLoadFile());
        execute.setOnAction(e -> executeOperation());
        save.setOnAction(e -> Show.saveModal(processedImageView, isImageProcessed, getWindow()));
        rotateLeft.setOnAction(e -> handleRotation(-90));
        rotateRight.setOnAction(e -> handleRotation(90));
    }

    public void setImageProcessed(boolean processed) {
        this.isImageProcessed = processed;
    }

    private Window getWindow() {
        return load.getScene().getWindow();
    }

    private void setControlsDisabled(boolean disabled) {
        save.setDisable(disabled);
        execute.setDisable(disabled);
        operationChoiceBox.setDisable(disabled);
        rotateLeft.setDisable(disabled);
        rotateRight.setDisable(disabled);
    }

    private void handleLoadFile() {
        File file = Load.openFile(getWindow());
        if (file != null) {
            currentImage = new Image(file.toURI().toString());
            originalImageView.setImage(currentImage);
            processedImageView.setImage(currentImage);
            isImageProcessed = false;

            setControlsDisabled(false);
            operationChoiceBox.getSelectionModel().selectFirst();
            Toast.show(getWindow(), "Wczytano plik");
        }
    }

    private void executeOperation() {
        if (currentImage == null || operationChoiceBox.getValue() == null) return;
        String op = operationChoiceBox.getValue();

        try {
            switch (op) {
                case "Skalowanie":
                    Show.scaleModal(processedImageView, getWindow());
                    break;
                case "Progowanie":
                    Show.thresholdModal(processedImageView, this, getWindow());
                    break;
                case "Konturowanie":
                    processedImageView.setImage(Filter.applyContouring(processedImageView.getImage()));
                    isImageProcessed = true;
                    Toast.show(getWindow(), "Konturowanie pomyślne!");
                    break;
                case "Negatyw":
                    processedImageView.setImage(Filter.applyNegative(processedImageView.getImage()));
                    isImageProcessed = true;
                    Toast.show(getWindow(), "Operacja Negatyw zakończona.");
                    break;
                case "Skala szarości":
                    processedImageView.setImage(Filter.applyGrayscale(processedImageView.getImage()));
                    isImageProcessed = true;
                    Toast.show(getWindow(), "Operacja Skala szarości zakończona.");
                    break;
            }
        } catch (Exception ex) {
            Toast.show(getWindow(), op.equals("Konturowanie") ? "Nie udało się wykonać konturowania." : "Operacja nieudana.");
        }
    }

    private void handleRotation(int angle) {
        processedImageView.setImage(Transform.rotate(processedImageView.getImage(), angle));
    }
}