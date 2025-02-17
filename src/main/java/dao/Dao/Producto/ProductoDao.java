package dao.Dao.Producto;

import model.ProductoBean.ProductoBean;
import java.util.List;

public interface ProductoDao {

    void insert(ProductoBean producto) throws Exception;

    void delete(int idProducto) throws Exception;

    void update(ProductoBean producto) throws Exception;

    ProductoBean findById(int idProducto) throws Exception;

    List<ProductoBean> findAll() throws Exception;
}