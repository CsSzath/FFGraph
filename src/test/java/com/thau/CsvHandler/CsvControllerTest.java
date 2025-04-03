package com.thau.CsvHandler;

import org.junit.jupiter.api.Test;


import com.thau.DataModel.DataRecord;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;



public class CsvControllerTest {

    @Test
    public void testExportRecordsToCsv() {
        CsvController csvController = new CsvController();
        String testFilePath = "test_export.csv";
        String productName = "TestProduct";

        // Create test records
        ArrayList<DataRecord> records = new ArrayList<>();
        records.add(new DataRecord(0, 44264, 0.86191, 1000, 1, 5000, 2, 1, 25, 30, 50, 1, 20, 25, 40));

        // Test the method
        csvController.exportRecordsToCsv(testFilePath, productName, records);

        // Verify the file content
        try (BufferedReader reader = new BufferedReader(new FileReader(testFilePath))) {
            assertEquals("", reader.readLine());
            assertEquals(productName, reader.readLine());
            assertEquals("This is a generated file from a stored database.", reader.readLine());
            assertEquals("id,date,time,milliseconds,dayOfWeek,elapsedTimeMs,subInterval,triggered,airTemp,coreTemp,humidity,stepType,setAirTemp,setCoreTemp,setHumidity", reader.readLine());
            assertEquals("0,44264,0.86191,1000,1,5000,2,1,25,30,50,1,20,25,40", reader.readLine());
        } catch (Exception e) {
            fail("Failed to read test CSV file.");
        }

        
        new File(testFilePath).delete();
    }




    @Test
    public void testImportCsvToRecords() {
        CsvController csvController = new CsvController();
        String testFilePath = "Test.csv";
      

        // Test the method
        ArrayList<DataRecord> records = csvController.importCsvToRecords(testFilePath);
        DataRecord record = records.get(0);
        assertEquals(6, records.size());
        assertEquals(44264, record.getDate());
        assertEquals(0.86191, record.getTime());
        assertEquals(0, record.getMilliseconds());
        assertEquals(525, record.getCoreTemp());

        record = records.get(3);
        assertEquals(44264, record.getDate());
        assertEquals(0.863993, record.getTime());
        assertEquals(656, record.getAirTemp());
        assertEquals(49, record.getHumidity());

        
    }

    
    

    @Test
    public void testGetProductNameFromCsv() {
        CsvController csvController = new CsvController();
        String testFilePath = "Test.csv";

        // Test the method
        String productName = csvController.getProductNameFromCsv(testFilePath);
        assertEquals("Kontra füst", productName);
    }

    
}
