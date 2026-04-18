package modelo.turnos;

import modelo.usuarios.Empleado;

public class SolicitudCambioTurno {
    private Empleado solicitante;
    private Empleado reemplazo; // Puede ser null si es solicitud general
    private String fechaAsignada;
    private boolean aprobada;

    public SolicitudCambioTurno(Empleado solicitante, Empleado reemplazo, String fechaAsignada) {
        this.solicitante = solicitante;
        this.reemplazo = reemplazo;
        this.fechaAsignada = fechaAsignada;
        this.aprobada = false;
    }

    public Empleado getSolicitante() { return solicitante; }
    public Empleado getReemplazo() { return reemplazo; }
    public String getFechaAsignada() { return fechaAsignada; }
    public boolean isAprobada() { return aprobada; }

    public void aprobar() { this.aprobada = true; }
}
