package com.thau.ffgraph;

import org.junit.jupiter.api.Test;

import com.thau.Db.Registry;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.sql.DriverManager;


public class RegistryTest {
    

    /*@Test
    public void testCloseConnection() {

    }*/

    @Test
    public void testInitializeDatabase() {
        Registry db = new Registry("Test_export.sql");
        db.initializeDatabase();


        try (var connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
         var statement = connection.createStatement();
         var resultSet = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'PRODUCT'")) {

        assertTrue(resultSet.next(), "DB Init Failed: Table 'PRODUCT' should exist in the database.");
    } catch (Exception e) {
        fail("An exception occurred while checking the database: " + e.getMessage());
    } finally {
        db.closeConnection();
    }
    }

    @Test
    public void testExportDatabase() {
        Registry db = new Registry("Test_export.sql");
        db.initializeDatabase();

        db.exportDatabase();
        File exportFile = new File("Test_export.sql");
        assertTrue(exportFile.exists(), "SqlFile export Failed: File 'Test_export.sql' should exist.");
        assertTrue(exportFile.length() > 0, "SqlFile export Failed: File 'Test_export.sql' should not be empty.");
        
        
        exportFile.delete(); 
        db.closeConnection();
    }

    
    
}
