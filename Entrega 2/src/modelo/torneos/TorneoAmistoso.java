package modelo.torneos;

import modelo.juegos.Juego;

public class TorneoAmistoso extends Torneo {

    public TorneoAmistoso(String nombre, Juego juego, String diaSemana, int maxParticipantes) {
        super(nombre, juego, diaSemana, maxParticipantes);
    }

    // El premio será un bono de descuento en porcentaje o monto fijo.
    public double calcularPremioBono() {
        return 15.0; // Ejemplo: Bono del 15% de descuento
    }
}
