package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Aeroport;
import util.DatabaseConnection;

/**
 * DAO for Aeroport with CRUD operations
 */
public class AeroportDAO {
    
    /**
     * Get all aeroports
     */
    public List<Aeroport> getAll() {
        return search(null);
    }
    
    /**
     * Search aeroports by code, nom, ville or pays
     */
    public List<Aeroport> search(String keyword) {
        List<Aeroport> list = new ArrayList<>();
        String sql;
        
        if (keyword == null || keyword.trim().isEmpty()) {
            sql = "SELECT id, code_aeroport, nom, ville, pays, created_at, updated_at FROM aeroport ORDER BY nom";
        } else {
            sql = "SELECT id, code_aeroport, nom, ville, pays, created_at, updated_at FROM aeroport " +
                  "WHERE LOWER(code_aeroport) LIKE ? OR LOWER(nom) LIKE ? OR LOWER(ville) LIKE ? OR LOWER(pays) LIKE ? ORDER BY nom";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
                stmt.setString(3, pattern);
                stmt.setString(4, pattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Aeroport a = new Aeroport();
                a.setId(rs.getInt("id"));
                a.setCodeAeroport(rs.getString("code_aeroport"));
                a.setNom(rs.getString("nom"));
                a.setVille(rs.getString("ville"));
                a.setPays(rs.getString("pays"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                a.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(a);
            }
            
        } catch (SQLException e) {
            System.err.println("Error in AeroportDAO.search(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get one aeroport by ID
     */
    public Aeroport getById(int id) {
        String sql = "SELECT id, code_aeroport, nom, ville, pays, created_at, updated_at FROM aeroport WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Aeroport a = new Aeroport();
                a.setId(rs.getInt("id"));
                a.setCodeAeroport(rs.getString("code_aeroport"));
                a.setNom(rs.getString("nom"));
                a.setVille(rs.getString("ville"));
                a.setPays(rs.getString("pays"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                a.setUpdatedAt(rs.getTimestamp("updated_at"));
                return a;
            }
            
        } catch (SQLException e) {
            System.err.println("Error in AeroportDAO.getById(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Insert new aeroport
     */
    public boolean insert(Aeroport a) {
        String sql = "INSERT INTO aeroport (code_aeroport, nom, ville, pays) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, a.getCodeAeroport());
            stmt.setString(2, a.getNom());
            stmt.setString(3, a.getVille());
            stmt.setString(4, a.getPays());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in AeroportDAO.insert(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update existing aeroport
     */
    public boolean update(Aeroport a) {
        String sql = "UPDATE aeroport SET code_aeroport = ?, nom = ?, ville = ?, pays = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, a.getCodeAeroport());
            stmt.setString(2, a.getNom());
            stmt.setString(3, a.getVille());
            stmt.setString(4, a.getPays());
            stmt.setInt(5, a.getId());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in AeroportDAO.update(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete aeroport by ID
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM aeroport WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in AeroportDAO.delete(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}
