package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.VolOpere;
import util.DatabaseConnection;

/**
 * DAO for VolOpere with CRUD operations
 */
public class VolOpereDAO {
    
    /**
     * Get all vols_operes with details (JOIN)
     */
    public List<VolOpere> getAll() {
        return search(null, null, null, null, null, null);
    }
    
    /**
     * Get vols_operes by ligne_vol_id
     */
    public List<VolOpere> getByLigneVolId(int ligneVolId) {
        List<VolOpere> list = new ArrayList<>();
        String sql = "SELECT vo.id, vo.ligne_vol_id, vo.avion_id, vo.date_heure_depart, " +
                     "vo.date_heure_arrivee, sv.libelle as status, vo.retard_minutes, vo.motif_annulation, " +
                     "lv.numero_vol, ad.nom as depart_nom, aa.nom as arrivee_nom, av.code_avion " +
                     "FROM vol_opere vo " +
                     "JOIN ligne_vol lv ON vo.ligne_vol_id = lv.id " +
                     "LEFT JOIN status_vol sv ON vo.status_id = sv.id " +
                     "LEFT JOIN aeroport ad ON lv.aeroport_depart_id = ad.id " +
                     "LEFT JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id " +
                     "LEFT JOIN avion av ON vo.avion_id = av.id " +
                     "WHERE vo.ligne_vol_id = ? " +
                     "ORDER BY vo.date_heure_depart DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ligneVolId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSetToVolOpere(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error in VolOpereDAO.getByLigneVolId(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get vols_operes by aeroport depart and arrivee
     */
    public List<VolOpere> getByAeroports(int aeroportDepartId, int aeroportArriveeId) {
        return getByFilters(aeroportDepartId, aeroportArriveeId, null, null, null);
    }
    
    /**
     * Get vols_operes with multiple filters
     */
    public List<VolOpere> getByFilters(int aeroportDepartId, int aeroportArriveeId, Integer avionId, java.sql.Date dateDebut, java.sql.Date dateFin) {
        List<VolOpere> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT vo.id, vo.ligne_vol_id, vo.avion_id, vo.date_heure_depart, ");
        sql.append("vo.date_heure_arrivee, sv.libelle as status, vo.retard_minutes, vo.motif_annulation, ");
        sql.append("lv.numero_vol, ad.nom as depart_nom, aa.nom as arrivee_nom ");
        sql.append("FROM vol_opere vo ");
        sql.append("JOIN ligne_vol lv ON vo.ligne_vol_id = lv.id ");
        sql.append("LEFT JOIN status_vol sv ON vo.status_id = sv.id ");
        sql.append("LEFT JOIN aeroport ad ON lv.aeroport_depart_id = ad.id ");
        sql.append("LEFT JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id ");
        sql.append("WHERE lv.aeroport_depart_id = ? AND lv.aeroport_arrivee_id = ? ");
        sql.append("AND vo.date_heure_depart > CURRENT_TIMESTAMP ");
        
        if (avionId != null) {
            sql.append("AND vo.avion_id = ? ");
        }
        
        if (dateDebut != null) {
            sql.append("AND DATE(vo.date_heure_depart) >= ? ");
        }
        
        if (dateFin != null) {
            sql.append("AND DATE(vo.date_heure_depart) <= ? ");
        }
        
        sql.append("ORDER BY vo.date_heure_depart ASC");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            stmt.setInt(paramIndex++, aeroportDepartId);
            stmt.setInt(paramIndex++, aeroportArriveeId);
            
            if (avionId != null) {
                stmt.setInt(paramIndex++, avionId);
            }
            
            if (dateDebut != null) {
                stmt.setDate(paramIndex++, dateDebut);
            }
            
            if (dateFin != null) {
                stmt.setDate(paramIndex++, dateFin);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSetToVolOpere(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error in VolOpereDAO.getByFilters(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get vols_operes by ligne_vol with filters
     */
    public List<VolOpere> getByLigneVolFilters(Integer ligneVolId, Integer avionId, java.sql.Date dateDebut, java.sql.Date dateFin) {
        List<VolOpere> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT vo.id, vo.ligne_vol_id, vo.avion_id, vo.date_heure_depart, ");
        sql.append("vo.date_heure_arrivee, sv.libelle as status, vo.retard_minutes, vo.motif_annulation, ");
        sql.append("lv.numero_vol, ad.nom as depart_nom, aa.nom as arrivee_nom ");
        sql.append("FROM vol_opere vo ");
        sql.append("JOIN ligne_vol lv ON vo.ligne_vol_id = lv.id ");
        sql.append("LEFT JOIN status_vol sv ON vo.status_id = sv.id ");
        sql.append("LEFT JOIN aeroport ad ON lv.aeroport_depart_id = ad.id ");
        sql.append("LEFT JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id ");
        sql.append("WHERE vo.ligne_vol_id = ? ");
        // Commenté pour permettre les réservations sur vols passés (pour tests)
        // sql.append("AND vo.date_heure_depart > CURRENT_TIMESTAMP ");
        
        if (avionId != null) {
            sql.append("AND vo.avion_id = ? ");
        }
        
        if (dateDebut != null) {
            sql.append("AND DATE(vo.date_heure_depart) >= ? ");
        }
        
        if (dateFin != null) {
            sql.append("AND DATE(vo.date_heure_depart) <= ? ");
        }
        
        sql.append("ORDER BY vo.date_heure_depart ASC");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            stmt.setInt(paramIndex++, ligneVolId);
            
            if (avionId != null) {
                stmt.setInt(paramIndex++, avionId);
            }
            
            if (dateDebut != null) {
                stmt.setDate(paramIndex++, dateDebut);
            }
            
            if (dateFin != null) {
                stmt.setDate(paramIndex++, dateFin);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSetToVolOpere(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error in VolOpereDAO.getByLigneVolFilters(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Search vols_operes with filters
     */
    public List<VolOpere> search(String keyword, String status, Integer ligneVolId, Integer avionId, java.sql.Date dateDebut, java.sql.Date dateFin) {
        List<VolOpere> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT vo.id, vo.ligne_vol_id, vo.avion_id, vo.date_heure_depart, ");
        sql.append("vo.date_heure_arrivee, sv.libelle as status, vo.retard_minutes, vo.motif_annulation, ");
        sql.append("lv.numero_vol, ad.nom as depart_nom, aa.nom as arrivee_nom, av.code_avion ");
        sql.append("FROM vol_opere vo ");
        sql.append("JOIN ligne_vol lv ON vo.ligne_vol_id = lv.id ");
        sql.append("LEFT JOIN status_vol sv ON vo.status_id = sv.id ");
        sql.append("LEFT JOIN aeroport ad ON lv.aeroport_depart_id = ad.id ");
        sql.append("LEFT JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id ");
        sql.append("LEFT JOIN avion av ON vo.avion_id = av.id ");
        
        List<String> conditions = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            conditions.add("(LOWER(lv.numero_vol) LIKE ? OR LOWER(ad.nom) LIKE ? OR LOWER(aa.nom) LIKE ?)");
        }
        if (status != null && !status.trim().isEmpty()) {
            conditions.add("sv.libelle = ?");
        }
        if (ligneVolId != null) {
            conditions.add("vo.ligne_vol_id = ?");
        }
        if (avionId != null) {
            conditions.add("vo.avion_id = ?");
        }
        if (dateDebut != null) {
            conditions.add("DATE(vo.date_heure_depart) >= ?");
        }
        if (dateFin != null) {
            conditions.add("DATE(vo.date_heure_depart) <= ?");
        }
        
        if (!conditions.isEmpty()) {
            sql.append("WHERE ").append(String.join(" AND ", conditions)).append(" ");
        }
        
        sql.append("ORDER BY vo.date_heure_depart DESC");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                stmt.setString(paramIndex++, pattern);
                stmt.setString(paramIndex++, pattern);
                stmt.setString(paramIndex++, pattern);
            }
            if (status != null && !status.trim().isEmpty()) {
                stmt.setString(paramIndex++, status);
            }
            if (ligneVolId != null) {
                stmt.setInt(paramIndex++, ligneVolId);
            }
            if (avionId != null) {
                stmt.setInt(paramIndex++, avionId);
            }
            if (dateDebut != null) {
                stmt.setDate(paramIndex++, dateDebut);
            }
            if (dateFin != null) {
                stmt.setDate(paramIndex++, dateFin);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSetToVolOpere(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error in VolOpereDAO.search(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get one vol_opere by ID
     */
    public VolOpere getById(int id) {
        String sql = "SELECT vo.id, vo.ligne_vol_id, vo.avion_id, vo.date_heure_depart, vo.date_heure_arrivee, " +
                     "sv.libelle as status, vo.status_id, vo.retard_minutes, vo.motif_annulation " +
                     "FROM vol_opere vo " +
                     "LEFT JOIN status_vol sv ON vo.status_id = sv.id " +
                     "WHERE vo.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                VolOpere vo = new VolOpere();
                vo.setId(rs.getInt("id"));
                vo.setLigneVolId(rs.getInt("ligne_vol_id"));
                vo.setAvionId(rs.getInt("avion_id"));
                vo.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
                vo.setDateHeureArrivee(rs.getTimestamp("date_heure_arrivee"));
                vo.setStatus(rs.getString("status"));
                vo.setStatusId(rs.getInt("status_id"));
                vo.setRetardMinutes(rs.getInt("retard_minutes"));
                vo.setMotifAnnulation(rs.getString("motif_annulation"));
                return vo;
            }
            
        } catch (SQLException e) {
            System.err.println("Error in VolOpereDAO.getById(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Insert new vol_opere
     */
    public boolean insert(VolOpere vo) {
        String sql = "INSERT INTO vol_opere (ligne_vol_id, avion_id, date_heure_depart, " +
                     "date_heure_arrivee, status_id, retard_minutes) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, vo.getLigneVolId());
            stmt.setInt(2, vo.getAvionId());
            stmt.setTimestamp(3, vo.getDateHeureDepart());
            stmt.setTimestamp(4, vo.getDateHeureArrivee());
            stmt.setInt(5, vo.getStatusId() > 0 ? vo.getStatusId() : 1);
            stmt.setInt(6, vo.getRetardMinutes());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in VolOpereDAO.insert(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Insert new vol_opere and return generated ID
     */
    public int insertAndGetId(VolOpere vo) {
        String sql = "INSERT INTO vol_opere (ligne_vol_id, avion_id, date_heure_depart, " +
                     "date_heure_arrivee, status_id, retard_minutes) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, vo.getLigneVolId());
            stmt.setInt(2, vo.getAvionId());
            stmt.setTimestamp(3, vo.getDateHeureDepart());
            stmt.setTimestamp(4, vo.getDateHeureArrivee());
            stmt.setInt(5, vo.getStatusId() > 0 ? vo.getStatusId() : 1);
            stmt.setInt(6, vo.getRetardMinutes());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            
        } catch (SQLException e) {
            System.err.println("Error in VolOpereDAO.insertAndGetId(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Update existing vol_opere
     */
    public boolean update(VolOpere vo) {
        String sql = "UPDATE vol_opere SET ligne_vol_id = ?, avion_id = ?, " +
                     "date_heure_depart = ?, date_heure_arrivee = ?, status_id = ?, " +
                     "retard_minutes = ?, motif_annulation = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, vo.getLigneVolId());
            stmt.setInt(2, vo.getAvionId());
            stmt.setTimestamp(3, vo.getDateHeureDepart());
            stmt.setTimestamp(4, vo.getDateHeureArrivee());
            stmt.setInt(5, vo.getStatusId());
            stmt.setInt(6, vo.getRetardMinutes());
            stmt.setString(7, vo.getMotifAnnulation());
            stmt.setInt(8, vo.getId());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in VolOpereDAO.update(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete vol_opere by ID
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM vol_opere WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in VolOpereDAO.delete(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Helper method to map ResultSet to VolOpere
     */
    private VolOpere mapResultSetToVolOpere(ResultSet rs) throws SQLException {
        VolOpere vo = new VolOpere();
        vo.setId(rs.getInt("id"));
        vo.setLigneVolId(rs.getInt("ligne_vol_id"));
        vo.setAvionId(rs.getInt("avion_id"));
        vo.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
        vo.setDateHeureArrivee(rs.getTimestamp("date_heure_arrivee"));
        vo.setStatus(rs.getString("status"));
        vo.setRetardMinutes(rs.getInt("retard_minutes"));
        vo.setMotifAnnulation(rs.getString("motif_annulation"));
        vo.setNumeroVol(rs.getString("numero_vol"));
        vo.setAeroportDepartNom(rs.getString("depart_nom"));
        vo.setAeroportArriveeNom(rs.getString("arrivee_nom"));
        
        // code_avion est optionnel (si la requête JOIN avec avion)
        try {
            vo.setCodeAvion(rs.getString("code_avion"));
        } catch (SQLException e) {
            vo.setCodeAvion(null);
        }
        
        return vo;
    }
}
