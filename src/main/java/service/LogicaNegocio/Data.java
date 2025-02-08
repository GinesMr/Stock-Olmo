package service.LogicaNegocio;
import dao.Cliente.ClienteServices;
import dao.Producto.ProductoServices;
import model.ClienteBean.ClienteBean;

import java.util.List;

public class Data {
    ClienteServices clienteServices;

    public void Insertar(ClienteBean cli) throws Exception {
        clienteServices.insert(cli);
    }
    public void Actualizar(ClienteBean cli) throws Exception {
        //clienteServices.update(cli);
    }
    public void Eliminar(ClienteBean cli) throws Exception {
        clienteServices.delete(cli.getDni());
    }
    public ClienteBean BuscarCliente(String dni) throws Exception {
        return clienteServices.findById(Integer.parseInt(dni));
    }
    public List<ClienteBean> ListaClientes() throws Exception {
       return clienteServices.findAll();

    }
}
