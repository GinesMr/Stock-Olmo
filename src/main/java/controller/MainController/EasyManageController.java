package controller.MainController;


import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import model.ClienteBean.ClienteBean;
import model.ProductoBean.ProductoBean;
import dao.Cliente.ClienteServices;
import dao.Producto.ProductoServices;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EasyManageController extends Thread{
    // Initialize services
    private ClienteServices clienteServices = new ClienteServices();
    private ProductoServices productoServices = new ProductoServices();




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
    private TextField productoNombreField;
    @FXML
    private TextField productoDescripcionField;
    @FXML
    private TextField productoPrecioField;
    @FXML
    private TextField productoStockField;

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
        agregarProductoButton.setOnAction(event -> agregarProducto());

        cagarTablaCliente();
        cagarTablaProducto();


    }



    private void agregarCliente() {
        try {
            int dni = Integer.parseInt(clienteDniField.getText());
            String nombre = clienteNombreField.getText();
            String direccion = clienteDireccionField.getText();
            String telefono = clienteTelefonoField.getText();
            String email = clienteEmailField.getText();
            ClienteBean cli= clienteServices.findById(dni);

            if (!nombre.isEmpty() && !telefono.isEmpty() && !email.isEmpty()&& !direccion.isEmpty()) {
                    if(email.matches("^[A-Za-z0-9+_.-]+@(.+)$")==false){
                        mostrarAlerta("Error","El email introducido es invalido","El cliente no ha sido agregado",Alert.AlertType.ERROR);
                        return;
                    }
                ClienteBean cliente = new ClienteBean(dni, nombre, direccion, telefono, email, null);
                clientesList.add(cliente);
                clienteServices.insert(cliente);
                limpiarCamposCliente();
                mostrarAlerta("Exito","Cliente agregado con exito ","El cliente fue agregado .",Alert.AlertType.INFORMATION);

            } else {
                mostrarAlerta("Campos Invalidos","Rellene todos los campos de manera correcta ","Tenga en cuenta el formato del Dni y Email.",Alert.AlertType.INFORMATION);
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Campos Invalidos","Rellene todos los campos de manera correcta ","Tenga en cuenta el formato del Dni y Email.",Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
                clienteServices.delete(clienteSeleccionado.getDni());
                mostrarAlerta("Exito","Cliente eliminado con exito","El cliente fue eliminado permanentamente.",Alert.AlertType.INFORMATION);

            }}


    }

    private void updateCliente() throws Exception {
        ClienteBean clienteSeleccionado = clientesTable.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            mostrarAlerta("Informacion", "Sin seleccion", "El cliente no ha sido seleccionado para ser actualizado.", Alert.AlertType.INFORMATION);
            System.out.println("No hay cliente seleccionado.");
        } else {
            Alert confirmAlert = mostrarAlerta("Confirmacion", "¿Está seguro que desea actualizar este cliente?", "El cliente será actualizado permanentemente.", Alert.AlertType.CONFIRMATION);
            if (confirmAlert.getResult() == ButtonType.OK) {
                // Crear un diálogo para que el usuario ingrese los nuevos datos
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Actualizar Cliente");
                dialog.setHeaderText("Ingrese los nuevos datos del cliente");
                String cssPath = "/MainView/custom-alert.css";
                URL cssResource = getClass().getResource(cssPath);

                if (cssResource != null) {
                    dialog.getDialogPane().getStylesheets().add(cssResource.toExternalForm());
                } else {
                    System.err.println("No se pudo cargar el archivo CSS: " + cssPath);
                }

                // Crear el GridPane y los campos de texto
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField nombreTextField = new TextField(clienteSeleccionado.getNombre()); // Prellenar con el valor actual
                nombreTextField.setPromptText("Nombre");

                TextField directionTextField = new TextField(clienteSeleccionado.getDireccion()); // Prellenar con el valor actual
                directionTextField.setPromptText("Direccion");

                TextField phoneTextField = new TextField(clienteSeleccionado.getTelefono()); // Prellenar con el valor actual
                phoneTextField.setPromptText("Telefono");

                TextField emailTextField = new TextField(clienteSeleccionado.getEmail()); // Prellenar con el valor actual
                emailTextField.setPromptText("Email");

                grid.add(new Label("Nombre:"), 0, 0);
                grid.add(nombreTextField, 1, 0);
                grid.add(new Label("Direccion:"), 0, 1);
                grid.add(directionTextField, 1, 1);
                grid.add(new Label("Telefono:"), 0, 2);
                grid.add(phoneTextField, 1, 2);
                grid.add(new Label("Email:"), 0, 3);
                grid.add(emailTextField, 1, 3);

                dialog.getDialogPane().setContent(grid);

                // Agregar botones de OK y Cancelar
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                // Mostrar el diálogo y esperar la respuesta del usuario
                Optional<ButtonType> result = dialog.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Obtener los valores ingresados por el usuario
                    String nombreResult = nombreTextField.getText();
                    String directionResult = directionTextField.getText();
                    String phoneResult = phoneTextField.getText();
                    String emailResult = emailTextField.getText();

                    // Actualizar el cliente con los nuevos valores
                    clienteSeleccionado.setNombre(nombreResult);
                    clienteSeleccionado.setDireccion(directionResult);
                    clienteSeleccionado.setTelefono(phoneResult);
                    clienteSeleccionado.setEmail(emailResult);

                    clienteServices.update(clienteSeleccionado);

                    // Refrescar la tabla para mostrar los cambios

                    // Mostrar mensaje de éxito
                    mostrarAlerta("Exito", "Cliente actualizado", "El cliente fue actualizado permanentemente.", Alert.AlertType.INFORMATION);
                }
            }
        }
    }
    private void cagarTablaCliente() throws Exception {
        try {
            List<ClienteBean> clientesBean = clienteServices.findAll();

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





    private void agregarProducto() {
        try {
            String nombre = productoNombreField.getText();
            double precio = Double.parseDouble(productoPrecioField.getText());
            int cantidad = Integer.parseInt(productoStockField.getText());

            if (!nombre.isEmpty() && precio > 0 && cantidad >= 0) {
                ProductoBean producto = new ProductoBean(0, nombre, "", precio, cantidad, null); // ID y descripción no se usan en este ejemplo
                productosList.add(producto);
                productoServices.insert(producto);
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
            List<ProductoBean> productosBeans = productoServices.findAll();

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


    private void limpiarCamposCliente() {
        clienteDniField.clear();
        clienteNombreField.clear();
        clienteDireccionField.clear();
        clienteTelefonoField.clear();
        clienteEmailField.clear();
    }

    private void limpiarCamposProducto() {
        productoNombreField.clear();
        productoDescripcionField.clear();
        productoPrecioField.clear();
        productoStockField.clear();
    }
}