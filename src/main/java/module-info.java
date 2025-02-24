module org.example.ejer1 {
    requires org.kordamp.ikonli.javafx;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires javafx.fxml;
    requires java.desktop;
    requires atlantafx.base;
    requires eu.hansolo.tilesfx;

    opens model.PedidoBean to javafx.base;
    opens controller.MainController to javafx.fxml;

    opens model.ProductoBean to javafx.base;
    exports App to javafx.graphics;
    exports controller.MainController to javafx.fxml, javafx.graphics;

}
