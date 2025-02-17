package dao.Dao.Pedido;

import model.PedidoBean.PedidoBean;
import java.util.List;

public interface PedidoDao {
    void insert(PedidoBean pedido) throws Exception;

    void delete(int numeroPedido) throws Exception;

    void update(PedidoBean pedido) throws Exception;

    PedidoBean findById(int numeroPedido) throws Exception;

    List<PedidoBean> findAll() throws Exception;
}