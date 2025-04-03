package com.thau.CsvHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ImportChecker {

    String filePath;

    public ImportChecker(String filePath) {
        this.filePath = filePath;
    }

    public boolean isCsv() {
        return filePath != null && filePath.toLowerCase().endsWith(".csv");
    }

    public boolean hasValidDataRecord() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null || lineCount < 6) {
                lineCount++;
                if (lineCount == 5) {
                    String[] parts = line.split(",");
                    if (parts.length != 15) {
                        return false;
                    }
                }
            }
            return lineCount >= 6;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
