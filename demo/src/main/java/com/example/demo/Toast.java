package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class Toast {

    public static void show(Window owner, String msg) {
        Popup p = new Popup();
        Label l = new Label(msg);
        l.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 5;");
        p.getContent().add(l);

        p.setOnShown(e -> {
            p.setX(owner.getX() + (owner.getWidth() - p.getWidth()) / 2);
            p.setY(owner.getY() + owner.getHeight()-180);
        });

        p.show(owner);
        PauseTransition d = new PauseTransition(Duration.seconds(2));
        d.setOnFinished(e -> p.hide());
        d.play();
    }
}