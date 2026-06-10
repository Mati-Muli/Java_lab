package com.example.demo;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
   public static void log(String level, String message) {
        try (FileWriter fw = new FileWriter("app_log.txt", true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fw.write(String.format("[%s] [%s] %s%n", timestamp, level, message));
        } catch (Exception e) {
            System.err.println("Błąd logowania: " + e.getMessage());
        }
    }
}
