package com.thau.ffgraph;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class StepsBar {


    private static String[] stepColors = {"white", "orange", "green", "black", "gray", "purple", "blue", "gray"};
    private static String[] stepNames = {"-", "Szárítás", "Főzés", "Füstölés", "Hőntartás", "Tisztítás", "Zuhany", "Várakozás"};

    HBox barBox; // The HBox that will contain the rectangles and labels
    private  ArrayList<Integer> percentages; // List of percentage occupied by each program step on the bar, in order
    private ArrayList<Integer> stepTypes; // List of step types imported from the database
    private  ArrayList<Integer> stepList; // Generated list of step types, without duplicates, in orde3r of appearence
    


    public StepsBar() {
        this.barBox = new HBox();
        this.percentages = new ArrayList<>(); 
        this.stepTypes = new ArrayList<>(); 
        this.stepList = new ArrayList<>(); 
    }

    public HBox getBarBox() {
        return generateRectangles(); // Call the method to generate rectangles and labels
    }

    public void setBarBox(HBox barSteps) {
        this.barBox = barSteps; // Set the HBox to the new one
    }

    public void setStepTypes(ArrayList<Integer> stepTypes) {
        this.stepTypes = stepTypes; // Set the step types to the new ones
    }

    public HBox generateRectangles() {

        // Clear the existing rectangles and labels
        barBox.getChildren().clear();
        double totalWidth = barBox.getWidth() - 45; //Compensate for padding
        double barHeight = barBox.getHeight();

        

        //calculatePercentages(StepTypes); // Call the method to calculate percentages -> has to be done from outside, to be able to react to window resize

        for (int i = 0; i < percentages.size(); i++) {
            double rectWidth = (percentages.get(i) / 100.0) * totalWidth; // Calculate the width of the rectangle based on the percentage

            Rectangle rectangle = new Rectangle(rectWidth, barHeight);
            rectangle.setStyle("-fx-fill: " + stepColors[stepList.get(i)] + ";");

            Label label = new Label(stepNames[stepList.get(i)]);
            label.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
            label.setPrefWidth(rectWidth);
            label.setPrefHeight(barHeight);
            label.setAlignment(javafx.geometry.Pos.CENTER);

            StackPane container = new StackPane(rectangle, label);
           
            barBox.getChildren().add(container);
            
        }
        return barBox; // Return the HBox containing the rectangles and labels

    }



    public void calculatePercentages() {
        ArrayList<Integer> counts = new ArrayList<>();

        percentages.clear();                                                // Clear the previous percentages
        stepList.clear();                                                   // Clear the previous step list

        for (int actualStepType : stepTypes) {                                                  // Iterate through the list of step types
            if (stepList.isEmpty() || stepList.get(stepList.size() - 1) != actualStepType) {    // If stepList is empty or the current StepType is different from the last one                                             
                stepList.add(actualStepType);                                                   // Add the current StepType to the list
                counts.add(1);                                                                  // Initialize the count for this StepType
            } else {                                                                            // If the current StepType is the same as the last one
                counts.set(counts.size() - 1, counts.get(counts.size() - 1) + 1);               // Increment the count
            }
        }
        System.out.println("Counts: " + counts); 
        System.out.println("StepList: " + stepList); 
        // Calculate percentages
        int total = 0;
        for (int integer : counts) {
            total += integer;                                               //sum all the counts to get the total number of entries
        }
        for (int count : counts) {
            percentages.add((int) Math.round((count / (double) total) * 100.0)); //calculate the percentage of each StepType and round it to the nearest integer
        }
    }
}
