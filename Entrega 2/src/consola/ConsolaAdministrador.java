package consola;

import java.util.Scanner;
import logica.BoardGameCafe;
import modelo.usuarios.Administrador;
import modelo.usuarios.Empleado;
import modelo.usuarios.Cocinero;
import modelo.usuarios.Mesero;
import modelo.juegos.Juego;
import modelo.torneos.Torneo;
import modelo.torneos.TorneoAmistoso;
import modelo.torneos.TorneoCompetitivo;

public class ConsolaAdministrador {
    private BoardGameCafe cafe;
    private Scanner scanner;
    private Administrador admin;

    public ConsolaAdministrador() {
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
            if (cafe.getUsuarios().get(login) instanceof Administrador) {
                admin = (Administrador) cafe.getUsuarios().get(login);
                menu();
            } else {
                System.out.println("Usuario no es administrador.");
            }
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    private void menu() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENU ADMINISTRADOR ---");
            System.out.println("1. Registrar Empleado");
            System.out.println("2. Crear Torneo");
            System.out.println("3. Salir");
            System.out.print("Opcion: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    registrarEmpleado();
                    break;
                case "2":
                    crearTorneo();
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

    private void registrarEmpleado() {
        System.out.print("Login nuevo empleado: ");
        String login = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();
        System.out.print("Tipo (Mesero/Cocinero): ");
        String tipo = scanner.nextLine();
        
        Empleado emp = null;
        if (tipo.equalsIgnoreCase("Mesero")) {
            emp = new Mesero(login, pass, login + "desc");
        } else if (tipo.equalsIgnoreCase("Cocinero")) {
            emp = new Cocinero(login, pass, login + "desc");
        }
        
        if (emp != null) {
            cafe.registrarUsuario(emp);
            System.out.println("Empleado registrado.");
        } else {
            System.out.println("Tipo no valido.");
        }
    }

    private void crearTorneo() {
        System.out.print("Nombre del torneo: ");
        String nombre = scanner.nextLine();
        System.out.print("Nombre del juego: ");
        String nombreJuego = scanner.nextLine();
        
        Juego juego = cafe.getJuegos().get(nombreJuego);
        if (juego == null) {
            System.out.println("Juego no encontrado en el sistema.");
            return;
        }
        
        System.out.print("Dia de la semana: ");
        String dia = scanner.nextLine();
        System.out.print("Max participantes: ");
        int max = Integer.parseInt(scanner.nextLine());
        System.out.print("Tipo (Amistoso/Competitivo): ");
        String tipo = scanner.nextLine();
        
        Torneo torneo = null;
        if (tipo.equalsIgnoreCase("Amistoso")) {
            torneo = new TorneoAmistoso(nombre, juego, dia, max);
        } else if (tipo.equalsIgnoreCase("Competitivo")) {
            System.out.print("Tarifa de entrada: ");
            double tarifa = Double.parseDouble(scanner.nextLine());
            torneo = new TorneoCompetitivo(nombre, juego, dia, max, tarifa);
        }
        
        if (torneo != null) {
            cafe.crearTorneo(torneo);
            System.out.println("Torneo creado exitosamente.");
        } else {
            System.out.println("Tipo de torneo invalido.");
        }
    }

    public static void main(String[] args) {
        ConsolaAdministrador consola = new ConsolaAdministrador();
        consola.iniciar();
    }
}
