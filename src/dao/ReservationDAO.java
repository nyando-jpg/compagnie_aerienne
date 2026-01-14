package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Reservation;
import util.DatabaseConnection;

/**
 * DAO for Reservation with CRUD operations
 */
public class ReservationDAO {
    
    /**
     * Get all reservations with details (JOIN)
     */
    public List<Reservation> getAll() {
        return search(null, null, null, null, null);
    }
    
    /**
     * Search reservations with filters
     */
    public List<Reservation> search(String keyword, String statut, Integer ligneVolId, java.sql.Date dateMin, java.sql.Date dateMax) {
        List<Reservation> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT r.id, r.client_id, r.vol_opere_id, r.date_reservation, r.statut, ");
        sql.append("c.nom as client_nom, c.prenom as client_prenom, ");
        sql.append("lv.numero_vol, ad.nom as aeroport_depart, aa.nom as aeroport_arrivee, vo.date_heure_depart, ");
        sql.append("s.numero_siege ");
        sql.append("FROM reservation r ");
        sql.append("JOIN client c ON r.client_id = c.id ");
        sql.append("JOIN vol_opere vo ON r.vol_opere_id = vo.id ");
        sql.append("JOIN ligne_vol lv ON vo.ligne_vol_id = lv.id ");
        sql.append("LEFT JOIN aeroport ad ON lv.aeroport_depart_id = ad.id ");
        sql.append("LEFT JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id ");
        sql.append("LEFT JOIN billet b ON r.id = b.reservation_id ");
        sql.append("LEFT JOIN siege_vol sv ON b.siege_vol_id = sv.id ");
        sql.append("LEFT JOIN siege s ON sv.siege_id = s.id ");
        
        List<String> conditions = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            conditions.add("(LOWER(c.nom) LIKE ? OR LOWER(c.prenom) LIKE ? OR LOWER(lv.numero_vol) LIKE ?)");
        }
        if (statut != null && !statut.trim().isEmpty()) {
            conditions.add("r.statut = ?::statut_reservation_enum");
        }
        if (ligneVolId != null) {
            conditions.add("vo.ligne_vol_id = ?");
        }
        if (dateMin != null) {
            conditions.add("DATE(vo.date_heure_depart) >= ?");
        }
        if (dateMax != null) {
            conditions.add("DATE(vo.date_heure_depart) <= ?");
        }
        
        if (!conditions.isEmpty()) {
            sql.append("WHERE ").append(String.join(" AND ", conditions)).append(" ");
        }
        
        sql.append("ORDER BY r.date_reservation DESC");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                stmt.setString(paramIndex++, pattern);
                stmt.setString(paramIndex++, pattern);
                stmt.setString(paramIndex++, pattern);
            }
            if (statut != null && !statut.trim().isEmpty()) {
                stmt.setString(paramIndex++, statut);
            }
            if (ligneVolId != null) {
                stmt.setInt(paramIndex++, ligneVolId);
            }
            if (dateMin != null) {
                stmt.setDate(paramIndex++, dateMin);
            }
            if (dateMax != null) {
                stmt.setDate(paramIndex++, dateMax);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSetToReservation(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error in ReservationDAO.search(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get one reservation by ID
     */
    public Reservation getById(int id) {
        String sql = "SELECT r.id, r.client_id, r.vol_opere_id, r.date_reservation, r.statut, " +
                     "c.nom as client_nom, c.prenom as client_prenom, " +
                     "lv.numero_vol, ad.nom as aeroport_depart, aa.nom as aeroport_arrivee, vo.date_heure_depart, " +
                     "s.numero_siege " +
                     "FROM reservation r " +
                     "JOIN client c ON r.client_id = c.id " +
                     "JOIN vol_opere vo ON r.vol_opere_id = vo.id " +
                     "JOIN ligne_vol lv ON vo.ligne_vol_id = lv.id " +
                     "LEFT JOIN aeroport ad ON lv.aeroport_depart_id = ad.id " +
                     "LEFT JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id " +
                     "LEFT JOIN billet b ON r.id = b.reservation_id " +
                     "LEFT JOIN siege_vol sv ON b.siege_vol_id = sv.id " +
                     "LEFT JOIN siege s ON sv.siege_id = s.id " +
                     "WHERE r.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error in ReservationDAO.getById(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Insert new reservation
     */
    public boolean insert(Reservation r) {
        String sql = "INSERT INTO reservation (client_id, vol_opere_id, statut) " +
                     "VALUES (?, ?, ?::statut_reservation_enum)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, r.getClientId());
            stmt.setInt(2, r.getVolOpereId());
            stmt.setString(3, r.getStatut() != null ? r.getStatut() : "EN_ATTENTE");
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in ReservationDAO.insert(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Insert new reservation and return ID
     */
    public int insertAndGetId(Reservation r) {
        String sql = "INSERT INTO reservation (client_id, vol_opere_id, statut) " +
                     "VALUES (?, ?, ?::statut_reservation_enum) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, r.getClientId());
            stmt.setInt(2, r.getVolOpereId());
            stmt.setString(3, r.getStatut() != null ? r.getStatut() : "EN_ATTENTE");
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            
        } catch (SQLException e) {
            System.err.println("Error in ReservationDAO.insertAndGetId(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Update existing reservation
     */
    public boolean update(Reservation r) {
        String sql = "UPDATE reservation SET client_id = ?, vol_opere_id = ?, " +
                     "statut = ?::statut_reservation_enum, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, r.getClientId());
            stmt.setInt(2, r.getVolOpereId());
            stmt.setString(3, r.getStatut());
            stmt.setInt(4, r.getId());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in ReservationDAO.update(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete reservation by ID
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in ReservationDAO.delete(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Helper method to map ResultSet to Reservation
     */
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setId(rs.getInt("id"));
        r.setClientId(rs.getInt("client_id"));
        r.setVolOpereId(rs.getInt("vol_opere_id"));
        r.setDateReservation(rs.getTimestamp("date_reservation"));
        r.setStatut(rs.getString("statut"));
        r.setClientNom(rs.getString("client_nom"));
        r.setClientPrenom(rs.getString("client_prenom"));
        r.setNumeroVol(rs.getString("numero_vol"));
        r.setAeroportDepart(rs.getString("aeroport_depart"));
        r.setAeroportArrivee(rs.getString("aeroport_arrivee"));
        r.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
        r.setNumeroSiege(rs.getString("numero_siege"));
        return r;
    }
}
