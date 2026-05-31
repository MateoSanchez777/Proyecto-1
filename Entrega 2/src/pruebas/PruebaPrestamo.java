package pruebas;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import logica.BoardGameCafe;
import modelo.Mesa;
import modelo.juegos.Copia;
import modelo.juegos.Juego;
import modelo.turnos.Turno;
import modelo.usuarios.Cliente;
import modelo.usuarios.Mesero;

public class PruebaPrestamo {

    private BoardGameCafe cafe;
    private Cliente usr1;
    private Juego j1, jDificil, jAdulto;
    private Copia c1, c2, c3;
    private Mesero m1;
    private Mesa mesa1, mesaMenores;

    @Before
    public void setUp() {
        cafe = new BoardGameCafe();
        usr1 = new Cliente("andres", "123", 0);

        j1      = new Juego("Catan", "Devir", 1995, 3, 4, 10, "Tablero", false, 150000);
        jDificil = new Juego("Twilight Imperium", "FFG", 2017, 3, 6, 14, "Tablero", true, 300000);
        jAdulto  = new Juego("Cards A.H.", "CAH", 2011, 4, 10, 18, "Cartas", false, 50000);

        c1 = new Copia("C1", j1,      "Nuevo", "Prestamo", true);
        c2 = new Copia("C2", jDificil,"Bueno", "Prestamo", true);
        c3 = new Copia("C3", jAdulto, "Bueno", "Prestamo", true);

        cafe.registrarUsuario(usr1);
        cafe.agregarCopia(c1);
        cafe.agregarCopia(c2);
        cafe.agregarCopia(c3);

        m1 = new Mesero("mesero1", "abc", "M123");
        mesa1       = new Mesa(1, 4, false, false);
        mesaMenores = new Mesa(2, 4, false, true);
    }

    @Test
    public void testPrestamoNormalExitoso() throws Exception {
        List<Copia> pedir = new ArrayList<>();
        pedir.add(c1);
        cafe.realizarPrestamo(usr1, mesa1, pedir, null);
        assertFalse("La copia debe quedar no disponible", c1.isDisponible());
        assertEquals("El usuario debe tener 1 prestamo activo", 1, usr1.getPrestamosActuales().size());
    }

    @Test
    public void testPrestamoBlockeadoPorEdad() {
        List<Copia> pedirAdulto = new ArrayList<>();
        pedirAdulto.add(c3);
        try {
            cafe.realizarPrestamo(usr1, mesaMenores, pedirAdulto, null);
            fail("Deberia lanzar excepcion por restriccion de edad");
        } catch (Exception e) {
            assertTrue("El mensaje debe mencionar menores o adultos",
                    e.getMessage().contains("menores") || e.getMessage().contains("adultos"));
        }
    }

    @Test
    public void testPrestamoBlockeadoPorCopiaNoDisponible() throws Exception {
        List<Copia> pedir = new ArrayList<>();
        pedir.add(c1);
        cafe.realizarPrestamo(usr1, mesa1, pedir, null); // primer prestamo OK

        try {
            cafe.realizarPrestamo(usr1, mesa1, pedir, null); // c1 ya no esta disponible
            fail("Deberia lanzar excepcion: copia no disponible");
        } catch (Exception e) {
            assertTrue("El mensaje debe mencionar disponibilidad",
                    e.getMessage().contains("disponible"));
        }
    }

    @Test
    public void testPrestamoJuegoDificilSinMeseroCapacitado() throws Exception {
        List<Copia> pedirDificil = new ArrayList<>();
        pedirDificil.add(c2);
        // m1 no conoce el juego: debe emitir advertencia pero NO lanzar excepcion
        cafe.realizarPrestamo(usr1, mesa1, pedirDificil, m1);
        assertFalse("La copia dificil debe quedar no disponible", c2.isDisponible());
    }

    @Test
    public void testEmpleadoEnTurnoNoPuedePedirPrestamo() throws Exception {
        Mesero meseroTurno = new Mesero("m2", "123", "M456");
        meseroTurno.setTurno(new Turno("Lunes", "Mañana"));
        cafe.registrarUsuario(meseroTurno);

        Copia c5 = new Copia("C5", j1, "Nuevo", "Prestamo", true);
        cafe.agregarCopia(c5);

        List<Copia> pedirEmpleado = new ArrayList<>();
        pedirEmpleado.add(c5);

        try {
            cafe.realizarPrestamo(meseroTurno, mesa1, pedirEmpleado, null);
            fail("Deberia lanzar excepcion: empleado en turno no puede pedir prestamo");
        } catch (Exception e) {
            assertTrue("El mensaje debe mencionar el turno",
                    e.getMessage().toLowerCase().contains("turno"));
        }
    }

    @Test
    public void testPrestamoJuegoAccionConMesaLanzaExcepcion() {
        Juego juegoAccion = new Juego("UNO Attack", "Mattel", 2019, 2, 6, 6, "Accion", false, 30000);
        Copia copiaAccion = new Copia("C4", juegoAccion, "Nuevo", "Prestamo", true);
        cafe.agregarCopia(copiaAccion);

        List<Copia> pedirAccion = new ArrayList<>();
        pedirAccion.add(copiaAccion);

        try {
            cafe.realizarPrestamo(usr1, mesa1, pedirAccion, null);
            fail("Deberia lanzar excepcion por juego de accion con mesa");
        } catch (Exception e) {
            assertNotNull("Debe haber un mensaje de error", e.getMessage());
        }
    }
}