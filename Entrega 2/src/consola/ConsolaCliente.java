package consola;

import java.util.Scanner;
import logica.BoardGameCafe;
import modelo.usuarios.Cliente;
import modelo.juegos.Juego;
import modelo.torneos.Torneo;

public class ConsolaCliente {
    private BoardGameCafe cafe;
    private Scanner scanner;
    private Cliente cliente;

    public ConsolaCliente() {
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
        System.out.println("1. Iniciar Sesion");
        System.out.println("2. Registrarse");
        System.out.print("Opcion: ");
        String op = scanner.nextLine();
        
        if (op.equals("2")) {
            System.out.print("Nuevo Login: ");
            String login = scanner.nextLine();
            System.out.print("Nuevo Password: ");
            String pass = scanner.nextLine();
            Cliente nuevo = new Cliente(login, pass, 0);
            cafe.registrarUsuario(nuevo);
            cliente = nuevo;
            System.out.println("Registro exitoso.");
            menu();
        } else {
            System.out.print("Login: ");
            String login = scanner.nextLine();
            System.out.print("Password: ");
            String pass = scanner.nextLine();

            if (cafe.getUsuarios().containsKey(login) && cafe.getUsuarios().get(login).getPassword().equals(pass)) {
                if (cafe.getUsuarios().get(login) instanceof Cliente) {
                    cliente = (Cliente) cafe.getUsuarios().get(login);
                    menu();
                } else {
                    System.out.println("El usuario no es un cliente.");
                }
            } else {
                System.out.println("Credenciales incorrectas.");
            }
        }
    }

    private void menu() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENU CLIENTE ---");
            System.out.println("1. Ver Torneos");
            System.out.println("2. Inscribirse a Torneo");
            System.out.println("3. Desinscribirse de Torneo");
            System.out.println("4. Marcar Juego como Favorito");
            System.out.println("5. Salir");
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
                    desinscribirseTorneo();
                    break;
                case "4":
                    marcarFavorito();
                    break;
                case "5":
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
            System.out.println("- " + t.getNombre() + " (Juego: " + t.getJuego().getNombre() + ", Dia: " + t.getDiaSemana() + ")");
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
            cafe.inscribirEnTorneo(cliente, torneo, cupos);
            System.out.println("Inscrito exitosamente!");
        } catch (Exception e) {
            System.out.println("Error en inscripcion: " + e.getMessage());
        }
    }
    
    private void desinscribirseTorneo() {
        System.out.print("Nombre del torneo del que desea desinscribirse: ");
        String nombreT = scanner.nextLine();
        for (Torneo t : cafe.getTorneos()) {
            if (t.getNombre().equalsIgnoreCase(nombreT)) {
                cafe.desinscribirDeTorneo(cliente, t);
                System.out.println("Desinscripcion realizada.");
                return;
            }
        }
        System.out.println("Torneo no encontrado.");
    }

    private void marcarFavorito() {
        System.out.print("Nombre del juego favorito: ");
        String nombreJuego = scanner.nextLine();
        Juego juego = cafe.getJuegos().get(nombreJuego);
        if (juego != null) {
            cliente.agregarJuegoFavorito(juego);
            System.out.println("Juego agreado a favoritos.");
        } else {
            System.out.println("Juego no encontrado.");
        }
    }

    public static void main(String[] args) {
        ConsolaCliente consola = new ConsolaCliente();
        consola.iniciar();
    }
}
