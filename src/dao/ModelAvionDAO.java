package dao;

import model.ModelAvion;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: ModelAvion
 */
public class ModelAvionDAO {
    
    /**
     * Get all model avions
     */
    public List<ModelAvion> getAll() throws SQLException {
        List<ModelAvion> models = new ArrayList<>();
        String sql = "SELECT * FROM model_avion ORDER BY designation";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                models.add(mapResultSetToModelAvion(rs));
            }
        }
        
        return models;
    }
    
    /**
     * Get model avion by ID
     */
    public ModelAvion getById(int id) throws SQLException {
        String sql = "SELECT * FROM model_avion WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToModelAvion(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Insert a new model avion
     */
    public boolean insert(ModelAvion model) throws SQLException {
        String sql = "INSERT INTO model_avion (designation, fabricant, capacite, autonomie_km, vitesse_km_h, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, model.getDesignation());
            stmt.setString(2, model.getFabricant());
            stmt.setInt(3, model.getCapacite());
            
            if (model.getAutonomieKm() != null) {
                stmt.setInt(4, model.getAutonomieKm());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            if (model.getVitesseKmH() != null) {
                stmt.setInt(5, model.getVitesseKmH());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            stmt.setString(6, model.getDescription());
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
    }
    
    /**
     * Update an existing model avion
     */
    public boolean update(ModelAvion model) throws SQLException {
        String sql = "UPDATE model_avion SET designation = ?, fabricant = ?, capacite = ?, " +
                     "autonomie_km = ?, vitesse_km_h = ?, description = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, model.getDesignation());
            stmt.setString(2, model.getFabricant());
            stmt.setInt(3, model.getCapacite());
            
            if (model.getAutonomieKm() != null) {
                stmt.setInt(4, model.getAutonomieKm());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            if (model.getVitesseKmH() != null) {
                stmt.setInt(5, model.getVitesseKmH());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            stmt.setString(6, model.getDescription());
            stmt.setInt(7, model.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }
    
    /**
     * Delete a model avion
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM model_avion WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }
    
    /**
     * Map ResultSet to ModelAvion object
     */
    private ModelAvion mapResultSetToModelAvion(ResultSet rs) throws SQLException {
        ModelAvion model = new ModelAvion();
        model.setId(rs.getInt("id"));
        model.setDesignation(rs.getString("designation"));
        model.setFabricant(rs.getString("fabricant"));
        model.setCapacite(rs.getInt("capacite"));
        
        int autonomie = rs.getInt("autonomie_km");
        if (!rs.wasNull()) {
            model.setAutonomieKm(autonomie);
        }
        
        int vitesse = rs.getInt("vitesse_km_h");
        if (!rs.wasNull()) {
            model.setVitesseKmH(vitesse);
        }
        
        model.setDescription(rs.getString("description"));
        model.setCreatedAt(rs.getTimestamp("created_at"));
        model.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return model;
    }
}
