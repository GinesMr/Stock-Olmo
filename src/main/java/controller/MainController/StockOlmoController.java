package controller.MainController;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;
import model.PedidoBean.PedidoBean;
import model.ProductoBean.ProductoBean;
import service.LogicaNegocio.dataProducto.DataProducto;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StockOlmoController {
    DataProducto dataProd = new DataProducto();
    @FXML
    private TextField idProductoField, descripcionField, stockActualField, stockMinimoField, pvpField,buscarProducto;
    @FXML
    private TextField numeroPedidoField, idProductoPedidoField, cantidadPedidoField;
    @FXML
    private DatePicker fechaPedidoField;
    @FXML
    private Button eliminarProductoButton;
    @FXML
    private Button agregarProductoButton;
    @FXML
    private Button actualizarProductoButton;
    @FXML
    private TableView<ProductoBean> productosTable;
    @FXML
    private TableView<PedidoBean> pedidosTable;
    @FXML
    private TableColumn<ProductoBean, String> colIdProducto, colDescripcion, colStockActual, colStockMinimo, colPvp;
    @FXML
    private TableColumn<PedidoBean, String> colNumeroPedido, colIdProductoPedido, colFechaPedido, colCantidadPedido, colPedir;

    private ObservableList<ProductoBean> productosList = FXCollections.observableArrayList();
    private ObservableList<PedidoBean> pedidosData = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws Exception {
        // Initialize product table columns
        colIdProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colStockActual.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colStockMinimo.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));
        colPvp.setCellValueFactory(new PropertyValueFactory<>("pvp"));
        // Initialize order table columns
        colNumeroPedido.setCellValueFactory(new PropertyValueFactory<>("numeroPedido"));
        colIdProductoPedido.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colFechaPedido.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
        colCantidadPedido.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPedir.setCellValueFactory(new PropertyValueFactory<>("pedir"));

        pedidosTable.setItems(pedidosData);
        agregarProductoButton.setOnAction(event -> {
            try {
                AgregarProducto();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        eliminarProductoButton.setOnAction(event -> {
            eliminarProducto();
        });
        actualizarProductoButton.setOnAction(event -> {
            actualizarProducto();
        });
        buscarProducto.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarProducto(newValue);
        });

        cagarTablaCliente();
    }
    private void AgregarProducto() {
        try {
            // Obtención de datos desde los campos de texto
            String idProductoStr = idProductoField.getText();
            String descripcion = descripcionField.getText();
            String stockActualStr = stockActualField.getText();
            String stockMinimoStr = stockMinimoField.getText();
            String pvpStr = pvpField.getText();

            // Validación de que los campos numéricos contengan solo números
            if (!idProductoStr.matches("\\d+")) {
                mostrarAlerta("Error", "El ID del producto debe ser un número válido.", "Por favor verifica el ID.", Alert.AlertType.ERROR);
                return;
            }

            if (!stockActualStr.matches("\\d+")) {
                mostrarAlerta("Error", "El stock actual debe ser un número válido.", "Por favor verifica el stock actual.", Alert.AlertType.ERROR);
                return;
            }

            if (!stockMinimoStr.matches("\\d+")) {
                mostrarAlerta("Error", "El stock mínimo debe ser un número válido.", "Por favor verifica el stock mínimo.", Alert.AlertType.ERROR);
                return;
            }

            if (!pvpStr.matches("\\d+(\\.\\d{1,2})?")) { // Permite hasta dos decimales
                mostrarAlerta("Error", "El precio debe ser un número válido.", "Por favor verifica el precio.", Alert.AlertType.ERROR);
                return;
            }

            // Conversión de los campos numéricos
            int idProducto = Integer.parseInt(idProductoStr);
            int stockActual = Integer.parseInt(stockActualStr);
            int stockMinimo = Integer.parseInt(stockMinimoStr);
            double pvp = Double.parseDouble(pvpStr);

            // Validación de los datos ingresados
            String errores = validarProducto(idProducto, descripcion, pvp, stockActual);
            if (!errores.isEmpty()) {
                mostrarAlerta("Error", errores, "El producto no ha sido agregado", Alert.AlertType.ERROR);
                return;
            }

            // Verificación de producto duplicado por ID
            ProductoBean prodExistente = dataProd.BuscarProducto(idProducto);
            if (prodExistente != null) {
                mostrarAlerta("Error", "El producto con ID " + idProducto + " ya existe.", "No se puede agregar un producto duplicado.", Alert.AlertType.ERROR);
                return;
            }

            // Creación y guardado del nuevo producto si pasa todas las validaciones
            ProductoBean nuevoProducto = new ProductoBean(idProducto, descripcion, stockActual, stockMinimo, (float) pvp);
            dataProd.Insertar(nuevoProducto);

            // Mostrar alerta de éxito y limpiar campos
            mostrarAlerta("Éxito", "El producto se ha agregado correctamente.", "Producto agregado.", Alert.AlertType.INFORMATION);
            limpiarCamposProducto();

        } catch (Exception e) {
            mostrarAlerta("Error", "Se ha producido un error inesperado: " + e.getMessage(), "No se pudo agregar el producto.", Alert.AlertType.ERROR);
        }
    }
    private void actualizarProducto() {
        // Obtiene el producto seleccionado
        ProductoBean productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlerta("Información", "Sin selección",
                    "Debe seleccionar un producto para actualizar.", Alert.AlertType.INFORMATION);
            return;
        }

        // Solicita confirmación de actualización
        Alert confirmAlert = mostrarAlerta("Confirmación", "¿Está seguro que desea actualizar este producto?",
                "El producto será actualizado permanentemente.", Alert.AlertType.CONFIRMATION);
        if (confirmAlert.getResult() != ButtonType.OK) {
            return;
        }

        // Configuración del diálogo de actualización
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Actualizar producto");
        dialog.setHeaderText("Ingrese los nuevos datos del producto");

        // Carga de estilos CSS
        String cssPath = "/MainView/updateCss.css";
        URL cssResource = getClass().getResource(cssPath);
        if (cssResource != null) {
            dialog.getDialogPane().getStylesheets().add(cssResource.toExternalForm());
        }

        // Configuración del grid para el formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Creación de campos de texto con valores actuales
        TextField idTextField = new TextField(String.valueOf(productoSeleccionado.getIdProducto()));
        TextField descripcionTextField = new TextField(productoSeleccionado.getDescripcion());
        TextField stockActualTextField = new TextField(String.valueOf(productoSeleccionado.getStockActual()));
        TextField stockMinimoTextField = new TextField(String.valueOf(productoSeleccionado.getStockMinimo()));
        TextField pvpTextField = new TextField(String.valueOf(productoSeleccionado.getPvp()));

        // Configuración del grid con los campos
        grid.add(new Label("Id:"), 0, 0);
        grid.add(idTextField, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1);
        grid.add(descripcionTextField, 1, 1);
        grid.add(new Label("Precio:"), 0, 2);
        grid.add(pvpTextField, 1, 2);
        grid.add(new Label("Stock Actual:"), 0, 3);
        grid.add(stockActualTextField, 1, 3);
        grid.add(new Label("Stock Mínimo:"), 0, 4);
        grid.add(stockMinimoTextField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Procesamiento del resultado del diálogo
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Obtención de los nuevos valores
            String idProducto = idTextField.getText();
            String descripcion = descripcionTextField.getText().trim();
            String pvp = pvpTextField.getText().trim();
            String stockActual = stockActualTextField.getText().trim();
            String stockMinimo = stockMinimoTextField.getText().trim();

            // Validación de los nuevos datos
            String error = validarProducto(productoSeleccionado.getIdProducto(), descripcion,
                    Double.parseDouble(pvp), Integer.parseInt(stockActual));
            if (!error.isEmpty()) {
                mostrarAlerta("Error", "Datos inválidos", error, Alert.AlertType.ERROR);
                return;
            }

            // Verificar que el ID no sea duplicado en la base de datos
            try {
                ProductoBean productoExistente = dataProd.BuscarProducto(Integer.parseInt(idProducto));
                if (productoExistente != null && productoExistente.getIdProducto() != productoSeleccionado.getIdProducto()) {
                    mostrarAlerta("Error", "ID duplicado", "El ID ingresado ya existe. Por favor, elija otro.", Alert.AlertType.ERROR);
                    return;
                }
            } catch (Exception e) {
                // Si el producto no existe, el catch es esperado y no se hace nada
            }

            try {
                // Actualización del producto con los nuevos valores
                productoSeleccionado.setIdProducto(Integer.parseInt(idProducto));
                productoSeleccionado.setDescripcion(descripcion);
                productoSeleccionado.setPvp(Float.parseFloat(pvp));
                productoSeleccionado.setStockActual(Integer.parseInt(stockActual));
                productoSeleccionado.setStockMinimo(Integer.parseInt(stockMinimo));

                // Actualización en la base de datos o almacenamiento
                dataProd.Actualizar(productoSeleccionado);
                productosTable.refresh();
                mostrarAlerta("Éxito", "Producto actualizado",
                        "El producto fue actualizado correctamente.", Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "Formato inválido", "Verifique los valores ingresados.", Alert.AlertType.ERROR);
            }
        }
    }
    // Método para eliminar un producto seleccionado
    private void eliminarProducto() {
        // Obtiene el producto seleccionado en la tabla
        ProductoBean productSelecionado = productosTable.getSelectionModel().getSelectedItem();

        if (productSelecionado == null) {
            mostrarAlerta("Informacion", "Sin seleccion", "El producto no ha sido seleccionado.", Alert.AlertType.INFORMATION);
            System.out.println("No hay producto seleccionado.");
        } else {
            // Solicita confirmación antes de eliminar
            Alert e = mostrarAlerta("Confirmacion", "Esta seguro que desea eliminar este producto",
                    "El producto sera eliminado permanentamente.", Alert.AlertType.CONFIRMATION);
            if (e.getResult() == ButtonType.OK) {
                productosList.remove(productSelecionado);
                int id = productSelecionado.getIdProducto();
                dataProd.Eliminar(id);
                mostrarAlerta("Exito", "Producto eliminado con exito",
                        "El producto fue eliminado permanentamente.", Alert.AlertType.INFORMATION);
            }
        }
    }
    // Método para cargar la tabla de productos
    private void cagarTablaCliente() throws Exception {
        try {
            List<ProductoBean> prodBean = dataProd.listaproductos();
            for (ProductoBean prod : prodBean) {
                ProductoBean prods = new ProductoBean(
                        prod.getIdProducto(),
                        prod.getDescripcion(),
                        prod.getStockActual(),
                        prod.getStockMinimo(),
                        prod.getPvp()
                );
                productosList.add(prods);
            }
            productosTable.setItems(productosList);
        } catch (Exception e) {
            mostrarAlerta("Error", "An Exception Occurred", e.getMessage(), Alert.AlertType.ERROR);
        }}

    // Método de validación de datos del producto
    private String validarProducto(int idProducto, String descripcion, double pvp, int stockActual) {
        StringBuilder error = new StringBuilder();

        // Validación de ID de producto
        if (idProducto <= 0) {
            error.append("El ID del producto debe ser un número positivo.\n");
        }

        // Validación de descripción
        if (descripcion == null || descripcion.isEmpty()) {
            error.append("La descripción del producto no puede estar vacía.\n");
        } else if (descripcion.length() > 255) {
            error.append("La descripción del producto no debe exceder los 255 caracteres.\n");
        }

        // Validación de precio (PVP)
        if (pvp <= 0) {
            error.append("El precio del producto debe ser mayor a 0.\n");
        }

        // Validación de stock
        if (stockActual < 0) {
            error.append("El stock actual no puede ser negativo.\n");
        }

        return error.toString();
    }

    private void buscarProducto(String texto) {
        if (texto.isEmpty()) {
            productosTable.setItems(productosList);
        } else {
            // Filtra los productos según el texto de búsqueda
            ObservableList<ProductoBean> productosFiltrados = FXCollections.observableArrayList();
            for (ProductoBean producto : productosList) {
                if (String.valueOf(producto.getIdProducto()).contains(texto) ||
                        producto.getDescripcion().toLowerCase().contains(texto.toLowerCase()) ||
                        String.valueOf(producto.getPvp()).contains(texto) ||
                        String.valueOf(producto.getStockActual()).contains(texto)||
                        String.valueOf(producto.getStockMinimo()).contains(texto))
                {
                    productosFiltrados.add(producto);
                }
            }
            productosTable.setItems(productosFiltrados);
        }
    }
    private Alert mostrarAlerta(String titulo, String encabezado, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);

        DialogPane dialogPane = alert.getDialogPane();
        String cssPath = "/MainView/custom-alert.css";
        dialogPane.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        switch (tipo) {
            case CONFIRMATION:
                dialogPane.getStyleClass().add("confirmation");
                break;
            case INFORMATION:
                dialogPane.getStyleClass().add("information");
                break;
            case ERROR:
                dialogPane.getStyleClass().add("error");
                break;
            case WARNING:
                dialogPane.getStyleClass().add("warning");
                break;
        }

        alert.showAndWait();
        return alert;
    }
    // Método para limpiar los campos del formulario de producto
    private void limpiarCamposProducto() {
        idProductoField.clear();
        descripcionField.clear();
        stockActualField.clear();
        stockMinimoField.clear();
        pvpField.clear();
    }
}