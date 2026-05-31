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
    private double subtotalCafeteria; // subtotal de productos de cafetería
    private double subtotalJuegos;    // subtotal de juegos vendidos

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
        this.subtotalCafeteria = 0;
        this.subtotalJuegos = 0;
    }

    // Constructor extendido para cargar desde persistencia con subtotales por tipo
    public Venta(List<ItemVenta> items, UsuarioComprador comprador, double impuestos, double propina,
                 double totalParcial, double descuentoAplicado, int puntosGenerados, String fecha,
                 double subtotalCafeteria, double subtotalJuegos) {
        this(items, comprador, impuestos, propina, totalParcial, descuentoAplicado, puntosGenerados, fecha);
        this.subtotalCafeteria = subtotalCafeteria;
        this.subtotalJuegos = subtotalJuegos;
    }

    public List<ItemVenta> getItems()             { return items; }
    public UsuarioComprador getComprador()         { return comprador; }
    public double getImpuestos()                  { return impuestos; }
    public double getPropina()                    { return propina; }
    public double getTotalParcial()               { return totalParcial; }
    public double getSubtotal()                   { return totalParcial; } // alias para persistencia
    public double getDescuentoAplicado()          { return descuentoAplicado; }
    public double getTotalFinal()                 { return totalParcial + impuestos + propina - descuentoAplicado; }
    public int getPuntosGenerados()               { return puntosGenerados; }
    public String getFecha()                      { return fecha; }
    public double getSubtotalCafeteria()          { return subtotalCafeteria; }
    public double getSubtotalJuegos()             { return subtotalJuegos; }

    public void setSubtotalCafeteria(double v)    { this.subtotalCafeteria = v; }
    public void setSubtotalJuegos(double v)       { this.subtotalJuegos = v; }
}