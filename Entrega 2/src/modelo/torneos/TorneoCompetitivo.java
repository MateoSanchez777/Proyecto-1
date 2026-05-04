package modelo.torneos;

import modelo.juegos.Juego;

public class TorneoCompetitivo extends Torneo {
    private double tarifaEntrada;

    public TorneoCompetitivo(String nombre, Juego juego, String diaSemana, int maxParticipantes, double tarifaEntrada) {
        super(nombre, juego, diaSemana, maxParticipantes);
        this.tarifaEntrada = tarifaEntrada;
    }

    public double getTarifaEntrada() {
        return tarifaEntrada;
    }

    // El premio es un porcentaje del total recaudado (ej. 80%)
    public double calcularPremioMetalico() {
        int cuposPagados = 0;
        for (Inscripcion ins : inscripciones) {
            if (!ins.isEmpleado()) {
                cuposPagados += ins.getCantidadCupos();
            }
        }
        return cuposPagados * tarifaEntrada * 0.8;
    }
}
