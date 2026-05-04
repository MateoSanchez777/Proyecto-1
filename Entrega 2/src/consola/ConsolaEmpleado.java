package consola;

import java.util.Scanner;
import logica.BoardGameCafe;
import modelo.usuarios.Empleado;
import modelo.torneos.Torneo;

public class ConsolaEmpleado {
    private BoardGameCafe cafe;
    private Scanner scanner;
    private Empleado empleado;

    public ConsolaEmpleado() {
        cafe = new BoardGameCafe();
        scanner = new Scanner(System.in);
        try {
            cafe.cargarDatos();
            cafe.cargarTorneosJSON();
        } catch (Exception e) {
            System.out.println("Error cargando datos: " + e.getMessage());
        }
    }

    public void iniciar() {
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        if (cafe.getUsuarios().containsKey(login) && cafe.getUsuarios().get(login).getPassword().equals(pass)) {
            if (cafe.getUsuarios().get(login) instanceof Empleado) {
                empleado = (Empleado) cafe.getUsuarios().get(login);
                menu();
            } else {
                System.out.println("El usuario no es un empleado.");
            }
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    private void menu() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENU EMPLEADO ---");
            System.out.println("1. Ver Torneos");
            System.out.println("2. Inscribirse a Torneo");
            System.out.println("3. Salir");
            System.out.print("Opcion: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    verTorneos();
                    break;
                case "2":
                    inscribirseTorneo();
                    break;
                case "3":
                    salir = true;
                    cafe.guardarDatos();
                    cafe.guardarTorneosJSON();
                    System.out.println("Datos guardados. Saliendo...");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private void verTorneos() {
        for (Torneo t : cafe.getTorneos()) {
            System.out.println("- " + t.getNombre() + " (Dia: " + t.getDiaSemana() + ")");
        }
    }

    private void inscribirseTorneo() {
        System.out.print("Nombre del torneo al que desea inscribirse: ");
        String nombreT = scanner.nextLine();
        Torneo torneo = null;
        for (Torneo t : cafe.getTorneos()) {
            if (t.getNombre().equalsIgnoreCase(nombreT)) {
                torneo = t; break;
            }
        }
        if (torneo == null) {
            System.out.println("Torneo no encontrado.");
            return;
        }
        
        System.out.print("Cantidad de cupos a tomar (max 3): ");
        try {
            int cupos = Integer.parseInt(scanner.nextLine());
            cafe.inscribirEnTorneo(empleado, torneo, cupos);
            System.out.println("Inscrito exitosamente!");
        } catch (Exception e) {
            System.out.println("Error en inscripcion: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ConsolaEmpleado consola = new ConsolaEmpleado();
        consola.iniciar();
    }
}
