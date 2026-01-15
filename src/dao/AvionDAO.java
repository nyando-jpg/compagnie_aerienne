package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Avion;
import util.DatabaseConnection;

/**
 * DAO for Avion with CRUD operations
 */
public class AvionDAO {
    
    /**
     * Get all avions with details (JOIN with model_avion and etat_avion)
     */
    public List<Avion> getAll() {
        List<Avion> list = new ArrayList<>();
        String sql = "SELECT a.id, a.code_avion, a.model_avion_id, a.etat_avion_id, a.capacite_totale, " +
                     "a.created_at, a.updated_at, " +
                     "m.designation as designation_model, e.libelle as libelle_etat " +
                     "FROM avion a " +
                     "LEFT JOIN model_avion m ON a.model_avion_id = m.id " +
                     "LEFT JOIN etat_avion e ON a.etat_avion_id = e.id " +
                     "ORDER BY a.code_avion";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToAvion(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error in AvionDAO.getAll(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get one avion by ID
     */
    public Avion getById(int id) {
        String sql = "SELECT a.id, a.code_avion, a.model_avion_id, a.etat_avion_id, a.capacite_totale, " +
                     "a.created_at, a.updated_at, " +
                     "m.designation as designation_model, e.libelle as libelle_etat " +
                     "FROM avion a " +
                     "LEFT JOIN model_avion m ON a.model_avion_id = m.id " +
                     "LEFT JOIN etat_avion e ON a.etat_avion_id = e.id " +
                     "WHERE a.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAvion(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error in AvionDAO.getById(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Insert a new avion and return the generated ID
     * @return the ID of the inserted avion, or -1 if failed
     */
    public int insert(Avion avion) {
        String sql = "INSERT INTO avion (code_avion, model_avion_id, etat_avion_id, capacite_totale) " +
                     "VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, avion.getCodeAvion());
            stmt.setInt(2, avion.getModelAvionId());
            stmt.setInt(3, avion.getEtatAvionId());
            stmt.setInt(4, avion.getCapaciteTotale());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            
        } catch (SQLException e) {
            System.err.println("Error in AvionDAO.insert(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Update an existing avion
     */
    public boolean update(Avion avion) {
        String sql = "UPDATE avion SET code_avion = ?, model_avion_id = ?, " +
                     "etat_avion_id = ?, capacite_totale = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, avion.getCodeAvion());
            stmt.setInt(2, avion.getModelAvionId());
            stmt.setInt(3, avion.getEtatAvionId());
            stmt.setInt(4, avion.getCapaciteTotale());
            stmt.setInt(5, avion.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in AvionDAO.update(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete an avion by ID
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM avion WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in AvionDAO.delete(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Map ResultSet to Avion object
     */
    private Avion mapResultSetToAvion(ResultSet rs) throws SQLException {
        Avion avion = new Avion();
        avion.setId(rs.getInt("id"));
        avion.setCodeAvion(rs.getString("code_avion"));
        avion.setModelAvionId(rs.getInt("model_avion_id"));
        avion.setEtatAvionId(rs.getInt("etat_avion_id"));
        avion.setCapaciteTotale(rs.getInt("capacite_totale"));
        avion.setCreatedAt(rs.getTimestamp("created_at"));
        avion.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Fields from JOIN (can be null)
        try {
            avion.setDesignationModel(rs.getString("designation_model"));
            avion.setLibelleEtat(rs.getString("libelle_etat"));
        } catch (SQLException e) {
            // JOIN fields not present in query
        }
        
        return avion;
    }
}
