package pruebas;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import logica.BoardGameCafe;
import modelo.juegos.Copia;
import modelo.juegos.Juego;
import modelo.usuarios.Cliente;
import modelo.usuarios.Empleado;
import modelo.usuarios.Mesero;
import modelo.ventas.ItemVenta;

public class PruebaVentaJuegos {

    private BoardGameCafe cafe;
    private Empleado emp;
    private Cliente cliente;
    private Juego j1;
    private Copia c1, c2;

    @Before
    public void setUp() {
        cafe    = new BoardGameCafe();
        emp     = new Mesero("emp1", "pass", "DESC20");
        cliente = new Cliente("cli1", "pass", 100); // 100 puntos iniciales

        j1 = new Juego("Uno", "Mattel", 2000, 2, 10, 5, "Cartas", false, 20000);
        c1 = new Copia("U1", j1, "Nuevo", "Venta", true);
        c2 = new Copia("U2", j1, "Nuevo", "Venta", true);

        cafe.registrarUsuario(emp);
        cafe.registrarUsuario(cliente);
        cafe.agregarJuego(j1);
        cafe.agregarCopia(c1);
        cafe.agregarCopia(c2);
    }

    @Test
    public void testVentaEmpleadoRegistradaCorrectamente() throws Exception {
        List<ItemVenta> items = new ArrayList<>();
        items.add(new ItemVenta(c1, j1.getPrecioVenta(), 1));
        cafe.venderProductos(emp, items, null);
        assertEquals("Debe registrarse 1 venta", 1, cafe.getVentas().size());
    }

    @Test
    public void testDescuentoEmpleadoAplicado() throws Exception {
        List<ItemVenta> items = new ArrayList<>();
        items.add(new ItemVenta(c1, j1.getPrecioVenta(), 1));
        cafe.venderProductos(emp, items, null);
        // subtotal=20000, IVA 19%=3800, propina 10%=2000, descuento empleado 20%=4000
        // total = 20000 + 3800 + 2000 - 4000 = 21800
        assertEquals(21800.0, cafe.getVentas().get(0).getTotalFinal(), 0.01);
    }

    @Test
    public void testCopiaNoDisponibleTrasVenta() throws Exception {
        List<ItemVenta> items = new ArrayList<>();
        items.add(new ItemVenta(c1, j1.getPrecioVenta(), 1));
        cafe.venderProductos(emp, items, null);
        assertFalse("La copia vendida no debe quedar disponible", c1.isDisponible());
    }

    @Test
    public void testClienteUsaPuntosFidelidadComoDescuento() throws Exception {
        int puntosAntes = cliente.getPuntosFidelidad(); // 100
        List<ItemVenta> items = new ArrayList<>();
        items.add(new ItemVenta(c2, j1.getPrecioVenta(), 1));
        cafe.venderProductos(cliente, items, null);
        // Los 100 puntos se usaron como descuento, luego se generan nuevos
        assertNotEquals("Los puntos deben cambiar tras la compra",
                puntosAntes, cliente.getPuntosFidelidad());
    }

    @Test
    public void testVentaGeneraPuntosFidelidadCliente() throws Exception {
        Cliente c = new Cliente("cli2", "pass", 0); // sin puntos
        cafe.registrarUsuario(c);
        List<ItemVenta> items = new ArrayList<>();
        items.add(new ItemVenta(c1, j1.getPrecioVenta(), 1));
        cafe.venderProductos(c, items, null);
        assertTrue("Debe generar puntos de fidelidad", c.getPuntosFidelidad() > 0);
    }

    @Test
    public void testSubtotalJuegosRegistradoEnVenta() throws Exception {
        List<ItemVenta> items = new ArrayList<>();
        items.add(new ItemVenta(c1, j1.getPrecioVenta(), 1));
        cafe.venderProductos(emp, items, null);
        assertEquals("El subtotal de juegos debe ser 20000",
                20000.0, cafe.getVentas().get(0).getSubtotalJuegos(), 0.01);
        assertEquals("El subtotal de cafeteria debe ser 0",
                0.0, cafe.getVentas().get(0).getSubtotalCafeteria(), 0.01);
    }
}