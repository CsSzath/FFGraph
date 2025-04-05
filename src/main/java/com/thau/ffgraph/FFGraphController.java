package com.thau.ffgraph;

import java.io.IOException;

import com.thau.Db.DbQueries;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class FFGraphController implements Initializable {

    DbQueries dbQueries = new DbQueries(App.getConnection());
    StepsBar stepsBar = new StepsBar();

    @FXML
    ComboBox<String> selectCompanyName, selectMachineId, selectProductName, selectDateTime;

    @FXML
    Button switchToSecondary, btnExport, btnExit;

    @FXML
    LineChart<String, Number> chtTemperature, chtHumidity;

    @FXML
    Label lblError;

    @FXML
    HBox barSteps;

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            String [] existingCompaniesList = dbQueries.getAllCompanyNames().toArray(new String[0]);
            selectCompanyName.getItems().clear();
            selectCompanyName.getItems().addAll(existingCompaniesList);
            if(existingCompaniesList.length > 0) {
                selectCompanyName.setValue(existingCompaniesList[0]);
            }
            lblError.setVisible(false);
            selectMachineId.setDisable(true);
            selectProductName.setDisable(true);
            selectDateTime.setDisable(true);

            bindBarStepsToChartWidth();

            //System.out.println("FFGraph Controller initialized.");
        }
    
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("NewProduct");
    }

    @FXML
    private void exitApp() {
        //System.out.println("Exiting application...");
        App.closeConnection();
        System.exit(0);
    }

    @FXML
    private void bindBarStepsToChartWidth() {
        chtTemperature.widthProperty().addListener((observable, oldValue, newValue) -> {
            barSteps.setPrefWidth(newValue.doubleValue());
            stepsBar.setBarBox(barSteps); // Update the HBox in stepsBar
            stepsBar.generateRectangles(); // Regenerate rectangles and labels
        });
    }

    @FXML
    private void exportData() {
        System.out.println("Exporting data...");
        App.exportDatabase();
    }

    @FXML
    private void companyChosen() {
        if(selectCompanyName.getValue() != null) {
            String selectedCompany = selectCompanyName.getValue();
            String[] existingMachineIdsList = dbQueries.getMachineIdsByCompanyName(selectedCompany).toArray(new String[0]);
            
            //Clear out the previous selections
           
                selectMachineId.getItems().clear();
                selectProductName.getItems().clear();
                selectDateTime.getItems().clear();
                chtTemperature.getData().clear();
                chtHumidity.getData().clear();
            

            //Start new selection process
            selectMachineId.getItems().addAll(existingMachineIdsList);
            System.out.println("Selected company: " + selectedCompany);
            selectMachineId.setDisable(false);
        } else {
            selectMachineId.setDisable(true);
        }
    }

    @FXML
    private void machineChosen() {
        if(selectMachineId.getValue() != null) {
            String selectedCompany = selectCompanyName.getValue();
            String selectedMachineId = selectMachineId.getValue();
            String[] existingProductNamesList = dbQueries.getNamesByCompanyAndMachine(selectedCompany, selectedMachineId).toArray(new String[0]);
            
            //Clear out the previous selections
            
                selectProductName.getItems().clear();
                selectDateTime.getItems().clear();
                chtTemperature.getData().clear();
                chtHumidity.getData().clear();
            
            selectProductName.getItems().addAll(existingProductNamesList);
            System.out.println("Selected machine: " + selectedMachineId);
            selectProductName.setDisable(false);
        } else {
            selectProductName.setDisable(true);
        }
    }
    @FXML
    private void productChosen() {
        if(selectProductName.getValue() != null) {
            String selectedCompany = selectCompanyName.getValue();
            String selectedMachineId = selectMachineId.getValue();
            String selectedProductName = selectProductName.getValue();
            String[] existingDateTimesList = dbQueries.getDateTimesByCompanyMachineAndName(selectedCompany, selectedMachineId, selectedProductName).toArray(new String[0]);
            
            //Clear out the previous selections
            
                selectDateTime.getItems().clear();
                chtTemperature.getData().clear();
                chtHumidity.getData().clear();
            

            selectDateTime.getItems().addAll(existingDateTimesList);
            System.out.println("Selected product: " + selectedProductName);
            selectDateTime.setDisable(false);
        } else {
            selectDateTime.setDisable(true);
        }
    }    

    @FXML
    private void dateTimeChosen() {
        if(selectDateTime.getValue() != null) {
            String selectedCompany = selectCompanyName.getValue();
            String selectedMachineId = selectMachineId.getValue();
            String selectedProductName = selectProductName.getValue();
            String selectedDateTime = selectDateTime.getValue();
            System.out.println("Selected date and time: " + selectedDateTime);
            int productId = dbQueries.getProductId(selectedProductName, selectedCompany, selectedMachineId, selectedDateTime);
            if(productId != -1) {
                System.out.println("Product ID: " + productId);
                var records = dbQueries.getDataRecordsByProductId(productId);
                chtTemperature.getData().clear();
                chtHumidity.getData().clear();
                Graph graph = new Graph(chtTemperature, chtHumidity, records);
                graph.populateTempChart();
                graph.populateHumidityChart();

                stepsBar.setBarBox(barSteps); // Set the HBox to the new one
                stepsBar.setStepTypes(dbQueries.getProcessStepsByProductId(productId)); // Set the step types to the new ones
        
                stepsBar.calculatePercentages(); // Call the method to calculate percentages
                stepsBar.generateRectangles(); // Call the method to generate rectangles and labels
                barSteps.getChildren().clear(); // Clear the existing rectangles and labels
                barSteps = stepsBar.getBarBox(); // Call the method to draw the rectangles and labels

            } else {
                lblError.setText("Adatbázis hiba: Nincs ilyen termék!");
                lblError.setVisible(true);
                System.out.println("No product found with the given details.");
            }
        } 
    }

    @FXML
    private void clearErrorLabel() {
        lblError.setVisible(false);
    }

}
