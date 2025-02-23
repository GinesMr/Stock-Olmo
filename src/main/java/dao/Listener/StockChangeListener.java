package dao.Listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Date;

import controller.MainController.StockOlmoController;
import dao.Pedido.PedidoDaoImpl;
import model.PedidoBean.PedidoBean;
import model.ProductoBean.ProductoBean;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class StockChangeListener implements PropertyChangeListener {
    private final PedidoDaoImpl pedidoDao;
    private final ProductoBean producto;

    public StockChangeListener(ProductoBean producto) {
        this.pedidoDao = new PedidoDaoImpl();
        this.producto = producto;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Evento detectado: " + evt.getPropertyName());
        if ("stockActual".equals(evt.getPropertyName())) {
            int nuevoStock = (int) evt.getNewValue();
            System.out.println("Nuevo stock: " + nuevoStock);

            // Verificar si el stock actual está por debajo del mínimo
            if (nuevoStock < producto.getStockMinimo()) {
                try {
                    // Calcular la cantidad a pedir
                    int cantidadAPedir = (producto.getStockMinimo() * 2) - nuevoStock;

                    // Crear nuevo pedido
                    PedidoBean nuevoPedido = new PedidoBean(
                            generarNumeroPedido(), // Deberías implementar este método
                            producto.getIdProducto(),
                            new Date(),
                            cantidadAPedir,
                            true
                    );

                    // Insertar el pedido en la base de datos
                    System.out.println("Insertando pedido: " + nuevoPedido.getNumeroPedido());
                    pedidoDao.insert(nuevoPedido);

                    // Mostrar alerta en el hilo de la interfaz gráfica
                    Platform.runLater(() -> {
                        System.out.println("Mostrando alerta de pedido automático");

                        // Crear una nueva alerta
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Pedido Automático Generado");
                        alert.setHeaderText("Stock Bajo Detectado");
                        alert.setContentText("Se ha generado un pedido automático para el producto ID: " + producto.getIdProducto() + " Nombre: " + producto.getDescripcion() + "\nCantidad pedida: " + cantidadAPedir);

                        // Mostrar la alerta y esperar a que el usuario la cierre
                        alert.showAndWait();
                    });
                } catch (Exception e) {
                    // Mostrar alerta de error en el hilo de la interfaz gráfica
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error al Generar Pedido");
                        alert.setHeaderText("Error Detectado");
                        alert.setContentText("No se pudo generar el pedido automático: " + e.getMessage());
                        alert.showAndWait();
                    });

                    System.err.println("Error al generar pedido automático: " + e.getMessage());
                }
            }
        }
    }

    // Método simple para generar un número de pedido
    private int generarNumeroPedido() {
        return (int) (System.currentTimeMillis() % 1000000);
    }
}