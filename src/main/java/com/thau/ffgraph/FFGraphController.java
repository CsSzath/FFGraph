package com.thau.ffgraph;

import java.io.IOException;

import javafx.fxml.FXML;


public class FFGraphController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("NewProduct");
    }


}
