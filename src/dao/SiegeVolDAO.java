package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.SiegeVol;
import util.DatabaseConnection;

public class SiegeVolDAO {

    public List<SiegeVol> getSiegesDisponibles(int volOpereId) {
        List<SiegeVol> sieges = new ArrayList<>();
        // Récupérer tous les sièges de l'avion qui NE SONT PAS réservés dans siege_vol
        String sql = "SELECT s.id as siege_id, s.numero_siege, cs.libelle as classe_libelle, s.classe_siege_id " +
                     "FROM siege s " +
                     "INNER JOIN classe_siege cs ON s.classe_siege_id = cs.id " +
                     "WHERE s.avion_id = (SELECT avion_id FROM vol_opere WHERE id = ?) " +
                     "  AND s.id NOT IN ( " +
                     "    SELECT siege_id FROM siege_vol " +
                     "    WHERE vol_opere_id = ? AND statut = 'RESERVE' " +
                     "  ) " +
                     "ORDER BY s.numero_siege";
        
        System.out.println("[DEBUG] SiegeVolDAO.getSiegesDisponibles() - Recherche sièges pour vol_opere_id=" + volOpereId);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volOpereId);
            stmt.setInt(2, volOpereId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                SiegeVol sv = new SiegeVol();
                sv.setId(0); // Pas encore dans siege_vol
                sv.setVolOpereId(volOpereId);
                sv.setSiegeId(rs.getInt("siege_id"));
                sv.setStatut("LIBRE");
                sv.setNumeroSiege(rs.getString("numero_siege"));
                sv.setClasseLibelle(rs.getString("classe_libelle"));
                sv.setClasseSiegeId(rs.getInt("classe_siege_id"));
                sieges.add(sv);
            }
            
            System.out.println("[DEBUG] " + sieges.size() + " sièges disponibles trouvés");
        } catch (SQLException e) {
            System.err.println("[ERROR] SQL Exception in getSiegesDisponibles: " + e.getMessage());
            e.printStackTrace();
        }
        return sieges;
    }

    // Récupérer infos d'un siège depuis la table siege (pas encore dans siege_vol)
    public SiegeVol getSiegeInfoById(int siegeId) {
        String sql = "SELECT s.id as siege_id, s.numero_siege, cs.libelle as classe_libelle, s.classe_siege_id " +
                     "FROM siege s " +
                     "INNER JOIN classe_siege cs ON s.classe_siege_id = cs.id " +
                     "WHERE s.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, siegeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                SiegeVol sv = new SiegeVol();
                sv.setId(0); // Pas encore dans siege_vol
                sv.setSiegeId(rs.getInt("siege_id"));
                sv.setNumeroSiege(rs.getString("numero_siege"));
                sv.setClasseLibelle(rs.getString("classe_libelle"));
                sv.setClasseSiegeId(rs.getInt("classe_siege_id"));
                return sv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SiegeVol getById(int id) {
        String sql = "SELECT sv.id, sv.vol_opere_id, sv.siege_id, sv.statut, " +
                     "s.numero_siege, cs.libelle as classe_libelle, s.classe_siege_id " +
                     "FROM siege_vol sv " +
                     "INNER JOIN siege s ON sv.siege_id = s.id " +
                     "INNER JOIN classe_siege cs ON s.classe_siege_id = cs.id " +
                     "WHERE sv.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                SiegeVol sv = new SiegeVol();
                sv.setId(rs.getInt("id"));
                sv.setVolOpereId(rs.getInt("vol_opere_id"));
                sv.setSiegeId(rs.getInt("siege_id"));
                sv.setStatut(rs.getString("statut"));
                sv.setNumeroSiege(rs.getString("numero_siege"));
                sv.setClasseLibelle(rs.getString("classe_libelle"));
                sv.setClasseSiegeId(rs.getInt("classe_siege_id"));
                return sv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Créer un nouveau siege_vol et retourner son ID
    public int insertSiegeVol(int volOpereId, int siegeId, String statut) {
        String sql = "INSERT INTO siege_vol (vol_opere_id, siege_id, statut) VALUES (?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volOpereId);
            stmt.setInt(2, siegeId);
            stmt.setString(3, statut);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Récupérer siege_vol par vol_opere_id et siege_id
    public SiegeVol getBySiegeId(int volOpereId, int siegeId) {
        String sql = "SELECT sv.id, sv.vol_opere_id, sv.siege_id, sv.statut, " +
                     "s.numero_siege, cs.libelle as classe_libelle, s.classe_siege_id " +
                     "FROM siege_vol sv " +
                     "INNER JOIN siege s ON sv.siege_id = s.id " +
                     "INNER JOIN classe_siege cs ON s.classe_siege_id = cs.id " +
                     "WHERE sv.vol_opere_id = ? AND sv.siege_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volOpereId);
            stmt.setInt(2, siegeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                SiegeVol sv = new SiegeVol();
                sv.setId(rs.getInt("id"));
                sv.setVolOpereId(rs.getInt("vol_opere_id"));
                sv.setSiegeId(rs.getInt("siege_id"));
                sv.setStatut(rs.getString("statut"));
                sv.setNumeroSiege(rs.getString("numero_siege"));
                sv.setClasseLibelle(rs.getString("classe_libelle"));
                sv.setClasseSiegeId(rs.getInt("classe_siege_id"));
                return sv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateStatut(int id, String statut) {
        String sql = "UPDATE siege_vol SET statut = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, statut);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
