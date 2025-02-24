package controller.MainController;

import atlantafx.base.theme.*;
import dao.Listener.StockChangeListener;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.PedidoBean.PedidoBean;
import model.ProductoBean.ProductoBean;
import service.LogicaNegocio.dataPedido.DataPedido;
import service.LogicaNegocio.dataProducto.DataProducto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StockOlmoController {

    // Instancias de las clases de servicio para manejar pedidos y productos
    DataPedido dataPedido = new DataPedido();
    DataProducto dataProd = new DataProducto();

    // Campos de texto y botones para la interfaz de usuario
    @FXML
    private TextField numeroPedidoField, idProductoPedidoField, cantidadPedidoField,buscarPedido;
    @FXML
    private DatePicker fechaPedidoField;
    @FXML
    private Button generarPedidoButton,FinalizarPedidoButton;
    @FXML
    private TableView<PedidoBean> pedidosTable;
    @FXML
    private TableColumn<PedidoBean, String> colNumeroPedido, colIdProductoPedido, colFechaPedido, colCantidadPedido, colPedir;
    private ObservableList<PedidoBean> pedidosData = FXCollections.observableArrayList();

    @FXML
    private TextField idProductoField, descripcionField, stockActualField, stockMinimoField, pvpField, buscarProducto;
    @FXML
    private Button eliminarProductoButton, agregarProductoButton, actualizarProductoButton;
    @FXML
    private TableView<ProductoBean> productosTable;
    @FXML
    private TableColumn<ProductoBean, String> colIdProducto, colDescripcion, colStockActual, colStockMinimo, colPvp;
    private ObservableList<ProductoBean> productosList = FXCollections.observableArrayList();

    // Método que se ejecuta al inicializar la ventana
    @FXML
    public void initialize() throws Exception {
        // Configuración de las columnas de la tabla de productos
        colIdProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colStockActual.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colStockMinimo.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));
        colPvp.setCellValueFactory(new PropertyValueFactory<>("pvp"));

        // Configuración de los botones y sus acciones
        agregarProductoButton.setOnAction(event -> {
            try {
                AgregarProducto(); // Llama al método para agregar un producto
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        eliminarProductoButton.setOnAction(event -> {
            eliminarProducto(); // Llama al método para eliminar un producto
        });

        actualizarProductoButton.setOnAction(event -> {
            try {
                actualizarProducto(); // Llama al método para actualizar un producto
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Listener para buscar productos mientras se escribe en el campo de búsqueda
        buscarProducto.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarProducto(newValue); // Llama al método para buscar productos
        });

        // Carga las tablas con los datos iniciales
        cagarTablaCliente(); // Carga la tabla de productos
        cagarTablaPedido();  // Carga la tabla de pedidos

        // Configuración de las columnas de la tabla de pedidos
        colNumeroPedido.setCellValueFactory(new PropertyValueFactory<>("numeroPedido"));
        colIdProductoPedido.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colFechaPedido.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colCantidadPedido.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPedir.setCellValueFactory(new PropertyValueFactory<>("pedir"));

        // Configuración del botón para generar un pedido
        generarPedidoButton.setOnAction(event -> {
            crearPedido(); // Llama al método para crear un pedido
        });
        buscarPedido.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarPedido(newValue); // Llama al método para buscar productos
        });
        FinalizarPedidoButton.setOnAction(event -> {
            finalizarPedido();
        });

    }

    // Método para agregar un producto
    private void AgregarProducto() {
        try {
            // Obtiene los valores de los campos de texto
            String idProductoStr = idProductoField.getText();
            String descripcion = descripcionField.getText();
            String stockActualStr = stockActualField.getText();
            String stockMinimoStr = stockMinimoField.getText();
            String pvpStr = pvpField.getText();

            // Validación de que los campos numéricos sean válidos
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

            if (!pvpStr.matches("\\d+(\\.\\d{1,2})?")) {
                mostrarAlerta("Error", "El precio debe ser un número válido.", "Por favor verifica el precio.", Alert.AlertType.ERROR);
                return;
            }

            // Convierte los valores a los tipos correctos
            int idProducto = Integer.parseInt(idProductoStr);
            int stockActual = Integer.parseInt(stockActualStr);
            int stockMinimo = Integer.parseInt(stockMinimoStr);
            double pvp = Double.parseDouble(pvpStr);

            // Valida los datos del producto
            String errores = validarProducto(idProducto, descripcion, pvp, stockActual,stockMinimo);
            if (!errores.isEmpty()) {
                mostrarAlerta("Error", errores, "El producto no ha sido agregado", Alert.AlertType.ERROR);
                return;
            }

            // Verifica si el producto ya existe
            ProductoBean nuevoProducto = new ProductoBean(idProducto, descripcion, stockActual, stockMinimo, (float) pvp);
            nuevoProducto.addPropertyChangeListener(new StockChangeListener(nuevoProducto));
            dataProd.Insertar(nuevoProducto);

            // Actualizar la tabla después de agregar
            cagarTablaCliente();
            cagarTablaPedido();
            mostrarAlerta("Éxito", "El producto se ha agregado correctamente.", "Producto agregado.", Alert.AlertType.INFORMATION);
            limpiarCamposProducto();

        } catch (Exception e) {
            mostrarAlerta("Error", "Se ha producido un error inesperado: " + e.getMessage(), "No se pudo agregar el producto.", Alert.AlertType.ERROR);
        }
    }

    // Método para actualizar un producto
    private void actualizarProducto() throws Exception {
        // Obtiene el producto seleccionado en la tabla
        ProductoBean productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlerta("Información", "Sin selección", "Debe seleccionar un producto para actualizar.", Alert.AlertType.INFORMATION);
            return;
        }

        // Muestra un diálogo de confirmación para actualizar el producto
        Alert confirmAlert = mostrarAlerta("Confirmación", "¿Está seguro que desea actualizar este producto?", "El producto será actualizado permanentemente.", Alert.AlertType.CONFIRMATION);
        if (confirmAlert.getResult() != ButtonType.OK) {
            return;
        }

        // Configura el diálogo de actualización
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Actualizar producto");
        dialog.setHeaderText("Ingrese los nuevos datos del producto");

        // Configura el diseño del diálogo
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Crea campos de texto con los valores actuales del producto
        TextField idTextField = new TextField(String.valueOf(productoSeleccionado.getIdProducto()));
        TextField descripcionTextField = new TextField(productoSeleccionado.getDescripcion());
        TextField stockActualTextField = new TextField(String.valueOf(productoSeleccionado.getStockActual()));
        TextField stockMinimoTextField = new TextField(String.valueOf(productoSeleccionado.getStockMinimo()));
        TextField pvpTextField = new TextField(String.valueOf(productoSeleccionado.getPvp()));

        // Añade los campos al diálogo
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

        // Procesa el resultado del diálogo
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Obtiene los nuevos valores
            String idProducto = idTextField.getText();
            String descripcion = descripcionTextField.getText().trim();
            String pvp = pvpTextField.getText().trim();
            String stockActual = stockActualTextField.getText().trim();
            String stockMinimo = stockMinimoTextField.getText().trim();

            // Valida los nuevos datos
            String error = validarProducto(productoSeleccionado.getIdProducto(), descripcion, Double.parseDouble(pvp), Integer.parseInt(stockActual),Integer.parseInt(stockActual));
            if (!error.isEmpty()) {
                mostrarAlerta("Error", "Datos inválidos", error, Alert.AlertType.ERROR);
                return;
            }

            // Verifica si el ID ya existe
            try {
                ProductoBean productoExistente = dataProd.BuscarProducto(Integer.parseInt(idProducto));
                if (productoExistente != null && productoExistente.getIdProducto() != productoSeleccionado.getIdProducto()) {
                    mostrarAlerta("Error", "ID duplicado", "El ID ingresado ya existe. Por favor, elija otro.", Alert.AlertType.ERROR);
                    return;
                }
            } catch (Exception e) {
                // Si el producto no existe, no se hace nada
            }

            // Actualiza el producto
            productoSeleccionado.setIdProducto(Integer.parseInt(idProducto));
            productoSeleccionado.setDescripcion(descripcion);
            productoSeleccionado.setPvp(Float.parseFloat(pvp));
            productoSeleccionado.setStockActual(Integer.parseInt(stockActual));
            productoSeleccionado.setStockMinimo(Integer.parseInt(stockMinimo));

            // Guarda los cambios en la base de datos
            dataProd.Actualizar(productoSeleccionado);
            try {
                cagarTablaCliente();
                cagarTablaPedido();
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al actualizar la tabla", ex.getMessage(), Alert.AlertType.ERROR);
            }
            // Refresca la tabla de productos y muestra un mensaje de éxito
            productosTable.refresh();
            mostrarAlerta("Éxito", "Producto actualizado", "El producto fue actualizado correctamente.", Alert.AlertType.INFORMATION);
        }
    }

    // Método para eliminar un producto
    private void eliminarProducto() {
        ProductoBean productSelecionado = productosTable.getSelectionModel().getSelectedItem();

        if (productSelecionado == null) {
            mostrarAlerta("Informacion", "Sin seleccion", "El producto no ha sido seleccionado.", Alert.AlertType.INFORMATION);
        } else {
            Alert e = mostrarAlerta("Confirmacion", "Esta seguro que desea eliminar este producto", "El producto sera eliminado permanentemente.", Alert.AlertType.CONFIRMATION);
            if (e.getResult() == ButtonType.OK) {
                int id = productSelecionado.getIdProducto();
                dataProd.Eliminar(id);

                // Actualizar la tabla después de eliminar
                try {
                    cagarTablaCliente();
                    cagarTablaPedido();
                } catch (Exception ex) {
                    mostrarAlerta("Error", "Error al actualizar la tabla", ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
    }

    // Método para cargar la tabla de productos
    private void cagarTablaCliente() throws Exception {
        try {
            // Obtiene la lista de productos desde la base de datos
            List<ProductoBean> prodBean = dataProd.listaproductos();
            productosList.clear(); // Limpia la lista antes de cargar nuevos datos

            // Añade cada producto a la lista observable
            for (ProductoBean prod : prodBean) {
                ProductoBean prods = new ProductoBean(
                        prod.getIdProducto(),
                        prod.getDescripcion(),
                        prod.getStockActual(),
                        prod.getStockMinimo(),
                        prod.getPvp()
                );
                prods.addPropertyChangeListener(new StockChangeListener(prods));
                productosList.add(prods);
            }

            // Asigna la lista observable a la tabla y la refresca
            productosTable.setItems(productosList);
            productosTable.refresh();
            cagarTablaPedido();
        } catch (Exception e) {
            mostrarAlerta("Error", "An Exception Occurred", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Método para validar los datos de un producto
    private String validarProducto(int idProducto, String descripcion, double pvp, int stockActual,int stockminimo) throws Exception {
        StringBuilder error = new StringBuilder();

        // Validación del ID del producto
        if (idProducto <= 0) {
            error.append("El ID del producto debe ser un número positivo.\n");
        }

        // Validación de la descripción
        if (descripcion == null || descripcion.isEmpty()) {
            error.append("La descripción del producto no puede estar vacía.\n");
        } else if (descripcion.length() > 255) {
            error.append("La descripción del producto no debe exceder los 255 caracteres.\n");
        }

        // Validación del precio
        if (pvp <= 0) {
            error.append("El precio del producto debe ser mayor a 0.\n");
        }

        // Validación del stock
        if (stockActual < 0) {
            error.append("El stock actual no puede ser negativo.\n");
        }
        if(stockActual < stockminimo){
            error.append("El stock actual no puede ser menor que el stock minimo.\n");
        }
        return error.toString();
    }

    // Método para buscar productos
    private void buscarProducto(String texto) {
        if (texto.isEmpty()) {
            // Si no hay texto de búsqueda, muestra todos los productos
            productosTable.setItems(productosList);
        } else {
            // Filtra los productos según el texto de búsqueda
            ObservableList<ProductoBean> productosFiltrados = FXCollections.observableArrayList();
            for (ProductoBean producto : productosList) {
                if (String.valueOf(producto.getIdProducto()).contains(texto) ||
                        producto.getDescripcion().toLowerCase().contains(texto.toLowerCase()) ||
                        String.valueOf(producto.getPvp()).contains(texto) ||
                        String.valueOf(producto.getStockActual()).contains(texto) ||
                        String.valueOf(producto.getStockMinimo()).contains(texto)) {
                    productosFiltrados.add(producto);
                }
            }
            // Asigna la lista filtrada a la tabla y la refresca
            productosTable.setItems(productosFiltrados);
            productosTable.refresh();
        }
    }

    private void crearPedido() {
        try {
            // Obtener y validar los datos del formulario
            int numeroPedido = Integer.parseInt(numeroPedidoField.getText());
            int idProducto = Integer.parseInt(idProductoPedidoField.getText());
            int cantidad = Integer.parseInt(cantidadPedidoField.getText());
            LocalDate fechaPedidoLocalDate = fechaPedidoField.getValue();
            // Convertir LocalDate a java.util.Date
            Date fechaPedido = Date.from(fechaPedidoLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            // Validar datos básicos del pedido
            if (!validarDatosPedido(numeroPedido, idProducto, cantidad, fechaPedido)) {
                return;
            }

            // Verificar que el producto existe
            ProductoBean producto = dataProd.BuscarProducto(idProducto);
            if (producto == null) {
                mostrarAlerta("Error", "Producto no encontrado",
                        "El ID de producto " + idProducto + " no existe en la base de datos.",
                        Alert.AlertType.ERROR);
                return;
            }

            // Verificar si hay pedidos existentes con el mismo número
            List<PedidoBean> pedidosExistentes = dataPedido.ListaPedidos();
            for (PedidoBean pedidoExistente : pedidosExistentes) {
                if (pedidoExistente.getNumeroPedido() == numeroPedido) {
                    mostrarAlerta("Error", "Pedido duplicado",
                            "Ya existe un pedido con el número " + numeroPedido,
                            Alert.AlertType.ERROR);
                    return;
                }
            }

            // Crear y guardar el pedido
            PedidoBean pedido = new PedidoBean(numeroPedido, idProducto, fechaPedido, cantidad, true);
            dataPedido.Insertar(pedido);

            // Actualizar las tablas
            cagarTablaPedido();
            cagarTablaCliente(); // Actualizar tabla de productos por si el pedido afecta al stock

            // Mostrar mensaje de éxito y limpiar formulario
            mostrarAlerta("Éxito", "Pedido creado",
                    "El pedido se ha creado correctamente para el producto: " + producto.getDescripcion(),
                    Alert.AlertType.INFORMATION);
            limpiarCamposPedido();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Datos inválidos",
                    "Asegúrese de ingresar valores numéricos válidos.",
                    Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Excepción inesperada",
                    e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    // Método para cargar la tabla de pedidos
    private void cagarTablaPedido() throws Exception {
        try {
            // Obtiene la lista de pedidos desde la base de datos
            List<PedidoBean> PedidoBean = dataPedido.ListaPedidos();
            pedidosData.clear(); // Limpia la lista antes de cargar nuevos datos

            // Añade cada pedido a la lista observable
            for (PedidoBean pedid : PedidoBean) {
                PedidoBean pedids = new PedidoBean(
                        pedid.getNumeroPedido(),
                        pedid.getIdProducto(),
                        pedid.getFecha(),
                        pedid.getCantidad(),
                        pedid.isPedir()
                );
                pedidosData.add(pedids);
            }

            // Asigna la lista observable a la tabla y la refresca
            pedidosTable.setItems(pedidosData);
            pedidosTable.refresh();
        } catch (Exception e) {
            mostrarAlerta("Error", "An Exception Occurred", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private void buscarPedido(String texto) {
        if (texto.isEmpty()) {
            // Si no hay texto de búsqueda, muestra todos los pedidos
            pedidosTable.setItems(pedidosData);
        } else {
            // Filtra los pedidos según el texto de búsqueda
            ObservableList<PedidoBean> pedidosFiltrados = FXCollections.observableArrayList();
            for (PedidoBean pedido : pedidosData) {
                if (String.valueOf(pedido.getNumeroPedido()).contains(texto) ||
                        String.valueOf(pedido.getIdProducto()).contains(texto) ||
                        pedido.getFecha().toString().contains(texto) ||
                        String.valueOf(pedido.getCantidad()).contains(texto) ||
                        String.valueOf(pedido.isPedir()).contains(texto)) {
                    pedidosFiltrados.add(pedido);
                }
            }
            // Asigna la lista filtrada a la tabla y la refresca
            pedidosTable.setItems(pedidosFiltrados);
            pedidosTable.refresh();
        }
    }
    private void finalizarPedido() {
        // Obtiene el pedido seleccionado en la tabla de pedidos
        PedidoBean pedidoSeleccionado = pedidosTable.getSelectionModel().getSelectedItem();

        if (pedidoSeleccionado == null) {
            // Si no hay ningún pedido seleccionado, muestra una alerta
            mostrarAlerta("Información", "Sin selección", "El pedido no ha sido seleccionado.", Alert.AlertType.INFORMATION);
        } else {
            // Muestra una alerta de confirmación para finalizar el pedido
            Alert confirmacion = mostrarAlerta("Confirmación", "¿Está seguro que desea finalizar este pedido?", "El pedido será finalizado permanentemente.", Alert.AlertType.CONFIRMATION);

            // Si el usuario confirma la finalización
            if (confirmacion.getResult() == ButtonType.OK) {
                int numeroPedido = pedidoSeleccionado.getNumeroPedido(); // Obtiene el número del pedido

                try {
                    // Obtener el producto asociado al pedido
                    ProductoBean producto = dataProd.BuscarProducto(pedidoSeleccionado.getIdProducto());

                    if (producto != null) {
                        // Aumentar el stock del producto
                        int nuevoStock = producto.getStockActual() + pedidoSeleccionado.getCantidad();
                        producto.setStockActual(nuevoStock);

                        // Actualizar el producto en la base de datos
                        dataProd.Actualizar(producto);

                        // Eliminar el pedido de la base de datos
                        dataPedido.Eliminar(numeroPedido);

                        // Actualizar las tablas después de finalizar el pedido
                        cagarTablaPedido(); // Recarga la tabla de pedidos
                        cagarTablaCliente(); // Recarga la tabla de productos (si es necesario)

                        // Muestra una alerta de éxito
                        mostrarAlerta("Éxito", "Pedido finalizado", "El pedido fue finalizado correctamente y el stock del producto se ha actualizado.", Alert.AlertType.INFORMATION);
                    } else {
                        // Si no se encuentra el producto, muestra una alerta de error
                        mostrarAlerta("Error", "Error al finalizar el pedido", "No se encontró el producto asociado al pedido.", Alert.AlertType.ERROR);
                    }
                } catch (Exception ex) {
                    // Si ocurre un error, muestra una alerta de error
                    mostrarAlerta("Error", "Error al finalizar el pedido", ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
    }
    private boolean validarDatosPedido(int numeroPedido, int idProducto, int cantidad, Date fechaPedido) {
        if (numeroPedido <= 0) {
            mostrarAlerta("Error", "Número de pedido inválido",
                    "El número de pedido debe ser positivo.",
                    Alert.AlertType.ERROR);
            return false;
        }

        if (idProducto <= 0) {
            mostrarAlerta("Error", "ID de producto inválido",
                    "El ID del producto debe ser positivo.",
                    Alert.AlertType.ERROR);
            return false;
        }

        if (cantidad <= 0) {
            mostrarAlerta("Error", "Cantidad inválida",
                    "La cantidad debe ser mayor a 0.",
                    Alert.AlertType.ERROR);
            return false;
        }

        if (fechaPedido == null) {
            mostrarAlerta("Error", "Fecha inválida",
                    "Debe seleccionar una fecha.",
                    Alert.AlertType.ERROR);
            return false;
        }

        // Obtener la fecha actual
        Date fechaActual = new Date();
        // Verificar si la fecha del pedido es anterior a la fecha actual
        if (fechaPedido.before(fechaActual)) {
            mostrarAlerta("Error", "Fecha inválida",
                    "La fecha del pedido no puede ser anterior a la fecha actual.",
                    Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    // Método para limpiar los campos del formulario de pedido
    private void limpiarCamposPedido() {
        numeroPedidoField.clear();
        idProductoPedidoField.clear();
        cantidadPedidoField.clear();
        fechaPedidoField.setValue(null);
    }

    // Método para mostrar alertas personalizadas
    public Alert mostrarAlerta(String titulo, String encabezado, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);

        // Aplica estilos CSS personalizados a la alerta
        DialogPane dialogPane = alert.getDialogPane();
        //String cssPath = "/MainView/custom-alert.css";
        //dialogPane.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        //dialogPane.getStyleClass().add("custom-alert");

        // Asigna clases CSS según el tipo de alerta
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
    @FXML
    private void cambiarTemaCupertinoLight() {
        // Cambiar al tema claro (por ejemplo, CupertinoLight)
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
    }

    @FXML
    private void cambiarTemaCupertinoDark() {
        // Cambiar al tema oscuro (por ejemplo, CupertinoDark)
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
    }
    @FXML
    private void cambiarTemaPrimerDark() {
        // Cambiar al tema oscuro (por ejemplo, CupertinoDark)
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }
    @FXML
    private void cambiarTemaNordLight() {
        // Cambiar al tema oscuro (por ejemplo, CupertinoDark)
        Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
    }
    @FXML
    private void cambiarTemaNordDark() {
        // Cambiar al tema oscuro (por ejemplo, CupertinoDark)
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
    }
    @FXML
    private void cambiarTemaDracula() {
        // Cambiar al tema oscuro (por ejemplo, CupertinoDark)
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
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