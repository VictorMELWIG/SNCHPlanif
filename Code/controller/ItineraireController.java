package snchbilletterie.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import snchbilletterie.app.AuthGuard;
import snchbilletterie.app.SceneManager;
import snchbilletterie.dao.ItineraireDAO;
import snchbilletterie.model.Itineraire;
import snchbilletterie.model.Role;

public class ItineraireController {

    @FXML private TableView<Itineraire> table;
    @FXML private TableColumn<Itineraire, Integer> colId;
    @FXML private TableColumn<Itineraire, String> colDepart;
    @FXML private TableColumn<Itineraire, String> colArrivee;
    @FXML private TableColumn<Itineraire, Integer> colDuree;

    @FXML private TextField tfDepart;
    @FXML private TextField tfArrivee;
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

        data.setAll(dao.findAll());
        table.setItems(data);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                tfDepart.setText(newV.getVilleDepart() == null ? "" : newV.getVilleDepart());
                tfArrivee.setText(newV.getVilleArrivee() == null ? "" : newV.getVilleArrivee());
                tfDuree.setText(String.valueOf(newV.getDureePrevue()));
                lblInfo.setText("");
            }
        });
    }

    @FXML
    public void refresh() {
        data.setAll(dao.findAll());
        table.setItems(data);
        lblInfo.setText("");
    }

    @FXML
    public void add() {
        String depart = tfDepart.getText();
        String arrivee = tfArrivee.getText();
        Integer duree = parseDuree();

        if (depart == null || depart.isBlank()
                || arrivee == null || arrivee.isBlank()
                || duree == null) {
            lblInfo.setText("Champs invalides.");
            return;
        }

        dao.findOrCreateArret(depart.trim());
        dao.findOrCreateArret(arrivee.trim());

        Itineraire iti = new Itineraire(0, duree);
        iti.setVilleDepart(depart.trim());
        iti.setVilleArrivee(arrivee.trim());

        int id = dao.insertAndReturnId(iti);

        if (id != -1) {
            iti.setIdItineraire(id);
            ItineraireDAO.enregistrerVilles(id, depart.trim(), arrivee.trim());

            data.add(0, iti);
            table.refresh();

            lblInfo.setText("Ajout OK");
            clearFormWithoutSelection();
        } else {
            lblInfo.setText("Ajout échoué");

        }
    }

    @FXML
    public void update() {
        Itineraire selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            lblInfo.setText("Sélectionne un itinéraire.");
            return;
        }

        String depart = tfDepart.getText();
        String arrivee = tfArrivee.getText();
        Integer duree = parseDuree();

        if (depart == null || depart.isBlank()
                || arrivee == null || arrivee.isBlank()
                || duree == null) {
            lblInfo.setText("Champs invalides.");
            return;
        }

        dao.findOrCreateArret(depart.trim());
        dao.findOrCreateArret(arrivee.trim());

        selected.setVilleDepart(depart.trim());
        selected.setVilleArrivee(arrivee.trim());
        selected.setDureePrevue(duree);

        boolean ok = dao.update(selected);

        ItineraireDAO.enregistrerVilles(
                selected.getIdItineraire(),
                depart.trim(),
                arrivee.trim()
        );

        table.refresh();
        lblInfo.setText(ok ? "Modification OK" : "Modification échouée");
    }

    @FXML
    public void delete() {
        Itineraire selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            lblInfo.setText("Sélectionne un itinéraire.");
            return;
        }

        boolean confirm = confirm("Supprimer l’itinéraire ID " + selected.getIdItineraire() + " ?");

        if (!confirm) {
            return;
        }

        boolean ok = selected.getIdItineraire() == 0 || dao.delete(selected.getIdItineraire());

        if (ok) {
            data.remove(selected);
        }

        lblInfo.setText(ok ? "Suppression OK" : "Suppression échouée");
        clearFormWithoutSelection();
    }

    @FXML
    public void clearForm() {
        clearFormWithoutSelection();
        table.getSelectionModel().clearSelection();
    }

    private void clearFormWithoutSelection() {
        tfDepart.clear();
        tfArrivee.clear();
        tfDuree.clear();
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

    private boolean confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmation");
        a.setHeaderText(null);
        a.setContentText(msg);
        return a.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }
}