package dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import model.TypeClient;
import util.DatabaseConnection;

public class PrixVolDAO {

    /**
     * Récupère le prix pour un vol, classe et type de client donnés
     * Calcule automatiquement si le type est en mode POURCENTAGE
     */
    public double getPrix(int volOpereId, int classeSiegeId, int typeClientId) {
        String sql = "SELECT prix_base, pourcentage_base FROM prix_vol " +
                     "WHERE vol_opere_id = ? AND classe_siege_id = ? AND type_client_id = ?";
        
        System.out.println("[DEBUG] PrixVolDAO.getPrix() - Recherche prix pour: volOpere=" + volOpereId + ", classe=" + classeSiegeId + ", typeClient=" + typeClientId);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volOpereId);
            stmt.setInt(2, classeSiegeId);
            stmt.setInt(3, typeClientId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Récupérer les valeurs avec gestion NULL
                double prixBase = rs.getDouble("prix_base");
                boolean prixBaseIsNull = rs.wasNull();
                
                double pourcentageBase = rs.getDouble("pourcentage_base");
                boolean pourcentageIsNull = rs.wasNull();
                
                System.out.println("[DEBUG] Prix trouvé: prix_base=" + (prixBaseIsNull ? "NULL" : prixBase) + 
                                   ", pourcentage_base=" + (pourcentageIsNull ? "NULL" : pourcentageBase));
                
                // Si c'est un prix fixe
                if (!prixBaseIsNull && prixBase > 0) {
                    System.out.println("[DEBUG] Retour prix fixe: " + prixBase);
                    return prixBase;
                }
                
                // Si c'est un pourcentage, calculer basé sur le prix adulte (type_client_id = 1)
                if (!pourcentageIsNull && pourcentageBase > 0) {
                    System.out.println("[DEBUG] Mode pourcentage détecté: " + pourcentageBase + "%");
                    
                    String sqlAdulte = "SELECT prix_base FROM prix_vol " +
                                      "WHERE vol_opere_id = ? AND classe_siege_id = ? AND type_client_id = 1";
                    
                    try (PreparedStatement stmtAdulte = conn.prepareStatement(sqlAdulte)) {
                        stmtAdulte.setInt(1, volOpereId);
                        stmtAdulte.setInt(2, classeSiegeId);
                        
                        ResultSet rsAdulte = stmtAdulte.executeQuery();
                        if (rsAdulte.next()) {
                            double prixAdulte = rsAdulte.getDouble("prix_base");
                            if (!rsAdulte.wasNull()) {
                                double prixCalcule = prixAdulte * (pourcentageBase / 100.0);
                                System.out.println("[DEBUG] Prix adulte: " + prixAdulte + ", pourcentage: " + pourcentageBase + "%, prix calculé: " + prixCalcule);
                                return prixCalcule;
                            } else {
                                System.err.println("[ERROR] Prix adulte est NULL!");
                            }
                        } else {
                            System.err.println("[ERROR] Aucun prix adulte trouvé pour le calcul du pourcentage!");
                        }
                    }
                }
                
                System.err.println("[ERROR] Prix trouvé mais prix_base et pourcentage_base sont tous les deux NULL ou <= 0!");
            } else {
                System.err.println("[ERROR] Aucun prix trouvé dans prix_vol pour ce vol/classe/type!");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] SQL Exception in PrixVolDAO.getPrix(): " + e.getMessage());
            e.printStackTrace();
        }
        System.err.println("[ERROR] Retour 0.0 - Aucun prix valide trouvé");
        return 0.0;
    }
    
    /**
     * Insérer les prix pour un vol opéré (prix fixes ET pourcentages)
     * @param volOpereId ID du vol opéré
     * @param prixEtPourcentages Map avec clé "classeSiegeId_typeClientId" et valeur prix ou pourcentage
     */
    public void insertPrixPourVol(int volOpereId, Map<String, Object> prixEtPourcentages) {
        String sql = "INSERT INTO prix_vol (vol_opere_id, classe_siege_id, type_client_id, prix_base, pourcentage_base) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        TypeClientDAO typeClientDAO = new TypeClientDAO();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (Map.Entry<String, Object> entry : prixEtPourcentages.entrySet()) {
                String[] parts = entry.getKey().split("_");
                int classeSiegeId = Integer.parseInt(parts[0]);
                int typeClientId = Integer.parseInt(parts[1]);
                
                TypeClient tc = typeClientDAO.getById(typeClientId);
                if (tc == null) continue;
                
                stmt.setInt(1, volOpereId);
                stmt.setInt(2, classeSiegeId);
                stmt.setInt(3, typeClientId);
                
                if ("POURCENTAGE".equals(tc.getModeCalcul())) {
                    // C'est un pourcentage
                    double pourcentage = ((Number) entry.getValue()).doubleValue();
                    stmt.setNull(4, Types.NUMERIC); // prix_base = NULL
                    stmt.setDouble(5, pourcentage); // pourcentage_base
                } else {
                    // C'est un prix fixe
                    double prix = ((Number) entry.getValue()).doubleValue();
                    stmt.setDouble(4, prix); // prix_base
                    stmt.setNull(5, Types.NUMERIC); // pourcentage_base = NULL
                }
                
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            
        } catch (SQLException e) {
            System.err.println("Error in PrixVolDAO.insertPrixPourVol(): " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Récupère tous les prix pour un vol opéré
     * @return Map avec clé "classeSiegeId_typeClientId" et valeur prix ou pourcentage
     */
    public Map<String, Object> getPrixByVolOpere(int volOpereId) {
        Map<String, Object> prixMap = new HashMap<>();
        String sql = "SELECT classe_siege_id, type_client_id, prix_base, pourcentage_base FROM prix_vol WHERE vol_opere_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volOpereId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String key = rs.getInt("classe_siege_id") + "_" + rs.getInt("type_client_id");
                
                // Récupérer avec gestion NULL
                double prixBase = rs.getDouble("prix_base");
                boolean prixBaseIsNull = rs.wasNull();
                
                double pourcentageBase = rs.getDouble("pourcentage_base");
                boolean pourcentageIsNull = rs.wasNull();
                
                // Stocker le prix ou le pourcentage selon ce qui est défini
                if (!prixBaseIsNull) {
                    prixMap.put(key, prixBase);
                } else if (!pourcentageIsNull) {
                    prixMap.put(key, pourcentageBase);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in PrixVolDAO.getPrixByVolOpere(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return prixMap;
    }
}
