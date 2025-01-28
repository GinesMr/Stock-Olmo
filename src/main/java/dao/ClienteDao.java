package dao;

import model.ClienteBean.ClienteBean;
import java.util.List;


public interface ClienteDao {

    void insert(ClienteBean cliente) throws Exception;


    void delete(int dni) throws Exception;


    void update(ClienteBean cliente) throws Exception;

    ClienteBean findById(int dni) throws Exception;

    List<ClienteBean> findAll() throws Exception;
}