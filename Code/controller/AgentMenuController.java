package snchbilletterie.controller;

import javafx.fxml.FXML;
import snchbilletterie.app.SceneManager;
import snchbilletterie.app.Session;

public class AgentMenuController {

    @FXML public void goVenteBillet() {}
    @FXML public void goBillets() {}

    @FXML
    private void logout() {
        //Session.user = null;
        SceneManager.show("/snchbilletterie/view/login.fxml", "SNCH - Connexion");
    }

    @FXML
    public void goRechercheTrajets() {
        SceneManager.show("/snchbilletterie/view/recherche_trajets.fxml", "SNCH - Rechercher trajets");
    }

}
