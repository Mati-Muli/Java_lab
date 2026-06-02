package com.example.demo;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Load {

    public static File openFile(Window ownerWindow) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        return fc.showOpenDialog(ownerWindow);
    }

    public static void saveFile(Image fxImage, File file) throws IOException {
        BufferedImage b = SwingFXUtils.fromFXImage(fxImage, null);
        BufferedImage rgb = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgb.createGraphics();
        g.setColor(java.awt.Color.white);
        g.fillRect(0, 0, b.getWidth(), b.getHeight());
        g.drawImage(b, 0, 0, null);
        g.dispose();
        ImageIO.write(rgb, "jpg", file);
    }
}