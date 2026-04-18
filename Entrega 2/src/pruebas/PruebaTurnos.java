package pruebas;

import logica.BoardGameCafe;
import modelo.turnos.SolicitudCambioTurno;
import modelo.turnos.Turno;
import modelo.usuarios.Administrador;
import modelo.usuarios.Mesero;
import modelo.usuarios.Cocinero;

public class PruebaTurnos {
    public static void main(String[] args) {
        System.out.println("--- PRUEBA GESTION DE TURNOS ---");
        BoardGameCafe cafe = new BoardGameCafe();
        Administrador admin = new Administrador("admin", "123");
        
        Mesero m1 = new Mesero("m1", "1", "A");
        Mesero m2 = new Mesero("m2", "1", "B"); // reemplazo
        Cocinero c1 = new Cocinero("c1", "1", "C"); // para no violar minima, aunque aca esta de muestra
        
        Turno tManana = new Turno("Lunes", "Mañana");
        Turno tNoche = new Turno("Lunes", "Noche");
        
        m1.setTurno(tManana);
        m2.setTurno(tNoche);
        
        System.out.println("Turno Original m1: " + m1.getTurno().getHorario());
        System.out.println("Turno Original m2: " + m2.getTurno().getHorario());
        
        SolicitudCambioTurno solicitud = new SolicitudCambioTurno(m1, m2, "12/05");
        try {
            cafe.cambiarTurno(solicitud, admin);
            System.out.println("\nOK: Solicitud Aprobada.");
            System.out.println("Nuevo Turno m1: " + m1.getTurno().getHorario());
            System.out.println("Nuevo Turno m2: " + m2.getTurno().getHorario());
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }
}
