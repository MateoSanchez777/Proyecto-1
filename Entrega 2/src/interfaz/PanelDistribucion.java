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
import modelo.juegos.Juego;

public class PanelDistribucion extends JPanel {

    private BoardGameCafe cafe;
    private JComboBox<String> comboJuegos;
    private GraficoPastel grafico;

    public PanelDistribucion(BoardGameCafe cafe) {
        this.cafe = cafe;
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título
        JLabel lblTitulo = new JLabel("Distribución de Copias");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(20, 30, 50));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel central
        JPanel pnlCentro = new JPanel(new BorderLayout(0, 20));
        pnlCentro.setOpaque(false);

        // Selector
        JPanel pnlSelector = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSelector.setOpaque(false);
        JLabel lblSelec = new JLabel("Seleccione el juego: ");
        lblSelec.setFont(new Font("SansSerif", Font.PLAIN, 16));
        
        comboJuegos = new JComboBox<>();
        comboJuegos.addItem("Seleccione un juego...");
        for (String nombre : cafe.getJuegos().keySet()) {
            comboJuegos.addItem(nombre);
        }
        comboJuegos.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        pnlSelector.add(lblSelec);
        pnlSelector.add(comboJuegos);
        pnlCentro.add(pnlSelector, BorderLayout.NORTH);

        // Gráfico
        JPanel pnlGraficoContenedor = new JPanel(new BorderLayout());
        pnlGraficoContenedor.setBackground(Color.WHITE);
        pnlGraficoContenedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        grafico = new GraficoPastel();
        pnlGraficoContenedor.add(grafico, BorderLayout.CENTER);
        
        pnlCentro.add(pnlGraficoContenedor, BorderLayout.CENTER);
        
        add(pnlCentro, BorderLayout.CENTER);

        // Eventos
        comboJuegos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccionado = (String) comboJuegos.getSelectedItem();
                if (seleccionado != null && !seleccionado.startsWith("Seleccione")) {
                    Map<String, Integer> datos = cafe.getDistribucionCopias(seleccionado);
                    grafico.setDatos(datos);
                } else {
                    grafico.setDatos(null);
                }
            }
        });
        
        // Cargar dato inicial si hay juegos
        if (comboJuegos.getItemCount() > 1) {
            comboJuegos.setSelectedIndex(1);
        }
    }
}
