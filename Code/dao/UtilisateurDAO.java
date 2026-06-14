package snchbilletterie.dao;

import snchbilletterie.model.Role;
import snchbilletterie.model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    public Utilisateur authentifier(String login, String motDePasse) {
        String sql = "SELECT id_utilisateur, login, mot_de_passe, nom, prenom, role, actif FROM utilisateur WHERE login = ?";

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, login);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                if (rs.getInt("actif") != 1) return null;

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

    public List<Utilisateur> findAll() {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT id_utilisateur, login, nom, prenom, role, actif FROM utilisateur ORDER BY id_utilisateur DESC";

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("login"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        Role.valueOf(rs.getString("role")),
                        rs.getInt("actif") == 1
                );
                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(Utilisateur u) {
        String sql = "INSERT INTO utilisateur(login, mot_de_passe, nom, prenom, role, actif) VALUES(?, ?, ?, ?, ?, 1)";

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, u.getLogin());
            ps.setString(2, u.getMotDePasse());
            ps.setString(3, u.getNom());
            ps.setString(4, u.getPrenom());
            ps.setString(5, u.getRole().name());

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Utilisateur u) {
        String sql = "UPDATE utilisateur SET login=?, nom=?, prenom=?, role=?, actif=? WHERE id_utilisateur=?";

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, u.getLogin());
            ps.setString(2, u.getNom());
            ps.setString(3, u.getPrenom());
            ps.setString(4, u.getRole().name());
            ps.setInt(5, u.isActif() ? 1 : 0);
            ps.setInt(6, u.getId());

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int idUtilisateur) {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur=?";

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean toggleActif(int idUtilisateur, boolean actif) {
        String sql = "UPDATE utilisateur SET actif=? WHERE id_utilisateur=?";

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, actif ? 1 : 0);
            ps.setInt(2, idUtilisateur);
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}