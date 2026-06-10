package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Filter {

    public static Image applyContouring(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader();
        PixelWriter wOut = out.getPixelWriter();

        ExecutorService executor = Executors.newFixedThreadPool(4);
        int totalRows = h - 2;
        int chunkSize = totalRows / 4;

        for (int i = 0; i < 4; i++) {
            final int startY = 1 + (i * chunkSize);
            final int endY = (i == 3) ? h - 1 : 1 + ((i + 1) * chunkSize);

            executor.submit(() -> {
                for (int y = startY; y < endY; y++) {
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
            });
        }

        waitForCompletion(executor);
        return out;
    }

    public static Image applyThreshold(Image img, int threshold) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader();
        PixelWriter wOut = out.getPixelWriter();
        double t = threshold / 255.0;

        ExecutorService executor = Executors.newFixedThreadPool(4);
        int chunkSize = h / 4;

        for (int i = 0; i < 4; i++) {
            final int startY = i * chunkSize;
            final int endY = (i == 3) ? h : (i + 1) * chunkSize;

            executor.submit(() -> {
                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < w; x++) {
                        double avg = r.getColor(x, y).getBrightness();
                        wOut.setColor(x, y, (avg > t) ? Color.WHITE : Color.BLACK);
                    }
                }
            });
        }

        waitForCompletion(executor);
        return out;
    }

    public static Image applyGrayscale(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader();
        PixelWriter wOut = out.getPixelWriter();

        ExecutorService executor = Executors.newFixedThreadPool(4);
        int chunkSize = h / 4;

        for (int i = 0; i < 4; i++) {
            final int startY = i * chunkSize;
            final int endY = (i == 3) ? h : (i + 1) * chunkSize;

            executor.submit(() -> {
                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < w; x++) {
                        double g = r.getColor(x, y).getBrightness();
                        wOut.setColor(x, y, new Color(g, g, g, 1.0));
                    }
                }
            });
        }

        waitForCompletion(executor);
        return out;
    }

    public static Image applyNegative(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader();
        PixelWriter wOut = out.getPixelWriter();

        ExecutorService executor = Executors.newFixedThreadPool(4);
        int chunkSize = h / 4;

        for (int i = 0; i < 4; i++) {
            final int startY = i * chunkSize;
            final int endY = (i == 3) ? h : (i + 1) * chunkSize;

            executor.submit(() -> {
                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < w; x++) {
                        Color c = r.getColor(x, y);
                        wOut.setColor(x, y, c.invert());
                    }
                }
            });
        }

        waitForCompletion(executor);
        return out;
    }

    private static void waitForCompletion(ExecutorService executor) {
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}