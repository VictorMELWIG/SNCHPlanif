package snchbilletterie.app;
import snchbilletterie.model.Role;
public final class AuthGuard {
    private AuthGuard() {}
    public static void require(Role... allowed) {
        if (Session.user == null) {
            SceneManager.show("/snchbilletterie/view/login.fxml", "SNCH - Connexion");
            throw new SecurityException("Utilisateur non connecté");
        }
        Role r = Session.user.getRole();
        for (Role a : allowed) {
            if (a == r) return;
        }
        SceneManager.show("/snchbilletterie/view/login.fxml", "SNCH - Connexion");
        throw new SecurityException("Accès refusé pour le rôle " + r);
    }
}
