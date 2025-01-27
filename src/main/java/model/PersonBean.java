package model;

import java.beans.PropertyChangeSupport;

public class PersonBean {

    private final PropertyChangeSupport propertyChangeSupport;
    private String dni;
    private String nombre;
    private String[] direccion;

    public PersonBean() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.direccion = new String[2];
    }



    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        String oldDni = this.dni;
        if (dni == null || dni.length() !=9) {
            throw new IllegalArgumentException("El dni no puede ser mayor que 9.");
        }
            this.dni = dni;
            propertyChangeSupport.firePropertyChange("dni", oldDni, dni); // Notifica el cambio


    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        String oldNombre = this.nombre;
        this.nombre = nombre;
        propertyChangeSupport.firePropertyChange("nombre", oldNombre, nombre); // Notifica el cambio
    }

    public String[] getDireccion() {
        return direccion.clone();
    }

    public void setDireccion(String[] direccion) {
        if (direccion == null || direccion.length != 3) {
            throw new IllegalArgumentException("La dirección debe ser un array de 3 elementos.");
        }
        String[] oldDireccion = this.direccion;
        this.direccion = direccion.clone(); // Almacena una copia para evitar modificación externa
        propertyChangeSupport.firePropertyChange("direccion", oldDireccion, direccion); // Notifica el cambio
    }
    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}