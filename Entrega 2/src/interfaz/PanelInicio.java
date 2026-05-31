package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelInicio extends JPanel {

    public PanelInicio() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));
        
        JPanel pnlContenido = new JPanel(new GridBagLayout());
        pnlContenido.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(10, 10, 20, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel lblBienvenido = new JLabel("<html><center>¡Bienvenido<br>Administrador!</center></html>");
        lblBienvenido.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 48));
        lblBienvenido.setHorizontalAlignment(JLabel.CENTER);
        pnlContenido.add(lblBienvenido, gbc);
        
        gbc.gridy = 1;
        JLabel lblInstruccion = new JLabel("<html><center>Seleccione una opción del<br>menú lateral</center></html>");
        lblInstruccion.setFont(new Font("SansSerif", Font.ITALIC, 24));
        lblInstruccion.setHorizontalAlignment(JLabel.CENTER);
        pnlContenido.add(lblInstruccion, gbc);
        
        add(pnlContenido);
    }
}
