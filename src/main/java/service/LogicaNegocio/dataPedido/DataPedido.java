package service.LogicaNegocio.dataPedido;

import dao.Pedido.PedidoDaoImpl;
import model.PedidoBean.PedidoBean;

import java.util.List;

public class DataPedido {
    private PedidoDaoImpl pedidoDao;

    public DataPedido() {
        this.pedidoDao = new PedidoDaoImpl();
    }

    // Método para insertar un pedido con validación
    public void Insertar(PedidoBean pedido) {
        if (pedido == null) {
            System.err.println("Error: El pedido no puede ser nulo.");
            return;
        }

        if (!validarPedido(pedido)) {
            System.err.println("Error: Datos del pedido no válidos.");
            return;
        }

        try {
            pedidoDao.insert(pedido);
            System.out.println("Pedido insertado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al insertar pedido: " + e.getMessage());
        }
    }

    // Método para actualizar un pedido con validación
    public void Actualizar(PedidoBean pedido) {
        if (pedido == null) {
            System.err.println("Error: El pedido no puede ser nulo.");
            return;
        }

        if (!validarPedido(pedido)) {
            System.err.println("Error: Datos del pedido no válidos.");
            return;
        }

        try {
            pedidoDao.update(pedido);
            System.out.println("Pedido actualizado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar pedido: " + e.getMessage());
        }
    }

    // Método para eliminar un pedido
    public void Eliminar(int numeroPedido) {
        if (numeroPedido < 0) {
            System.err.println("Error: El número de pedido no puede ser menor o igual a 0.");
            return;
        }

        try {
            pedidoDao.delete(numeroPedido);
            System.out.println("Pedido eliminado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
        }
    }

    // Método para buscar un pedido por número de pedido
    public PedidoBean BuscarPedido(int numeroPedido) {
        if (numeroPedido <= 0) {
            System.err.println("Error: El número de pedido no puede ser menor o igual a 0.");
            return null;
        }

        try {
            return pedidoDao.findById(numeroPedido);
        } catch (Exception e) {
            System.err.println("Error al buscar pedido: " + e.getMessage());
            return null;
        }
    }

    // Método para listar todos los pedidos
    public List<PedidoBean> ListaPedidos() {
        try {
            return pedidoDao.findAll();
        } catch (Exception e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
            return null;
        }
    }

    // Método para validar los datos del pedido
    private boolean validarPedido(PedidoBean pedido) {

        if (pedido.getNumeroPedido() < 0) {
            System.err.println("Error: El número de pedido no puede ser menor o igual a 0.");
            return false;
        }

        if (pedido.getIdProducto() <= 0) {
            System.err.println("Error: El ID del producto no puede ser menor o igual a 0.");
            return false;
        }

        if (pedido.getFecha() == null) {
            System.err.println("Error: La fecha del pedido no puede estar vacía.");
            return false;
        }

        if (pedido.getCantidad() <= 0) {
            System.err.println("Error: La cantidad no puede ser menor o igual a 0.");
            return false;
        }

        return true;
    }
}