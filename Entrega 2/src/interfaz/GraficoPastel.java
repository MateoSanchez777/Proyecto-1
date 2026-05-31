package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.util.Map;

import javax.swing.JComponent;

public class GraficoPastel extends JComponent {
    
    private Map<String, Integer> datos;
    private String titulo;
    private Color colorPrestamo = new Color(255, 80, 80);
    private Color colorInventario = new Color(70, 130, 240);

    public GraficoPastel() {
        this.titulo = "Distribución entre copias para vender y copias para prestar\nde un juego en particular.";
    }

    public void setDatos(Map<String, Integer> datos) {
        this.datos = datos;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (datos == null || datos.isEmpty()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();
        
        // Dibujar título
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2d.setColor(Color.BLACK);
        String[] lineas = titulo.split("\n");
        int yTexto = 30;
        for (String linea : lineas) {
            int anchoTexto = g2d.getFontMetrics().stringWidth(linea);
            g2d.drawString(linea, (ancho - anchoTexto) / 2, yTexto);
            yTexto += 25;
        }

        // Calcular datos
        double total = datos.getOrDefault("Prestamo", 0) + datos.getOrDefault("Inventario", 0);
        if (total == 0) return;

        double anguloPrestamo = (datos.getOrDefault("Prestamo", 0) / total) * 360.0;
        double anguloInventario = 360.0 - anguloPrestamo;

        // Tamaño del gráfico
        int diametro = Math.min(ancho, alto) - 150;
        int x = (ancho - diametro) / 2;
        int y = yTexto + 20;

        // Dibujar tajadas
        // Inventario (Azul) empieza desde 90 grados
        g2d.setColor(colorInventario);
        g2d.fill(new Arc2D.Double(x, y, diametro, diametro, 90, anguloInventario, Arc2D.PIE));
        
        // Prestamo (Rojo)
        g2d.setColor(colorPrestamo);
        g2d.fill(new Arc2D.Double(x, y, diametro, diametro, 90 + anguloInventario, anguloPrestamo, Arc2D.PIE));

        // Dibujar labels (tooltips style)
        dibujarLabel(g2d, "Copias Inventario", colorInventario, x, y, diametro, 90 + anguloInventario / 2);
        dibujarLabel(g2d, "Copias Préstamo", colorPrestamo, x, y, diametro, 90 + anguloInventario + anguloPrestamo / 2);

        // Leyenda inferior
        int yLeyenda = y + diametro + 40;
        int xLeyenda = ancho / 2 - 120;
        
        g2d.setColor(colorPrestamo);
        g2d.fillOval(xLeyenda, yLeyenda - 12, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Copias Préstamo", xLeyenda + 25, yLeyenda);
        
        g2d.setColor(colorInventario);
        g2d.fillOval(xLeyenda + 150, yLeyenda - 12, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Copias Inventario", xLeyenda + 175, yLeyenda);
    }
    
    private void dibujarLabel(Graphics2D g2d, String texto, Color colorSector, int x, int y, int diametro, double anguloGrd) {
        double anguloRad = Math.toRadians(anguloGrd);
        int radio = diametro / 2;
        int cx = x + radio;
        int cy = y + radio;
        
        // Punto en el borde del pastel
        int px = (int) (cx + (radio * 0.9) * Math.cos(anguloRad));
        int py = (int) (cy - (radio * 0.9) * Math.sin(anguloRad));
        
        // Línea hacia afuera
        int lx = (int) (cx + (radio + 30) * Math.cos(anguloRad));
        int ly = (int) (cy - (radio + 30) * Math.sin(anguloRad));
        
        g2d.setColor(Color.BLACK);
        g2d.drawLine(px, py, lx, ly);
        
        // Dibujar caja amarilla
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        int anchoTexto = g2d.getFontMetrics().stringWidth(texto) + 10;
        int altoCaja = 30; // 2 lineas
        
        int boxX = lx;
        if (Math.cos(anguloRad) < 0) { // Si está a la izquierda, la caja va hacia la izquierda
            boxX = lx - anchoTexto;
            g2d.drawLine(lx, ly, boxX, ly); // Línea horizontal
        } else {
            g2d.drawLine(lx, ly, boxX + anchoTexto, ly); // Línea horizontal
        }
        
        int boxY = ly - altoCaja / 2;
        
        g2d.setColor(new Color(255, 255, 200)); // Amarillo claro
        g2d.fillRect(boxX, boxY, anchoTexto, altoCaja);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(boxX, boxY, anchoTexto, altoCaja);
        
        String[] partes = texto.split(" ");
        g2d.drawString(partes[0], boxX + 5, boxY + 12);
        if (partes.length > 1) {
            g2d.drawString(partes[1], boxX + 5, boxY + 25);
        }
    }
}
