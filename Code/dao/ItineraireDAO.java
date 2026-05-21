package snchbilletterie.dao;

import snchbilletterie.model.Itineraire;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItineraireDAO {

    private static final Map<Integer, String[]> villesParItineraire = new HashMap<>();

    public static void enregistrerVilles(int idItineraire, String depart, String arrivee) {
        villesParItineraire.put(idItineraire, new String[]{depart, arrivee});
    }

    public List<Itineraire> findAll() {
        String sql = "SELECT id_itineraire, duree_prevue FROM itineraire ORDER BY id_itineraire DESC";
        List<Itineraire> list = new ArrayList<>();

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Itineraire iti = new Itineraire(
                        rs.getInt("id_itineraire"),
                        rs.getInt("duree_prevue")
                );

                String[] villes = villesParItineraire.get(iti.getIdItineraire());
                if (villes != null) {
                    iti.setVilleDepart(villes[0]);
                    iti.setVilleArrivee(villes[1]);
                }

                list.add(iti);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(Itineraire it) {
        return insertAndReturnId(it) != -1;
    }

    public int insertAndReturnId(Itineraire it) {
        String sql = "INSERT INTO itineraire(duree_prevue) VALUES(?)";

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, it.getDureePrevue());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean update(Itineraire it) {
        String sql = "UPDATE itineraire SET duree_prevue=? WHERE id_itineraire=?";

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, it.getDureePrevue());
            ps.setInt(2, it.getIdItineraire());

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int idItineraire) {
        String checkSql = "SELECT COUNT(*) FROM trajet WHERE id_itineraire = ?";
        String deleteSql = "DELETE FROM itineraire WHERE id_itineraire = ?";

        try (Connection cnx = DBConnection.getConnection()) {

            try (PreparedStatement check = cnx.prepareStatement(checkSql)) {
                check.setInt(1, idItineraire);

                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }

            try (PreparedStatement ps = cnx.prepareStatement(deleteSql)) {
                ps.setInt(1, idItineraire);
                return ps.executeUpdate() == 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Integer findOrCreateArret(String nom) {
        String selectSql = "SELECT id_arret FROM arret WHERE nom = ?";
        String insertSql = "INSERT INTO arret(nom) VALUES(?)";

        try (Connection cnx = DBConnection.getConnection()) {

            try (PreparedStatement ps = cnx.prepareStatement(selectSql)) {
                ps.setString(1, nom.trim());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id_arret");
                    }
                }
            }

            try (PreparedStatement ps = cnx.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, nom.trim());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}