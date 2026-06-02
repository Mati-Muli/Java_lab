package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Filter {

    public static Image applyContouring(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader();
        PixelWriter wOut = out.getPixelWriter();

        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                double gx = -r.getColor(x-1, y-1).getBrightness() + r.getColor(x+1, y-1).getBrightness()
                        - 2*r.getColor(x-1, y).getBrightness() + 2*r.getColor(x+1, y).getBrightness()
                        - r.getColor(x-1, y+1).getBrightness() + r.getColor(x+1, y+1).getBrightness();

                double gy = -r.getColor(x-1, y-1).getBrightness() - 2*r.getColor(x, y-1).getBrightness() - r.getColor(x+1, y-1).getBrightness()
                        + r.getColor(x-1, y+1).getBrightness() + 2*r.getColor(x, y+1).getBrightness() + r.getColor(x+1, y+1).getBrightness();

                double val = Math.sqrt(gx * gx + gy * gy);
                val = Math.min(val, 1.0);
                wOut.setColor(x, y, new Color(val, val, val, 1.0));
            }
        }
        return out;
    }

    public static Image applyThreshold(Image img, int threshold) {
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

    public static Image applyGrayscale(Image img) {
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

    public static Image applyNegative(Image img) {
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
}