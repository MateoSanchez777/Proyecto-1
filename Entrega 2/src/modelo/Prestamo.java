package modelo;

import java.util.List;
import modelo.juegos.Copia;
import modelo.usuarios.UsuarioComprador;
import modelo.usuarios.Mesero;

public class Prestamo {
    private List<Copia> copiasPrestadas;
    private UsuarioComprador usuario;
    private Mesa mesa; // Puede ser null si es un empleado sin mesa
    private String fechaPrestamo;
    private String fechaDevolucion; // Null si esta activo
    private Mesero meseroIntroductor; // Null si no aplica

    public Prestamo(List<Copia> copiasPrestadas, UsuarioComprador usuario, Mesa mesa, String fechaPrestamo, Mesero meseroIntroductor) {
        this.copiasPrestadas = copiasPrestadas;
        this.usuario = usuario;
        this.mesa = mesa;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = null;
        this.meseroIntroductor = meseroIntroductor;
    }

    public List<Copia> getCopiasPrestadas() { return copiasPrestadas; }
    public UsuarioComprador getUsuario() { return usuario; }
    public Mesa getMesa() { return mesa; }
    public String getFechaPrestamo() { return fechaPrestamo; }
    public String getFechaDevolucion() { return fechaDevolucion; }
    public Mesero getMeseroIntroductor() { return meseroIntroductor; }

    public void finalizarPrestamo(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}
