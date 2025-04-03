
package com.thau.ffgraph;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class Registry {
    
    private static final String JDBC_URL = "jdbc:h2:mem:testdb"; // In-memory H2 database
    //private static final String JDBC_URL = "jdbc:h2:~/testdb"; // File-based H2 database
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    private String EXPORT_FILE;


    private Connection connection;
    // Constructor to initialize the connection
    public Registry(String EXPORT_FILE) {
        this.EXPORT_FILE = EXPORT_FILE;
        try {
            this.connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Method to close the connection
    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() {
        return this.connection;
    }

    public int initializeDatabase() {
        try (Statement statement = connection.createStatement()) {

            File exportFile = new File(EXPORT_FILE);

            if (exportFile.exists()) {
                // Import the database from the export file
                System.out.println("Importing database from export file...");
                try (FileReader reader = new FileReader(exportFile)) {
                    StringBuilder sql = new StringBuilder();
                    int c;
                    while ((c = reader.read()) != -1) {
                        sql.append((char) c);
                    }
                    statement.execute(sql.toString());
                    return 1; // Return 1 if the database is imported successfully
                }
            } else {
                // Create new tables
                System.out.println("Export file not found. Creating new tables...");

                String createProductTableSql = "CREATE TABLE IF NOT EXISTS Product (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(255), " +
                        "companyName VARCHAR(255), " +
                        "machineId VARCHAR(255), " +
                        "dateTime VARCHAR(255)" +
                        ");";
                statement.execute(createProductTableSql);

                String createDataRecordTableSql = "CREATE TABLE IF NOT EXISTS DataRecord (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "date INT, " +
                        "time DOUBLE, " +
                        "milliseconds INT, " +
                        "airTemp INT, " +
                        "coreTemp INT, " +
                        "humidity INT, " +
                        "stepType INT, " +
                        "setAirTemp INT, " +
                        "setCoreTemp INT, " +
                        "setHumidity INT, " +
                        "productId INT, " +
                        "FOREIGN KEY (productId) REFERENCES Product(id)" +
                        ");";
                statement.execute(createDataRecordTableSql);
                return 0; // Return 0 if new tables are created
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return -1 in case of an error
        }
    }




    public void exportDatabase() {
        try (Statement statement = connection.createStatement();
             FileWriter writer = new FileWriter(EXPORT_FILE)) {

            // Export the database schema and data
            System.out.println("Exporting database to file...");
            var resultSet = statement.executeQuery("SCRIPT");
            while (resultSet.next()) {
                writer.write(resultSet.getString(1) + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    





    
}
