package interfaz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;

public class GraficoLineas extends JComponent {

    private Map<String, Integer> datos;
    private String titulo;
    private Color colorLinea = new Color(255, 30, 30);
    private Color colorFondo = new Color(230, 230, 230);

    public GraficoLineas() {
        this.titulo = "Reservas Semana 15";
        this.datos = new LinkedHashMap<>();
    }

    public void setDatos(Map<String, Integer> datos, String tituloSemana) {
        this.datos = datos;
        if (tituloSemana != null && !tituloSemana.isEmpty()) {
            this.titulo = tituloSemana;
        }
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
        g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2d.setColor(Color.BLACK);
        int anchoTexto = g2d.getFontMetrics().stringWidth(titulo);
        g2d.drawString(titulo, (ancho - anchoTexto) / 2, 30);

        // Márgenes
        int margenIzq = 80;
        int margenDer = 40;
        int margenSup = 60;
        int margenInf = alto - 80;
        
        int anchoArea = ancho - margenIzq - margenDer;
        int altoArea = margenInf - margenSup;

        // Encontrar valor máximo
        int maxValor = 0;
        for (Integer v : datos.values()) {
            if (v > maxValor) maxValor = v;
        }
        
        // Redondear máximo a un número bonito
        maxValor = Math.max(maxValor, 45); // Para coincidir con la captura
        maxValor = (int) (Math.ceil(maxValor / 5.0) * 5);

        // Fondo gris
        g2d.setColor(colorFondo);
        g2d.fillRect(margenIzq, margenSup, anchoArea, altoArea);

        // Grid horizontal
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f));
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        int pasos = maxValor / 5;
        if (pasos == 0) pasos = 1;
        
        for (int i = 0; i <= pasos; i++) {
            int y = margenInf - (i * altoArea / pasos);
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawLine(margenIzq, y, ancho - margenDer, y);
            
            // Etiqueta Eje Y
            int val = (maxValor / pasos) * i;
            String lbl = String.valueOf(val);
            g2d.setColor(Color.DARK_GRAY);
            int lblWidth = g2d.getFontMetrics().stringWidth(lbl);
            g2d.drawString(lbl, margenIzq - lblWidth - 10, y + 5);
        }

        // Título Eje Y
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
        Graphics2D g2dRot = (Graphics2D) g2d.create();
        g2dRot.rotate(-Math.PI / 2);
        g2dRot.drawString("Número de reservas", -margenSup - altoArea/2 - 60, margenIzq - 50);
        g2dRot.dispose();

        // Borde
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.setColor(Color.GRAY);
        g2d.drawRect(margenIzq, margenSup, anchoArea, altoArea);

        // Dibujar línea
        int numPuntos = datos.size();
        int anchoEspacio = anchoArea / numPuntos;
        
        int[] xPoints = new int[numPuntos];
        int[] yPoints = new int[numPuntos];
        
        int i = 0;
        for (String dia : datos.keySet()) {
            int x = margenIzq + (i * anchoEspacio) + (anchoEspacio / 2);
            int v = datos.get(dia);
            int y = margenInf - (int) (((double) v / maxValor) * altoArea);
            
            xPoints[i] = x;
            yPoints[i] = y;
            
            // Etiqueta Eje X
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
            int w = g2d.getFontMetrics().stringWidth(dia);
            g2d.drawString(dia, x - w/2, margenInf + 20);
            
            i++;
        }
        
        // Dibujar la línea
        g2d.setColor(colorLinea);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawPolyline(xPoints, yPoints, numPuntos);

        // Título Eje X
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2d.drawString("Día de la semana", margenIzq + anchoArea/2 - 60, margenInf + 45);

        // Leyenda inferior
        int yLeyenda = margenInf + 70;
        int xLeyenda = margenIzq + anchoArea / 2 - 40;
        
        g2d.setColor(colorLinea);
        g2d.drawLine(xLeyenda, yLeyenda - 5, xLeyenda + 20, yLeyenda - 5);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2d.drawString("Reservas", xLeyenda + 30, yLeyenda);
    }
}
