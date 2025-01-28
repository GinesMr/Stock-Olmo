package model.ProductoBean;

import java.io.Serializable;
import java.util.Date;

public class ProductoBean implements Serializable {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int cantidad;
    private Date fechaRegistro;

    public ProductoBean() {
        this.fechaRegistro = new Date();
    }

    public ProductoBean(int id, String nombre, String descripcion, double precio, int cantidad, Date fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.fechaRegistro = fechaRegistro != null ? fechaRegistro : new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID inválido");
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio >= 0) {
            this.precio = precio;
        } else {
            throw new IllegalArgumentException("Precio no puede ser negativo");
        }
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad >= 0) {
            this.cantidad = cantidad;
        } else {
            throw new IllegalArgumentException("Cantidad no puede ser negativa");
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
        return "ProductoBean{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}