package dao;

import model.LigneVol;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for LigneVol with CRUD operations
 */
public class LigneVolDAO {
    
    /**
     * Get all lignes_vol with aeroport names (JOIN)
     */
    public List<LigneVol> getAll() {
        return search(null);
    }
    
    /**
     * Search lignes_vol by numero_vol or aeroport names
     */
    public List<LigneVol> search(String keyword) {
        List<LigneVol> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT lv.id, lv.numero_vol, lv.aeroport_depart_id, lv.aeroport_arrivee_id, ");
        sql.append("lv.duree_estimee_minutes, lv.distance_km, lv.description, ");
        sql.append("ad.nom as depart_nom, aa.nom as arrivee_nom ");
        sql.append("FROM ligne_vol lv ");
        sql.append("LEFT JOIN aeroport ad ON lv.aeroport_depart_id = ad.id ");
        sql.append("LEFT JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id ");
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("WHERE LOWER(lv.numero_vol) LIKE ? OR LOWER(ad.nom) LIKE ? OR LOWER(aa.nom) LIKE ? ");
        }
        
        sql.append("ORDER BY lv.numero_vol");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
                stmt.setString(3, pattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                LigneVol lv = new LigneVol();
                lv.setId(rs.getInt("id"));
                lv.setNumeroVol(rs.getString("numero_vol"));
                lv.setAeroportDepartId(rs.getInt("aeroport_depart_id"));
                lv.setAeroportArriveeId(rs.getInt("aeroport_arrivee_id"));
                lv.setDureeEstimeeMinutes(rs.getInt("duree_estimee_minutes"));
                lv.setDistanceKm(rs.getObject("distance_km", Integer.class));
                lv.setDescription(rs.getString("description"));
                lv.setAeroportDepartNom(rs.getString("depart_nom"));
                lv.setAeroportArriveeNom(rs.getString("arrivee_nom"));
                list.add(lv);
            }
            
        } catch (SQLException e) {
            System.err.println("Error in LigneVolDAO.search(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get one ligne_vol by ID
     */
    public LigneVol getById(int id) {
        String sql = "SELECT id, numero_vol, aeroport_depart_id, aeroport_arrivee_id, " +
                     "duree_estimee_minutes, distance_km, description " +
                     "FROM ligne_vol WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                LigneVol lv = new LigneVol();
                lv.setId(rs.getInt("id"));
                lv.setNumeroVol(rs.getString("numero_vol"));
                lv.setAeroportDepartId(rs.getInt("aeroport_depart_id"));
                lv.setAeroportArriveeId(rs.getInt("aeroport_arrivee_id"));
                lv.setDureeEstimeeMinutes(rs.getInt("duree_estimee_minutes"));
                lv.setDistanceKm(rs.getObject("distance_km", Integer.class));
                lv.setDescription(rs.getString("description"));
                return lv;
            }
            
        } catch (SQLException e) {
            System.err.println("Error in LigneVolDAO.getById(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Insert new ligne_vol
     */
    public boolean insert(LigneVol lv) {
        String sql = "INSERT INTO ligne_vol (numero_vol, aeroport_depart_id, aeroport_arrivee_id, " +
                     "duree_estimee_minutes, distance_km, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, lv.getNumeroVol());
            stmt.setInt(2, lv.getAeroportDepartId());
            stmt.setInt(3, lv.getAeroportArriveeId());
            stmt.setInt(4, lv.getDureeEstimeeMinutes());
            if (lv.getDistanceKm() != null) {
                stmt.setInt(5, lv.getDistanceKm());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setString(6, lv.getDescription());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in LigneVolDAO.insert(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update existing ligne_vol
     */
    public boolean update(LigneVol lv) {
        String sql = "UPDATE ligne_vol SET numero_vol = ?, aeroport_depart_id = ?, " +
                     "aeroport_arrivee_id = ?, duree_estimee_minutes = ?, distance_km = ?, " +
                     "description = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, lv.getNumeroVol());
            stmt.setInt(2, lv.getAeroportDepartId());
            stmt.setInt(3, lv.getAeroportArriveeId());
            stmt.setInt(4, lv.getDureeEstimeeMinutes());
            if (lv.getDistanceKm() != null) {
                stmt.setInt(5, lv.getDistanceKm());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setString(6, lv.getDescription());
            stmt.setInt(7, lv.getId());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in LigneVolDAO.update(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete ligne_vol by ID
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM ligne_vol WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in LigneVolDAO.delete(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}
