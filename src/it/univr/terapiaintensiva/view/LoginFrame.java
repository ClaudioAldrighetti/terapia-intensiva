package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LoginFrame extends JFrame implements MouseListener {

    private static final String title = "Log in";
    // Center panel
    private static final JLabel unameLabel = new JLabel("Username ");
    private static final JLabel passLabel = new JLabel(("Password "));
    private static final JTextField unameTField = new JTextField();
    private static final JTextField passField = new JPasswordField();
    private static final JPanel centerPanel = new JPanel(new GridBagLayout());
    // South panel
    private static final JButton loginButton = new JButton("Log in");
    private static final JButton guestButton = new JButton("Guest");
    private static final JPanel southPanel = new JPanel();
    private static GridBagConstraints c = new GridBagConstraints();
    private final Model model = Model.getIstance();

    public LoginFrame() {

        loginButton.addMouseListener(this);
        guestButton.addMouseListener(this);

        // Fill center panel
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        centerPanel.add(unameLabel, c);

        c.gridy = 1;
        centerPanel.add(passLabel, c);

        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(passField, c);

        c.gridy = 0;
        centerPanel.add(unameTField, c);

        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Fill south panel
        southPanel.add(guestButton);
        southPanel.add(loginButton);

        // Fill parent
        Container contentPane = this.getContentPane();
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(southPanel, BorderLayout.SOUTH);

        this.setTitle(title);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(300, 150);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(loginButton)) {
            String user = unameTField.getText();
            String psw = passField.getText();
            if (model.authenticate(user, psw) != Model.GUEST) {
                MonitorFrame monitorFrame = MonitorFrame.getIstance();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenziali errate", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource().equals((guestButton))) {
            MonitorFrame monitorFrame = MonitorFrame.getIstance();
            this.dispose();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
