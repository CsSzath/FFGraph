
package com.thau.CsvHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.thau.DataModel.DataRecord;



public class CsvController {

    public ArrayList<DataRecord> importCsvToRecords(String filePath) {
        ArrayList<DataRecord> records = new ArrayList<>();
        DataRecord record;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));     
        } catch (IOException e) {
            e.printStackTrace();
        }
            String line;
            int lineNumber = 0;
            try {
                while ((line = br.readLine()) != null) {
                    lineNumber++;
                    if (lineNumber < 5) {
                        continue; 
                    }
                    try {
                        String[] fields = line.split(","); 
                        
                        record = new DataRecord(
                            Integer.parseInt(fields[0]), 
                            Integer.parseInt(fields[1]), 
                            Double.parseDouble(fields[2]), 
                            Integer.parseInt(fields[3]), 
                            Integer.parseInt(fields[4]), 
                            Long.parseLong(fields[5]), 
                            Integer.parseInt(fields[6]), 
                            Integer.parseInt(fields[7]), 
                            Integer.parseInt(fields[8]), 
                            Integer.parseInt(fields[9]), 
                            Integer.parseInt(fields[10]), 
                            Integer.parseInt(fields[11]), 
                            Integer.parseInt(fields[12]), 
                            Integer.parseInt(fields[13]), 
                            Integer.parseInt(fields[14])  
                        );
                    } catch (NumberFormatException e) {
                        System.err.println("Corrupted data in line " + lineNumber + ": " + e.getMessage());
                        continue; 
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Missing data in line " + lineNumber + ": " + e.getMessage());
                        continue; 
                    } catch (Exception e) {
                        System.err.println("Unexpected error on line " + lineNumber + ": " + e.getMessage());
                        continue; 
                    }
                    records.add(record);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
       
        return records;
    }


    public String getProductNameFromCsv(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); 
            return br.readLine(); 
        } catch (IOException e) {
            e.printStackTrace();
            return "Unknown Product";
        }
    }


    public void exportRecordsToCsv(String filePath, String productName, ArrayList<DataRecord> records) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(""); // Clear the file
            bw.newLine(); // First line empty
            bw.write(productName); // Write product name on the second line
            bw.newLine(); 
            bw.write("This is a generated file from a stored database."); // Write a comment on the third line
            bw.newLine(); 
            // Write empty lines until the 5th line
            bw.write("id,date,time,milliseconds,dayOfWeek,elapsedTimeMs,subInterval,triggered,airTemp,coreTemp,humidity,stepType,setAirTemp,setCoreTemp,setHumidity"); // Write header on the 4th line
            bw.newLine();
            // Write records starting from the 5th line
            for (int i = 0; i < records.size(); i++) {
                DataRecord record = records.get(i);
                bw.write(String.join(",",
                    String.valueOf(i),
                    String.valueOf(record.getDate()),
                    String.valueOf(record.getTime()),
                    String.valueOf(record.getMilliseconds()),
                    String.valueOf(record.getDayOfWeek()),
                    String.valueOf(record.getElapsedTimeMs()),
                    String.valueOf(record.getSubInterval()),
                    String.valueOf(record.getTriggered()),
                    String.valueOf(record.getAirTemp()),
                    String.valueOf(record.getCoreTemp()),
                    String.valueOf(record.getHumidity()),
                    String.valueOf(record.getStepType()),
                    String.valueOf(record.getSetAirTemp()),
                    String.valueOf(record.getSetCoreTemp()),
                    String.valueOf(record.getSetHumidity())
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDateTimeFromCsv(String filePath) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;
    
            while ((line = reader.readLine()) != null || lineCount < 6) {
                lineCount++;
                if (lineCount == 5) {
                    String[] parts = line.split(",");
                    int date = Integer.parseInt(parts[1]);
                    Double time = Double.parseDouble(parts[2]);

                    return java.time.LocalDate.of(1900, 1, 1).plusDays(date) + 
                            " " + 
                            java.time.LocalTime.ofSecondOfDay((long) (time * 60 * 60 * 24)); // Combine date and time fields
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown DateTime";
    }

    public ArrayList<Integer> getProcessStepsFromCsv(ArrayList<DataRecord> records) {
        ArrayList<Integer> processSteps = new ArrayList<>();
        for (DataRecord record : records) {
            int stepType = record.getStepType();
            processSteps.add(stepType);
        }
        return processSteps;
    }

}
