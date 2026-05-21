package snchbilletterie.controller;

import javafx.fxml.FXML;
import snchbilletterie.app.SceneManager;

public class PlanifMenuController {

    @FXML
    public void goItineraires() {
        System.out.println(">>> CLICK goItineraires");
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
    public void logout() {
        System.out.println(">>> CLICK logout");
        SceneManager.show("/snchbilletterie/view/login.fxml", "SNCH - Connexion");
    }
}
