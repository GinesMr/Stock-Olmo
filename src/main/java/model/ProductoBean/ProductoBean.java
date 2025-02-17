package model.ProductoBean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public class ProductoBean implements Serializable {
    private int idProducto;
    private String descripcion;
    private int stockActual;
    private int stockMinimo;
    private float pvp;

    // PropertyChangeSupport to manage bound properties
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public ProductoBean() {
    }

    public ProductoBean(int idProducto, String descripcion, int stockActual, int stockMinimo, float pvp) {
        this.idProducto = idProducto;
        this.descripcion = descripcion;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.pvp = pvp;
    }

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
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            this.descripcion = descripcion;
        } else {
            throw new IllegalArgumentException("Descripción no puede estar vacía");
        }
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        int oldStockActual = this.stockActual;
        this.stockActual = stockActual;
        // Notify listeners of the change
        propertyChangeSupport.firePropertyChange("stockActual", oldStockActual, this.stockActual);
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        int oldStockMinimo = this.stockMinimo;
        this.stockMinimo = stockMinimo;
        // Notify listeners of the change
        propertyChangeSupport.firePropertyChange("stockMinimo", oldStockMinimo, this.stockMinimo);
    }

    public float getPvp() {
        return pvp;
    }

    public void setPvp(float pvp) {
        if (pvp >= 0) {
            this.pvp = pvp;
        } else {
            throw new IllegalArgumentException("PVP no puede ser negativo");
        }
    }

    // Methods to manage PropertyChangeListeners
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return "ProductoBean{" +
                "idProducto=" + idProducto +
                ", descripcion='" + descripcion + '\'' +
                ", stockActual=" + stockActual +
                ", stockMinimo=" + stockMinimo +
                ", pvp=" + pvp +
                '}';
    }
}