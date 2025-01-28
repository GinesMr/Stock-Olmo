package dao;

import model.ProductoBean.ProductoBean;

import java.util.List;

public interface ProductoDao {

    void insert(ProductoBean producto) throws Exception;


    void delete(int dni) throws Exception;


    void update(ProductoBean producto) throws Exception;

    ProductoBean findById(int dni) throws Exception;

    List<ProductoBean> findAll() throws Exception;
}
