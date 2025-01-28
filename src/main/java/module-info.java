module org.example.ejer1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    // Export the MainController package to javafx.fxml
    exports controller.MainController to javafx.fxml;

    exports App to javafx.graphics;
}
