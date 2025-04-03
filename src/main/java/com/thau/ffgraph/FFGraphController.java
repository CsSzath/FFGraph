package com.thau.ffgraph;

import java.io.IOException;

import com.thau.Db.DbQueries;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;


import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class FFGraphController implements Initializable {

    DbQueries dbQueries = new DbQueries(App.getConnection());

    @FXML
    ComboBox<String> selectCompanyName, selectMachineId, selectProductName, selectDateTime;

    @FXML
    Button switchToSecondary, btnExport, btnExit;

    @FXML
    LineChart<String, Number> chtTemperature, chtHumidity;

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            String [] existingCompaniesList = dbQueries.getAllCompanyNames().toArray(new String[0]);
            selectCompanyName.getItems().clear();
            selectCompanyName.getItems().addAll(existingCompaniesList);
            if(existingCompaniesList.length > 0) {
                selectCompanyName.setValue(existingCompaniesList[0]);
            }

            selectMachineId.setDisable(true);
            selectProductName.setDisable(true);
            selectDateTime.setDisable(true);

            System.out.println("Controller initialized.");
        }
    
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("NewProduct");
    }

    @FXML
    private void exitApp() {
        System.out.println("Exiting application...");
        App.closeConnection();
        System.exit(0);
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
            selectMachineId.getItems().clear();
            selectMachineId.getItems().addAll(existingMachineIdsList);
            
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
            selectProductName.getItems().clear();
            selectProductName.getItems().addAll(existingProductNamesList);
            
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
            selectDateTime.getItems().clear();
            selectDateTime.getItems().addAll(existingDateTimesList);
            
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
            int productId = dbQueries.getProductId(selectedProductName, selectedCompany, selectedMachineId, selectedDateTime);
            if(productId != -1) {
                System.out.println("Product ID: " + productId);
                var records = dbQueries.getDataRecordsByProductId(productId);
                chtTemperature.getData().clear();
                chtHumidity.getData().clear();
                Graph graph = new Graph(chtTemperature, chtHumidity, records);
                graph.populateTempChart();
                graph.populateHumidityChart();
            } else {
                System.out.println("No product found with the given details.");
            }
        } 
    }

}
