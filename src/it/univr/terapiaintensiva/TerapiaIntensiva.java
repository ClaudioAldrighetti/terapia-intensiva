package it.univr.terapiaintensiva;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.view.LoginFrame;

import javax.swing.*;

public class TerapiaIntensiva {
    public static void main(String[] args) {
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Model model = Model.getIstance();
        LoginFrame loginFrame = new LoginFrame();
    }
}
