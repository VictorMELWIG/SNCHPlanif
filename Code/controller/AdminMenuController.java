package snchbilletterie.controller;

import javafx.fxml.FXML;
import snchbilletterie.app.SceneManager;

public class AdminMenuController {

    @FXML
    public void goItineraires() {
        SceneManager.show("/snchbilletterie/view/itineraires.fxml", "SNCH - Itinéraires");
    }

    @FXML
    public void goTrains() {
        SceneManager.show("/snchbilletterie/view/trains.fxml", "SNCH - Trains");
    }

    @FXML
    public void goTrajets() {
        SceneManager.show("/snchbilletterie/view/trajets.fxml", "SNCH - Trajets");
    }

    @FXML
    public void goUtilisateurs() {
        SceneManager.show("/snchbilletterie/view/utilisateurs.fxml", "SNCH - Utilisateurs");
    }

    @FXML
    public void logout() {
        SceneManager.show("/snchbilletterie/view/login.fxml", "SNCH - Connexion");
    }
}