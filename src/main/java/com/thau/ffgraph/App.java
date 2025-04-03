package com.thau.ffgraph;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Connection;

import com.thau.Db.Registry;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Registry db;

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
        
        db = new Registry("database_export.sql");
        int initResult = db.initializeDatabase();    
        
        
         
        if(initResult == -1) { 
            System.out.println("Database initialization failed.");
            
        } else if (initResult == 0) {
            System.out.println("New Database was initialized.");
        } else {
            System.out.println("Database was already present.");

        }
        launch();
    }

    public static Window getStage() {
        return scene.getWindow();
    }

    public static Connection getConnection() {
       return db.getConnection();
    }

    public static void closeConnection() {
        db.closeConnection();
    }

    public static void exportDatabase () {
        db.exportDatabase();
    }
}