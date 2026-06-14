package snchbilletterie.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public final class SceneManager {

    private static Stage stage;

    private SceneManager() {}

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void show(String fxmlPath, String title) {
        try {
            double w = stage.getWidth();
            double h = stage.getHeight();
            boolean firstShow = (w <= 0 || h <= 0); // au tout 1er affichage

            URL url = SceneManager.class.getResource(fxmlPath);
            if (url == null) throw new RuntimeException("FXML introuvable : " + fxmlPath);

            FXMLLoader loader = new FXMLLoader(url);
            Scene scene = new Scene(loader.load());

            stage.setTitle(title);
            stage.setScene(scene);

            //fixe la taille de l'app
            if (!firstShow) {
                stage.setWidth(w);
                stage.setHeight(h);
            } else {
                // taille par défaut au 1er écran
                stage.setWidth(1100);
                stage.setHeight(700);
            }

            //empeche les rétrécisements
            stage.setMinWidth(950);
            stage.setMinHeight(600);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Impossible de charger : " + fxmlPath, e);
        }
    }

}
