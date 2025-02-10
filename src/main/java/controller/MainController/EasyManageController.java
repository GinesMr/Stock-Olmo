package controller.MainController;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import model.ClienteBean.ClienteBean;
import model.ProductoBean.ProductoBean;
import service.LogicaNegocio.dataCliente.DataCliente;
import service.LogicaNegocio.dataProducto.DataProducto;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EasyManageController extends Thread{
    // Initialize services
    private DataCliente x = new DataCliente();
    private DataProducto xp = new DataProducto();



    @FXML
    private TextField buscarCliente;
    @FXML
    private Button actualizarClienteButton;
    @FXML
    private Button eliminarClienteButton;
    @FXML
    private Button agregarClienteButton;
    @FXML
    private Button ActualizarClienteButton;


    @FXML
    private Button actualizarProductoButton;
    @FXML
    private Button eliminarProductoButton;
    @FXML
    private Button agregarProductoButton;
    @FXML
    private Button ActualizarProdcutoButton;


    @FXML
    private TextField clienteDniField;
    @FXML
    private TextField clienteNombreField;
    @FXML
    private TextField clienteDireccionField;
    @FXML
    private TextField clienteTelefonoField;
    @FXML
    private TextField clienteEmailField;

    @FXML
    private TextField idNombreField;
    @FXML
    private TextField productoNombreField;
    @FXML
    private TextField productoDescripcionField;
    @FXML
    private TextField productoPrecioField;
    @FXML
    private TextField productoStockField;
    @FXML
    private TextField buscarProducto;

    @FXML
    private TableView<ClienteBean> clientesTable;
    @FXML
    private TableColumn<ClienteBean, Integer> colDni;
    @FXML
    private TableColumn<ClienteBean, String> colNombre;
    @FXML
    private TableColumn<ClienteBean, String> colDireccion;
    @FXML
    private TableColumn<ClienteBean, String> colTelefono;
    @FXML
    private TableColumn<ClienteBean, String> colEmail;
    @FXML
    private TableColumn<ClienteBean, Date> colFechaRegistro;

    @FXML
    private TableView<ProductoBean> productosTable;
    @FXML
    private TableColumn<ProductoBean, String> colProductoNombre;
    @FXML
    private TableColumn<ProductoBean, String> colProductoId;
    @FXML
    private TableColumn<ProductoBean, String> colProductoDescripcion;
    @FXML
    private TableColumn<ProductoBean, Double> colProductoPrecio;
    @FXML
    private TableColumn<ProductoBean, Integer> colProductoStock;

    // Listas observables para almacenar los datos
    private ObservableList<ClienteBean> clientesList = FXCollections.observableArrayList();
    private ObservableList<ProductoBean> productosList = FXCollections.observableArrayList();

    // Método de inicialización
    @FXML
    public void initialize() throws Exception {
        // Configurar columnas de la tabla de clientes
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colFechaRegistro.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));

        // Configurar columnas de la tabla de productos
        colProductoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductoNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colProductoDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colProductoPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colProductoStock.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        // Asignar acciones a los botones
        agregarClienteButton.setOnAction(event -> agregarCliente());

        eliminarClienteButton.setOnAction(event -> {
            try {
                eliminarCliente();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        actualizarClienteButton.setOnAction(event -> {
            try {
                updateCliente();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        agregarProductoButton.setOnAction(event -> agregarProducto());
        eliminarProductoButton.setOnAction(event -> eliminarProducto());
        actualizarProductoButton.setOnAction(event -> actualizarProducto());

        cagarTablaCliente();
        cagarTablaProducto();

        buscarCliente.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarCliente(newValue);
        });

        buscarProducto.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarProducto(newValue);
        });

    }

    /**
     * This part of the code is only for client View.
     */

    private void agregarCliente() {
        try {
            int dni = Integer.parseInt(clienteDniField.getText().trim());
            String nombre = clienteNombreField.getText().trim();
            String direccion = clienteDireccionField.getText().trim();
            String telefono = clienteTelefonoField.getText().trim();
            String email = clienteEmailField.getText().trim();

            String error = validarCliente(dni, nombre, telefono, email, direccion);
            if (!error.isEmpty()) {
                mostrarAlerta("Error", error, "El cliente no ha sido agregado", Alert.AlertType.ERROR);
                return;
            }

            ClienteBean cli = x.BuscarCliente(dni);
            if (cli != null) {
                mostrarAlerta("Error", "El cliente con DNI " + dni + " ya existe.", "No se puede agregar un cliente duplicado.", Alert.AlertType.ERROR);
                return;
            }

            ClienteBean cliente = new ClienteBean(dni, nombre, direccion, telefono, email, null);
            clientesList.add(cliente);
            x.Insertar(cliente);

            limpiarCamposCliente();
            mostrarAlerta("Éxito", "Cliente agregado con éxito", "El cliente fue agregado correctamente.", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            mostrarAlerta("Campos Inválidos", "El DNI debe ser un número válido.", "Tenga en cuenta el formato del DNI.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error inesperado", "Ha ocurrido un error al agregar el cliente.", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String validarCliente(int dni, String nombre, String telefono, String email, String direccion) {
        StringBuilder error = new StringBuilder();

        if (String.valueOf(dni).length() != 8) {
            error.append("El DNI debe contener exactamente 8 dígitos numéricos.\n");
        }

        if (nombre.isEmpty() || !nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$")) {
            error.append("El nombre solo debe contener letras y no puede estar vacío.\n");
        }

        if (!telefono.matches("\\d{9}")) {
            error.append("El teléfono debe contener exactamente 9 dígitos numéricos.\n");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            error.append("El email introducido no es válido.\n");
        }

        if (direccion.isEmpty()) {
            error.append("La dirección no puede estar vacía.\n");
        }

        return error.toString();
    }


    private void eliminarCliente() throws Exception {

        ClienteBean clienteSeleccionado = clientesTable.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado==null){
            mostrarAlerta("Informacion","Sin seleccion","El cliente no ha sido seleccionado.",Alert.AlertType.INFORMATION);
            System.out.println("No hay cliente seleccionado.");
        }else{
            Alert e= mostrarAlerta("Confirmacion","Esta seguro que desea eliminar este cliente","El cliente sera eliminado permanentamente.",Alert.AlertType.CONFIRMATION);
            if(e.getResult() == ButtonType.OK) {
                System.out.println("Cliente seleccionado: " + clienteSeleccionado.getNombre());
                clientesList.remove(clienteSeleccionado);
                int dni = clienteSeleccionado.getDni();
                x.Eliminar(dni);
                mostrarAlerta("Exito","Cliente eliminado con exito","El cliente fue eliminado permanentamente.",Alert.AlertType.INFORMATION);

            }}


    }
    private void updateCliente() throws Exception {
        ClienteBean clienteSeleccionado = clientesTable.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            mostrarAlerta("Información", "Sin selección", "Debe seleccionar un cliente para actualizar.", Alert.AlertType.INFORMATION);
            return;
        }

        Alert confirmAlert = mostrarAlerta("Confirmación", "¿Está seguro que desea actualizar este cliente?", "El cliente será actualizado permanentemente.", Alert.AlertType.CONFIRMATION);
        if (confirmAlert.getResult() != ButtonType.OK) {
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Actualizar Cliente");
        dialog.setHeaderText("Ingrese los nuevos datos del cliente");
        String cssPath = "/MainView/updateCss.css";
        URL cssResource = getClass().getResource(cssPath);

        if (cssResource != null) {
            dialog.getDialogPane().getStylesheets().add(cssResource.toExternalForm());
        } else {
            System.err.println("No se pudo cargar el archivo CSS: " + cssPath);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nombreTextField = new TextField(clienteSeleccionado.getNombre());
        nombreTextField.setPromptText("Nombre");

        TextField direccionTextField = new TextField(clienteSeleccionado.getDireccion());
        direccionTextField.setPromptText("Dirección");

        TextField telefonoTextField = new TextField(clienteSeleccionado.getTelefono());
        telefonoTextField.setPromptText("Teléfono");

        TextField emailTextField = new TextField(clienteSeleccionado.getEmail());
        emailTextField.setPromptText("Email");

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nombreTextField, 1, 0);
        grid.add(new Label("Dirección:"), 0, 1);
        grid.add(direccionTextField, 1, 1);
        grid.add(new Label("Teléfono:"), 0, 2);
        grid.add(telefonoTextField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailTextField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String nombre = nombreTextField.getText().trim();
            String direccion = direccionTextField.getText().trim();
            String telefono = telefonoTextField.getText().trim();
            String email = emailTextField.getText().trim();

            // Validar los datos ingresados
            String error = validarCliente(clienteSeleccionado.getDni(), nombre, telefono, email, direccion);
            if (!error.isEmpty()) {
                mostrarAlerta("Error", "Datos inválidos", error, Alert.AlertType.ERROR);
                return;
            }

            // Actualizar el cliente con los nuevos valores
            clienteSeleccionado.setNombre(nombre);
            clienteSeleccionado.setDireccion(direccion);
            clienteSeleccionado.setTelefono(telefono);
            clienteSeleccionado.setEmail(email);

            x.Actualizar(clienteSeleccionado);

            // Refrescar la tabla para mostrar los cambios
            clientesTable.refresh();

            mostrarAlerta("Éxito", "Cliente actualizado", "El cliente fue actualizado correctamente.", Alert.AlertType.INFORMATION);
        }
    }

    private void cagarTablaCliente() throws Exception {
        try {
            List<ClienteBean> clientesBean = x.ListaClientes();

            for (ClienteBean clienteBean : clientesBean) {
                ClienteBean cliente = new ClienteBean(
                        clienteBean.getDni(),
                        clienteBean.getNombre(),
                        clienteBean.getDireccion(),
                        clienteBean.getTelefono(),
                        clienteBean.getEmail(),
                        clienteBean.getFechaRegistro()
                );
                clientesList.add(cliente);
            }

            clientesTable.setItems(clientesList);
        } catch (Exception e) {
            mostrarAlerta("Error","An Exception Occurred",e.getMessage(),Alert.AlertType.ERROR);
        }    }

    private void buscarCliente(String texto) {
        if (texto.isEmpty()) {
            clientesTable.setItems(clientesList);
        } else {
            ObservableList<ClienteBean> clientesFiltrados = FXCollections.observableArrayList();
            for (ClienteBean cliente : clientesList) {
                if (cliente.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                        String.valueOf(cliente.getDni()).contains(texto) ||
                        cliente.getDireccion().toLowerCase().contains(texto.toLowerCase()) ||
                        cliente.getTelefono().contains(texto) ||
                        cliente.getEmail().toLowerCase().contains(texto.toLowerCase())) {
                    clientesFiltrados.add(cliente);
                }
            }
            clientesTable.setItems(clientesFiltrados);
        }
    }

    private void limpiarCamposCliente() {
        clienteDniField.clear();
        clienteNombreField.clear();
        clienteDireccionField.clear();
        clienteTelefonoField.clear();
        clienteEmailField.clear();
    }

    /**
     * Until this part.
     * After that is the products zone
     */

    private void agregarProducto() {
        try {
            int id= Integer.parseInt(idNombreField.getText());
            String nombre = productoNombreField.getText();
            String descripcion = productoDescripcionField.getText();
            double precio = Double.parseDouble(productoPrecioField.getText());
            int stock = Integer.parseInt(productoStockField.getText());

            String error = validarProducto( nombre, descripcion, precio,stock);
            if (!error.isEmpty()) {
                mostrarAlerta("Error", error, "El producto no ha sido agregado", Alert.AlertType.ERROR);
                return;
            }
            ProductoBean pro = xp.BuscarProducto(id);
            if (pro != null) {
                mostrarAlerta("Error", "El cliente con id " + id + " ya existe.", "No se puede agregar un cliente duplicado.", Alert.AlertType.ERROR);
                return;
            }

            if (!nombre.isEmpty() && precio > 0 && stock >= 0) {
                ProductoBean producto = new ProductoBean(id, nombre, descripcion, precio, stock, null);
                productosList.add(producto);
                xp.Insertar(producto);
                limpiarCamposProducto();
            } else {
                System.out.println("Por favor, complete todos los campos del producto correctamente.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en el formato de precio o cantidad: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void cagarTablaProducto() throws Exception {
        try {
            List<ProductoBean> productosBeans = xp.listaproductos();

            for (ProductoBean productobean : productosBeans) {
                ProductoBean producto = new ProductoBean(
                        productobean.getId(),
                        productobean.getNombre(),
                        productobean.getDescripcion(),
                        productobean.getPrecio(),
                        productobean.getCantidad(),
                        productobean.getFechaRegistro()
                );
                    productosList.add(producto);
            }

            productosTable.setItems(productosList);
        } catch (Exception e) {
            mostrarAlerta("Error","An Exception Occurred",e.getMessage(),Alert.AlertType.ERROR);
        }    }

    private void eliminarProducto() {

        ProductoBean productSelecionado = productosTable.getSelectionModel().getSelectedItem();

        if (productSelecionado==null){
            mostrarAlerta("Informacion","Sin seleccion","El producot no ha sido seleccionado.",Alert.AlertType.INFORMATION);
            System.out.println("No hay producto seleccionado.");
        }else{
            Alert e= mostrarAlerta("Confirmacion","Esta seguro que desea eliminar este producto","El producto sera eliminado permanentamente.",Alert.AlertType.CONFIRMATION);
            if(e.getResult() == ButtonType.OK) {
                productosList.remove(productSelecionado);
                 String nombre = productSelecionado.getNombre();
                xp.Eliminar(nombre);
                mostrarAlerta("Exito","Cliente eliminado con exito","El cliente fue eliminado permanentamente.",Alert.AlertType.INFORMATION);

            }}
    }

    private void actualizarProducto() {
        ProductoBean productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlerta("Información", "Sin selección", "Debe seleccionar un producto para actualizar.", Alert.AlertType.INFORMATION);
            return;
        }

        Alert confirmAlert = mostrarAlerta("Confirmación", "¿Está seguro que desea actualizar este producto?", "El producto será actualizado permanentemente.", Alert.AlertType.CONFIRMATION);
        if (confirmAlert.getResult() != ButtonType.OK) {
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Actualizar producto");
        dialog.setHeaderText("Ingrese los nuevos datos del producto");
        String cssPath = "/MainView/updateCss.css";
        URL cssResource = getClass().getResource(cssPath);

        if (cssResource != null) {
            dialog.getDialogPane().getStylesheets().add(cssResource.toExternalForm());
        } else {
            System.err.println("No se pudo cargar el archivo CSS: " + cssPath);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nombreTextField = new TextField(productoSeleccionado.getNombre());
        nombreTextField.setPromptText("Nombre");

        TextField descripcionTextField = new TextField(productoSeleccionado.getDescripcion());
        descripcionTextField.setPromptText("Descripcion");

        TextField precioTextField = new TextField(String.valueOf(productoSeleccionado.getPrecio()));
        precioTextField.setPromptText("Precio");

        TextField stockTextField = new TextField(String.valueOf(productoSeleccionado.getCantidad()));
        stockTextField.setPromptText("Stock");

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nombreTextField, 1, 0);
        grid.add(new Label("Descripcion:"), 0, 1);
        grid.add(descripcionTextField, 1, 1);
        grid.add(new Label("Precio:"), 0, 2);
        grid.add(precioTextField, 1, 2);
        grid.add(new Label("Stock:"), 0, 3);
        grid.add(stockTextField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String nombre = nombreTextField.getText().trim();
            String descripcion = descripcionTextField.getText().trim();
            String precio = precioTextField.getText().trim();
            String stock = stockTextField.getText().trim();

            String error = validarProducto(nombre, descripcion, Double.parseDouble(precio),Integer.parseInt(stock));
            if (!error.isEmpty()) {
                mostrarAlerta("Error", "Datos inválidos", error, Alert.AlertType.ERROR);
                return;
            }

            productoSeleccionado.setNombre(nombre);
            productoSeleccionado.setDescripcion(descripcion);
            productoSeleccionado.setPrecio(Double.parseDouble(precio));
            productoSeleccionado.setCantidad(Integer.parseInt(stock));

            xp.Actualizar(productoSeleccionado);

            productosTable.refresh();

            mostrarAlerta("Éxito", "Producto actualizado", "El producto fue actualizado correctamente.", Alert.AlertType.INFORMATION);
        }
    }
    private String validarProducto(String nombre, String descripcion, double precio, int stock) {
            StringBuilder error = new StringBuilder();

            if (nombre.isEmpty()) {
                error.append("El nombre solo debe contener letras y no puede estar vacío.\n");
            }

            if (descripcion.isEmpty()) {
                error.append("La descripcion no debe estar vacia.\n");
            }

            if (precio < 0) {
                error.append("El precio no puede ser menor que 0.\n");
            }

             if(stock < 0) {
                error.append("El Stock no puede ser menor que 0.\n");
            }
            return error.toString();

    }
    private void buscarProducto(String texto) {
        if (texto.isEmpty()) {
            productosTable.setItems(productosList);
        } else {
            ObservableList<ProductoBean> productosFiltrados = FXCollections.observableArrayList();
            for (ProductoBean producto : productosList) {
                if (String.valueOf(producto.getId()).contains(texto) ||
                        producto.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                        producto.getDescripcion().toLowerCase().contains(texto.toLowerCase()) ||
                        String.valueOf(producto.getPrecio()).contains(texto) ||
                        String.valueOf(producto.getCantidad()).contains(texto)) {
                    productosFiltrados.add(producto);
                }
            }
            productosTable.setItems(productosFiltrados);
        }
    }
    /**
     * This method is for both (client,product).
     */
    private Alert mostrarAlerta(String titulo, String encabezado, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);

        DialogPane dialogPane = alert.getDialogPane();

        String cssPath = "/MainView/custom-alert.css";
        dialogPane.getStylesheets().add(
                getClass().getResource(cssPath).toExternalForm()
        );

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


    private void limpiarCamposProducto() {
        productoNombreField.clear();
        productoDescripcionField.clear();
        productoPrecioField.clear();
        productoStockField.clear();
        idNombreField.clear();
    }
}