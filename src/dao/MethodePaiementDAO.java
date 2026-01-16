package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.MethodePaiement;
import util.DatabaseConnection;

public class MethodePaiementDAO {

    public List<MethodePaiement> getAll() {
        List<MethodePaiement> list = new ArrayList<>();
        String sql = "SELECT id, libelle, description, created_at, updated_at FROM methode_paiement ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MethodePaiement m = new MethodePaiement();
                m.setId(rs.getInt("id"));
                m.setLibelle(rs.getString("libelle"));
                m.setDescription(rs.getString("description"));
                m.setCreatedAt(rs.getTimestamp("created_at"));
                m.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error in MethodePaiementDAO.getAll(): " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public MethodePaiement getById(int id) {
        String sql = "SELECT id, libelle, description, created_at, updated_at FROM methode_paiement WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                MethodePaiement m = new MethodePaiement();
                m.setId(rs.getInt("id"));
                m.setLibelle(rs.getString("libelle"));
                m.setDescription(rs.getString("description"));
                m.setCreatedAt(rs.getTimestamp("created_at"));
                m.setUpdatedAt(rs.getTimestamp("updated_at"));
                return m;
            }
        } catch (SQLException e) {
            System.err.println("Error in MethodePaiementDAO.getById(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean insert(MethodePaiement m) {
        String sql = "INSERT INTO methode_paiement (libelle, description) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, m.getLibelle());
            stmt.setString(2, m.getDescription());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in MethodePaiementDAO.insert(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(MethodePaiement m) {
        String sql = "UPDATE methode_paiement SET libelle = ?, description = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, m.getLibelle());
            stmt.setString(2, m.getDescription());
            stmt.setInt(3, m.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in MethodePaiementDAO.update(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM methode_paiement WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in MethodePaiementDAO.delete(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}