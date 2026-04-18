package modelo.ventas;

import java.util.List;
import modelo.usuarios.UsuarioComprador;

public class Venta {
    private List<ItemVenta> items;
    private UsuarioComprador comprador;
    private double impuestos;
    private double propina;
    private double totalParcial;
    private double descuentoAplicado;
    private int puntosGenerados;
    private String fecha;

    public Venta(List<ItemVenta> items, UsuarioComprador comprador, double impuestos, double propina,
                 double totalParcial, double descuentoAplicado, int puntosGenerados, String fecha) {
        this.items = items;
        this.comprador = comprador;
        this.impuestos = impuestos;
        this.propina = propina;
        this.totalParcial = totalParcial;
        this.descuentoAplicado = descuentoAplicado;
        this.puntosGenerados = puntosGenerados;
        this.fecha = fecha;
    }

    public List<ItemVenta> getItems() { return items; }
    public UsuarioComprador getComprador() { return comprador; }
    public double getImpuestos() { return impuestos; }
    public double getPropina() { return propina; }
    public double getTotalParcial() { return totalParcial; }
    public double getDescuentoAplicado() { return descuentoAplicado; }
    public double getTotalFinal() {
        return totalParcial + impuestos + propina - descuentoAplicado;
    }
    public int getPuntosGenerados() { return puntosGenerados; }
    public String getFecha() { return fecha; }
}
