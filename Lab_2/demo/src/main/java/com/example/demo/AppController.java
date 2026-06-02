package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
        save.setDisable(true);
        execute.setDisable(true);
        operationChoiceBox.setDisable(true);
        rotateLeft.setDisable(true);
        rotateRight.setDisable(true);

        load.setOnAction(e -> loadFile());
        execute.setOnAction(e -> executeOperation());
        save.setOnAction(e -> showSaveModal());
        rotateLeft.setOnAction(e -> rotateImage(-90));
        rotateRight.setOnAction(e -> rotateImage(90));
    }

    private void loadFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        File file = fc.showOpenDialog(load.getScene().getWindow());

        if (file != null) {
            currentImage = new Image(file.toURI().toString());
            originalImageView.setImage(currentImage);
            processedImageView.setImage(currentImage);
            isImageProcessed = false;
            save.setDisable(false);
            execute.setDisable(false);
            operationChoiceBox.setDisable(false);
            rotateLeft.setDisable(false);
            rotateRight.setDisable(false);
            operationChoiceBox.getSelectionModel().selectFirst();
            showToast("Wczytano plik");
        }
    }

    private void executeOperation() {
        if (currentImage == null || operationChoiceBox.getValue() == null) return;
        String op = operationChoiceBox.getValue();

        try {
            switch (op) {
                case "Skalowanie": showScaleModal(); break;
                case "Progowanie": showThresholdModal(); break;
                case "Konturowanie":
                    processedImageView.setImage(applyContouring(processedImageView.getImage()));
                    isImageProcessed = true;
                    showToast("Konturowanie zostało przeprowadzone pomyślnie!");
                    break;
                case "Negatyw":
                    processedImageView.setImage(applyNegative(processedImageView.getImage()));
                    isImageProcessed = true;
                    showToast("Operacja Negatyw zakończona.");
                    break;
                case "Skala szarości":
                    processedImageView.setImage(applyGrayscale(processedImageView.getImage()));
                    isImageProcessed = true;
                    showToast("Operacja Skala szarości zakończona.");
                    break;
            }
        } catch (Exception ex) {
            showToast(op.equals("Konturowanie") ? "Nie udało się wykonać konturowania." : "Operacja nieudana.");
        }
    }

    // --- Algorytmy ---

    private Image applyContouring(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader();
        PixelWriter wOut = out.getPixelWriter();

        // Maska Sobela
        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                double gx = -r.getColor(x-1, y-1).getBrightness() + r.getColor(x+1, y-1).getBrightness()
                        - 2*r.getColor(x-1, y).getBrightness() + 2*r.getColor(x+1, y).getBrightness()
                        - r.getColor(x-1, y+1).getBrightness() + r.getColor(x+1, y+1).getBrightness();

                double gy = -r.getColor(x-1, y-1).getBrightness() - 2*r.getColor(x, y-1).getBrightness() - r.getColor(x+1, y-1).getBrightness()
                        + r.getColor(x-1, y+1).getBrightness() + 2*r.getColor(x, y+1).getBrightness() + r.getColor(x+1, y+1).getBrightness();

                double val = Math.sqrt(gx * gx + gy * gy);
                wOut.setColor(x, y, new Color(val, val, val, 1.0));
            }
        }
        return out;
    }

    private Image applyThreshold(Image img, int threshold) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader();
        PixelWriter wOut = out.getPixelWriter();
        double t = threshold / 255.0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                double avg = r.getColor(x, y).getBrightness();
                wOut.setColor(x, y, (avg > t) ? Color.WHITE : Color.BLACK);
            }
        }
        return out;
    }

    private Image applyGrayscale(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader();
        PixelWriter wOut = out.getPixelWriter();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                double g = r.getColor(x, y).getBrightness();
                wOut.setColor(x, y, new Color(g, g, g, 1.0));
            }
        }
        return out;
    }

    private Image applyNegative(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader();
        PixelWriter wOut = out.getPixelWriter();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = r.getColor(x, y);
                wOut.setColor(x, y, c.invert());
            }
        }
        return out;
    }

    private Image scaleImage(Image img, int nw, int nh) {
        WritableImage out = new WritableImage(nw, nh);
        PixelReader r = img.getPixelReader();
        PixelWriter wOut = out.getPixelWriter();
        double wf = img.getWidth() / nw, hf = img.getHeight() / nh;
        for (int y = 0; y < nh; y++) {
            for (int x = 0; x < nw; x++) {
                wOut.setColor(x, y, r.getColor(Math.min((int)(x*wf), (int)img.getWidth()-1), Math.min((int)(y*hf), (int)img.getHeight()-1)));
            }
        }
        return out;
    }

    private void rotateImage(int angle) {
        Image img = processedImageView.getImage();
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(h, w);
        PixelReader r = img.getPixelReader();
        PixelWriter wOut = out.getPixelWriter();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (angle == 90) wOut.setColor(h - 1 - y, x, r.getColor(x, y));
                else wOut.setColor(y, w - 1 - x, r.getColor(x, y));
            }
        }
        processedImageView.setImage(out);
    }

    // --- Okna modalne i Toasty ---

    private void showScaleModal() {
        Stage m = new Stage();
        m.initModality(Modality.APPLICATION_MODAL);
        TextField wF = new TextField(String.valueOf((int)processedImageView.getImage().getWidth()));
        TextField hF = new TextField(String.valueOf((int)processedImageView.getImage().getHeight()));
        Label err = new Label(); err.setStyle("-fx-text-fill: red;");
        Button btn = new Button("Zmień rozmiar");
        btn.setOnAction(e -> {
            try {
                int w = Integer.parseInt(wF.getText()), h = Integer.parseInt(hF.getText());
                if (w > 0 && w <= 3000 && h > 0 && h <= 3000) {
                    processedImageView.setImage(scaleImage(processedImageView.getImage(), w, h));
                    m.close();
                } else err.setText("Zakres 1-3000!");
            } catch (Exception ex) { err.setText("Pole wymagane"); }
        });
        m.setScene(new Scene(new VBox(10, new Label("Szerokość:"), wF, new Label("Wysokość:"), hF, err, btn), 250, 200));
        m.showAndWait();
    }

    private void showThresholdModal() {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        TextField tf = new TextField("128");
        Button btn = new Button("Wykonaj progowanie");
        btn.setOnAction(e -> {
            try {
                processedImageView.setImage(applyThreshold(processedImageView.getImage(), Integer.parseInt(tf.getText())));
                isImageProcessed = true;
                showToast("Progowanie wykonane pomyślnie!");
                modal.close();
            } catch (Exception ex) { showToast("Błąd progowania"); }
        });
        modal.setScene(new Scene(new VBox(10, new Label("Próg:"), tf, btn), 200, 150));
        modal.showAndWait();
    }

    private void showSaveModal() {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        VBox layout = new VBox(10); layout.setPadding(new Insets(20));
        if (!isImageProcessed) {
            Label al = new Label("Na pliku nie wykonano operacji!");
            al.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
            layout.getChildren().add(al);
        }
        TextField nF = new TextField(); nF.setPromptText("Nazwa (3-100 znaków)");
        Label err = new Label();
        Button sB = new Button("Zapisz"), cB = new Button("Anuluj");
        HBox bB = new HBox(10, sB, cB); bB.setAlignment(Pos.CENTER);

        sB.setOnAction(e -> {
            String n = nF.getText().trim();
            if (n.length() < 3) { err.setText("Wpisz min 3 znaki"); return; }
            File d = new File(System.getProperty("user.home"), "Pictures");
            File f = new File(d, n + ".jpg");
            if (f.exists()) { showToast("Plik już istnieje!"); return; }
            try {
                BufferedImage b = SwingFXUtils.fromFXImage(processedImageView.getImage(), null);
                BufferedImage rgb = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = rgb.createGraphics(); g.setColor(java.awt.Color.white); g.fillRect(0,0,b.getWidth(),b.getHeight()); g.drawImage(b,0,0,null); g.dispose();
                ImageIO.write(rgb, "jpg", f);
                showToast("Zapisano: " + n + ".jpg");
                modal.close();
            } catch (Exception ex) { showToast("Błąd zapisu"); }
        });
        cB.setOnAction(e -> modal.close());
        layout.getChildren().addAll(new Label("Nazwa:"), nF, err, bB);
        modal.setScene(new Scene(layout, 300, 200));
        modal.showAndWait();
    }

    private void showToast(String msg) {
        Stage s = (Stage) load.getScene().getWindow();
        Popup p = new Popup();
        Label l = new Label(msg);
        l.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 5;");
        p.getContent().add(l);
        p.setOnShown(e -> { p.setX(s.getX() + (s.getWidth()-p.getWidth())/2); p.setY(s.getY() + s.getHeight()-100); });
        p.show(s);
        PauseTransition d = new PauseTransition(Duration.seconds(2));
        d.setOnFinished(e -> p.hide());
        d.play();
    }
}