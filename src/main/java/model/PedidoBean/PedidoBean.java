package model.PedidoBean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class PedidoBean implements PropertyChangeListener, Serializable {
    private int numeroPedido;
    private int idProducto;
    private Date fecha;
    private int cantidad;
    private boolean pedir;

    public PedidoBean(int numeroPedido, int idProducto, LocalDate fechaPedido, int cantidad, boolean pedir) {
        this.fecha = new Date(); // Default to current date
    }

    public PedidoBean(int numeroPedido, int idProducto, Date fecha, int cantidad, boolean pedir) {
        this.numeroPedido = numeroPedido;
        this.idProducto = idProducto;
        this.fecha = fecha != null ? fecha : new Date();
        this.cantidad = cantidad;
        this.pedir = pedir;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha != null ? fecha : new Date();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad > 0) {
            this.cantidad = cantidad;
        } else {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
        }
    }

    public boolean isPedir() {
        return pedir;
    }

    public void setPedir(boolean pedir) {
        this.pedir = pedir;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("stockActual".equals(evt.getPropertyName()) || "stockMinimo".equals(evt.getPropertyName())) {
            int stockActual = (int) evt.getNewValue();
            int stockMinimo = (int) evt.getNewValue();

            if (stockActual < stockMinimo) {
                this.pedir = true;
                this.cantidad = stockMinimo - stockActual;
                System.out.println("Pedido generado: " + this);
            } else {
                this.pedir = false;
            }
        }
    }

    @Override
    public String toString() {
        return "PedidoBean{" +
                "numeroPedido=" + numeroPedido +
                ", idProducto=" + idProducto +
                ", fecha=" + fecha +
                ", cantidad=" + cantidad +
                ", pedir=" + pedir +
                '}';
    }
}