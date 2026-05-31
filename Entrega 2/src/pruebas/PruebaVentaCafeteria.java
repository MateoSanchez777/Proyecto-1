package pruebas;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import logica.BoardGameCafe;
import modelo.Mesa;
import modelo.cafeteria.Bebida;
import modelo.cafeteria.Pasteleria;
import modelo.juegos.Copia;
import modelo.juegos.Juego;
import modelo.usuarios.Cliente;
import modelo.ventas.ItemVenta;

public class PruebaVentaCafeteria {

    private BoardGameCafe cafe;
    private Cliente usr1;
    private Mesa mesaMenores;
    private Bebida cerveza, teCaliente;
    private Pasteleria brownie;

    @Before
    public void setUp() {
        cafe = new BoardGameCafe();
        usr1 = new Cliente("juan", "123", 0);
        mesaMenores = new Mesa(1, 3, false, true);

        cerveza    = new Bebida("Cerveza", 5000, true, false);
        teCaliente = new Bebida("Te Caliente", 3000, false, true);

        List<String> alergenos = new ArrayList<>();
        alergenos.add("Mani");
        brownie = new Pasteleria("Brownie", 4500, alergenos);
    }

    @Test
    public void testVentaNormalConAlergeno() throws Exception {
        List<ItemVenta> items = new ArrayList<>();
        items.add(new ItemVenta(brownie, brownie.getPrecio(), 2));
        cafe.venderProductos(usr1, items, mesaMenores);

        assertEquals("Debe registrarse 1 venta", 1, cafe.getVentas().size());
        assertTrue("El total debe ser positivo", cafe.getVentas().get(0).getTotalFinal() > 0);
    }

    @Test
    public void testAlcoholBlockeadoPorMenoresEnMesa() {
        List<ItemVenta> itemsAlc = new ArrayList<>();
        itemsAlc.add(new ItemVenta(cerveza, cerveza.getPrecio(), 1));

        try {
            cafe.venderProductos(usr1, itemsAlc, mesaMenores);
            fail("Deberia lanzar excepcion: no se puede vender alcohol a mesa con menores");
        } catch (Exception e) {
            assertTrue("El mensaje debe mencionar menores o alcohol",
                    e.getMessage().toLowerCase().contains("menores") ||
                    e.getMessage().toLowerCase().contains("alcohol"));
        }
    }

    @Test
    public void testBebidaCalienteBlockeadaPorJuegoAccion() {
        // Simulamos directamente que el usuario tiene un juego de accion prestado
        Juego jAccion = new Juego("Twister", "Hasbro", 2000, 2, 4, 5, "Accion", false, 40000);
        Copia cTwister = new Copia("T1", jAccion, "Nuevo", "Prestamo", false);
        usr1.agregarPrestamo(cTwister);

        List<ItemVenta> itemsCaliente = new ArrayList<>();
        itemsCaliente.add(new ItemVenta(teCaliente, teCaliente.getPrecio(), 1));

        try {
            cafe.venderProductos(usr1, itemsCaliente, new Mesa(2, 2, false, false));
            fail("Deberia lanzar excepcion: bebida caliente con juego de accion activo");
        } catch (Exception e) {
            assertTrue("El mensaje debe mencionar Accion o caliente",
                    e.getMessage().contains("Accion") || e.getMessage().contains("caliente"));
        }
    }

    @Test
    public void testVentaExitosaGeneraPuntosCliente() throws Exception {
        List<ItemVenta> items = new ArrayList<>();
        items.add(new ItemVenta(brownie, brownie.getPrecio(), 1));
        cafe.venderProductos(usr1, items, mesaMenores);

        assertTrue("La venta exitosa debe generar puntos de fidelidad",
                usr1.getPuntosFidelidad() > 0);
    }
}