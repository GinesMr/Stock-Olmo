package service.LogicaNegocio.dataCliente;

import dao.Cliente.ClienteServices;
import model.ClienteBean.ClienteBean;

import java.util.List;

public class DataCliente {
    private ClienteServices clienteServices;

    public DataCliente() {
        this.clienteServices = new ClienteServices(); // Asegúrate de que ClienteServices tenga un constructor sin argumentos
    }

    public void Insertar(ClienteBean cli) {
        try {
            clienteServices.insert(cli);
        } catch (Exception e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
        }
    }

    public void Actualizar(ClienteBean cli) {
        try {
            clienteServices.update(cli);
        } catch (Exception e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
        }
    }

    public void Eliminar(int dni) {
        try {
            clienteServices.delete(dni);
        } catch (Exception e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        }
    }

    public ClienteBean BuscarCliente(int dni) {
        try {
            return clienteServices.findById(dni);
        } catch (Exception e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
            return null;
        }
    }

    public List<ClienteBean> ListaClientes() {
        try {
            return clienteServices.findAll();
        } catch (Exception e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
            return null;
        }
    }
}
