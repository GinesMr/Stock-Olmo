package controller.MainController;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ClienteBean.ClienteBean;
import model.ProductoBean.ProductoBean;
import service.DaoMongoDb.Cliente.ClienteServices;
import service.DaoMongoDb.Producto.ProductoServices;

import java.util.Date;
import java.util.List;

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
    private Button actualizarProductoButton;
    @FXML
    private Button eliminarProductoButton;
    @FXML
    private Button agregarProductoButton;

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

            if (!nombre.isEmpty() && !telefono.isEmpty() && !email.isEmpty()&& !direccion.isEmpty()) {
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

    private void updateClienete(){
//Terminar este metodo despues de cenar

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
        alert.showAndWait();
        return alert;
    }

    // Método para limpiar los campos de cliente
    private void limpiarCamposCliente() {
        clienteDniField.clear();
        clienteNombreField.clear();
        clienteDireccionField.clear();
        clienteTelefonoField.clear();
        clienteEmailField.clear();
    }

    // Método para limpiar los campos de producto
    private void limpiarCamposProducto() {
        productoNombreField.clear();
        productoDescripcionField.clear();
        productoPrecioField.clear();
        productoStockField.clear();
    }
}