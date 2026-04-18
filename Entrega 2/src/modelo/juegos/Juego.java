package modelo.juegos;

public class Juego {
    private String nombre;
    private String empresa;
    private int anioPublicacion;
    private int minJugadores;
    private int maxJugadores;
    private int minEdad; // Ejemplo: 5, 18, etc. (0 si es para todos)
    private String categoria; // "Tablero", "Cartas", "Accion"
    private boolean esDificil;
    private double precioVenta; // > 0 si se puede vender

    public Juego(String nombre, String empresa, int anioPublicacion, int minJugadores, int maxJugadores,
                 int minEdad, String categoria, boolean esDificil, double precioVenta) {
        this.nombre = nombre;
        this.empresa = empresa;
        this.anioPublicacion = anioPublicacion;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.minEdad = minEdad;
        this.categoria = categoria;
        this.esDificil = esDificil;
        this.precioVenta = precioVenta;
    }

    public String getNombre() { return nombre; }
    public String getEmpresa() { return empresa; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public int getMinJugadores() { return minJugadores; }
    public int getMaxJugadores() { return maxJugadores; }
    public int getMinEdad() { return minEdad; }
    public String getCategoria() { return categoria; }
    public boolean isEsDificil() { return esDificil; }
    public double getPrecioVenta() { return precioVenta; }
}
