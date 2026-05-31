package interfaz;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import logica.BoardGameCafe;

public class InterfazAdministrador extends JFrame {
    
    private BoardGameCafe cafe;
    private JPanel panelCentral;
    private CardLayout cardLayout;
    
    private JPanel panelBotones;
    private JButton btnInicio;
    private JButton btnDistribucion;
    private JButton btnVentas;
    private JButton btnEvolucion;
    
    private PanelInicio pnlInicio;
    private PanelDistribucion pnlDistribucion;
    private PanelVentas pnlVentas;
    private PanelEvolucion pnlEvolucion;

    public InterfazAdministrador() {
        setTitle("Sistema Administrativo");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Inicializar lógica
        cafe = new BoardGameCafe();
        try {
            cafe.cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setLayout(new BorderLayout());
        
        // Crear paneles
        pnlInicio = new PanelInicio();
        pnlDistribucion = new PanelDistribucion(cafe);
        pnlVentas = new PanelVentas(cafe);
        pnlEvolucion = new PanelEvolucion(cafe);
        
        // Configurar panel central con CardLayout
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);
        panelCentral.add(pnlInicio, "Inicio");
        panelCentral.add(pnlDistribucion, "Distribucion");
        panelCentral.add(pnlVentas, "Ventas");
        panelCentral.add(pnlEvolucion, "Evolucion");
        
        // Configurar menú lateral
        configurarMenuLateral();
        
        add(panelBotones, BorderLayout.WEST);
        add(panelCentral, BorderLayout.CENTER);
        
        // Estilo general
        getContentPane().setBackground(new Color(245, 247, 250));
    }
    
    private void configurarMenuLateral() {
        panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(6, 1, 0, 20));
        panelBotones.setPreferredSize(new Dimension(200, 0));
        panelBotones.setBackground(new Color(240, 242, 245));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        btnInicio = crearBotonMenu("Inicio");
        btnDistribucion = crearBotonMenu("Distribución");
        btnVentas = crearBotonMenu("Ventas");
        btnEvolucion = crearBotonMenu("Evolución");
        
        // Estado inicial
        seleccionarBoton(btnInicio);
        
        // Acciones
        btnInicio.addActionListener(e -> {
            cardLayout.show(panelCentral, "Inicio");
            seleccionarBoton(btnInicio);
        });
        
        btnDistribucion.addActionListener(e -> {
            cardLayout.show(panelCentral, "Distribucion");
            seleccionarBoton(btnDistribucion);
        });
        
        btnVentas.addActionListener(e -> {
            cardLayout.show(panelCentral, "Ventas");
            seleccionarBoton(btnVentas);
        });
        
        btnEvolucion.addActionListener(e -> {
            cardLayout.show(panelCentral, "Evolucion");
            seleccionarBoton(btnEvolucion);
        });
        
        panelBotones.add(btnInicio);
        panelBotones.add(btnDistribucion);
        panelBotones.add(btnVentas);
        panelBotones.add(btnEvolucion);
    }
    
    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return btn;
    }
    
    private void seleccionarBoton(JButton seleccionado) {
        JButton[] botones = {btnInicio, btnDistribucion, btnVentas, btnEvolucion};
        for (JButton btn : botones) {
            if (btn == seleccionado) {
                btn.setBackground(new Color(220, 230, 255));
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(150, 180, 255)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            } else {
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            InterfazAdministrador ventana = new InterfazAdministrador();
            ventana.setVisible(true);
        });
    }
}
