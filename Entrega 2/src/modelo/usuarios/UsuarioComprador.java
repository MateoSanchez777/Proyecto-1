package modelo.usuarios;

import java.util.ArrayList;
import java.util.List;
import modelo.juegos.Juego;
import modelo.juegos.Copia;
import modelo.ventas.Venta;

public abstract class UsuarioComprador extends Usuario {
    protected List<Juego> juegosFavoritos;
    protected List<Copia> prestamosActuales;
    protected List<Venta> comprasGlobales;

    public UsuarioComprador(String login, String password) {
        super(login, password);
        this.juegosFavoritos = new ArrayList<>();
        this.prestamosActuales = new ArrayList<>();
        this.comprasGlobales = new ArrayList<>();
    }

    public void agregarJuegoFavorito(Juego juego) {
        if (!juegosFavoritos.contains(juego)) {
            juegosFavoritos.add(juego);
        }
    }

    public void agregarPrestamo(Copia copia) {
        prestamosActuales.add(copia);
    }

    public void removerPrestamo(Copia copia) {
        prestamosActuales.remove(copia);
    }

    public void registrarCompra(Venta venta) {
        comprasGlobales.add(venta);
    }

    public List<Juego> getJuegosFavoritos() { return juegosFavoritos; }
    public List<Copia> getPrestamosActuales() { return prestamosActuales; }
    public List<Venta> getComprasGlobales() { return comprasGlobales; }
}
