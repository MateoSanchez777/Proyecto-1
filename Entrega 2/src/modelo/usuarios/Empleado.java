package modelo.usuarios;

import modelo.turnos.Turno;

public abstract class Empleado extends UsuarioComprador {
    protected String codigoDescuento;
    protected Turno turno;

    public Empleado(String login, String password, String codigoDescuento) {
        super(login, password);
        this.codigoDescuento = codigoDescuento;
    }

    public String getCodigoDescuento() { return codigoDescuento; }
    public Turno getTurno() { return turno; }
    public void setTurno(Turno turno) { this.turno = turno; }
}
