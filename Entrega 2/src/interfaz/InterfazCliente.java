package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import logica.BoardGameCafe;
import modelo.juegos.Juego;
import modelo.torneos.Torneo;
import modelo.torneos.TorneoCompetitivo;
import modelo.usuarios.Cliente;

public class InterfazCliente extends JFrame {

    private BoardGameCafe cafe;
    private Cliente cliente;
    private CardLayout cardLayout;
    private JPanel panelCentral;

    public InterfazCliente() {
        cafe = new BoardGameCafe();
        try { cafe.cargarDatos(); cafe.cargarTorneosJSON(); } catch (Exception e) { e.printStackTrace(); }
        setTitle("Board Game Cafe - Cliente");
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

        JLabel titulo = new JLabel("Board Game Cafe - Cliente");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Login:"), gbc);
        JTextField campoLogin = new JTextField(15);
        gbc.gridx = 1; panel.add(campoLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Contrasena:"), gbc);
        JPasswordField campoPass = new JPasswordField(15);
        gbc.gridx = 1; panel.add(campoPass, gbc);

        JButton btnLogin    = new JButton("Iniciar Sesion");
        JButton btnRegistro = new JButton("Registrarse");
        btnLogin.setBackground(new Color(70, 130, 240));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        JPanel botones = new JPanel(new FlowLayout());
        botones.setOpaque(false);
        botones.add(btnLogin); botones.add(btnRegistro);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; panel.add(botones, gbc);

        btnLogin.addActionListener(e -> {
            String login = campoLogin.getText().trim();
            String pass  = new String(campoPass.getPassword());
            if (cafe.getUsuarios().containsKey(login) &&
                cafe.getUsuarios().get(login).getPassword().equals(pass) &&
                cafe.getUsuarios().get(login) instanceof Cliente) {
                cliente = (Cliente) cafe.getUsuarios().get(login);
                panelCentral.add(crearPanelMenu(), "Menu");
                cardLayout.show(panelCentral, "Menu");
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRegistro.addActionListener(e -> {
            String login = campoLogin.getText().trim();
            String pass  = new String(campoPass.getPassword());
            if (login.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete login y contrasena.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (cafe.getUsuarios().containsKey(login)) {
                JOptionPane.showMessageDialog(this, "Ese login ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cliente = new Cliente(login, pass, 0);
            cafe.registrarUsuario(cliente);
            panelCentral.add(crearPanelMenu(), "Menu");
            cardLayout.show(panelCentral, "Menu");
        });

        return panel;
    }

    private JPanel crearPanelMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        JLabel lbl = new JLabel("Bienvenido, " + cliente.getLogin() + " | Puntos: " + cliente.getPuntosFidelidad(), SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        panel.add(lbl, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Torneos", crearPanelTorneos());
        tabs.addTab("Juegos Favoritos", crearPanelFavoritos());
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
        JButton btnDes = new JButton("Desinscribirse");
        btnIns.setBackground(new Color(70, 130, 240)); btnIns.setForeground(Color.WHITE); btnIns.setFocusPainted(false);
        btnDes.setBackground(new Color(220, 80, 80));  btnDes.setForeground(Color.WHITE); btnDes.setFocusPainted(false);
        ctrl.add(new JLabel("Cupos:")); ctrl.add(spinner); ctrl.add(btnIns); ctrl.add(btnDes);
        panel.add(ctrl, BorderLayout.SOUTH);

        btnIns.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un torneo.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
            String nombre = (String) model.getValueAt(fila, 0);
            int cupos = (int) spinner.getValue();
            for (Torneo t : cafe.getTorneos()) {
                if (t.getNombre().equals(nombre)) {
                    try {
                        cafe.inscribirEnTorneo(cliente, t, cupos);
                        JOptionPane.showMessageDialog(this, "Inscripcion exitosa!", "OK", JOptionPane.INFORMATION_MESSAGE);
                        model.setRowCount(0); cargarFilasTorneos(model);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }
        });

        btnDes.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un torneo.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
            String nombre = (String) model.getValueAt(fila, 0);
            for (Torneo t : cafe.getTorneos()) {
                if (t.getNombre().equals(nombre)) {
                    cafe.desinscribirDeTorneo(cliente, t);
                    JOptionPane.showMessageDialog(this, "Desinscripcion realizada.", "OK", JOptionPane.INFORMATION_MESSAGE);
                    model.setRowCount(0); cargarFilasTorneos(model);
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
                t instanceof TorneoCompetitivo ? "Competitivo" : "Amistoso",
                t.getDiaSemana(), t.getMaxParticipantes() - ocupados
            });
        }
    }

    private JPanel crearPanelFavoritos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        panel.setBackground(Color.WHITE);

        String[] cols = {"Nombre", "Categoria", "Favorito?"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cargarFilasJuegos(model);

        JTable tabla = new JTable(model);
        tabla.setRowHeight(26);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnMarcar = new JButton("Marcar como Favorito");
        btnMarcar.setBackground(new Color(255, 200, 50)); btnMarcar.setFocusPainted(false);
        btnMarcar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un juego.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
            String nombre = (String) model.getValueAt(fila, 0);
            Juego j = cafe.getJuegos().get(nombre);
            if (j != null) {
                cliente.agregarJuegoFavorito(j);
                JOptionPane.showMessageDialog(this, "Juego marcado como favorito.", "OK", JOptionPane.INFORMATION_MESSAGE);
                model.setRowCount(0); cargarFilasJuegos(model);
            }
        });

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(Color.WHITE); footer.add(btnMarcar);
        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarFilasJuegos(DefaultTableModel model) {
        for (Juego j : cafe.getJuegos().values()) {
            model.addRow(new Object[]{
                j.getNombre(), j.getCategoria(),
                cliente.getJuegosFavoritos().contains(j) ? "Favorito" : "-"
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new InterfazCliente().setVisible(true);
        });
    }
}