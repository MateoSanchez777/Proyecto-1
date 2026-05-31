package interfaz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;

public class GraficoBarras extends JComponent {

    private Map<String, Map<String, Double>> datos; // fecha -> (tipo -> valor)
    private String titulo;
    private Color colorCafeteria = new Color(255, 80, 80);
    private Color colorJuegos = new Color(70, 130, 240);
    private DecimalFormat df = new DecimalFormat("#,###");

    public GraficoBarras() {
        this.titulo = "Ventas discriminadas entre cafetería y juegos en un rango de 5 días.\n(Valores descontados impuestos)";
        this.datos = new LinkedHashMap<>();
    }

    public void setDatos(Map<String, Map<String, Double>> datos) {
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

        // Márgenes del área de dibujo
        int margenIzq = 80;
        int margenDer = 40;
        int margenSup = yTexto + 20;
        int margenInf = alto - 80;
        
        int anchoArea = ancho - margenIzq - margenDer;
        int altoArea = margenInf - margenSup;

        // Encontrar valor máximo
        double maxValor = 0;
        for (Map<String, Double> porTipo : datos.values()) {
            if (porTipo.getOrDefault("Cafeteria", 0.0) > maxValor) maxValor = porTipo.getOrDefault("Cafeteria", 0.0);
            if (porTipo.getOrDefault("Juegos", 0.0) > maxValor) maxValor = porTipo.getOrDefault("Juegos", 0.0);
        }
        
        // Redondear máximo (ej. si es 80000, dejar en 80000 o 90000)
        maxValor = Math.max(maxValor, 80000); // Forzar mínimo de 80k para la captura
        maxValor = Math.ceil(maxValor / 10000) * 10000;

        // Fondo gris para el área de gráfico
        g2d.setColor(new Color(230, 230, 230));
        g2d.fillRect(margenIzq, margenSup, anchoArea, altoArea);

        // Líneas horizontales (Grid)
        g2d.setColor(new Color(200, 200, 200));
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f));
        
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        int pasos = 8;
        for (int i = 0; i <= pasos; i++) {
            int y = margenInf - (i * altoArea / pasos);
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawLine(margenIzq, y, ancho - margenDer, y);
            
            // Etiqueta Eje Y
            double val = (maxValor / pasos) * i;
            String lbl = df.format(val).replace(',', '.');
            g2d.setColor(Color.DARK_GRAY);
            int lblWidth = g2d.getFontMetrics().stringWidth(lbl);
            g2d.drawString(lbl, margenIzq - lblWidth - 10, y + 5);
        }

        // Título Eje Y
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
        Graphics2D g2dRot = (Graphics2D) g2d.create();
        g2dRot.rotate(-Math.PI / 2);
        g2dRot.drawString("Valor en Pesos", -margenSup - altoArea/2 - 50, margenIzq - 60);
        g2dRot.dispose();

        // Borde izquierdo e inferior del área
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.setColor(Color.GRAY);
        g2d.drawLine(margenIzq, margenSup, margenIzq, margenInf);
        g2d.drawLine(margenIzq, margenInf, ancho - margenDer, margenInf);

        // Dibujar barras
        int numFechas = datos.size();
        int anchoEspacio = anchoArea / numFechas;
        int anchoBarra = Math.min(30, (anchoEspacio - 20) / 2);
        
        int i = 0;
        for (String fecha : datos.keySet()) {
            int xCentro = margenIzq + (i * anchoEspacio) + (anchoEspacio / 2);
            
            Map<String, Double> valores = datos.get(fecha);
            double valCafe = valores.getOrDefault("Cafeteria", 0.0);
            double valJuegos = valores.getOrDefault("Juegos", 0.0);
            
            int altoCafe = (int) ((valCafe / maxValor) * altoArea);
            int altoJuegos = (int) ((valJuegos / maxValor) * altoArea);
            
            // Barra Cafeteria (Roja) - Izquierda
            g2d.setColor(colorCafeteria);
            g2d.fillRect(xCentro - anchoBarra - 5, margenInf - altoCafe, anchoBarra, altoCafe);
            
            // Barra Juegos (Azul) - Derecha
            g2d.setColor(colorJuegos);
            g2d.fillRect(xCentro + 5, margenInf - altoJuegos, anchoBarra, altoJuegos);
            
            // Etiqueta Eje X (solo día/mes/año corto)
            String lblX = fecha.substring(0, 8); // dd/MM/yy
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
            int w = g2d.getFontMetrics().stringWidth(lblX);
            g2d.drawString(lblX, xCentro - w/2, margenInf + 20);
            
            i++;
        }

        // Título Eje X
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2d.drawString("Categorías", margenIzq + anchoArea/2 - 40, margenInf + 45);

        // Leyenda inferior
        int yLeyenda = margenInf + 70;
        int xLeyenda = margenIzq + anchoArea / 2 - 80;
        
        g2d.setColor(colorCafeteria);
        g2d.fillRect(xLeyenda, yLeyenda - 12, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2d.drawString("Cafetería", xLeyenda + 20, yLeyenda);
        
        g2d.setColor(colorJuegos);
        g2d.fillRect(xLeyenda + 100, yLeyenda - 12, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Juegos", xLeyenda + 120, yLeyenda);
    }
}
