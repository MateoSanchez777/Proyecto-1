package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logica.BoardGameCafe;

public class PanelVentas extends JPanel {

    private BoardGameCafe cafe;
    private GraficoBarras grafico;
    private JComboBox<String> combo1, combo2, combo3, combo4, combo5;

    public PanelVentas(BoardGameCafe cafe) {
        this.cafe = cafe;
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título
        JLabel lblTitulo = new JLabel("Ventas por período");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(20, 30, 50));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel central
        JPanel pnlCentro = new JPanel(new BorderLayout(0, 20));
        pnlCentro.setOpaque(false);

        // Selectores de fechas
        JPanel pnlSelectorContenedor = new JPanel(new BorderLayout(0, 5));
        pnlSelectorContenedor.setOpaque(false);
        
        JLabel lblSelec = new JLabel("Seleccione el rango de fechas (5 días):");
        lblSelec.setFont(new Font("SansSerif", Font.PLAIN, 16));
        pnlSelectorContenedor.add(lblSelec, BorderLayout.NORTH);
        
        JPanel pnlSelectorFechas = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlSelectorFechas.setOpaque(false);
        
        // Simular comboboxes de fechas
        String[] opcionesFechas = {"11/05/2026", "12/05/2026", "13/05/2026", "14/05/2026", "15/05/2026"};
        combo1 = new JComboBox<>(opcionesFechas); combo1.setSelectedIndex(0);
        combo2 = new JComboBox<>(opcionesFechas); combo2.setSelectedIndex(1);
        combo3 = new JComboBox<>(opcionesFechas); combo3.setSelectedIndex(2);
        combo4 = new JComboBox<>(opcionesFechas); combo4.setSelectedIndex(3);
        combo5 = new JComboBox<>(opcionesFechas); combo5.setSelectedIndex(4);
        
        pnlSelectorFechas.add(combo1);
        pnlSelectorFechas.add(new JLabel(" - "));
        pnlSelectorFechas.add(combo2);
        pnlSelectorFechas.add(new JLabel(" - "));
        pnlSelectorFechas.add(combo3);
        pnlSelectorFechas.add(new JLabel(" - "));
        pnlSelectorFechas.add(combo4);
        pnlSelectorFechas.add(new JLabel(" - "));
        pnlSelectorFechas.add(combo5);
        
        pnlSelectorContenedor.add(pnlSelectorFechas, BorderLayout.CENTER);
        
        pnlCentro.add(pnlSelectorContenedor, BorderLayout.NORTH);

        // Gráfico
        JPanel pnlGraficoContenedor = new JPanel(new BorderLayout());
        pnlGraficoContenedor.setBackground(Color.WHITE);
        pnlGraficoContenedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        grafico = new GraficoBarras();
        pnlGraficoContenedor.add(grafico, BorderLayout.CENTER);
        
        pnlCentro.add(pnlGraficoContenedor, BorderLayout.CENTER);
        
        add(pnlCentro, BorderLayout.CENTER);

        // Actualizar datos
        ActionListener updater = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarGrafico();
            }
        };
        combo1.addActionListener(updater);
        combo2.addActionListener(updater);
        combo3.addActionListener(updater);
        combo4.addActionListener(updater);
        combo5.addActionListener(updater);
        
        actualizarGrafico();
    }
    
    private void actualizarGrafico() {
        Map<String, Map<String, Double>> datos = new LinkedHashMap<>();
        
        String[] fechas = {
            (String)combo1.getSelectedItem(),
            (String)combo2.getSelectedItem(),
            (String)combo3.getSelectedItem(),
            (String)combo4.getSelectedItem(),
            (String)combo5.getSelectedItem()
        };
        
        for (String f : fechas) {
            datos.put(f, cafe.getVentasPorFechaYTipo(f));
        }
        
        grafico.setDatos(datos);
    }
}
