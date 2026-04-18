package modelo.usuarios;

public class Cliente extends UsuarioComprador {
    private int puntosFidelidad;

    public Cliente(String login, String password, int puntosFidelidadIniciales) {
        super(login, password);
        this.puntosFidelidad = puntosFidelidadIniciales;
    }

    public int getPuntosFidelidad() { return puntosFidelidad; }

    public void agregarPuntosFidelidad(int puntos) {
        this.puntosFidelidad += puntos;
    }

    public void restarPuntosFidelidad(int puntos) {
        this.puntosFidelidad -= puntos;
    }
}
