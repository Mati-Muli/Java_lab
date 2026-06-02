package com.example.demo;
import javafx.stage.Window;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;

public class Show {

    public static void scaleModal(ImageView processedImageView, Window owner) {
        Stage m = new Stage();
        m.initOwner(owner);
        m.initModality(Modality.APPLICATION_MODAL);
        TextField wF = new TextField(String.valueOf((int)processedImageView.getImage().getWidth()));
        TextField hF = new TextField(String.valueOf((int)processedImageView.getImage().getHeight()));
        Label err = new Label(); err.setStyle("-fx-text-fill: red;");
        Button btn = new Button("Zmień rozmiar");

        btn.setOnAction(e -> {
            try {
                int w = Integer.parseInt(wF.getText()), h = Integer.parseInt(hF.getText());
                if (w > 0 && w <= 3000 && h > 0 && h <= 3000) {
                    processedImageView.setImage(Transform.scale(processedImageView.getImage(), w, h));
                    m.close();
                } else err.setText("Zakres 1-3000!");
            } catch (Exception ex) { err.setText("Pole wymagane"); }
        });
        m.setScene(new Scene(new VBox(10, new Label("Szerokość:"), wF, new Label("Wysokość:"), hF, err, btn), 250, 200));
        m.showAndWait();
    }

    public static void thresholdModal(ImageView processedImageView, AppController controller, Window owner) {
        Stage modal = new Stage();
        modal.initOwner(owner);
        modal.initModality(Modality.APPLICATION_MODAL);
        TextField tf = new TextField("128");
        Button btn = new Button("Wykonaj progowanie");

        btn.setOnAction(e -> {
            try {
                processedImageView.setImage(Filter.applyThreshold(processedImageView.getImage(), Integer.parseInt(tf.getText())));
                controller.setImageProcessed(true);
                Toast.show(owner, "Progowanie wykonane pomyślnie!");
                modal.close();
            } catch (Exception ex) { Toast.show(owner, "Błąd progowania"); }
        });
        modal.setScene(new Scene(new VBox(10, new Label("Próg:"), tf, btn), 200, 150));
        modal.showAndWait();
    }

    public static void saveModal(ImageView processedImageView, boolean isImageProcessed, Window owner) {
        Stage modal = new Stage();
        modal.initOwner(owner);
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
            if (f.exists()) { Toast.show(owner, "Plik już istnieje!"); return; }

            try {
                Load.saveFile(processedImageView.getImage(), f);
                Toast.show(owner, "Zapisano: " + n + ".jpg");
                modal.close();
            } catch (Exception ex) { Toast.show(owner, "Błąd zapisu"); }
        });

        cB.setOnAction(e -> modal.close());
        layout.getChildren().addAll(new Label("Nazwa:"), nF, err, bB);
        modal.setScene(new Scene(layout, 300, 200));
        modal.showAndWait();
    }
}