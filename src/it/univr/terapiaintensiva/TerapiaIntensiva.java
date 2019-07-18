package it.univr.terapiaintensiva;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.view.LoginFrame;

public class TerapiaIntensiva {
    public static void main(String[] args) {
        Model model = new Model();
        LoginFrame loginFrame = new LoginFrame(model);
    }
}
