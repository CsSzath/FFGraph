package com.thau.ffgraph;

//import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.List;

import com.thau.CsvHandler.CsvController;
import com.thau.CsvHandler.ImportChecker;
import com.thau.DataModel.DataRecord;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
//import javafx.scene.input.Dragboard;
import javafx.scene.control.Tooltip;

public class NewProductController {

    ArrayList<DataRecord> records = new ArrayList<>();

@FXML
    TextField inpCompanyName, inpMachineId;
@FXML
    Label dispProductName, dispDateTime;
@FXML
    Button switchToFFGraph, btnSave, btnExit;
@FXML
    LineChart<String, Number> chtImportedTemperature;
@FXML
    LineChart<String, Number> chtImportedHumidity;

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
                        
                    }
                }
                event.setDropCompleted(isCompleted);
                event.consume();*/
    }

    private void processFile(String filePath) {
       
        //System.out.println("File dropped: " + filePath);
        
        ImportChecker importChecker = new ImportChecker(filePath);
        if(importChecker.isCsv() && importChecker.hasValidDataRecord()) {
            CsvController csvController = new CsvController();
            records = csvController.importCsvToRecords(filePath);
            String productName = csvController.getProductNameFromCsv(filePath);
            dispProductName.setText(productName);
            dispDateTime.setText(csvController.getDateTimeFromCsv(filePath));
        } else {
            System.out.println("Invalid file format or data record." +" " + importChecker.isCsv() + " " + importChecker.hasValidDataRecord());
        }

        Graph graph = new Graph(chtImportedTemperature, chtImportedHumidity, records);
        graph.populateTempChart();
        graph.populateHumidityChart();

        
    }

    @FXML
    public void addTooltipsToChart(LineChart<String, Number> chart) {
        for (XYChart.Series<String, Number> series : chart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Tooltip tooltip = new Tooltip("X: " + data.getXValue() + "\nY: " + data.getYValue());
                Tooltip.install(data.getNode(), tooltip);
                data.getNode().setOnMouseEntered(event -> tooltip.show(data.getNode(), event.getScreenX(), event.getScreenY()));
                data.getNode().setOnMouseExited(event -> tooltip.hide());
            }
        }

        /*for (XYChart.Series<String, Number> series : tempChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip("X: " + data.getXValue() + "\nY: " + data.getYValue());
            Tooltip.install(data.getNode(), tooltip);
            data.getNode().setOnMouseClicked(event -> {
                tooltip.setText("X: " + data.getXValue() + "\nY: " + data.getYValue());
            });
            }
        }
        /*for (final XYChart.Data<String, Number> data : series1.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    Tooltip.install(data.getNode(), new Tooltip("Kamrahőmérséklet: " + data.getYValue()));
                }
            });                      
        }*/
    }

}