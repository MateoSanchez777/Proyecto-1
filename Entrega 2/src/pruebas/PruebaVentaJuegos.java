package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logica.BoardGameCafe;
import modelo.juegos.Copia;
import modelo.juegos.Juego;
import modelo.usuarios.Cliente;
import modelo.ventas.ItemVenta;
import modelo.ventas.Venta;

class PruebaVentaJuegos {

    private BoardGameCafe cafe;
    private Cliente cliente;
    private Juego juego;
    private Copia copia;

    @BeforeEach
    void setUp() throws Exception {
        cafe = new BoardGameCafe();
        cliente = new Cliente("cliente1", "pass123", 0);
        cafe.registrarUsuario(cliente);
        
        juego = new Juego("Catan", "Devir", 1995, 3, 4, 10, "Tablero", false, 150000);
        cafe.agregarJuego(juego);
        
        copia = new Copia("C1", juego, "Nuevo", "Venta", true);
        cafe.getCopias().put("C1", copia);
    }

    @Test
    void testVentaCopia() {
        ArrayList<ItemVenta> items = new ArrayList<>();
        items.add(new ItemVenta(copia, 1, 150000));
        
        Venta venta = new Venta(items, cliente, 0, 0, 150000, 0, 10, "15/05/2026");
        
        assertEquals(150000, venta.getSubtotal(), "El subtotal debe ser 150000.");
        assertEquals("Venta", copia.getInventario(), "El inventario debe ser de venta.");
        assertEquals("cliente1", venta.getComprador().getLogin(), "El comprador debe ser cliente1.");
    }
}