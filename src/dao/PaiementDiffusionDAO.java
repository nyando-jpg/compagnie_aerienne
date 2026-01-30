
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.DatabaseConnection;

public class PaiementDiffusionDAO {

    /**
     * Insère un paiement pour chaque diffusion d'une société sur un mois donné.
     * 
     * @param idSociete         l'id de la société
     * @param moisStr           le mois au format yyyy-MM
     * @param montantTotal      le montant total à répartir
     * @param datePaiement      la date du paiement
     * @param idMethodePaiement la méthode de paiement
     * @return true si au moins un paiement a été inséré
     */
    public static boolean insertPaiementParMoisSociete(int idSociete, String moisStr, double montantTotal,
            java.sql.Timestamp datePaiement, int idMethodePaiement, StringBuilder errorBuilder) {
        class DiffusionMontant {
            int idDiffusion;
            double montantDu;

            DiffusionMontant(int id, double montant) {
                this.idDiffusion = id;
                this.montantDu = montant;
            }
        }
        List<DiffusionMontant> diffusions = new ArrayList<>();
        String sql = "SELECT d.id, pd.valeur * d.nombre AS montantDu FROM diffusion d JOIN prixDiffusion pd ON pd.idSociete = d.idSociete WHERE d.idSociete = ? AND to_char(d.dateDiff, 'YYYY-MM') = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSociete);
            ps.setString(2, moisStr);
            ResultSet rs = ps.executeQuery();
            double montantTotalDu = 0;
            while (rs.next()) {
                int idDiff = rs.getInt("id");
                double montantDu = rs.getDouble("montantDu");
                diffusions.add(new DiffusionMontant(idDiff, montantDu));
                montantTotalDu += montantDu;
            }
            System.out.println("[PaiementDiffusionDAO] Diffusions trouvées pour societe=" + idSociete + ", mois="
                    + moisStr + " : " + diffusions.size() + " => " + diffusions);
            if (diffusions.isEmpty()) {
                if (errorBuilder != null)
                    errorBuilder.append("Aucune diffusion trouvée pour cette société et ce mois.\n");
                return false;
            }
            if (diffusions.size() == 1 && errorBuilder != null) {
                errorBuilder.append("Attention : une seule diffusion trouvée pour ce mois/société. IDs : ");
                for (DiffusionMontant d : diffusions)
                    errorBuilder.append(d.idDiffusion).append(" ");
                errorBuilder.append("\n");
            }
            if (montantTotalDu <= 0) {
                if (errorBuilder != null)
                    errorBuilder.append("Montant total dû nul ou négatif.\n");
                return false;
            }
            double pourcentage = montantTotal / montantTotalDu;
            boolean atLeastOne = false;
            for (DiffusionMontant d : diffusions) {
                double montantRegle = d.montantDu * pourcentage;
                try {
                    String sqlInsert = "INSERT INTO paiementDiff(idDiffusion, montant, datePaiement) VALUES (?, ?, ?) RETURNING id";
                    try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                        psInsert.setInt(1, d.idDiffusion);
                        psInsert.setDouble(2, montantRegle);
                        psInsert.setTimestamp(3, datePaiement);
                        ResultSet rsInsert = psInsert.executeQuery();
                        if (rsInsert.next()) {
                            int paiementDiffId = rsInsert.getInt("id");
                            String sql2 = "INSERT INTO paiement_diff_methode(paiement_diff_id, methode_paiement_id, montant) VALUES (?, ?, ?)";
                            try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                                ps2.setInt(1, paiementDiffId);
                                ps2.setInt(2, idMethodePaiement);
                                ps2.setDouble(3, montantRegle);
                                int affected2 = ps2.executeUpdate();
                                if (affected2 > 0)
                                    atLeastOne = true;
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (errorBuilder != null)
                        errorBuilder.append("Erreur pour diffusion ").append(d.idDiffusion).append(" : ")
                                .append(ex.getMessage()).append("\n");
                }
            }
            return atLeastOne;
        } catch (Exception e) {
            e.printStackTrace();
            if (errorBuilder != null)
                errorBuilder.append(e.getMessage()).append("\n");
        }
        return false;
    }

