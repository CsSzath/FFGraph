module com.thau.ffgraph {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
    requires transitive javafx.graphics;

    opens com.thau.ffgraph to javafx.fxml;
    exports com.thau.ffgraph;
    exports com.thau.DataModel;
}
