package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ClasseSiege;
import util.DatabaseConnection;

/**
 * DAO for ClasseSiege with CRUD operations
 */
public class ClasseSiegeDAO {
    
    /**
     * Get all classe_siege records
     */
    public List<ClasseSiege> getAll() {
        return search(null);
    }
    
    /**
     * Search classe_siege by libelle or description
     */
    public List<ClasseSiege> search(String keyword) {
        List<ClasseSiege> list = new ArrayList<>();
        String sql;
        
        if (keyword == null || keyword.trim().isEmpty()) {
            sql = "SELECT id, libelle, description, created_at, updated_at FROM classe_siege ORDER BY id";
        } else {
            sql = "SELECT id, libelle, description, created_at, updated_at FROM classe_siege " +
                  "WHERE LOWER(libelle) LIKE ? OR LOWER(description) LIKE ? ORDER BY id";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ClasseSiege cs = new ClasseSiege();
                cs.setId(rs.getInt("id"));
                cs.setLibelle(rs.getString("libelle"));
                cs.setDescription(rs.getString("description"));
                cs.setCreatedAt(rs.getTimestamp("created_at"));
                cs.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(cs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error in ClasseSiegeDAO.search(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get one classe_siege by ID
     */
    public ClasseSiege getById(int id) {
        String sql = "SELECT id, libelle, description, created_at, updated_at FROM classe_siege WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                ClasseSiege cs = new ClasseSiege();
                cs.setId(rs.getInt("id"));
                cs.setLibelle(rs.getString("libelle"));
                cs.setDescription(rs.getString("description"));
                cs.setCreatedAt(rs.getTimestamp("created_at"));
                cs.setUpdatedAt(rs.getTimestamp("updated_at"));
                return cs;
            }
            
        } catch (SQLException e) {
            System.err.println("Error in ClasseSiegeDAO.getById(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Insert new classe_siege
     */
    public boolean insert(ClasseSiege cs) {
        String sql = "INSERT INTO classe_siege (libelle, description) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cs.getLibelle());
            stmt.setString(2, cs.getDescription());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in ClasseSiegeDAO.insert(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update existing classe_siege
     */
    public boolean update(ClasseSiege cs) {
        String sql = "UPDATE classe_siege SET libelle = ?, description = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cs.getLibelle());
            stmt.setString(2, cs.getDescription());
            stmt.setInt(3, cs.getId());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in ClasseSiegeDAO.update(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete classe_siege by ID
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM classe_siege WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in ClasseSiegeDAO.delete(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}
