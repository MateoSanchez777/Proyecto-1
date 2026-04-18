package modelo.juegos;

public class Copia {
    private String id;
    private Juego juego;
    private String estado; // "Nuevo", "Bueno", "Falta una pieza", "Desaparecido"
    private String inventario; // "Venta", "Prestamo"
    private boolean disponible; // Indica si esta prestada/vendida

    public Copia(String id, Juego juego, String estado, String inventario, boolean disponible) {
        this.id = id;
        this.juego = juego;
        this.estado = estado;
        this.inventario = inventario;
        this.disponible = disponible;
    }

    public String getId() { return id; }
    public Juego getJuego() { return juego; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getInventario() { return inventario; }
    public void setInventario(String inventario) { this.inventario = inventario; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
