package snchbilletterie.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import snchbilletterie.app.AuthGuard;
import snchbilletterie.app.SceneManager;
import snchbilletterie.dao.UtilisateurDAO;
import snchbilletterie.model.Role;
import snchbilletterie.model.Utilisateur;

public class UtilisateurController {

    @FXML private TableView<Utilisateur> table;
    @FXML private TableColumn<Utilisateur, Integer> colId;
    @FXML private TableColumn<Utilisateur, String> colLogin;
    @FXML private TableColumn<Utilisateur, String> colNom;
    @FXML private TableColumn<Utilisateur, String> colPrenom;
    @FXML private TableColumn<Utilisateur, String> colRole;
    @FXML private TableColumn<Utilisateur, Boolean> colActif;

    @FXML private TextField tfLogin;
    @FXML private TextField tfMotDePasse;
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private ComboBox<Role> cbRole;

    @FXML private Label lblInfo;

    private final UtilisateurDAO dao = new UtilisateurDAO();
    private final ObservableList<Utilisateur> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            AuthGuard.require(Role.ADMIN);
        } catch (SecurityException e) {
            SceneManager.show("/snchbilletterie/view/login.fxml", "SNCH - Connexion");
            return;
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colActif.setCellValueFactory(new PropertyValueFactory<>("actif"));

        cbRole.setItems(FXCollections.observableArrayList(Role.values()));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                tfLogin.setText(newV.getLogin());
                tfMotDePasse.clear();
                tfNom.setText(newV.getNom());
                tfPrenom.setText(newV.getPrenom());
                cbRole.setValue(newV.getRole());
                lblInfo.setText("");
            }
        });

        refresh();
    }

    @FXML
    public void refresh() {
        data.setAll(dao.findAll());
        table.setItems(data);
        lblInfo.setText("");
    }

    @FXML
    public void add() {
        String login = tfLogin.getText();
        String mdp = tfMotDePasse.getText();
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        Role role = cbRole.getValue();

        if (login == null || login.isBlank() || mdp == null || mdp.isBlank()
                || nom == null || nom.isBlank() || prenom == null || prenom.isBlank()
                || role == null) {
            lblInfo.setText("Tous les champs sont obligatoires.");
            return;
        }

        Utilisateur u = new Utilisateur(login.trim(), mdp.trim(), nom.trim(), prenom.trim(), role);
        boolean ok = dao.insert(u);
        lblInfo.setText(ok ? "Ajout OK" : "Ajout échoué (login déjà existant ?)");
        refresh();
        clearForm();
    }

    @FXML
    public void update() {
        Utilisateur selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblInfo.setText("Sélectionne un utilisateur.");
            return;
        }

        String login = tfLogin.getText();
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        Role role = cbRole.getValue();

        if (login == null || login.isBlank() || nom == null || nom.isBlank()
                || prenom == null || prenom.isBlank() || role == null) {
            lblInfo.setText("Tous les champs sont obligatoires.");
            return;
        }

        Utilisateur u = new Utilisateur(selected.getId(), login.trim(), nom.trim(), prenom.trim(), role, selected.isActif());
        boolean ok = dao.update(u);
        lblInfo.setText(ok ? "Modification OK" : "Modification échouée");
        refresh();
    }

    @FXML
    public void delete() {
        Utilisateur selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblInfo.setText("Sélectionne un utilisateur.");
            return;
        }

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmation");
        a.setHeaderText(null);
        a.setContentText("Supprimer l'utilisateur " + selected.getLogin() + " ?");
        if (a.showAndWait().filter(b -> b == ButtonType.OK).isEmpty()) return;

        boolean ok = dao.delete(selected.getId());
        lblInfo.setText(ok ? "Suppression OK" : "Suppression échouée");
        refresh();
        clearForm();
    }

    @FXML
    public void toggleActif() {
        Utilisateur selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblInfo.setText("Sélectionne un utilisateur.");
            return;
        }

        boolean nouvelEtat = !selected.isActif();
        boolean ok = dao.toggleActif(selected.getId(), nouvelEtat);
        lblInfo.setText(ok ? (nouvelEtat ? "Compte activé" : "Compte désactivé") : "Erreur");
        refresh();
    }

    @FXML
    public void clearForm() {
        tfLogin.clear();
        tfMotDePasse.clear();
        tfNom.clear();
        tfPrenom.clear();
        cbRole.setValue(null);
        table.getSelectionModel().clearSelection();
        lblInfo.setText("");
    }

    @FXML
    public void goBack() {
        SceneManager.show("/snchbilletterie/view/menu_admin.fxml", "SNCH - Admin");
    }
}