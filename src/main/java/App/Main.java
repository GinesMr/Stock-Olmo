package App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        // Cargar el archivo FXML desde la ruta especificada
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView/StockOlmoView.fxml"));
        Parent root = loader.load();

        // Configurar la escena
        Scene scene = new Scene(root);

        // Cargar y aplicar el archivo CSS
        String css = getClass().getResource("/MainView/si.css").toExternalForm();
        scene.getStylesheets().add(css);

        // ** Establecer el icono del Stage principal **
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/iconos/icon.png")));

        // Configurar la ventana principal
        primaryStage.setTitle("Stock-Olmo - Gestión de Productos y Pedidos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Iniciar la aplicación JavaFX
        launch(args);
    }
}
