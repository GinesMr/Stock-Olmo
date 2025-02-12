package dao.Dao.Producto;

import model.ProductoBean.ProductoBean;

import java.util.List;

//Metodos Crud
public interface ProductoDao {

    void insert(ProductoBean producto) throws Exception;


    void delete(String nombre) throws Exception;


    void update(ProductoBean producto) throws Exception;

    ProductoBean findById(int dni) throws Exception;

    List<ProductoBean> findAll() throws Exception;
}
