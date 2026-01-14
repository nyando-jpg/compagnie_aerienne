package dao;

import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO for StatusVol
 */
public class StatusVolDAO {
    
    /**
     * Get all status
     */
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT id, libelle, description FROM status_vol ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> status = new HashMap<>();
                status.put("id", rs.getInt("id"));
                status.put("libelle", rs.getString("libelle"));
                status.put("description", rs.getString("description"));
                list.add(status);
            }
            
        } catch (SQLException e) {
            System.err.println("Error in StatusVolDAO.getAll(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get status ID by libelle
     */
    public int getIdByLibelle(String libelle) {
        String sql = "SELECT id FROM status_vol WHERE libelle = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, libelle);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
            
        } catch (SQLException e) {
            System.err.println("Error in StatusVolDAO.getIdByLibelle(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return 1; // Default PLANIFIE
    }
}
