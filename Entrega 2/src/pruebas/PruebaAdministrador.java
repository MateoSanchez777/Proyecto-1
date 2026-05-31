package pruebas;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import logica.BoardGameCafe;
import modelo.juegos.Copia;
import modelo.juegos.Juego;
import modelo.usuarios.Administrador;

public class PruebaAdministrador {

    private BoardGameCafe cafe;
    private Juego j1;
    private Copia c1;
    private Administrador admin;

    @Before
    public void setUp() {
        cafe = new BoardGameCafe();
        admin = new Administrador("admin", "admin123");
        j1 = new Juego("Monopoly", "Hasbro", 1935, 2, 6, 8, "Tablero", false, 80000);
        c1 = new Copia("M1", j1, "Nuevo", "Venta", true);
        cafe.agregarJuego(j1);
        cafe.agregarCopia(c1);
        cafe.registrarUsuario(admin);
    }

    @Test
    public void testMoverCopiaDeVentaAPrestamo() {
        assertEquals("El inventario inicial debe ser Venta", "Venta", c1.getInventario());
        c1.setInventario("Prestamo");
        assertEquals("El inventario debe actualizarse a Prestamo", "Prestamo", c1.getInventario());
    }

    @Test
    public void testMarcarCopiaComoDesaparecida() {
        c1.setEstado("Desaparecido");
        c1.setDisponible(false);
        assertEquals("El estado debe ser Desaparecido", "Desaparecido", c1.getEstado());
        assertFalse("La copia no debe estar disponible", c1.isDisponible());
    }

    @Test
    public void testGuardarDatosNoLanzaExcepcion() {
        try {
            cafe.guardarDatos();
        } catch (Exception e) {
            fail("guardarDatos() no deberia lanzar excepcion: " + e.getMessage());
        }
    }

    @Test
    public void testInformeFinancieroInicialmenteVacio() {
        assertEquals("No debe haber ventas al inicio", 0, cafe.getVentas().size());
    }

    @Test
    public void testAdminRegistradoEnSistema() {
        assertNotNull("El admin debe existir en el sistema",
                cafe.getUsuarios().get("admin"));
    }
}