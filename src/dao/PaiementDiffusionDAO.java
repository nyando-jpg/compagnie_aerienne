
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
}
