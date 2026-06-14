package snchbilletterie.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import snchbilletterie.app.AuthGuard;
import snchbilletterie.app.SceneManager;
import snchbilletterie.dao.ItineraireDAO;
import snchbilletterie.model.Arret;
import snchbilletterie.model.Itineraire;
import snchbilletterie.model.Role;

public class ItineraireController {

    @FXML private TableView<Itineraire> table;
    @FXML private TableColumn<Itineraire, Integer> colId;
    @FXML private TableColumn<Itineraire, String> colDepart;
    @FXML private TableColumn<Itineraire, String> colArrivee;
    @FXML private TableColumn<Itineraire, Integer> colDuree;

    @FXML private ComboBox<Arret> cbDepart;
    @FXML private ComboBox<Arret> cbArrivee;
    @FXML private TextField tfDuree;

    @FXML private Label lblInfo;

    private final ItineraireDAO dao = new ItineraireDAO();
    private final ObservableList<Itineraire> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            AuthGuard.require(Role.PLANIFICATEUR, Role.ADMIN);
        } catch (SecurityException e) {
            SceneManager.show("/snchbilletterie/view/login.fxml", "SNCH - Connexion");
            return;
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("idItineraire"));
        colDepart.setCellValueFactory(new PropertyValueFactory<>("villeDepart"));
        colArrivee.setCellValueFactory(new PropertyValueFactory<>("villeArrivee"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("dureePrevue"));

        ObservableList<Arret> arrets = FXCollections.observableArrayList(dao.findAllArrets());
        cbDepart.setItems(arrets);
        cbArrivee.setItems(arrets);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                cbDepart.setValue(newV.getArretDepart());
                cbArrivee.setValue(newV.getArretArrive());
                tfDuree.setText(String.valueOf(newV.getDureePrevue()));
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
        Arret dep = cbDepart.getValue();
        Arret arr = cbArrivee.getValue();
        Integer duree = parseDuree();

        if (dep == null || arr == null || duree == null) {
            lblInfo.setText("Champs invalides.");
            return;
        }

        Itineraire iti = new Itineraire(0, duree, dep, arr);
        boolean ok = dao.insert(iti);
        lblInfo.setText(ok ? "Ajout OK" : "Ajout échoué");
        refresh();
        clearForm();
    }

    @FXML
    public void update() {
        Itineraire selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblInfo.setText("Sélectionne un itinéraire.");
            return;
        }

        Arret dep = cbDepart.getValue();
        Arret arr = cbArrivee.getValue();
        Integer duree = parseDuree();

        if (dep == null || arr == null || duree == null) {
            lblInfo.setText("Champs invalides.");
            return;
        }

        selected.setArretDepart(dep);
        selected.setArretArrive(arr);
        selected.setDureePrevue(duree);

        boolean ok = dao.update(selected);
        lblInfo.setText(ok ? "Modification OK" : "Modification échouée");
        table.refresh();
    }

    @FXML
    public void delete() {
        Itineraire selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblInfo.setText("Sélectionne un itinéraire.");
            return;
        }

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmation");
        a.setHeaderText(null);
        a.setContentText("Supprimer l'itinéraire ID " + selected.getIdItineraire() + " ?");
        if (a.showAndWait().filter(b -> b == ButtonType.OK).isEmpty()) return;

        boolean ok = dao.delete(selected.getIdItineraire());
        lblInfo.setText(ok ? "Suppression OK" : "Suppression échouée");
        if (ok) data.remove(selected);
        clearForm();
    }

    @FXML
    public void clearForm() {
        cbDepart.setValue(null);
        cbArrivee.setValue(null);
        tfDuree.clear();
        table.getSelectionModel().clearSelection();
        lblInfo.setText("");
    }

    @FXML
    public void goBack() {
        SceneManager.show("/snchbilletterie/view/menu_planif.fxml", "SNCH - Planificateur");
    }

    private Integer parseDuree() {
        try {
            int d = Integer.parseInt(tfDuree.getText().trim());
            return d > 0 ? d : null;
        } catch (Exception e) {
            return null;
        }
    }
}