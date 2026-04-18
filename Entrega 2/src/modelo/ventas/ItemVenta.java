package modelo.ventas;

public class ItemVenta {
    private Object item; // Copia o ProductoCafeteria
    private double precioUnitario;
    private int cantidad;

    public ItemVenta(Object item, double precioUnitario, int cantidad) {
        this.item = item;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public Object getItem() { return item; }
    public double getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return precioUnitario * cantidad; }
}
