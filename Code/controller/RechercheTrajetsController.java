package snchbilletterie.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import snchbilletterie.app.AuthGuard;
import snchbilletterie.app.SceneManager;
import snchbilletterie.app.Session;
import snchbilletterie.dao.TrajetDAO;
import snchbilletterie.model.Role;
import snchbilletterie.model.Trajet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RechercheTrajetsController {

    @FXML private DatePicker dpDate;
    @FXML private TextField tfDepart;
    @FXML private TextField tfArrivee;

    @FXML private TableView<Trajet> table;
    @FXML private TableColumn<Trajet, Integer> colId;
    @FXML private TableColumn<Trajet, String> colDate;
    @FXML private TableColumn<Trajet, String> colHeureDep;
    @FXML private TableColumn<Trajet, String> colHeureArr;
    @FXML private TableColumn<Trajet, String> colDepart;
    @FXML private TableColumn<Trajet, String> colArrivee;
    @FXML private TableColumn<Trajet, String> colTrain;

    @FXML private Button btnSelect;
    @FXML private Label lblInfo;

    private final TrajetDAO dao = new TrajetDAO();
    private final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private void initialize() {
        AuthGuard.require(Role.AGENT, Role.ADMIN);

        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        colId.setCellValueFactory(new PropertyValueFactory<>("idTrajet"));

        colDate.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDateHeureDepart() != null
                        ? c.getValue().getDateHeureDepart().format(DATE_FMT) : ""
        ));

        colHeureDep.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDateHeureDepart() != null
                        ? c.getValue().getDateHeureDepart().format(TIME_FMT) : ""
        ));

        colHeureArr.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDateHeureArrivee() != null
                        ? c.getValue().getDateHeureArrivee().format(TIME_FMT) : ""
        ));

        colDepart.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getItineraire() != null
                        ? c.getValue().getItineraire().getVilleDepart() : ""
        ));

        colArrivee.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getItineraire() != null
                        ? c.getValue().getItineraire().getVilleArrivee() : ""
        ));

        colTrain.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTrain() != null ? c.getValue().getTrain().toString() : ""
        ));

        if (btnSelect != null) {
            btnSelect.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        }

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                lblInfo.setText("Trajet sélectionné : ID " + newV.getIdTrajet());
            } else {
                lblInfo.setText("");
            }
        });

        table.setRowFactory(tv -> {
            TableRow<Trajet> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (!row.isEmpty() && e.getClickCount() == 2) {
                    selectTrajet();
                }
            });
            return row;
        });

        reset();
    }

    @FXML
    public void search() {
        LocalDate date = dpDate.getValue();
        String depart = tfDepart.getText();
        String arrivee = tfArrivee.getText();

        var res = dao.search(date, depart, arrivee);
        table.setItems(FXCollections.observableArrayList(res));
        lblInfo.setText(res.isEmpty() ? "Aucun trajet trouvé." : "");
    }

    @FXML
    public void reset() {
        dpDate.setValue(null);
        tfDepart.clear();
        tfArrivee.clear();
        table.setItems(FXCollections.observableArrayList(dao.findAll()));
        table.getSelectionModel().clearSelection();
        lblInfo.setText("");
    }

    @FXML
    public void selectTrajet() {
        Trajet selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblInfo.setText("Sélectionne un trajet.");
            return;
        }
        Session.selectedTrajet = selected;
        SceneManager.show("/snchbilletterie/view/menu_agent.fxml", "SNCH - Agent");
    }

    @FXML
    public void goBack() {
        SceneManager.show("/snchbilletterie/view/menu_agent.fxml", "SNCH - Agent");
    }
}