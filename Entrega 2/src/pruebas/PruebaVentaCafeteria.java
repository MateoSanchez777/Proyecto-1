package pruebas;

import java.util.ArrayList;
import java.util.List;

import logica.BoardGameCafe;
import modelo.Mesa;
import modelo.cafeteria.Bebida;
import modelo.cafeteria.Pasteleria;
import modelo.juegos.Juego;
import modelo.juegos.Copia;
import modelo.usuarios.Cliente;
import modelo.ventas.ItemVenta;

public class PruebaVentaCafeteria {
    public static void main(String[] args) {
        System.out.println("--- PRUEBA VENTAS CAFETERIA ---");
        BoardGameCafe cafe = new BoardGameCafe();
        
        Cliente usr1 = new Cliente("juan", "123", 0);
        Mesa mesaMenores = new Mesa(1, 3, false, true); // Mesa con menores de 18
        
        Bebida cerveza = new Bebida("Cerveza", 5000, true, false);
        Bebida teCaliente = new Bebida("Te Caliente", 3000, false, true);
        List<String> alergenosMani = new ArrayList<>(); alergenosMani.add("Mani");
        Pasteleria brownie = new Pasteleria("Brownie", 4500, alergenosMani);
        
        // 1. Venta normal con alergeno
        try {
            List<ItemVenta> items = new ArrayList<>();
            items.add(new ItemVenta(brownie, brownie.getPrecio(), 2)); // 9000
            System.out.println("1. Comprando Brownies...");
            cafe.venderProductos(usr1, items, mesaMenores);
            System.out.println("OK: Compra exitosa. Total final: " + cafe.getVentas().get(0).getTotalFinal());
        } catch (Exception e) { System.out.println(e.getMessage()); }
        
        // 2. Venta bloqueada por alcohol y menores
        try {
            List<ItemVenta> itemsAlc = new ArrayList<>();
            itemsAlc.add(new ItemVenta(cerveza, cerveza.getPrecio(), 1));
            System.out.println("\n2. Intentando comprar alcohol para mesa con menores...");
            cafe.venderProductos(usr1, itemsAlc, mesaMenores);
        } catch (Exception e) { System.out.println(e.getMessage()); }
        
        // 3. Venta bloqueada por bebida caliente con juego de accion
        Juego jAccion = new Juego("Twister", "Hasbro", 2000, 2, 4, 5, "Accion", false, 40000);
        Copia cTwister = new Copia("T1", jAccion, "Nuevo", "Prestamo", true);
        try {
            List<Copia> copiasPedidas = new ArrayList<>(); copiasPedidas.add(cTwister);
            cafe.realizarPrestamo(usr1, new Mesa(2,2,false,false), copiasPedidas, null);
            
            System.out.println("\n3. Ya prestado juego de Accion. Intentando pedir bebida caliente...");
            List<ItemVenta> itemsCaliente = new ArrayList<>();
            itemsCaliente.add(new ItemVenta(teCaliente, teCaliente.getPrecio(), 1));
            cafe.venderProductos(usr1, itemsCaliente, new Mesa(2,2,false,false));
            
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }
}
