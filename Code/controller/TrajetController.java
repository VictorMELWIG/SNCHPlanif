package snchbilletterie.controller;

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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TrajetController {

    @FXML private TableView<Trajet> table;
    @FXML private TableColumn<Trajet, Integer> colId;
    @FXML private TableColumn<Trajet, Object> colDate;
    @FXML private TableColumn<Trajet, Object> colHD;
    @FXML private TableColumn<Trajet, Object> colHA;
    @FXML private TableColumn<Trajet, Train> colTrain;
    @FXML private TableColumn<Trajet, Itineraire> colIti;

    @FXML private DatePicker dpDate;
    @FXML private TextField tfHeureDepart;
    @FXML private TextField tfHeureArrivee;

    @FXML private ComboBox<Train> cbTrain;
    @FXML private ComboBox<Itineraire> cbItineraire;
    @FXML private ComboBox<Arret> cbArretDepart;
    @FXML private ComboBox<Arret> cbArretArrive;

    @FXML private Label lblInfo;

    private final TrajetDAO trajetDAO = new TrajetDAO();
    private final TrainDAO trainDAO = new TrainDAO();
    private final ItineraireDAO itineraireDAO = new ItineraireDAO();

    private final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private void initialize() {
        AuthGuard.require(Role.PLANIFICATEUR, Role.ADMIN);

        colId.setCellValueFactory(new PropertyValueFactory<>("idTrajet"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateDepart"));
        colHD.setCellValueFactory(new PropertyValueFactory<>("heureDepart"));
        colHA.setCellValueFactory(new PropertyValueFactory<>("heureArriveePrevue"));
        colTrain.setCellValueFactory(new PropertyValueFactory<>("train"));
        colIti.setCellValueFactory(new PropertyValueFactory<>("itineraire"));

        tfHeureArrivee.setEditable(false);

        cbTrain.setItems(FXCollections.observableArrayList(trainDAO.findAll()));
        cbItineraire.setItems(FXCollections.observableArrayList(itineraireDAO.findAll()));
        configComboItineraire();
        cbArretDepart.setItems(FXCollections.observableArrayList(trajetDAO.findAllArrets()));
        cbArretArrive.setItems(FXCollections.observableArrayList(trajetDAO.findAllArrets()));

        tfHeureDepart.textProperty().addListener((obs, oldV, newV) -> calculerHeureArrivee());
        cbItineraire.valueProperty().addListener((obs, oldV, newV) -> calculerHeureArrivee());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, t) -> {
            if (t != null) {
                dpDate.setValue(t.getDateDepart());
                tfHeureDepart.setText(t.getHeureDepart().format(HHMM));
                tfHeureArrivee.setText(t.getHeureArriveePrevue().format(HHMM));

                cbTrain.getSelectionModel().select(
                        cbTrain.getItems().stream()
                                .filter(x -> x.getIdTrain() == t.getTrain().getIdTrain())
                                .findFirst()
                                .orElse(null)
                );

                cbItineraire.getSelectionModel().select(
                        cbItineraire.getItems().stream()
                                .filter(x -> x.getIdItineraire() == t.getItineraire().getIdItineraire())
                                .findFirst()
                                .orElse(null)
                );

                cbArretDepart.getSelectionModel().select(
                        cbArretDepart.getItems().stream()
                                .filter(x -> x.getIdArret() == t.getIdArretDepart())
                                .findFirst()
                                .orElse(null)
                );

                cbArretArrive.getSelectionModel().select(
                        cbArretArrive.getItems().stream()
                                .filter(x -> x.getIdArret() == t.getIdArretArrive())
                                .findFirst()
                                .orElse(null)
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
            LocalTime hd = LocalTime.parse(tfHeureDepart.getText().trim(), HHMM);
            int duree = cbItineraire.getValue().getDureePrevue();

            LocalTime ha = hd.plusMinutes(duree);
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

        if (selected == null) {
            lblInfo.setText("Sélectionne un trajet.");
            return;
        }

        Trajet t = buildFromForm();
        if (t == null) return;

        selected.setDateDepart(t.getDateDepart());
        selected.setHeureDepart(t.getHeureDepart());
        selected.setHeureArriveePrevue(t.getHeureArriveePrevue());
        selected.setTrain(t.getTrain());
        selected.setItineraire(t.getItineraire());
        selected.setIdArretDepart(t.getIdArretDepart());
        selected.setIdArretArrive(t.getIdArretArrive());

        boolean ok = trajetDAO.update(selected);
        lblInfo.setText(ok ? "Modification OK" : "Modification échouée");

        refresh();
    }

    @FXML
    public void delete() {
        Trajet selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            lblInfo.setText("Sélectionne un trajet.");
            return;
        }

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
        cbArretDepart.getSelectionModel().clearSelection();
        cbArretArrive.getSelectionModel().clearSelection();

        table.getSelectionModel().clearSelection();
        lblInfo.setText("");
    }

    @FXML
    public void goBack() {
        SceneManager.show("/snchbilletterie/view/menu_planif.fxml", "SNCH - Planificateur");
    }

    private Trajet buildFromForm() {
        if (dpDate.getValue() == null) {
            lblInfo.setText("Choisis une date.");
            return null;
        }

        if (cbTrain.getValue() == null) {
            lblInfo.setText("Choisis un train.");
            return null;
        }

        if (cbItineraire.getValue() == null) {
            lblInfo.setText("Choisis un itinéraire.");
            return null;
        }

        if (cbArretDepart.getValue() == null) {
            lblInfo.setText("Choisis un arrêt de départ.");
            return null;
        }

        if (cbArretArrive.getValue() == null) {
            lblInfo.setText("Choisis un arrêt d’arrivée.");
            return null;
        }

        LocalTime hd;
        LocalTime ha;

        try {
            hd = LocalTime.parse(tfHeureDepart.getText().trim(), HHMM);
            ha = hd.plusMinutes(cbItineraire.getValue().getDureePrevue());
            tfHeureArrivee.setText(ha.format(HHMM));
        } catch (Exception e) {
            lblInfo.setText("Heure départ invalide. Format attendu : HH:mm.");
            return null;
        }

        Trajet trajet = new Trajet(
                dpDate.getValue(),
                hd,
                ha,
                cbTrain.getValue(),
                cbItineraire.getValue()
        );

        trajet.setIdArretDepart(cbArretDepart.getValue().getIdArret());
        trajet.setIdArretArrive(cbArretArrive.getValue().getIdArret());

        return trajet;
    }
    private void configComboItineraire() {
        cbItineraire.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Itineraire item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });

        cbItineraire.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Itineraire item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });
    }
}