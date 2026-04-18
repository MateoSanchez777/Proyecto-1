package pruebas;

import java.util.ArrayList;
import java.util.List;

import logica.BoardGameCafe;
import modelo.juegos.Juego;
import modelo.juegos.Copia;
import modelo.usuarios.Empleado;
import modelo.usuarios.Mesero;
import modelo.ventas.ItemVenta;
import modelo.usuarios.Cliente;

public class PruebaVentaJuegos {
    public static void main(String[] args) {
        System.out.println("--- PRUEBA VENTAS JUEGOS ---");
        BoardGameCafe cafe = new BoardGameCafe();
        
        Empleado emp = new Mesero("emp1", "pass", "DESC20");
        Cliente cliente = new Cliente("cli1", "pass", 100); // 100 pesos de puntos
        
        Juego j1 = new Juego("Uno", "Mattel", 2000, 2, 10, 5, "Cartas", false, 20000);
        Copia c1 = new Copia("U1", j1, "Nuevo", "Venta", true);
        Copia c2 = new Copia("U2", j1, "Nuevo", "Venta", true);
        
        try {
            System.out.println("1. Empleado comprando (Con descuento interno de Empleado)");
            List<ItemVenta> citems1 = new ArrayList<>();
            citems1.add(new ItemVenta(c1, j1.getPrecioVenta(), 1));
            cafe.venderProductos(emp, citems1, null);
            System.out.println("OK: Venta a empleado. Impuestos(19%) y Descuento del 20% aplicado.");
            System.out.println("Total: " + cafe.getVentas().get(0).getTotalFinal());
            
            System.out.println("\n2. Cliente usando puntos de fidelidad para comprar");
            List<ItemVenta> citems2 = new ArrayList<>();
            citems2.add(new ItemVenta(c2, j1.getPrecioVenta(), 1));
            cafe.venderProductos(cliente, citems2, null);
            System.out.println("OK: Descuento de fidelidad (100) aplicado.");
            System.out.println("Total al Cliente: " + cafe.getVentas().get(1).getTotalFinal());
            System.out.println("Puntos fidelidad restantes (tras ganar los nuevos 1%): " + cliente.getPuntosFidelidad());
            
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }
}
