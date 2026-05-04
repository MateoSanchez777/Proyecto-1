package modelo.torneos;

import modelo.usuarios.UsuarioComprador;
import modelo.usuarios.Empleado;

public class Inscripcion {
    private UsuarioComprador usuario;
    private int cantidadCupos;
    private boolean esFanatico;

    public Inscripcion(UsuarioComprador usuario, int cantidadCupos, boolean esFanatico) {
        this.usuario = usuario;
        this.cantidadCupos = cantidadCupos;
        this.esFanatico = esFanatico;
    }

    public UsuarioComprador getUsuario() { return usuario; }
    public int getCantidadCupos() { return cantidadCupos; }
    public boolean isFanatico() { return esFanatico; }
    
    public boolean isEmpleado() {
        return usuario instanceof Empleado;
    }
}
