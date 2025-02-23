package model.ProductoBean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ProductoBean {
    private int idProducto;
    private String descripcion;
    private int stockActual;
    private int stockMinimo;
    private float pvp;

    // Soporte para PropertyChangeListener
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    // Constructor
    public ProductoBean(int idProducto, String descripcion, int stockActual, int stockMinimo, float pvp) {
        this.idProducto = idProducto;
        this.descripcion = descripcion;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.pvp = pvp;
    }

    // Getters y setters

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        int oldStock = this.stockActual; // Guardar el valor antiguo
        this.stockActual = stockActual;
        // Notificar a los listeners que el stock ha cambiado
        propertyChangeSupport.firePropertyChange("stockActual", oldStock, stockActual);
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public float getPvp() {
        return pvp;
    }

    public void setPvp(float pvp) {
        this.pvp = pvp;
    }

    // Métodos para agregar y eliminar listeners
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    // Método para acceder al PropertyChangeSupport
    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }
}