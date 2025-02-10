package service.LogicaNegocio.dataProducto;

import dao.Cliente.ClienteServices;
import dao.Producto.ProductoServices;
import model.ClienteBean.ClienteBean;
import model.ProductoBean.ProductoBean;

import java.util.List;

public class DataProducto {
    private ProductoServices productoServices;

    public DataProducto() {
        this.productoServices = new ProductoServices();
    }

    public void Insertar(ProductoBean prod) {
        try {
            productoServices.insert(prod);
        } catch (Exception e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
        }
    }

    public void Actualizar(ProductoBean prod) {
        try {
            productoServices.update(prod);
        } catch (Exception e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
    }

    public void Eliminar(String nombre) {
        try {
            productoServices.delete(nombre);
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
    }

    public ProductoBean BuscarProducto(int id) {
        try {
            return productoServices.findById(id);
        } catch (Exception e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
            return null;
        }
    }

    public List<ProductoBean> listaproductos() {
        try {
            return productoServices.findAll();
        } catch (Exception e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
            return null;
        }
    }
}
