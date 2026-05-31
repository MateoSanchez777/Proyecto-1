package modelo;

import modelo.usuarios.Cliente;

public class Reserva {
    private String fecha;
    private String diaSemana; // "Lunes", "Martes", etc.
    private Cliente cliente;
    private Mesa mesa;
    private String hora;
    
    public Reserva(String fecha, String diaSemana, Cliente cliente, Mesa mesa, String hora) {
        this.fecha = fecha;
        this.diaSemana = diaSemana;
        this.cliente = cliente;
        this.mesa = mesa;
        this.hora = hora;
    }

    public String getFecha() { return fecha; }
    public String getDiaSemana() { return diaSemana; }
    public Cliente getCliente() { return cliente; }
    public Mesa getMesa() { return mesa; }
    public String getHora() { return hora; }
}
