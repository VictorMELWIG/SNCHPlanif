package snchbilletterie.dao;

import snchbilletterie.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrajetDAO {

    public List<Trajet> findAll() {
        List<Trajet> list = new ArrayList<>();

        String sql = """
            SELECT 
                t.id_trajet,
                t.date_heure_depart,
                t.date_heure_arrivee,
                tr.id_train,
                tr.numero_train,
                tr.type_train,
                i.id_itineraire,
                i.duree_prevue,
                t.id_arret_depart,
                t.id_arret_arrive
            FROM trajet t
            JOIN train tr ON tr.id_train = t.id_train
            JOIN itineraire i ON i.id_itineraire = t.id_itineraire
            ORDER BY t.id_trajet DESC
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Train train = new Train(
                        rs.getInt("id_train"),
                        rs.getString("numero_train"),
                        rs.getString("type_train")
                );

                Itineraire iti = new Itineraire(
                        rs.getInt("id_itineraire"),
                        rs.getInt("duree_prevue")
                );

                LocalDateTime dep = rs.getTimestamp("date_heure_depart").toLocalDateTime();
                LocalDateTime arr = rs.getTimestamp("date_heure_arrivee").toLocalDateTime();

                Trajet trajet = new Trajet(
                        rs.getInt("id_trajet"),
                        dep.toLocalDate(),
                        dep.toLocalTime(),
                        arr.toLocalTime(),
                        train,
                        iti
                );

                trajet.setIdArretDepart(rs.getInt("id_arret_depart"));
                trajet.setIdArretArrive(rs.getInt("id_arret_arrive"));

                list.add(trajet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Trajet> search(LocalDate date, String depart, String arrivee) {
        List<Trajet> list = new ArrayList<>();

        String sql = """
            SELECT 
                t.id_trajet,
                t.date_heure_depart,
                t.date_heure_arrivee,
                tr.id_train,
                tr.numero_train,
                tr.type_train,
                i.id_itineraire,
                i.duree_prevue,
                t.id_arret_depart,
                t.id_arret_arrive
            FROM trajet t
            JOIN train tr ON tr.id_train = t.id_train
            JOIN itineraire i ON i.id_itineraire = t.id_itineraire
            JOIN arret ad ON ad.id_arret = t.id_arret_depart
            JOIN arret aa ON aa.id_arret = t.id_arret_arrive
            WHERE (? IS NULL OR DATE(t.date_heure_depart) = ?)
              AND (? IS NULL OR ad.nom LIKE ?)
              AND (? IS NULL OR aa.nom LIKE ?)
            ORDER BY t.date_heure_depart DESC
        """;

        String d = (depart == null || depart.isBlank()) ? null : depart.trim();
        String a = (arrivee == null || arrivee.isBlank()) ? null : arrivee.trim();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (date == null) {
                ps.setNull(1, Types.DATE);
                ps.setNull(2, Types.DATE);
            } else {
                ps.setDate(1, Date.valueOf(date));
                ps.setDate(2, Date.valueOf(date));
            }

            if (d == null) {
                ps.setNull(3, Types.VARCHAR);
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(3, d);
                ps.setString(4, "%" + d + "%");
            }

            if (a == null) {
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
            } else {
                ps.setString(5, a);
                ps.setString(6, "%" + a + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Train train = new Train(
                            rs.getInt("id_train"),
                            rs.getString("numero_train"),
                            rs.getString("type_train")
                    );

                    Itineraire iti = new Itineraire(
                            rs.getInt("id_itineraire"),
                            rs.getInt("duree_prevue")
                    );

                    LocalDateTime dep = rs.getTimestamp("date_heure_depart").toLocalDateTime();
                    LocalDateTime arr = rs.getTimestamp("date_heure_arrivee").toLocalDateTime();

                    Trajet trajet = new Trajet(
                            rs.getInt("id_trajet"),
                            dep.toLocalDate(),
                            dep.toLocalTime(),
                            arr.toLocalTime(),
                            train,
                            iti
                    );

                    trajet.setIdArretDepart(rs.getInt("id_arret_depart"));
                    trajet.setIdArretArrive(rs.getInt("id_arret_arrive"));

                    list.add(trajet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(Trajet t) {
        String sql = """
            INSERT INTO trajet(
                id_train,
                id_itineraire,
                id_arret_depart,
                id_arret_arrive,
                date_heure_depart,
                date_heure_arrivee
            )
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            LocalDateTime depart = LocalDateTime.of(t.getDateDepart(), t.getHeureDepart());
            LocalDateTime arrivee = LocalDateTime.of(t.getDateDepart(), t.getHeureArriveePrevue());

            ps.setInt(1, t.getTrain().getIdTrain());
            ps.setInt(2, t.getItineraire().getIdItineraire());
            ps.setInt(3, t.getIdArretDepart());
            ps.setInt(4, t.getIdArretArrive());
            ps.setTimestamp(5, Timestamp.valueOf(depart));
            ps.setTimestamp(6, Timestamp.valueOf(arrivee));

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Trajet t) {
        String sql = """
            UPDATE trajet
            SET id_train=?,
                id_itineraire=?,
                id_arret_depart=?,
                id_arret_arrive=?,
                date_heure_depart=?,
                date_heure_arrivee=?
            WHERE id_trajet=?
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            LocalDateTime depart = LocalDateTime.of(t.getDateDepart(), t.getHeureDepart());
            LocalDateTime arrivee = LocalDateTime.of(t.getDateDepart(), t.getHeureArriveePrevue());

            ps.setInt(1, t.getTrain().getIdTrain());
            ps.setInt(2, t.getItineraire().getIdItineraire());
            ps.setInt(3, t.getIdArretDepart());
            ps.setInt(4, t.getIdArretArrive());
            ps.setTimestamp(5, Timestamp.valueOf(depart));
            ps.setTimestamp(6, Timestamp.valueOf(arrivee));
            ps.setInt(7, t.getIdTrajet());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int idTrajet) {
        String sql = "DELETE FROM trajet WHERE id_trajet=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idTrajet);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Arret> findAllArrets() {
        List<Arret> list = new ArrayList<>();

        String sql = "SELECT id_arret, nom FROM arret ORDER BY nom";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Arret(
                        rs.getInt("id_arret"),
                        rs.getString("nom")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}