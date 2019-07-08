package it.univr.terapiaintensiva.view;

import javax.swing.*;
import java.awt.*;

public class MonitorFrame extends JFrame {

    private static final JPanel[] monitors = new JPanel[10];
    private static final JMenu chiefMenu = new JMenu("Primario");
    private static final JMenuItem dischargeMenuItem = new JMenuItem("Dimetti paziente");
    private static final JMenuItem reportMenuItem = new JMenuItem("Visualizza report");
    private static final JMenuBar menuBar = new JMenuBar();

    public MonitorFrame(){

        for (int i = 0; i < 10; i++) {
            monitors[i] = new MonitorPanel(i + 1);
        }

        Container contentPane = this.getContentPane();
        contentPane.setLayout(new GridLayout(2, 5));

        for (int i = 0; i < 10; i++) {
            contentPane.add(monitors[i]);
        }

        // Menu
        chiefMenu.add(dischargeMenuItem);
        chiefMenu.add(reportMenuItem);
        menuBar.add(chiefMenu);
        this.setJMenuBar(menuBar);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setMinimumSize(new Dimension(500, 500));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}

