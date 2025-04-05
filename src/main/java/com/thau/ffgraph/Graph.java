package com.thau.ffgraph;

import java.util.ArrayList;

import com.thau.DataModel.DataRecord;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;


public class Graph {

    private LineChart<String, Number> tempChart;
    private LineChart<String, Number> humidityChart;
    private ArrayList<DataRecord> dataRecords;
    

    public Graph(LineChart<String, Number> tempChart, LineChart<String, Number> humidityChart, ArrayList<DataRecord> dataRecord) {
        this.tempChart = tempChart;
        this.humidityChart = humidityChart;
        this.dataRecords = dataRecord;
    }

    public void populateTempChart() {

        tempChart.getData().clear(); //Clear the chart data before populating it again
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Kamrahőmérséklet");
        for (DataRecord dataRecord : dataRecords) {
            series1.getData().add(new XYChart.Data<>(timeToText(dataRecord.getTime()), dataRecord.getAirTemp()/10));
        }

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Beállított kamrahőmérséklet");
        for (DataRecord dataRecord : dataRecords) {
            series2.getData().add(new XYChart.Data<>(timeToText(dataRecord.getTime()), dataRecord.getSetAirTemp()));
        }

        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        series3.setName("Maghőmérséklet");
        for (DataRecord dataRecord : dataRecords) {
            series3.getData().add(new XYChart.Data<>(timeToText(dataRecord.getTime()), dataRecord.getCoreTemp()/10));
        }

        XYChart.Series<String, Number> series4 = new XYChart.Series<>();
        series4.setName("Beállított maghőmérséklet");
        for (DataRecord dataRecord : dataRecords) {
            series4.getData().add(new XYChart.Data<>(timeToText(dataRecord.getTime()), dataRecord.getSetCoreTemp()));
        }

        // Add all series to the line chart
        tempChart.getData().add(series1);
        tempChart.getData().add(series2);
        tempChart.getData().add(series3);
        tempChart.getData().add(series4);


        
    }

    public void populateHumidityChart() {
        
        humidityChart.getData().clear(); //Clear the chart data before populating it again
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Páratartalom");
        for (DataRecord dataRecord : dataRecords) {
            series1.getData().add(new XYChart.Data<>(timeToText(dataRecord.getTime()), dataRecord.getHumidity()));
        }
        

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Beállított páratartalom");
        for (DataRecord dataRecord : dataRecords) {
            series2.getData().add(new XYChart.Data<>(timeToText(dataRecord.getTime()), dataRecord.getSetHumidity()));
        }

        
        humidityChart.getData().add(series1);
        humidityChart.getData().add(series2);
       
    }

    public String timeToText(Double time) {
        return java.time.LocalTime.ofSecondOfDay((long) (time * 60 * 60 * 24)).toString();
        
    }

    
}
