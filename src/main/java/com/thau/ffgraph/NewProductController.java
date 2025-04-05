package com.thau.ffgraph;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.thau.CsvHandler.CsvController;
import com.thau.CsvHandler.ImportChecker;
import com.thau.DataModel.DataRecord;
import com.thau.DataModel.Product;
import com.thau.Db.DbQueries;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;


public class NewProductController implements Initializable{

    ArrayList<DataRecord> records = new ArrayList<>();
    DbQueries dbQueries = new DbQueries(App.getConnection());
    StepsBar stepsBar = new StepsBar();

@FXML
    ComboBox<String> selectCompanyName, selectMachineId;
@FXML
    TextField inpCompanyName, inpMachineId, inpCoreTempThreshold;
@FXML
    Label dispProductName, dispDateTime, lblError, lblTotalTime, lblMaxCoreTemp, lblTimeAboveTreshold;
@FXML
    Button switchToFFGraph, btnSave, btnExit;
@FXML
    LineChart<String, Number> chtImportedTemperature;
@FXML
    LineChart<String, Number> chtImportedHumidity;
@FXML
    HBox barSteps;
@FXML
    TilePane pnlSummary;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        String [] existingCompaniesList = dbQueries.getAllCompanyNames().toArray(new String[0]);
            selectCompanyName.getItems().clear();
            selectCompanyName.getItems().addAll(existingCompaniesList);
            
            selectCompanyName.setPromptText("---");
            selectMachineId.setPromptText("---");
            dispProductName.setText("---");
            dispDateTime.setText("---");
            pnlSummary.setVisible(false);

            lblError.setVisible(false);
            selectMachineId.setDisable(true);

            bindBarStepsToChartWidth();
            //System.out.println("NewProduct Controller initialized.");
    }



    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("FFGraph");
    }

    @FXML
    private void handleFileDragAndDrop() {
        App.getStage().getScene().setOnDragOver(event -> {
            if (event.getGestureSource() != App.getStage() &&
                event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            }
            event.consume();
        });

        App.getStage().getScene().setOnDragDropped(event -> {
            var dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                var file = dragboard.getFiles().get(0); // Get the first file
                processFile(file.getAbsolutePath());
            }
            event.setDropCompleted(true);
            event.consume();
        });

       /*DragEvent event = new DragEvent();
               
        Dragboard dragboard = event.getDragboard();
                boolean isCompleted = false;
                if (dragboard.hasFiles())
                {
                    List<File> files = dragboard.getFiles();
                    if (files.size() > 0)
                    {
                        var file = dragboard.getFiles().get(0); // Get the first file
                        processFile(file.getAbsolutePath());
                    }
                }
                event.setDropCompleted(isCompleted);
                event.consume();*/
    }

    private void processFile(String filePath) {
       
        //System.out.println("File dropped: " + filePath);
        
        ImportChecker importChecker = new ImportChecker(filePath);
        CsvController csvController = new CsvController();
        if(importChecker.isCsv() && importChecker.hasValidDataRecord()) {
            records = csvController.importCsvToRecords(filePath);
            String productName = csvController.getProductNameFromCsv(filePath);
            dispProductName.setText(productName);
            dispDateTime.setText(csvController.getDateTimeFromCsv(filePath));
        } else {
            lblError.setText("A behúzott file nem megfelelő formátumú vagy nem tartalmaz adatokat.");
            lblError.setVisible(true);
            System.out.println("Invalid file format or data record. " + importChecker.isCsv() + " " + importChecker.hasValidDataRecord());
        }

        Graph graph = new Graph(chtImportedTemperature, chtImportedHumidity, records);
        graph.populateTempChart();
        graph.populateHumidityChart();

        stepsBar.setBarBox(barSteps); // Set the HBox to the new one
        stepsBar.setStepTypes(csvController.getProcessStepsFromCsv(records)); // Set the step types to the new ones
        


        stepsBar.calculatePercentages(); // Call the method to calculate percentages
        stepsBar.generateRectangles(); // Call the method to generate rectangles and labels
        barSteps.getChildren().clear(); // Clear the existing rectangles and labels
        barSteps = stepsBar.getBarBox(); // Call the method to draw the rectangles and labels

        //Summary panel
        lblTotalTime.setText(Calculations.getTotalTime(records));
        lblMaxCoreTemp.setText(Calculations.getHighestCoreTemp(records));
        String stringCoreTempThreshold = inpCoreTempThreshold.getText();
        if(stringCoreTempThreshold.isBlank() || stringCoreTempThreshold.isEmpty()) {
            inpCoreTempThreshold.setText("80");
        }
        lblTimeAboveTreshold.setText(Calculations.countRecordsAboveCoreTemp(records, Integer.parseInt(inpCoreTempThreshold.getText())));
        pnlSummary.setVisible(true); // Show the summary panel

  }

