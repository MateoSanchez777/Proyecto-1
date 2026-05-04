package modelo.torneos;

import java.util.ArrayList;
import java.util.List;
import modelo.juegos.Juego;
import modelo.usuarios.UsuarioComprador;

public abstract class Torneo {
    protected String nombre;
    protected Juego juego;
    protected String diaSemana;
    protected int maxParticipantes;
    protected int cuposFanaticosTotal;
    protected List<Inscripcion> inscripciones;

    public Torneo(String nombre, Juego juego, String diaSemana, int maxParticipantes) {
        this.nombre = nombre;
        this.juego = juego;
        this.diaSemana = diaSemana;
        this.maxParticipantes = maxParticipantes;
        this.cuposFanaticosTotal = (int) Math.ceil(maxParticipantes * 0.2);
        this.inscripciones = new ArrayList<>();
    }

    public String getNombre() { return nombre; }
    public Juego getJuego() { return juego; }
    public String getDiaSemana() { return diaSemana; }
    public int getMaxParticipantes() { return maxParticipantes; }
    public int getCuposFanaticosTotal() { return cuposFanaticosTotal; }
    public List<Inscripcion> getInscripciones() { return inscripciones; }

    public int getCuposOcupados() {
        int ocupados = 0;
        for (Inscripcion ins : inscripciones) {
            ocupados += ins.getCantidadCupos();
        }
        return ocupados;
    }

    public int getCuposFanaticosOcupados() {
        int ocupados = 0;
        for (Inscripcion ins : inscripciones) {
            if (ins.isFanatico()) {
                ocupados += ins.getCantidadCupos();
            }
        }
        return ocupados;
    }

    public boolean puedeInscribir(int cantidad, boolean esFanatico) {
        if (getCuposOcupados() + cantidad > maxParticipantes) {
            return false;
        }
        // Si es fanatico y hay cupos de fanatico, bien. Si no hay cupos de fanatico, toma regulares.
        return true;
    }

    public void inscribir(Inscripcion inscripcion) throws Exception {
        if (!puedeInscribir(inscripcion.getCantidadCupos(), inscripcion.isFanatico())) {
            throw new Exception("No hay cupos suficientes en el torneo.");
        }
        this.inscripciones.add(inscripcion);
    }

    public void desinscribir(UsuarioComprador usuario) {
        inscripciones.removeIf(ins -> ins.getUsuario().getLogin().equals(usuario.getLogin()));
    }
}
