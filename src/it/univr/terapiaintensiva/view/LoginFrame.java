package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A frame used for authentication.
 */
public class LoginFrame extends JFrame implements ActionListener {

    private static final String title = "Log in";
    private static final JLabel unameLabel = new JLabel("Username ");
    private static final JLabel passLabel = new JLabel(("Password "));
    private static final JTextField unameTField = new JTextField();
    private static final JTextField passField = new JPasswordField();
    private static final JPanel centerPanel = new JPanel(new GridBagLayout());
    private static final JButton loginButton = new JButton("Log in");
    private static final JButton guestButton = new JButton("Guest");
    private static final JPanel southPanel = new JPanel();
    private static GridBagConstraints c = new GridBagConstraints();
    private final Model model = Model.getInstance();

    public LoginFrame() {

        loginButton.addActionListener(this);
        guestButton.addActionListener(this);

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
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(300, 150);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // Listener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(loginButton)) {    // Login
            String user = unameTField.getText();
            String psw = passField.getText();
            if (model.authenticate(user.trim(), psw.trim()) != Model.GUEST) {
                MonitorFrame.getInstance().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenziali errate", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else {                                    // Login as guest
            model.authenticate("guestUser", "guestPassword");
            MonitorFrame.getInstance().setVisible(true);
            this.dispose();
        }
    }
}
