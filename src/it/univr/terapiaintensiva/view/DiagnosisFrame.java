package it.univr.terapiaintensiva.view;

import javax.swing.*;
import java.awt.*;

public class DiagnosisFrame extends JFrame {
    private static final String title = "Inserisci diagnosi";
    private final JTextPane diagnosiPane = new JTextPane();
    private final JButton okButton = new JButton("Ok");
    private final JButton cancelButton = new JButton("Annulla");
    private final JScrollPane diagnosisScroll = new JScrollPane(diagnosiPane);
    private final JPanel centerPanel = new JPanel(new BorderLayout());
    private final JPanel southPanel = new JPanel(new FlowLayout());

    public DiagnosisFrame() {
        centerPanel.add(diagnosisScroll, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        southPanel.add(cancelButton);
        southPanel.add(okButton);

        Container contentPane = this.getContentPane();
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(southPanel, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setTitle(title);
        this.setPreferredSize(new Dimension(540, 250));
        this.setMinimumSize(new Dimension(540, 250));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
