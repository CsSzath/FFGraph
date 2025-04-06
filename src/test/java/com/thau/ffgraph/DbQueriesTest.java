package com.thau.ffgraph;

import org.junit.jupiter.api.Test;

import com.thau.CsvHandler.CsvController;
import com.thau.DataModel.DataRecord;
import com.thau.DataModel.Product;
import com.thau.Db.DbQueries;
import com.thau.Db.Registry;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class DbQueriesTest {

    @Test
    public void testInsertProduct() {

        Registry db = new Registry("Test_export.sql");
        DbQueries dbQueries = new DbQueries(db.getConnection());
        db.initializeDatabase();
        Product product = new Product("Kontra füst", "TestCompany", "TestMachineId", 44264, 0.86191);
        dbQueries.insertProduct(product); 


        int productId = dbQueries.getProductId("Kontra füst", "TestCompany", "TestMachineId", product.getDateTime());
        assertNotEquals(-1, productId, "Product insert failed: Product ID should not be -1.");
        db.closeConnection();
    }

    @Test
    public void testInsertDataRecords() {
        CsvController csvController = new CsvController();
        
        String testFilePath = "Test.csv";

        Registry db = new Registry("Test_export.sql");
        db.initializeDatabase();
        DbQueries dbQueries = new DbQueries(db.getConnection());
        Product product = new Product("Kontra füst", "TestCompany", "TestMachineId", 44264, 0.86191);
        dbQueries.insertProduct(product); 
        

        int productId = dbQueries.getProductId("Kontra füst", "TestCompany", "TestMachineId", product.getDateTime());
        
        ArrayList<DataRecord> records = csvController.importCsvToRecords(testFilePath);

        dbQueries.insertDataRecords(records, productId);


        // Check if the record was inserted correctly
        ArrayList<DataRecord> readRecords = dbQueries.getDataRecordsByProductId(productId);
        assertFalse(readRecords.isEmpty(), "Data record insert failed: No records found for the product ID.");
        
        db.closeConnection();
    }

    @Test
    public void testInsertSingleDataRecord() {

        //CsvController csvController = new CsvController();
        //String testFilePath = "Test.csv";
        DataRecord record = new DataRecord(1,44264,0.862604,0,3,60006,60001,0,684,574,27,3,65,0,0);
        Product product = new Product("Kontra füst", "TestCompany", "TestMachineId", 44264, 0.86191);
        
        
        Registry db = new Registry("Test_export.sql");
        db.initializeDatabase();
        DbQueries dbQueries = new DbQueries(db.getConnection());

        dbQueries.insertProduct(product); 

        int productId = dbQueries.getProductId("Kontra füst", "TestCompany", "TestMachineId", product.getDateTime());
        System.out.println("Product ID: " + productId);
        
       
        dbQueries.insertSingleDataRecord(record, productId); 
        
        // Check if the record was inserted correctly
        ArrayList<DataRecord> readRecords = dbQueries.getDataRecordsByProductId(productId);
        assertFalse(readRecords.isEmpty(), "Data record insert failed: No records found for the product ID.");
        
        db.closeConnection();
    }

    @Test
    public void testGetProductId() {

        Registry db = new Registry("Test_export.sql");
        db.initializeDatabase();
        DbQueries dbQueries = new DbQueries(db.getConnection());
        Product product = new Product("Kontra füst", "TestCompany", "TestMachineId", 44264, 0.86191);
        dbQueries.insertProduct(product); 

        
        int productId = dbQueries.getProductId("Kontra füst", "TestCompany", "TestMachineId", product.getDateTime());
        System.out.println("Product ID: " + productId);
        assertNotEquals(-1, productId, "Product ID should not be -1.");

        db.closeConnection();
    }

    

    @Test
    public void testGetDataRecordsByProductId() {

        CsvController csvController = new CsvController();
        String testFilePath = "Test.csv";

        Registry db = new Registry("Test_export.sql");
        db.initializeDatabase();
        DbQueries dbQueries = new DbQueries(db.getConnection());

        Product product = new Product("Kontra füst", "TestCompany", "TestMachineId", 44264, 0.86191);
        dbQueries.insertProduct(product); 

        int productId = dbQueries.getProductId("Kontra füst", "TestCompany", "TestMachineId", product.getDateTime());
        
        ArrayList<DataRecord> records = csvController.importCsvToRecords(testFilePath);

        dbQueries.insertDataRecords(records, productId);


        // Check if the record was inserted correctly
        ArrayList<DataRecord> readRecords = dbQueries.getDataRecordsByProductId(productId);
        assertEquals(6, readRecords.size(), "Data record insert failed: No records found for the product ID.");
        
        db.closeConnection();
    }


    @Test
        public void testDeleteDataRecordsByProductId() {

            ArrayList<DataRecord> records = new ArrayList<>();
            CsvController csvController = new CsvController();
            String testFilePath = "Test.csv";
            
            Registry db = new Registry("Test_export.sql");
            db.initializeDatabase();
            records = csvController.importCsvToRecords(testFilePath);
            DbQueries dbQueries = new DbQueries(db.getConnection());
            Product product = new Product("Kontra füst", "TestCompany", "TestMachineId", 44264, 0.86191);

            dbQueries.insertProduct(product);
            int productId = dbQueries.getProductId("Kontra füst", "TestCompany", "TestMachineId", product.getDateTime());
            dbQueries.insertDataRecords(records, productId);

            // Delete the data records for the given product ID
            dbQueries.deleteDataRecordsByProductId(productId);

            // Check if the records were deleted
            ArrayList<DataRecord> readRecords = dbQueries.getDataRecordsByProductId(productId);
            assertTrue(readRecords.isEmpty(), "Data records were not deleted for the product ID.");

            db.closeConnection();

        }


    @Test
    public void testDeleteProductById() {

        Registry db = new Registry("Test_export.sql");
        db.initializeDatabase();
        
        DbQueries dbQueries = new DbQueries(db.getConnection());
        Product product = new Product("Kontra füst", "TestCompany", "TestMachineId", 44264, 0.86191);
        dbQueries.insertProduct(product);

        int productId = dbQueries.getProductId("Kontra füst", "TestCompany", "TestMachineId", product.getDateTime());
        dbQueries.deleteProductById(productId);

        // Check if the product was deleted
        int deletedProductId = dbQueries.getProductId("Kontra füst", "TestCompany", "TestMachineId", product.getDateTime());
        assertEquals(-1, deletedProductId, "Product was not deleted: Product ID should be -1.");

        db.closeConnection();

    }
}
