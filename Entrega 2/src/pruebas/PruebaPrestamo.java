package pruebas;

import java.util.ArrayList;
import java.util.List;

import logica.BoardGameCafe;
import modelo.Mesa;
import modelo.juegos.Copia;
import modelo.juegos.Juego;
import modelo.usuarios.Cliente;
import modelo.usuarios.Mesero;

public class PruebaPrestamo {
    public static void main(String[] args) {
        System.out.println("--- PRUEBA DE PRESTAMOS ---");
        BoardGameCafe cafe = new BoardGameCafe();
        
        Cliente usr1 = new Cliente("andres", "123", 0);
        Juego j1 = new Juego("Catan", "Devir", 1995, 3, 4, 10, "Tablero", false, 150000);
        Juego jDificil = new Juego("Twilight Imperium", "FFG", 2017, 3, 6, 14, "Tablero", true, 300000);
        Juego jAdulto = new Juego("Cards A.H.", "CAH", 2011, 4, 10, 18, "Cartas", false, 50000);
        
        Copia c1 = new Copia("C1", j1, "Nuevo", "Prestamo", true);
        Copia c2 = new Copia("C2", jDificil, "Bueno", "Prestamo", true);
        Copia c3 = new Copia("C3", jAdulto, "Bueno", "Prestamo", true);
        
        cafe.registrarUsuario(usr1);
        cafe.agregarCopia(c1);
        cafe.agregarCopia(c2);
        cafe.agregarCopia(c3);
        
        Mesero m1 = new Mesero("mesero1", "abc", "M123");
        
        Mesa mesa1 = new Mesa(1, 4, false, false); // Mesa normal 4 adultos
        Mesa mesaMenores = new Mesa(2, 4, false, true); // Mesa con menores de 18
        
        System.out.println("1. Intento prestamo normal:");
        List<Copia> pedir1 = new ArrayList<>();
        pedir1.add(c1);
        try {
            cafe.realizarPrestamo(usr1, mesa1, pedir1, null);
            System.out.println("OK: Prestamo de Catan exitoso.");
        } catch (Exception e) { System.out.println(e.getMessage()); }
        
        System.out.println("\n2. Intento prestamo bloqueado por edad:");
        List<Copia> pedirAdulto = new ArrayList<>();
        pedirAdulto.add(c3);
        try {
            cafe.realizarPrestamo(usr1, mesaMenores, pedirAdulto, null);
        } catch (Exception e) { System.out.println(e.getMessage()); }
        
        System.out.println("\n3. Intento prestamo bloqueado (copia no disponible porque ya se presto):");
        try {
            cafe.realizarPrestamo(usr1, mesa1, pedir1, null); // pide la c1
        } catch (Exception e) { System.out.println(e.getMessage()); }
        
        System.out.println("\n4. Prestamo juego dificil sin mesero capacitado:");
        List<Copia> pedirDificil = new ArrayList<>();
        pedirDificil.add(c2);
        try {
            cafe.realizarPrestamo(usr1, mesa1, pedirDificil, m1);
            System.out.println("OK: Prestamo concedido pero el sistema emite advertencia.");
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }
}
