package pruebas;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import logica.BoardGameCafe;
import modelo.turnos.SolicitudCambioTurno;
import modelo.turnos.Turno;
import modelo.usuarios.Administrador;
import modelo.usuarios.Cocinero;
import modelo.usuarios.Mesero;

public class PruebaTurnos {

    private BoardGameCafe cafe;
    private Administrador admin;
    private Mesero m1, m2;
    private Cocinero c1;

    @Before
    public void setUp() {
        cafe  = new BoardGameCafe();
        admin = new Administrador("admin", "admin123");
        m1 = new Mesero("m1", "1", "A");
        m2 = new Mesero("m2", "1", "B");
        c1 = new Cocinero("c1", "1", "C");
        m1.setTurno(new Turno("Lunes", "Mañana"));
        m2.setTurno(new Turno("Lunes", "Noche"));
        cafe.registrarUsuario(m1);
        cafe.registrarUsuario(m2);
        cafe.registrarUsuario(c1);
        cafe.registrarUsuario(admin);
    }

    @Test
    public void testTurnosOriginalesCorrectos() {
        assertEquals("Mañana", m1.getTurno().getHorario());
        assertEquals("Noche",  m2.getTurno().getHorario());
    }

    @Test
    public void testCambioTurnoExitoso() throws Exception {
        SolicitudCambioTurno sol = new SolicitudCambioTurno(m1, m2, "12/05");
        cafe.cambiarTurno(sol, admin);
        assertEquals("Los turnos deben intercambiarse", "Noche",   m1.getTurno().getHorario());
        assertEquals("Los turnos deben intercambiarse", "Mañana",  m2.getTurno().getHorario());
    }

    @Test
    public void testCambioTurnoFallaSinMinimoMeseros() {
        // Café con solo 1 mesero registrado — no cumple mínimo de 2
        BoardGameCafe cafeChico = new BoardGameCafe();
        Mesero soloMesero = new Mesero("m3", "1", "D");
        Mesero reemplazo  = new Mesero("m4", "1", "E");
        soloMesero.setTurno(new Turno("Martes", "Tarde"));
        reemplazo.setTurno(new Turno("Martes", "Noche"));
        cafeChico.registrarUsuario(soloMesero);
        cafeChico.registrarUsuario(new Cocinero("c2", "1", "F"));

        SolicitudCambioTurno sol = new SolicitudCambioTurno(soloMesero, reemplazo, "13/05");
        try {
            cafeChico.cambiarTurno(sol, admin);
            fail("Deberia lanzar excepcion: minimo de personal no cumplido");
        } catch (Exception e) {
            assertTrue(e.getMessage().toLowerCase().contains("minimo") ||
                       e.getMessage().toLowerCase().contains("personal"));
        }
    }

    @Test
    public void testAsignarTurnoAEmpleado() {
        Turno t = new Turno("Viernes", "Tarde");
        m1.setTurno(t);
        assertNotNull(m1.getTurno());
        assertEquals("Viernes", m1.getTurno().getDia());
        assertEquals("Tarde",   m1.getTurno().getHorario());
    }
}