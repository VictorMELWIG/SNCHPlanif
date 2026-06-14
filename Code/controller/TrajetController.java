package snchbilletterie.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import snchbilletterie.app.AuthGuard;
import snchbilletterie.app.SceneManager;
import snchbilletterie.dao.ItineraireDAO;
import snchbilletterie.dao.TrajetDAO;
import snchbilletterie.dao.TrainDAO;
import snchbilletterie.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TrajetController {

    @FXML private TableView<Trajet> table;
    @FXML private TableColumn<Trajet, Integer> colId;
    @FXML private TableColumn<Trajet, String> colDepart;
    @FXML private TableColumn<Trajet, String> colArrivee;
    @FXML private TableColumn<Trajet, String> colTrain;
    @FXML private TableColumn<Trajet, String> colIti;

    @FXML private DatePicker dpDate;
    @FXML private TextField tfHeureDepart;
    @FXML private TextField tfHeureArrivee;

    @FXML private ComboBox<Train> cbTrain;
    @FXML private ComboBox<Itineraire> cbItineraire;

    @FXML private Label lblInfo;

    private final TrajetDAO trajetDAO = new TrajetDAO();
    private final TrainDAO trainDAO = new TrainDAO();
    private final ItineraireDAO itineraireDAO = new ItineraireDAO();

    private final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter DT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    private void initialize() {
        AuthGuard.require(Role.PLANIFICATEUR, Role.ADMIN);

        colId.setCellValueFactory(new PropertyValueFactory<>("idTrajet"));
        colDepart.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDateHeureDepart() != null ? c.getValue().getDateHeureDepart().format(DT) : ""
        ));
        colArrivee.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDateHeureArrivee() != null ? c.getValue().getDateHeureArrivee().format(DT) : ""
        ));
        colTrain.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTrain() != null ? c.getValue().getTrain().toString() : ""
        ));
        colIti.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getItineraire() != null ? c.getValue().getItineraire().toString() : ""
        ));

        tfHeureArrivee.setEditable(false);

        cbTrain.setItems(FXCollections.observableArrayList(trainDAO.findAll()));
        cbItineraire.setItems(FXCollections.observableArrayList(itineraireDAO.findAll()));
        configComboItineraire();

        tfHeureDepart.textProperty().addListener((obs, oldV, newV) -> calculerHeureArrivee());
        cbItineraire.valueProperty().addListener((obs, oldV, newV) -> calculerHeureArrivee());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, t) -> {
            if (t != null) {
                dpDate.setValue(t.getDateHeureDepart().toLocalDate());
                tfHeureDepart.setText(t.getDateHeureDepart().format(HHMM));
                tfHeureArrivee.setText(t.getDateHeureArrivee().format(HHMM));

                cbTrain.getSelectionModel().select(
                        cbTrain.getItems().stream()
                                .filter(x -> x.getIdTrain() == t.getTrain().getIdTrain())
                                .findFirst().orElse(null)
                );

                cbItineraire.getSelectionModel().select(
                        cbItineraire.getItems().stream()
                                .filter(x -> x.getIdItineraire() == t.getItineraire().getIdItineraire())
                                .findFirst().orElse(null)
                );

                lblInfo.setText("");
            }
        });

        refresh();
    }

    private void calculerHeureArrivee() {
        if (cbItineraire.getValue() == null || tfHeureDepart.getText().isBlank()) {
            tfHeureArrivee.clear();
            return;
        }
        try {
            var hd = java.time.LocalTime.parse(tfHeureDepart.getText().trim(), HHMM);
            var ha = hd.plusMinutes(cbItineraire.getValue().getDureePrevue());
            tfHeureArrivee.setText(ha.format(HHMM));
        } catch (Exception e) {
            tfHeureArrivee.clear();
        }
    }

    @FXML
    public void refresh() {
        table.setItems(FXCollections.observableArrayList(trajetDAO.findAll()));
        cbTrain.setItems(FXCollections.observableArrayList(trainDAO.findAll()));
        cbItineraire.setItems(FXCollections.observableArrayList(itineraireDAO.findAll()));
        configComboItineraire();
        lblInfo.setText("");
    }

    @FXML
    public void add() {
        Trajet t = buildFromForm();
        if (t == null) return;
        boolean ok = trajetDAO.insert(t);
        lblInfo.setText(ok ? "Ajout OK" : "Ajout échoué");
        refresh();
        clearForm();
    }

    @FXML
    public void update() {
        Trajet selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { lblInfo.setText("Sélectionne un trajet."); return; }

        Trajet t = buildFromForm();
        if (t == null) return;

        selected.setDateHeureDepart(t.getDateHeureDepart());
        selected.setDateHeureArrivee(t.getDateHeureArrivee());
        selected.setTrain(t.getTrain());
        selected.setItineraire(t.getItineraire());

        boolean ok = trajetDAO.update(selected);
        lblInfo.setText(ok ? "Modification OK" : "Modification échouée");
        refresh();
    }

    @FXML
    public void delete() {
        Trajet selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { lblInfo.setText("Sélectionne un trajet."); return; }

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmation");
        a.setHeaderText(null);
        a.setContentText("Supprimer le trajet ID " + selected.getIdTrajet() + " ?");
        if (a.showAndWait().filter(b -> b == ButtonType.OK).isEmpty()) return;

        boolean ok = trajetDAO.delete(selected.getIdTrajet());
        lblInfo.setText(ok ? "Suppression OK" : "Suppression échouée");
        refresh();
        clearForm();
    }

    @FXML
    public void clearForm() {
        dpDate.setValue(null);
        tfHeureDepart.clear();
        tfHeureArrivee.clear();
        cbTrain.getSelectionModel().clearSelection();
        cbItineraire.getSelectionModel().clearSelection();
        table.getSelectionModel().clearSelection();
        lblInfo.setText("");
    }

    @FXML
    public void goBack() {
        SceneManager.show("/snchbilletterie/view/menu_planif.fxml", "SNCH - Planificateur");
    }

    private Trajet buildFromForm() {
        if (dpDate.getValue() == null) { lblInfo.setText("Choisis une date."); return null; }
        if (cbTrain.getValue() == null) { lblInfo.setText("Choisis un train."); return null; }
        if (cbItineraire.getValue() == null) { lblInfo.setText("Choisis un itinéraire."); return null; }

        try {
            var hd = java.time.LocalTime.parse(tfHeureDepart.getText().trim(), HHMM);
            var ha = hd.plusMinutes(cbItineraire.getValue().getDureePrevue());
            tfHeureArrivee.setText(ha.format(HHMM));

            LocalDateTime depart = LocalDateTime.of(dpDate.getValue(), hd);
            LocalDateTime arrivee = LocalDateTime.of(dpDate.getValue(), ha);

            return new Trajet(depart, arrivee, cbTrain.getValue(), cbItineraire.getValue());
        } catch (Exception e) {
            lblInfo.setText("Heure départ invalide. Format attendu : HH:mm.");
            return null;
        }
    }

    private void configComboItineraire() {
        cbItineraire.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Itineraire item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });
        cbItineraire.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Itineraire item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });
    }
}