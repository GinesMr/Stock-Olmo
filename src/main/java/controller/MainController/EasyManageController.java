package controller.MainController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ClienteBean.ClienteBean;
import model.ProductoBean.ProductoBean;

import java.util.Date;

public class EasyManageController {
    @FXML
    private Button  agregarClienteButton;
    @FXML
    private Button  agregarProductoButton;
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
    public void initialize() {
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
        agregarProductoButton.setOnAction(event -> agregarProducto());
    }

    // Método para agregar un cliente
    private void agregarCliente() {
        try {
            String nombre = clienteNombreField.getText();
            String telefono = clienteTelefonoField.getText();
            String email = clienteEmailField.getText();

            if (!nombre.isEmpty() && !telefono.isEmpty() && !email.isEmpty()) {
                ClienteBean cliente = new ClienteBean(0, nombre, "", telefono, email, null); // DNI y dirección no se usan en este ejemplo
                clientesList.add(cliente);
                limpiarCamposCliente();
            } else {
                System.out.println("Por favor, complete todos los campos del cliente.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error al agregar cliente: " + e.getMessage());
        }
    }

    // Método para agregar un producto
    private void agregarProducto() {
        try {
            String nombre = productoNombreField.getText();
            double precio = Double.parseDouble(productoPrecioField.getText());
            int cantidad = Integer.parseInt(productoStockField.getText());

            if (!nombre.isEmpty() && precio > 0 && cantidad >= 0) {
                ProductoBean producto = new ProductoBean(0, nombre, "", precio, cantidad, null); // ID y descripción no se usan en este ejemplo
                productosList.add(producto);
                limpiarCamposProducto();
            } else {
                System.out.println("Por favor, complete todos los campos del producto correctamente.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en el formato de precio o cantidad: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
        }
    }

    // Método para limpiar los campos de cliente
    private void limpiarCamposCliente() {
        clienteNombreField.clear();
        clienteTelefonoField.clear();
        clienteEmailField.clear();
    }

    // Método para limpiar los campos de producto
    private void limpiarCamposProducto() {
        productoNombreField.clear();
        productoPrecioField.clear();
        productoStockField.clear();
    }
}