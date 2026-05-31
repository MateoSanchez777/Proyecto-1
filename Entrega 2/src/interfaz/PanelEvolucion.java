package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logica.BoardGameCafe;

public class PanelEvolucion extends JPanel {

    private BoardGameCafe cafe;
    private GraficoLineas grafico;
    private JComboBox<String> comboSemanas;

    public PanelEvolucion(BoardGameCafe cafe) {
        this.cafe = cafe;
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título
        JLabel lblTitulo = new JLabel("Evolución");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(20, 30, 50));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel central
        JPanel pnlCentro = new JPanel(new BorderLayout(0, 20));
        pnlCentro.setOpaque(false);

        // Selector
        JPanel pnlSelector = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSelector.setOpaque(false);
        JLabel lblSelec = new JLabel("Seleccionar semana: ");
        lblSelec.setFont(new Font("SansSerif", Font.PLAIN, 16));
        
        String[] semanas = {
            "11/05/2026 - 17/05/2026",
            "18/05/2026 - 24/05/2026"
        };
        comboSemanas = new JComboBox<>(semanas);
        comboSemanas.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        pnlSelector.add(lblSelec);
        pnlSelector.add(comboSemanas);
        pnlCentro.add(pnlSelector, BorderLayout.NORTH);

        // Gráfico
        JPanel pnlGraficoContenedor = new JPanel(new BorderLayout());
        pnlGraficoContenedor.setBackground(Color.WHITE);
        pnlGraficoContenedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        grafico = new GraficoLineas();
        pnlGraficoContenedor.add(grafico, BorderLayout.CENTER);
        
        pnlCentro.add(pnlGraficoContenedor, BorderLayout.CENTER);
        
        add(pnlCentro, BorderLayout.CENTER);

        // Eventos
        comboSemanas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarGrafico();
            }
        });
        
        actualizarGrafico();
    }
    
    private void actualizarGrafico() {
        String seleccionado = (String) comboSemanas.getSelectedItem();
        // Extraer fecha inicio y fin del string "dd/MM/yyyy - dd/MM/yyyy"
        String[] partes = seleccionado.split(" - ");
        String inicio = partes[0].trim();
        String fin    = partes[1].trim();
        String titulo = "Reservas " + seleccionado;
        if (seleccionado.startsWith("11/05")) titulo = "Reservas Semana 15";
        if (seleccionado.startsWith("18/05")) titulo = "Reservas Semana 16";
        Map<String, Integer> datos = cafe.getReservasPorSemana(inicio, fin);
        grafico.setDatos(datos, titulo);
    }
}
