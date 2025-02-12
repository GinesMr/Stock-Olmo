package model.ClienteBean;

import java.io.Serializable;
import java.util.Date;
//Modelo Bean
public class ClienteBean implements Serializable {
    private int dni;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private Date fechaRegistro;


    public ClienteBean() {
        this.fechaRegistro = new Date();
    }


    public ClienteBean(int dni, String nombre, String direccion, String telefono, String email, Date fechaRegistro) {
        this.dni = dni;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.fechaRegistro = fechaRegistro != null ? fechaRegistro : new Date();
    }



    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        if (dni > 0) {
            this.dni = dni;
        } else {
            throw new IllegalArgumentException("DNI inválido");
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre;
        } else {
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email inválido");
        }
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro != null ? fechaRegistro : new Date();
    }

    @Override
    public String toString() {
        return "ClienteBean{" +
                "dni=" + dni +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }

}