package it.univr.terapiaintensiva;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.view.LoginFrame;

public class TerapiaIntensiva {
    public static void main(String[] args) {
//        try {
//            // Set Nimbus LaF
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Model model = Model.getInstance();
        LoginFrame loginFrame = new LoginFrame();
    }
}
