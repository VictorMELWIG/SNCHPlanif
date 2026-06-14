package snchbilletterie.dao;

import snchbilletterie.model.Arret;
import snchbilletterie.model.Itineraire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItineraireDAO {

    public List<Itineraire> findAll() {
        String sql = """
            SELECT i.id_itineraire, i.duree_prevue_min,
                   ad.id_arret AS id_dep, ad.nom AS nom_dep,
                   aa.id_arret AS id_arr, aa.nom AS nom_arr
            FROM itineraire i
            JOIN arret ad ON ad.id_arret = i.id_arret_depart
            JOIN arret aa ON aa.id_arret = i.id_arret_arrive
            ORDER BY i.id_itineraire DESC
        """;
        List<Itineraire> list = new ArrayList<>();

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Arret dep = new Arret(rs.getInt("id_dep"), rs.getString("nom_dep"));
                Arret arr = new Arret(rs.getInt("id_arr"), rs.getString("nom_arr"));
                list.add(new Itineraire(rs.getInt("id_itineraire"), rs.getInt("duree_prevue_min"), dep, arr));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Arret> findAllArrets() {
        List<Arret> list = new ArrayList<>();
        String sql = "SELECT id_arret, nom FROM arret ORDER BY nom";

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Arret(rs.getInt("id_arret"), rs.getString("nom")));
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
        String sql = "INSERT INTO itineraire(duree_prevue_min, id_arret_depart, id_arret_arrive) VALUES(?, ?, ?)";

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, it.getDureePrevue());
            ps.setInt(2, it.getArretDepart().getIdArret());
            ps.setInt(3, it.getArretArrive().getIdArret());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean update(Itineraire it) {
        String sql = "UPDATE itineraire SET duree_prevue_min=?, id_arret_depart=?, id_arret_arrive=? WHERE id_itineraire=?";

        try (Connection cnx = DBConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, it.getDureePrevue());
            ps.setInt(2, it.getArretDepart().getIdArret());
            ps.setInt(3, it.getArretArrive().getIdArret());
            ps.setInt(4, it.getIdItineraire());

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
                    if (rs.next() && rs.getInt(1) > 0) return false;
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
}