package dao;

import java.sql.*;
import util.DatabaseConnection;

/**
 * DAO for Siege with operations
 */
public class SiegeDAO {
    
    /**
     * Insert multiple sieges for an avion
     * @param avionId ID of the avion
     * @param classeSiegeId ID of the classe_siege
     * @param nombreSieges Number of seats to create
     * @param startRow Starting row number for this class
     * @return the next available row number
     */
    public int insertSiegesForAvion(int avionId, int classeSiegeId, int nombreSieges, int startRow) {
        if (nombreSieges <= 0) {
            return startRow; // Nothing to insert
        }
        
        String sql = "INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false); // Start transaction
            
            try {
                // Configuration: 5 sièges par rangée (A, B, C, D, E)
                String[] colonnes = {"A", "B", "C", "D", "E"};
                int siegesParRangee = colonnes.length;
                
                int currentRow = startRow;
                int seatIndex = 0;
                
                for (int i = 0; i < nombreSieges; i++) {
                    int colIndex = seatIndex % siegesParRangee;
                    String numeroSiege = currentRow + colonnes[colIndex];
                    
                    stmt.setString(1, numeroSiege);
                    stmt.setInt(2, classeSiegeId);
                    stmt.setInt(3, avionId);
                    stmt.addBatch();
                    
                    seatIndex++;
                    if (colIndex == siegesParRangee - 1) {
                        currentRow++; // Passer à la rangée suivante
                    }
                }
                
                stmt.executeBatch();
                conn.commit();
                
                // Retourner la prochaine rangée disponible
                if (nombreSieges % siegesParRangee == 0) {
                    return currentRow;
                } else {
                    return currentRow + 1;
                }
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            System.err.println("Error in SiegeDAO.insertSiegesForAvion(): " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Get the prefix for seat numbering based on classe libelle
     * Not used anymore but kept for compatibility
     */
    public String getPrefixForClasse(String libelleClasse) {
        if (libelleClasse == null) return "SEG";
        
        String upper = libelleClasse.toUpperCase();
        if (upper.contains("ECONOM") || upper.contains("ECONOMY")) return "ECO";
        if (upper.contains("AFFAIRE") || upper.contains("BUSINESS")) return "BUS";
        if (upper.contains("PREMI") || upper.contains("FIRST")) return "FIR";
        
        // Default: use first 3 letters
        return upper.length() >= 3 ? upper.substring(0, 3) : upper;
    }
    
    /**
     * Delete all sieges for an avion
     */
    public boolean deleteSiegesForAvion(int avionId) {
        String sql = "DELETE FROM siege WHERE avion_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, avionId);
            stmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error in SiegeDAO.deleteSiegesForAvion(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
