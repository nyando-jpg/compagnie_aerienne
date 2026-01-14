package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Client;
import util.DatabaseConnection;

/**
 * DAO for Client with CRUD operations
 */
public class ClientDAO {
    
    /**
     * Get all clients
     */
    public List<Client> getAll() {
        return search(null);
    }
    
    /**
     * Search clients by nom, prenom or email
     */
    public List<Client> search(String keyword) {
        List<Client> list = new ArrayList<>();
        String sql;
        
        if (keyword == null || keyword.trim().isEmpty()) {
            sql = "SELECT id, nom, prenom, email, telephone, created_at, updated_at FROM client ORDER BY nom, prenom";
        } else {
            sql = "SELECT id, nom, prenom, email, telephone, created_at, updated_at FROM client " +
                  "WHERE LOWER(nom) LIKE ? OR LOWER(prenom) LIKE ? OR LOWER(email) LIKE ? ORDER BY nom, prenom";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
                stmt.setString(3, pattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setEmail(rs.getString("email"));
                c.setTelephone(rs.getString("telephone"));
                c.setCreatedAt(rs.getTimestamp("created_at"));
                c.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(c);
            }
            
        } catch (SQLException e) {
            System.err.println("Error in ClientDAO.search(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get one client by ID
     */
    public Client getById(int id) {
        String sql = "SELECT id, nom, prenom, email, telephone, created_at, updated_at FROM client WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setEmail(rs.getString("email"));
                c.setTelephone(rs.getString("telephone"));
                c.setCreatedAt(rs.getTimestamp("created_at"));
                c.setUpdatedAt(rs.getTimestamp("updated_at"));
                return c;
            }
            
        } catch (SQLException e) {
            System.err.println("Error in ClientDAO.getById(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Insert new client
     */
    public boolean insert(Client c) {
        String sql = "INSERT INTO client (nom, prenom, email, telephone) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getPrenom());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getTelephone());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in ClientDAO.insert(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update existing client
     */
    public boolean update(Client c) {
        String sql = "UPDATE client SET nom = ?, prenom = ?, email = ?, telephone = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getPrenom());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getTelephone());
            stmt.setInt(5, c.getId());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in ClientDAO.update(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete client by ID
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM client WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in ClientDAO.delete(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}
