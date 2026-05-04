package pruebas;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logica.BoardGameCafe;
import modelo.juegos.Juego;
import modelo.usuarios.Cliente;
import modelo.usuarios.Mesero;
import modelo.turnos.Turno;
import modelo.torneos.TorneoAmistoso;
import modelo.torneos.TorneoCompetitivo;

public class PruebaTorneos {
    
    private BoardGameCafe cafe;
    private Juego juego1;
    private Cliente cliente1;
    private Mesero mesero1;
    private TorneoAmistoso torneoA;
    private TorneoCompetitivo torneoC;

    @BeforeEach
    public void setUp() {
        cafe = new BoardGameCafe();
        
        juego1 = new Juego("Catan", "Devir", 1995, 3, 4, 10, "Tablero", false, 150000);
        cafe.agregarJuego(juego1);
        
        cliente1 = new Cliente("juan", "123", 0);
        mesero1 = new Mesero("maria", "123", "descuento");
        
        cafe.registrarUsuario(cliente1);
        cafe.registrarUsuario(mesero1);
        
        torneoA = new TorneoAmistoso("Catan Amistoso", juego1, "Lunes", 10);
        torneoC = new TorneoCompetitivo("Catan Pro", juego1, "Viernes", 5, 20000);
        
        cafe.crearTorneo(torneoA);
        cafe.crearTorneo(torneoC);
    }

    @Test
    public void testInscribirClienteNormal() {
        assertDoesNotThrow(() -> {
            cafe.inscribirEnTorneo(cliente1, torneoA, 2);
        });
        assertEquals(2, torneoA.getCuposOcupados());
        assertEquals(1, torneoA.getInscripciones().size());
    }

    @Test
    public void testInscripcionExcedeCupos() {
        Exception exception = assertThrows(Exception.class, () -> {
            cafe.inscribirEnTorneo(cliente1, torneoA, 4); // Maximo permitido es 3
        });
        assertTrue(exception.getMessage().contains("entre 1 y 3 participantes"));
    }

    @Test
    public void testInscribirEmpleadoConTurno() {
        Turno turno = new Turno("Lunes", "Mañana");
        mesero1.setTurno(turno);
        
        Exception exception = assertThrows(Exception.class, () -> {
            cafe.inscribirEnTorneo(mesero1, torneoA, 1); // TorneoA es en Lunes
        });
        assertTrue(exception.getMessage().contains("tienen turno el mismo dia"));
        
        assertDoesNotThrow(() -> {
            cafe.inscribirEnTorneo(mesero1, torneoC, 1); // TorneoC es en Viernes
        });
    }

    @Test
    public void testInscribirFanatico() {
        cliente1.agregarJuegoFavorito(juego1);
        assertDoesNotThrow(() -> {
            cafe.inscribirEnTorneo(cliente1, torneoA, 1);
        });
        assertEquals(1, torneoA.getCuposFanaticosOcupados());
    }

    @Test
    public void testDesinscribirUsuario() {
        assertDoesNotThrow(() -> {
            cafe.inscribirEnTorneo(cliente1, torneoA, 2);
        });
        assertEquals(2, torneoA.getCuposOcupados());
        
        cafe.desinscribirDeTorneo(cliente1, torneoA);
        assertEquals(0, torneoA.getCuposOcupados());
    }
}
