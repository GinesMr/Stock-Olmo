module org.example.ejer1 {
    requires javafx.controls;
    requires org.kordamp.ikonli.javafx;
    requires javafx.web;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires javafx.fxml;

    opens model.ClienteBean to javafx.base;
    // Grant reflective access to the controller.MainController package for javafx.fxml
    opens controller.MainController to javafx.fxml;

    opens model.ProductoBean to javafx.base;
    // Export the App package to javafx.graphics (if needed)
    exports App to javafx.graphics;
    // Export the MainController package to javafx.fxml
    exports controller.MainController to javafx.fxml;

}
