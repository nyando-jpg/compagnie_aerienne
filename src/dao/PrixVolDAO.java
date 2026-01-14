package dao;

import java.sql.*;
import java.util.Map;
import util.DatabaseConnection;

public class PrixVolDAO {

    public double getPrix(int volOpereId, int classeSiegeId) {
        String sql = "SELECT prix_base FROM prix_vol " +
                     "WHERE vol_opere_id = ? AND classe_siege_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volOpereId);
            stmt.setInt(2, classeSiegeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("prix_base");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    /**
     * Insérer les prix pour toutes les classes d'un vol opéré
     * @param volOpereId ID du vol opéré
     * @param prixParClasse Map avec classe_siege_id comme clé et prix comme valeur
     * @return nombre de prix insérés
     */
    public int insertPrixPourVol(int volOpereId, Map<Integer, Double> prixParClasse) {
        String sql = "INSERT INTO prix_vol (vol_opere_id, classe_siege_id, prix_base) VALUES (?, ?, ?)";
        int count = 0;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (Map.Entry<Integer, Double> entry : prixParClasse.entrySet()) {
                stmt.setInt(1, volOpereId);
                stmt.setInt(2, entry.getKey());
                stmt.setDouble(3, entry.getValue());
                
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    count++;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error in PrixVolDAO.insertPrixPourVol(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return count;
    }
}
