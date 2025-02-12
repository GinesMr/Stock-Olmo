package service.LogicaNegocio.dataCliente;

import dao.Cliente.ClienteServices;
import model.ClienteBean.ClienteBean;

import java.util.List;
import java.util.regex.Pattern;

public class DataCliente {
    private ClienteServices clienteServices;

    public DataCliente() {
        this.clienteServices = new ClienteServices();
    }

    // Método para insertar un cliente con doble check
    public void Insertar(ClienteBean cli) {
        if (cli == null) {
            System.err.println("Error: El cliente no puede ser nulo.");
            return;
        }

        if (!validarCliente(cli)) {
            System.err.println("Error: Datos del cliente no válidos.");
            return;
        }

        try {
            clienteServices.insert(cli);
            System.out.println("Cliente insertado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
        }
    }

    // Método para actualizar un cliente con doble check
    public void Actualizar(ClienteBean cli) {
        if (cli == null) {
            System.err.println("Error: El cliente no puede ser nulo.");
            return;
        }

        if (!validarCliente(cli)) {
            System.err.println("Error: Datos del cliente no válidos.");
            return;
        }

        try {
            clienteServices.update(cli);
            System.out.println("Cliente actualizado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
        }
    }

    // Método para eliminar un cliente con doble check
    public void Eliminar(int dni) {
        if (dni <= 0) {
            System.err.println("Error: El DNI no puede ser menor o igual a 0.");
            return;
        }

        try {
            clienteServices.delete(dni);
            System.out.println("Cliente eliminado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        }
    }

    // Método para buscar un cliente por DNI
    public ClienteBean BuscarCliente(int dni) {
        if (dni <= 0) {
            System.err.println("Error: El DNI no puede ser menor o igual a 0.");
            return null;
        }

        try {
            return clienteServices.findById(dni);
        } catch (Exception e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
            return null;
        }
    }

    // Método para listar todos los clientes
    public List<ClienteBean> ListaClientes() {
        try {
            return clienteServices.findAll();
        } catch (Exception e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
            return null;
        }
    }

    // Método para validar los datos del cliente
    private boolean validarCliente(ClienteBean cli) {
        if (cli.getDni() <= 0) {
            System.err.println("Error: El DNI no puede ser menor o igual a 0.");
            return false;
        }

        if (cli.getNombre() == null || cli.getNombre().trim().isEmpty()) {
            System.err.println("Error: El nombre no puede estar vacío.");
            return false;
        }

        if (cli.getDireccion() == null || cli.getDireccion().trim().isEmpty()) {
            System.err.println("Error: La dirección no puede estar vacía.");
            return false;
        }

        if (cli.getTelefono() == null || cli.getTelefono().trim().isEmpty()) {
            System.err.println("Error: El teléfono no puede estar vacío.");
            return false;
        }

        if (cli.getEmail() == null || !validarEmail(cli.getEmail())) {
            System.err.println("Error: El email no es válido.");
            return false;
        }

        return true;
    }

    // Método para validar el formato del email
    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(regex).matcher(email).matches();
    }
}