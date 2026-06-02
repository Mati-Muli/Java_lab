package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class Transform {

    public static Image scale(Image img, int nw, int nh) {
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

    public static Image rotate(Image img, int angle) {
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
        return out;
    }
}