    public static List<Map<String, Object>> getFiltered(Integer volOpereId, Integer avionId, Integer societeId,
            java.sql.Date dateDebut, java.sql.Date dateFin) {
        List<Map<String, Object>> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT pd.id, pd.datePaiement, pd.montant, d.id as diffusion_id, d.idSociete, d.idVolOpere, d.dateDiff, mp.libelle as methode_paiement ");
        sql.append("FROM paiementDiff pd ");
        sql.append("LEFT JOIN diffusion d ON pd.idDiffusion = d.id ");
        sql.append("LEFT JOIN paiement_diff_methode pdm ON pd.id = pdm.paiement_diff_id ");
        sql.append("LEFT JOIN methode_paiement mp ON pdm.methode_paiement_id = mp.id ");
        sql.append("LEFT JOIN vol_opere vo ON d.idVolOpere = vo.id ");
        sql.append("LEFT JOIN avion a ON vo.avion_id = a.id ");
        sql.append("WHERE 1=1 ");
        if (societeId != null)
            sql.append("AND d.idSociete = ? ");
        if (volOpereId != null)
            sql.append("AND d.idVolOpere = ? ");
        if (avionId != null)
            sql.append("AND vo.avion_id = ? ");
        if (dateDebut != null)
            sql.append("AND pd.datePaiement >= ? ");
        if (dateFin != null)
            sql.append("AND pd.datePaiement <= ? ");
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (societeId != null)
                ps.setInt(idx++, societeId);
            if (volOpereId != null)
                ps.setInt(idx++, volOpereId);
            if (avionId != null)
                ps.setInt(idx++, avionId);
            if (dateDebut != null)
                ps.setDate(idx++, dateDebut);
            if (dateFin != null)
                ps.setDate(idx++, dateFin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("date", rs.getTimestamp("datePaiement"));
                row.put("montant", rs.getDouble("montant"));
                int diffusionId = rs.getInt("diffusion_id");
                int societeIdRow = rs.getInt("idSociete");
                int volOpereIdRow = rs.getInt("idVolOpere");
                java.sql.Timestamp dateDiff = rs.getTimestamp("dateDiff");
                String info = "Diffusion #" + diffusionId + " (Societe: " + societeIdRow + ", VolOpere: "
                        + volOpereIdRow
                        + ", Date: " + (dateDiff != null ? dateDiff.toString() : "-") + ")";
                row.put("diffusion_info", info);
                row.put("methode_paiement", rs.getString("methode_paiement"));
                result.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean insert(int idDiffusion, double montant, java.sql.Timestamp datePaiement,
            int idMethodePaiement) {
        String sql = "INSERT INTO paiementDiff(idDiffusion, montant, datePaiement) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDiffusion);
            ps.setDouble(2, montant);
            ps.setTimestamp(3, datePaiement);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int paiementDiffId = rs.getInt("id");
                // Insert into paiement_diff_methode
                String sql2 = "INSERT INTO paiement_diff_methode(paiement_diff_id, methode_paiement_id, montant) VALUES (?, ?, ?)";
                try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                    ps2.setInt(1, paiementDiffId);
                    ps2.setInt(2, idMethodePaiement);
                    ps2.setDouble(3, montant);
                    int affected2 = ps2.executeUpdate();
                    return affected2 > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Map<String, Object>> getAll() {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT pd.id, pd.datePaiement, pd.montant, d.id as diffusion_id, d.idSociete, d.idVolOpere, d.dateDiff, mp.libelle as methode_paiement "
                + "FROM paiementDiff pd "
                + "LEFT JOIN diffusion d ON pd.idDiffusion = d.id "
                + "LEFT JOIN paiement_diff_methode pdm ON pd.id = pdm.paiement_diff_id "
                + "LEFT JOIN methode_paiement mp ON pdm.methode_paiement_id = mp.id ";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("date", rs.getTimestamp("datePaiement"));
                row.put("montant", rs.getDouble("montant"));
                int diffusionId = rs.getInt("diffusion_id");
                int societeId = rs.getInt("idSociete");
                int volOpereId = rs.getInt("idVolOpere");
                java.sql.Timestamp dateDiff = rs.getTimestamp("dateDiff");
                String info = "Diffusion #" + diffusionId + " (Societe: " + societeId + ", VolOpere: " + volOpereId
                        + ", Date: " + (dateDiff != null ? dateDiff.toString() : "-") + ")";
                row.put("diffusion_info", info);
                row.put("methode_paiement", rs.getString("methode_paiement"));
                result.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Map<String, Object>> getAllMethodesPaiement() {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT id, libelle FROM methode_paiement ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("libelle", rs.getString("libelle"));
                result.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Map<String, Object>> getAllDiffusions() {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT d.id, d.idSociete, d.idVolOpere, d.dateDiff FROM diffusion d";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                int id = rs.getInt("id");
                int societeId = rs.getInt("idSociete");
                int volOpereId = rs.getInt("idVolOpere");
                java.sql.Timestamp dateDiff = rs.getTimestamp("dateDiff");
                String info = "Diffusion #" + id + " (Societe: " + societeId + ", VolOpere: " + volOpereId
                        + ", Date: " + (dateDiff != null ? dateDiff.toString() : "-") + ")";
                row.put("id", id);
                row.put("info", info);
                result.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Calcule le montant total dû pour toutes les diffusions d'une société (somme
     * des prix * nombre).
     */
    public static Double getTotalDuPourSociete(int societeId) {
        String sql = "SELECT SUM(pd.valeur * d.nombre) AS totalDu FROM diffusion d JOIN prixDiffusion pd ON pd.idSociete = d.idSociete WHERE d.idSociete = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, societeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("totalDu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
