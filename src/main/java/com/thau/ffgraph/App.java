package com.thau.ffgraph;

//import com.thau.CsvHandler.CsvController;
//import com.thau.DataModel.DataRecord;
//import com.thau.DataModel.Product;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
//import java.util.ArrayList;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    //private static String filePath;
    //private static String fileName;
    //private static String fullPath = filePath + fileName;
    //private static ArrayList<DataRecord> records = new ArrayList<>();





    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("FFGraph"), 1024, 768);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        
        Registry db = new Registry("database_export.sql");
        int initResult = db.initializeDatabase();    
        //CsvController csvController = new CsvController();
        
         
        if(initResult == -1) { 
            System.out.println("Database initialization failed.");
            
        } else if (initResult == 0) {
            System.out.println("New Database was initialized.");
            
            /*fileName = "Test.csv";


            records = csvController.importCsvToRecords(fileName);
            System.out.println("Records imported: " + records.size());
    
            String productName = csvController.getProductNameFromCsv(fileName);
            if(productName == null) { 
                productName = "No_Data";
            }
            
            Product product = new Product(productName, "TestCompany", "TestMachineId", records.get(0).getDate(), records.get(0).getTime());
            System.out.println("Product: " + product.toString());
            db.insertProduct(product);
            int productId = db.getProductId(productName, "TestCompany", "TestMachineId");
    
            if(productId == -1) {
                System.out.println("Product insert failed.");
            } else {
                db.insertDataRecords(records, productId);
                System.out.println("Product ID: " + productId);
            }
            
            records = db.getDataRecordsByProductId(productId);
            System.out.println("Records retrieved: " + records.size());
            csvController.exportRecordsToCsv("Exported.csv", "TestProductExport", records);

            db.exportDatabase();*/

        } else {
            System.out.println("Database was already present.");

        }
        

        



        launch();
    





    }

    public static Window getStage() {
        return scene.getWindow();
    }

}