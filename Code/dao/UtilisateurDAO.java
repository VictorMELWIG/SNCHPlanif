package snchbilletterie.dao;

import snchbilletterie.model.Role;
import snchbilletterie.model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UtilisateurDAO {

    public Utilisateur authentifier(String login, String motDePasse) {
        String sql = """
                SELECT id_utilisateur, login, mot_de_passe, nom, prenom, role, actif
                FROM utilisateur
                WHERE login = ?
                """;

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, login);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                if (rs.getInt("actif") != 1) return null;

                // mot de passe en clair pour l’instant
                String mdpBDD = rs.getString("mot_de_passe");
                if (!mdpBDD.equals(motDePasse)) return null;

                Role role = Role.valueOf(rs.getString("role"));

                return new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("login"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        role
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
