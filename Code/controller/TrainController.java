package snchbilletterie.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import snchbilletterie.app.AuthGuard;
import snchbilletterie.app.SceneManager;
import snchbilletterie.dao.TrainDAO;
import snchbilletterie.model.Role;
import snchbilletterie.model.Train;

public class TrainController {

    @FXML private TableView<Train> table;
    @FXML private TableColumn<Train, Integer> colId;
    @FXML private TableColumn<Train, String> colNumero;
    @FXML private TableColumn<Train, String> colType;

    @FXML private TextField tfNumero;
    @FXML private ComboBox<String> cbType;   // <-- ComboBox au lieu de TextField
    @FXML private Label lblInfo;

    private final TrainDAO dao = new TrainDAO();

    @FXML
    private void initialize() {
        AuthGuard.require(Role.PLANIFICATEUR, Role.ADMIN);

        // Types fixes (TER / TGV)
        cbType.setItems(FXCollections.observableArrayList("TER", "TGV"));

        colId.setCellValueFactory(new PropertyValueFactory<>("idTrain"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroTrain"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeTrain"));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                tfNumero.setText(newV.getNumeroTrain());
                cbType.setValue(newV.getTypeTrain()); // <-- affiche le type sélectionné
                lblInfo.setText("");
            }
        });

        refresh();
    }

    @FXML
    public void refresh() {
        table.setItems(FXCollections.observableArrayList(dao.findAll()));
        lblInfo.setText("");
    }

    @FXML
    public void add() {
        String numero = tfNumero.getText();
        String type = cbType.getValue();

        if (numero == null || numero.isBlank() || type == null || type.isBlank()) {
            lblInfo.setText("Champs invalides.");
            return;
        }

        boolean ok = dao.insert(new Train(numero.trim(), type.trim()));
        lblInfo.setText(ok ? "Ajout OK" : "Ajout échoué");
        refresh();
        clearForm();
    }

    @FXML
    public void update() {
        Train selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblInfo.setText("Sélectionne un train dans le tableau.");
            return;
        }

        String numero = tfNumero.getText();
        String type = cbType.getValue();

        if (numero == null || numero.isBlank() || type == null || type.isBlank()) {
            lblInfo.setText("Champs invalides.");
            return;
        }

        selected.setNumeroTrain(numero.trim());
        selected.setTypeTrain(type.trim());

        boolean ok = dao.update(selected);
        lblInfo.setText(ok ? "Modification OK" : "Modification échouée");
        refresh();
    }

    @FXML
    public void delete() {
        Train selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblInfo.setText("Sélectionne un train dans le tableau.");
            return;
        }

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmation");
        a.setHeaderText(null);
        a.setContentText("Supprimer le train ID " + selected.getIdTrain() + " ?");

        if (a.showAndWait().filter(b -> b == ButtonType.OK).isEmpty()) return;

        boolean ok = dao.delete(selected.getIdTrain());
        lblInfo.setText(ok ? "Suppression OK" : "Suppression échouée (train utilisé dans un trajet)");
        refresh();
        clearForm();
    }

    @FXML
    public void clearForm() {
        tfNumero.clear();
        cbType.setValue(null); // <-- reset combo
        table.getSelectionModel().clearSelection();
        lblInfo.setText("");
    }

    @FXML
    public void goBack() {
        SceneManager.show("/snchbilletterie/view/menu_planif.fxml", "SNCH - Planificateur");
    }
}
