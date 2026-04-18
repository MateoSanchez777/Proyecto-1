package modelo.usuarios;

import java.util.ArrayList;
import java.util.List;
import modelo.juegos.Juego;

public class Mesero extends Empleado {
    private List<Juego> juegosQueConoce;

    public Mesero(String login, String password, String codigoDescuento) {
        super(login, password, codigoDescuento);
        this.juegosQueConoce = new ArrayList<>();
    }

    public void agregarJuegoConocido(Juego juego) {
        if (!juegosQueConoce.contains(juego)) {
            juegosQueConoce.add(juego);
        }
    }

    public List<Juego> getJuegosQueConoce() { return juegosQueConoce; }
}
