package snchbilletterie.dao;

import snchbilletterie.model.Train;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainDAO {

    public List<Train> findAll() {
        List<Train> list = new ArrayList<>();
        String sql = "SELECT id_train, numero_train, type_train FROM train ORDER BY id_train DESC";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Train(
                        rs.getInt("id_train"),
                        rs.getString("numero_train"),
                        rs.getString("type_train")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Train t) {
        String sql = "INSERT INTO train(numero_train, type_train) VALUES(?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, t.getNumeroTrain());
            ps.setString(2, t.getTypeTrain());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Train t) {
        String sql = "UPDATE train SET numero_train=?, type_train=? WHERE id_train=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, t.getNumeroTrain());
            ps.setString(2, t.getTypeTrain());
            ps.setInt(3, t.getIdTrain());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int idTrain) {
        String sql = "DELETE FROM train WHERE id_train=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idTrain);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
