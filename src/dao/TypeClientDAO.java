package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.TypeClient;
import util.DatabaseConnection;

public class TypeClientDAO {

    public List<TypeClient> getAll() {
        List<TypeClient> list = new ArrayList<>();
        String sql = "SELECT id, libelle, description, mode_calcul, created_at, updated_at " +
                     "FROM type_client ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TypeClient tc = new TypeClient();
                tc.setId(rs.getInt("id"));
                tc.setLibelle(rs.getString("libelle"));
                tc.setDescription(rs.getString("description"));
                tc.setModeCalcul(rs.getString("mode_calcul"));
                tc.setCreatedAt(rs.getTimestamp("created_at"));
                tc.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(tc);
            }
        } catch (SQLException e) {
            System.err.println("Error in TypeClientDAO.getAll(): " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public TypeClient getById(int id) {
        String sql = "SELECT id, libelle, description, mode_calcul, created_at, updated_at " +
                     "FROM type_client WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TypeClient tc = new TypeClient();
                tc.setId(rs.getInt("id"));
                tc.setLibelle(rs.getString("libelle"));
                tc.setDescription(rs.getString("description"));
                tc.setModeCalcul(rs.getString("mode_calcul"));
                tc.setCreatedAt(rs.getTimestamp("created_at"));
                tc.setUpdatedAt(rs.getTimestamp("updated_at"));
                return tc;
            }
        } catch (SQLException e) {
            System.err.println("Error in TypeClientDAO.getById(): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(TypeClient tc) {
        String sql = "INSERT INTO type_client (libelle, description, mode_calcul) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tc.getLibelle());
            stmt.setString(2, tc.getDescription());
            stmt.setString(3, tc.getModeCalcul() != null ? tc.getModeCalcul() : "FIXE");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error in TypeClientDAO.insert(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(TypeClient tc) {
        String sql = "UPDATE type_client SET libelle = ?, description = ?, mode_calcul = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tc.getLibelle());
            stmt.setString(2, tc.getDescription());
            stmt.setString(3, tc.getModeCalcul());
            stmt.setInt(4, tc.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error in TypeClientDAO.update(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM type_client WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error in TypeClientDAO.delete(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