@FXML
private void bindBarStepsToChartWidth() {
    chtImportedTemperature.widthProperty().addListener((observable, oldValue, newValue) -> {
        barSteps.setPrefWidth(newValue.doubleValue());
        stepsBar.setBarBox(barSteps); // Update the HBox in stepsBar
        stepsBar.generateRectangles(); // Regenerate rectangles and labels
    });
}


    @FXML
    private void addTooltipToTemperatureChartArea() {
        chtImportedTemperature.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();
            String tooltipText = "Mouse Position - X: " + x + ", Y: " + y;
            Tooltip tooltip = new Tooltip(tooltipText);
            Tooltip.install(chtImportedTemperature, tooltip);
            tooltip.show(chtImportedTemperature, event.getScreenX(), event.getScreenY());
        });

        chtImportedTemperature.setOnMouseExited(event -> {
            Tooltip.uninstall(chtImportedTemperature, null);
        });
    }


    /*@FXML
    public void addTooltipsToChart(LineChart<String, Number> chart) {
        for (XYChart.Series<String, Number> series : chart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Tooltip tooltip = new Tooltip("X: " + data.getXValue() + "\nY: " + data.getYValue());
                Tooltip.install(data.getNode(), tooltip);
                data.getNode().setOnMouseEntered(event -> tooltip.show(data.getNode(), event.getScreenX(), event.getScreenY()));
                data.getNode().setOnMouseExited(event -> tooltip.hide());
            }
        }*/

        /*for (XYChart.Series<String, Number> series : tempChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip("X: " + data.getXValue() + "\nY: " + data.getYValue());
            Tooltip.install(data.getNode(), tooltip);
            data.getNode().setOnMouseClicked(event -> {
                tooltip.setText("X: " + data.getXValue() + "\nY: " + data.getYValue());
            });
            }
        }*/

        /*for (final XYChart.Data<String, Number> data : series1.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    Tooltip.install(data.getNode(), new Tooltip("Kamrahőmérséklet: " + data.getYValue()));
                }
            });                      
        }
    }*/
    @FXML
    private void saveData() {
        String companyName = inpCompanyName.getText();
        if(companyName == null || companyName.isEmpty() || companyName.isBlank()) {
            companyName = "Ismeretlen";
        }
        String machineId = inpMachineId.getText();
        if(machineId == null || machineId.isEmpty() || machineId.isBlank()) {
            machineId = "Ismeretlen";
        }
        
        Product product = new Product(dispProductName.getText(), companyName, machineId, records.get(0).getDate(), records.get(0).getTime());
        DbQueries dbQueries = new DbQueries(App.getConnection());

        if(dbQueries.getProductId(companyName, companyName, machineId, machineId) == -1){
            dbQueries.insertProduct(product);
            int productId = dbQueries.getProductId(dispProductName.getText(), companyName, machineId, dispDateTime.getText());
            if (productId != -1) {
                dbQueries.insertDataRecords(records, productId);
                //System.out.println("New Product ID: " + productId);
                //Clear out imported data
                companyName = "";
                machineId = "";
                inpCompanyName.setText(companyName);
                inpMachineId.setText(machineId);
                records.clear();
                chtImportedTemperature.getData().clear();
                chtImportedHumidity.getData().clear();
                dispProductName.setText("---");
                dispDateTime.setText("---");
                barSteps.getChildren().clear();
                pnlSummary.setVisible(false); // Hide the summary panel


            } else {
                lblError.setText("Sikertelen mentés.");
                lblError.setVisible(true);
                System.out.println("Product insert failed. Data records were not inserted.");
            }
        } else {
            lblError.setText("A termék már szerepel az adatbázisban.");
            lblError.setVisible(true);
            System.out.println("Product already exists in the database. Data records were not inserted.");
        }

        
    }

    @FXML
    private void exitApp() {
        //System.out.println("Exiting application...");
        App.closeConnection();
        System.exit(0);
    }

    @FXML
    private void companyChosen() {
        if(selectCompanyName.getValue() != null) {
            String selectedCompany = selectCompanyName.getValue();
            String[] existingMachineIdsList = dbQueries.getMachineIdsByCompanyName(selectedCompany).toArray(new String[0]);
            selectMachineId.getItems().clear();
            selectMachineId.getItems().addAll(existingMachineIdsList);
            
            selectMachineId.setDisable(false);
            inpCompanyName.setText(selectedCompany);
            inpMachineId.setText("");


        } else {
            selectMachineId.setDisable(true);
        }
    }

    @FXML
    private void machineChosen() {
        if(selectMachineId.getValue() != null) {
            inpMachineId.setText(selectMachineId.getValue());
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


}