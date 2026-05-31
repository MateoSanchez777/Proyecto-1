package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import logica.BoardGameCafe;
import modelo.torneos.Torneo;
import modelo.torneos.TorneoCompetitivo;
import modelo.usuarios.Empleado;

public class InterfazEmpleado extends JFrame {

    private BoardGameCafe cafe;
    private Empleado empleado;
    private CardLayout cardLayout;
    private JPanel panelCentral;

    public InterfazEmpleado() {
        cafe = new BoardGameCafe();
        try { cafe.cargarDatos(); cafe.cargarTorneosJSON(); } catch (Exception e) { e.printStackTrace(); }
        setTitle("Board Game Cafe - Empleado");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);
        panelCentral.add(crearPanelLogin(), "Login");
        add(panelCentral);
    }

    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titulo = new JLabel("Board Game Cafe - Empleado");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Login:"), gbc);
        JTextField campoLogin = new JTextField(15);
        gbc.gridx = 1; panel.add(campoLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Contrasena:"), gbc);
        JPasswordField campoPass = new JPasswordField(15);
        gbc.gridx = 1; panel.add(campoPass, gbc);

        JButton btnLogin = new JButton("Iniciar Sesion");
        btnLogin.setBackground(new Color(70, 130, 240));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            String login = campoLogin.getText().trim();
            String pass  = new String(campoPass.getPassword());
            if (cafe.getUsuarios().containsKey(login) &&
                cafe.getUsuarios().get(login).getPassword().equals(pass) &&
                cafe.getUsuarios().get(login) instanceof Empleado) {
                empleado = (Empleado) cafe.getUsuarios().get(login);
                panelCentral.add(crearPanelMenu(), "Menu");
                cardLayout.show(panelCentral, "Menu");
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel crearPanelMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        JLabel lbl = new JLabel("Bienvenido, " + empleado.getLogin() + " (" + empleado.getClass().getSimpleName() + ")", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        panel.add(lbl, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Torneos", crearPanelTorneos());
        tabs.addTab("Mi Turno", crearPanelTurno());
        panel.add(tabs, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(new Color(245, 247, 250));
        JButton btnSalir = new JButton("Guardar y Salir");
        btnSalir.addActionListener(e -> { cafe.guardarDatos(); cafe.guardarTorneosJSON(); dispose(); });
        footer.add(btnSalir);
        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelTorneos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        panel.setBackground(Color.WHITE);

        String[] cols = {"Nombre", "Juego", "Tipo", "Dia", "Cupos libres"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cargarFilasTorneos(model);

        JTable tabla = new JTable(model);
        tabla.setRowHeight(26);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ctrl.setBackground(Color.WHITE);
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
        JButton btnIns = new JButton("Inscribirse");
        btnIns.setBackground(new Color(70, 130, 240)); btnIns.setForeground(Color.WHITE); btnIns.setFocusPainted(false);
        ctrl.add(new JLabel("Cupos:")); ctrl.add(spinner); ctrl.add(btnIns);
        panel.add(ctrl, BorderLayout.SOUTH);

        btnIns.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un torneo.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
            String nombre = (String) model.getValueAt(fila, 0);
            int cupos = (int) spinner.getValue();
            for (Torneo t : cafe.getTorneos()) {
                if (t.getNombre().equals(nombre)) {
                    try {
                        cafe.inscribirEnTorneo(empleado, t, cupos);
                        JOptionPane.showMessageDialog(this, "Inscripcion exitosa!", "OK", JOptionPane.INFORMATION_MESSAGE);
                        model.setRowCount(0); cargarFilasTorneos(model);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }
        });

        return panel;
    }

    private void cargarFilasTorneos(DefaultTableModel model) {
        for (Torneo t : cafe.getTorneos()) {
            int ocupados = 0;
            for (modelo.torneos.Inscripcion i : t.getInscripciones()) ocupados += i.getCantidadCupos();
            model.addRow(new Object[]{
                t.getNombre(), t.getJuego().getNombre(),
                t instanceof TorneoCompetitivo ? "Competitivo (Gratis)" : "Amistoso",
                t.getDiaSemana(), t.getMaxParticipantes() - ocupados
            });
        }
    }

    private JPanel crearPanelTurno() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        String turnoInfo = empleado.getTurno() != null
            ? empleado.getTurno().getDia() + " - " + empleado.getTurno().getHorario()
            : "Sin turno asignado";

        String[][] filas = {
            {"Empleado:", empleado.getLogin()},
            {"Rol:", empleado.getClass().getSimpleName()},
            {"Turno:", turnoInfo},
            {"En turno ahora:", empleado.estaEnTurno() ? "Si" : "No"}
        };

        for (int i = 0; i < filas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            JLabel lbl = new JLabel(filas[i][0]);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
            panel.add(lbl, gbc);
            gbc.gridx = 1;
            JLabel val = new JLabel(filas[i][1]);
            val.setFont(new Font("SansSerif", Font.PLAIN, 14));
            panel.add(val, gbc);
        }

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new InterfazEmpleado().setVisible(true);
        });
    }
}