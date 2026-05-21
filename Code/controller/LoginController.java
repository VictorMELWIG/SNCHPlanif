package snchbilletterie.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import snchbilletterie.app.SceneManager;
import snchbilletterie.app.Session;
import snchbilletterie.dao.UtilisateurDAO;
import snchbilletterie.model.Utilisateur;

public class LoginController {

    @FXML private TextField tfLogin;
    @FXML private PasswordField pfPassword;
    @FXML private Label lblInfo;

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @FXML
    private void handleLogin() {
        String login = tfLogin.getText();
        String mdp = pfPassword.getText();

        if (login == null || login.isBlank() || mdp == null || mdp.isBlank()) {
            lblInfo.setText("Veuillez saisir login et mot de passe.");
            return;
        }

        Utilisateur u = utilisateurDAO.authentifier(login.trim(), mdp);
        if (u == null) {
            lblInfo.setText("Identifiants incorrects.");
            return;
        }

        //stocke l'utilisateur connecté en session
        Session.user = u;

        switch (u.getRole()) {
            case AGENT -> SceneManager.show("/snchbilletterie/view/menu_agent.fxml", "SNCH - Agent");
            case PLANIFICATEUR -> SceneManager.show("/snchbilletterie/view/menu_planif.fxml", "SNCH - Planificateur");
            case ADMIN -> SceneManager.show("/snchbilletterie/view/menu_admin.fxml", "SNCH - Admin");
        }
    }
}
