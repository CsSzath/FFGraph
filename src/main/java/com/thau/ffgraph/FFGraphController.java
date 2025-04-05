package com.thau.ffgraph;

import java.io.IOException;

import com.thau.CsvHandler.CsvController;
import com.thau.DataModel.DataRecord;
import com.thau.Db.DbQueries;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.fxml.Initializable;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FFGraphController implements Initializable {

    DbQueries dbQueries = new DbQueries(App.getConnection());
    StepsBar stepsBar = new StepsBar();
    ArrayList<DataRecord> records = new ArrayList<>(); // Initialize the records list

    @FXML
    ComboBox<String> selectCompanyName, selectMachineId, selectProductName, selectDateTime;

    @FXML
    Button switchToSecondary, btnExport, btnExit, btnDeleteProduct;

    @FXML
    LineChart<String, Number> chtTemperature, chtHumidity;

    @FXML
    Label lblError, lblTotalTime, lblMaxCoreTemp, lblTimeAboveTreshold;

    @FXML
    HBox barSteps;

    @FXML
    TextField inpCoreTempThreshold;

    @FXML
    TilePane pnlSummary;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String [] existingCompaniesList = dbQueries.getAllCompanyNames().toArray(new String[0]);

        selectCompanyName.setPromptText("---");
        selectMachineId.setPromptText("---");
        selectProductName.setPromptText("---");
        selectDateTime.setPromptText("---");

        selectCompanyName.getItems().clear();
        selectCompanyName.getItems().addAll(existingCompaniesList);

        //if(existingCompaniesList.length > 0) {
        //    selectCompanyName.setValue(existingCompaniesList[0]);
        //}
        lblError.setVisible(false);
        pnlSummary.setVisible(false);
        selectMachineId.setDisable(true);
        selectProductName.setDisable(true);
        selectDateTime.setDisable(true);
        btnExport.setDisable(true);
        btnDeleteProduct.setDisable(true);

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
        App.exportDatabase();
        App.closeConnection();
        System.exit(0);
    }

    @FXML
    private void bindBarStepsToChartWidth() {
        chtTemperature.widthProperty().addListener((observable, oldValue, newValue) -> {
            barSteps.setPrefWidth(newValue.doubleValue());
            stepsBar.setBarBox(barSteps);                                                   // Update the HBox in stepsBar
            stepsBar.generateRectangles();                                                  // Regenerate rectangles and labels
        });
    }

    @FXML
    private void exportData() {
        //System.out.println("Exporting data...");
        CsvController csvController = new CsvController();
        DataRecord firstRecord = records.get(0);
        String startDate = java.time.LocalDate.of(1900, 1, 1).plusDays(firstRecord.getDate()).toString();
        String startTime = java.time.LocalTime.ofSecondOfDay((long) (firstRecord.getTime() * 60 * 60 * 24)).toString();
        startTime = startTime.replace(":", "-");
        String fileName = selectCompanyName.getValue() + "_" 
                        + selectMachineId.getValue() + "_" 
                        + selectProductName.getValue() + "_" 
                        + startDate + "_"
                        + startTime + ".csv";
        csvController.exportRecordsToCsv(fileName, selectProductName.getValue(), records);
    }

    @FXML
        private void deleteProduct() {
            String selectedCompany = selectCompanyName.getValue();
            String selectedMachineId = selectMachineId.getValue();
            String selectedProductName = selectProductName.getValue();
            String selectedDateTime = selectDateTime.getValue();
            
            int productId = dbQueries.getProductId(selectedProductName, selectedCompany, selectedMachineId, selectedDateTime);
            
            if(productId != -1) {
                dbQueries.deleteDataRecordsByProductId(productId);
                dbQueries.deleteProductById(productId);
                selectMachineId.getItems().clear();
                selectProductName.getItems().clear();
                selectDateTime.getItems().clear();
                clearData();
                lblError.setText("Sikeresen törölve: " + selectedProductName + " (" + selectedDateTime + ")");
                lblError.setVisible(true);
            } else {
                lblError.setText("Adatbázis hiba: Nincs ilyen termék!");
                lblError.setVisible(true);
            }
        }

    @FXML
    private void handleChartClick(MouseEvent event) {
        // Get the source chart from the event
        if (event.getSource() instanceof LineChart<?, ?>) {
            LineChart<String, Number> chart = (LineChart<String, Number>) event.getSource();
            double mouseX = event.getX();
            javafx.scene.chart.Axis<String> xAxis = chart.getXAxis();
            String xValue = xAxis.getValueForDisplay(mouseX);
            //System.out.println("Clicked X value: " + xValue);

            for(int i =0; i < records.size(); i++) {
                DataRecord recordY = records.get(i);
                String yValue = LocalTime.ofSecondOfDay((long) (recordY.getTime() * 60 * 60 * 24)).toString();
                if(yValue.equals(xValue)) {
                    //System.out.println("Clicked Y value: " + records.get(i).getCoreTemp() / 10);
                    Tooltip tooltip = new Tooltip("Kamrahőmérséklet: " + String.format("%.1f", (float) records.get(i).getAirTemp() / 10) + " °C\n" +
                                                "Maghőmérséklet " + String.format("%.1f", (float) records.get(i).getCoreTemp() / 10) + " °C\n" +
                                                "Páratartalom: " + records.get(i).getHumidity() + " %\n" +
                                                "Eltelt idő: " + Calculations.getElapsedTime(records.get(i)));
                    tooltip.setAutoHide(true);
                    tooltip.show(chart, event.getScreenX(), event.getScreenY());
                    break;
                }
            }
        }
    }

    @FXML
    private void companyChosen() {
        if(selectCompanyName.getValue() != null) {
            //System.out.println("Selected company: " + selectCompanyName.getValue());
            String selectedCompany = selectCompanyName.getValue();
            String[] existingMachineIdsList = dbQueries.getMachineIdsByCompanyName(selectedCompany).toArray(new String[0]);
            
            //Clear out the previous selections
                
                selectMachineId.getItems().clear();
                selectProductName.getItems().clear();
                selectDateTime.getItems().clear();
                clearData();

            //Start new selection process
            selectMachineId.getItems().addAll(existingMachineIdsList);
            //System.out.println("Selected company: " + selectedCompany);
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
                clearData();
            
            selectProductName.getItems().addAll(existingProductNamesList);
            //System.out.println("Selected machine: " + selectedMachineId);
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
                clearData();
            

            selectDateTime.getItems().addAll(existingDateTimesList);
            //System.out.println("Selected product: " + selectedProductName);
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
            //System.out.println("Selected date and time: " + selectedDateTime);
            int productId = dbQueries.getProductId(selectedProductName, selectedCompany, selectedMachineId, selectedDateTime);
            if(productId != -1) {
                //System.out.println("Product ID: " + productId);
                records = dbQueries.getDataRecordsByProductId(productId);
                btnExport.setDisable(false);
                btnDeleteProduct.setDisable(false);
                
                //Charts
                chtTemperature.getData().clear();
                chtHumidity.getData().clear();
                Graph graph = new Graph(chtTemperature, chtHumidity, records);
                graph.populateTempChart();
                graph.populateHumidityChart();

                //Summary panel
                lblTotalTime.setText(Calculations.getTotalTime(records));
                lblMaxCoreTemp.setText(Calculations.getHighestCoreTemp(records));
                String stringCoreTempThreshold = inpCoreTempThreshold.getText();
                if(stringCoreTempThreshold.isBlank() || stringCoreTempThreshold.isEmpty()) {
                    inpCoreTempThreshold.setText("80");
                }
                lblTimeAboveTreshold.setText(Calculations.countRecordsAboveCoreTemp(records, Integer.parseInt(inpCoreTempThreshold.getText())));
                pnlSummary.setVisible(true); // Show the summary panel

                // stepsBar
                stepsBar.setBarBox(barSteps);                                           // Set the HBox to the new one
                stepsBar.setStepTypes(dbQueries.getProcessStepsByProductId(productId)); // Set the step types to the new ones
        
                stepsBar.calculatePercentages();                                        // Call the method to calculate percentages
                stepsBar.generateRectangles();                                          // Call the method to generate rectangles and labels
                barSteps.getChildren().clear();                                         // Clear the existing rectangles and labels
                barSteps = stepsBar.getBarBox();                                        // Call the method to draw the rectangles and labels

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

    @FXML
    private void chkThreshold() {
        if(!inpCoreTempThreshold.getText().matches("\\d+")) {
            lblError.setText("Kérjük, adjon meg egy egész számot a küszöbértékhez!");
            lblError.setVisible(true);
            inpCoreTempThreshold.setText("80"); 
        } else {
            System.out.println("Time above threshold will be updated with: " + inpCoreTempThreshold.getText());
            lblTimeAboveTreshold.setText(Calculations.countRecordsAboveCoreTemp(records, Integer.parseInt(inpCoreTempThreshold.getText())));
        }
    }

    @FXML
    private void clearData() {
        chtTemperature.getData().clear();
        chtHumidity.getData().clear();
        pnlSummary.setVisible(false);
        barSteps.getChildren().clear();
        btnExport.setDisable(true);
        btnDeleteProduct.setDisable(true);
    }

    @FXML
    private void initCompanySelectionComboBox() {
        selectCompanyName.getSelectionModel().clearSelection();
        selectMachineId.getItems().clear();
        selectProductName.getItems().clear();
        selectDateTime.getItems().clear();
        clearData();
    }

}
