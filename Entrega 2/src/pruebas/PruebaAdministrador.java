package pruebas;

import logica.BoardGameCafe;
import modelo.juegos.Copia;
import modelo.juegos.Juego;

public class PruebaAdministrador {
    public static void main(String[] args) {
        System.out.println("--- PRUEBA ADMINISTRADOR ---");
        BoardGameCafe cafe = new BoardGameCafe();
        
        Juego j1 = new Juego("Monopoly", "Hasbro", 1935, 2, 6, 8, "Tablero", false, 80000);
        Copia c1 = new Copia("M1", j1, "Nuevo", "Venta", true);
        
        cafe.agregarJuego(j1);
        cafe.agregarCopia(c1);
        
        System.out.println("1. Movimiento del inventario (Venta -> Prestamo)");
        System.out.println("Inventario Inicial: " + c1.getInventario());
        c1.setInventario("Prestamo");
        System.out.println("Inventario Actualizado: " + c1.getInventario());
        
        System.out.println("\n2. Marcar copia como robada/desaparecida");
        c1.setEstado("Desaparecido");
        c1.setDisponible(false);
        System.out.println("Estado de C1: " + c1.getEstado() + " | Disponible: " + c1.isDisponible());
        
        System.out.println("\n3. Guardar informacion de toda la plataforma a CSV (.txt plain text)");
        cafe.guardarDatos();
        System.out.println("OK: Datos guardados al directorio /datos/");
        
        System.out.println("\n4. Generación de informe financiero:");
        System.out.println("Total Ventas generadas: " + cafe.getVentas().size());
        // En una ejecución real aqui se iterarían las ventas llamando a getVentas() para segregar ingresos por rubro.
        System.out.println("Fin del informe financiero básico.");
    }
}
