package service.LogicaNegocio.dataProducto;

import dao.Producto.ProductoServices;
import model.ProductoBean.ProductoBean;

import java.util.List;

public class DataProducto {
    private ProductoServices productoServices;

    public DataProducto() {
        this.productoServices = new ProductoServices();
    }

    // Método para insertar un producto con doble check
    public void Insertar(ProductoBean prod) {
        if (prod == null) {
            System.err.println("Error: El producto no puede ser nulo.");
            return;
        }

        if (!validarProducto(prod)) {
            System.err.println("Error: Datos del producto no válidos.");
            return;
        }

        try {
            productoServices.insert(prod);
            System.out.println("Producto insertado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
        }
    }

    // Método para actualizar un producto con doble check
    public void Actualizar(ProductoBean prod) {
        if (prod == null) {
            System.err.println("Error: El producto no puede ser nulo.");
            return;
        }

        if (!validarProducto(prod)) {
            System.err.println("Error: Datos del producto no válidos.");
            return;
        }

        try {
            productoServices.update(prod);
            System.out.println("Producto actualizado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
    }

    // Método para eliminar un producto con doble check
    public void Eliminar(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("Error: El nombre del producto no puede estar vacío.");
            return;
        }

        try {
            productoServices.delete(nombre);
            System.out.println("Producto eliminado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
    }

    // Método para buscar un producto por ID
    public ProductoBean BuscarProducto(int id) {
        if (id <= 0) {
            System.err.println("Error: El ID no puede ser menor o igual a 0.");
            return null;
        }

        try {
            return productoServices.findById(id);
        } catch (Exception e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
            return null;
        }
    }

    // Método para listar todos los productos
    public List<ProductoBean> listaproductos() {
        try {
            return productoServices.findAll();
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
            return null;
        }
    }

    // Método para validar los datos del producto
    private boolean validarProducto(ProductoBean prod) {
        if (prod.getId() <= 0) {
            System.err.println("Error: El ID no puede ser menor o igual a 0.");
            return false;
        }

        if (prod.getNombre() == null || prod.getNombre().trim().isEmpty()) {
            System.err.println("Error: El nombre no puede estar vacío.");
            return false;
        }

        if (prod.getDescripcion() == null || prod.getDescripcion().trim().isEmpty()) {
            System.err.println("Error: La descripción no puede estar vacía.");
            return false;
        }

        if (prod.getPrecio() <= 0) {
            System.err.println("Error: El precio no puede ser menor o igual a 0.");
            return false;
        }

        if (prod.getCantidad() < 0) {
            System.err.println("Error: La cantidad no puede ser negativa.");
            return false;
        }

        return true;
    }
}