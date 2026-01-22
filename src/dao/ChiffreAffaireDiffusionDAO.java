package dao;

import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO to compute chiffre d'affaire for diffusions
 */
public class ChiffreAffaireDiffusionDAO {

    /**
     * Calculate total revenue for diffusions with optional filters.
     * ChiffreAffaire = SUM(price_at_diffusion * diffusion.nombre)
     * price_at_diffusion is the latest prixDiffusion for the société with
     * datePrixDiff <= diffusion.dateDiff
     */
    public static Map<String, Object> calculerCA(Integer volOpereId, Integer avionId, Integer societeId,
            Date dateDebut, Date dateFin) {
        Map<String, Object> result = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT COALESCE(SUM(pd.valeur * d.nombre), 0) AS total, COALESCE(SUM(d.nombre),0) as total_tickets FROM diffusion d\n");
        sql.append("JOIN vol_opere vo ON d.idVolOpere = vo.id\n");
        sql.append("JOIN (\n");
        sql.append("  SELECT p1.id, p1.idSociete, p1.valeur, p1.datePrixDiff FROM prixDiffusion p1\n");
        sql.append(
                "  JOIN (SELECT idSociete, MAX(datePrixDiff) as max_date, id as dummy FROM prixDiffusion GROUP BY idSociete) p2\n");
        sql.append("    ON p1.idSociete = p2.idSociete AND p1.datePrixDiff = p2.max_date\n");
        sql.append(") pd ON pd.idSociete = d.idSociete AND pd.datePrixDiff <= d.dateDiff\n");
        // Note: the subquery above picks the latest price per societe overall; to be
        // strictly correct per diffusion date we need a correlated subquery. Use a
        // correlated approach instead.
        // We'll replace with a correlated subquery in prepared statement later if
        // needed.

        // Simpler correct correlated SQL below (we will use it)
        // Use a correlated subquery to fetch the single applicable price per diffusion
        sql = new StringBuilder();
        sql.append("SELECT COALESCE(SUM( (\n");
        sql.append(
                "  SELECT p.valeur FROM prixDiffusion p WHERE p.idSociete = d.idSociete AND p.datePrixDiff <= d.dateDiff ORDER BY p.datePrixDiff DESC LIMIT 1\n");
        sql.append(") * d.nombre ), 0) AS total, COALESCE(SUM(d.nombre),0) as total_tickets FROM diffusion d\n");
        sql.append("JOIN vol_opere vo ON d.idVolOpere = vo.id\n");
        sql.append("WHERE 1=1 \n");

        if (volOpereId != null)
            sql.append("AND d.idVolOpere = ? \n");
        if (avionId != null)
            sql.append("AND vo.avion_id = ? \n");
        if (societeId != null)
            sql.append("AND d.idSociete = ? \n");
        if (dateDebut != null)
            sql.append("AND d.dateDiff >= ? \n");
        if (dateFin != null)
            sql.append("AND d.dateDiff <= ? \n");

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (volOpereId != null)
                ps.setInt(idx++, volOpereId);
            if (avionId != null)
                ps.setInt(idx++, avionId);
            if (societeId != null)
                ps.setInt(idx++, societeId);
            if (dateDebut != null)
                ps.setDate(idx++, dateDebut);
            if (dateFin != null)
                ps.setDate(idx++, dateFin);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result.put("total", rs.getDouble("total"));
                result.put("total_tickets", rs.getInt("total_tickets"));
            } else {
                result.put("total", 0.0);
                result.put("total_tickets", 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("total", 0.0);
            result.put("total_tickets", 0);
        }

        return result;
    }

    /**
     * Liste des sociétés avec somme des diffusions et CA par société (selon
     * filtres)
     */
    public static java.util.List<java.util.Map<String, Object>> getCaParSociete(Integer volOpereId, Integer avionId,
            Integer societeId, Date dateDebut, Date dateFin) {
        java.util.List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.id as societe_id, s.nom as societe_nom, ");
        sql.append("COALESCE(SUM(d.nombre),0) as total_diffusions, ");
        sql.append(
                "COALESCE(SUM((SELECT p.valeur FROM prixDiffusion p WHERE p.idSociete = d.idSociete AND p.datePrixDiff <= d.dateDiff ORDER BY p.datePrixDiff DESC LIMIT 1) * d.nombre),0) as ca_societe, ");
        sql.append(
                "COALESCE((SELECT SUM(pd.montant) FROM paiementDiff pd JOIN diffusion df ON pd.idDiffusion = df.id WHERE df.idSociete = s.id ");
        if (volOpereId != null)
            sql.append(" AND df.idVolOpere = ? ");
        if (avionId != null)
            sql.append(" AND df.idVolOpere IN (SELECT vo.id FROM vol_opere vo WHERE vo.avion_id = ?) ");
        if (dateDebut != null)
            sql.append(" AND pd.datePaiement >= ? ");
        if (dateFin != null)
            sql.append(" AND pd.datePaiement <= ? ");
        sql.append("), 0) as somme_payee, ");
        sql.append(
                "(COALESCE(SUM((SELECT p.valeur FROM prixDiffusion p WHERE p.idSociete = d.idSociete AND p.datePrixDiff <= d.dateDiff ORDER BY p.datePrixDiff DESC LIMIT 1) * d.nombre),0) - ");
        sql.append(
                "COALESCE((SELECT SUM(pd.montant) FROM paiementDiff pd JOIN diffusion df ON pd.idDiffusion = df.id WHERE df.idSociete = s.id ");
        if (volOpereId != null)
            sql.append(" AND df.idVolOpere = ? ");
        if (avionId != null)
            sql.append(" AND df.idVolOpere IN (SELECT vo.id FROM vol_opere vo WHERE vo.avion_id = ?) ");
        if (dateDebut != null)
            sql.append(" AND pd.datePaiement >= ? ");
        if (dateFin != null)
            sql.append(" AND pd.datePaiement <= ? ");
        sql.append("), 0)) as reste_a_payer ");
        sql.append("FROM diffusion d ");
        sql.append("JOIN societe s ON d.idSociete = s.id ");
        sql.append("JOIN vol_opere vo ON d.idVolOpere = vo.id ");
        sql.append("WHERE 1=1 ");
        if (volOpereId != null)
            sql.append("AND d.idVolOpere = ? ");
        if (avionId != null)
            sql.append("AND vo.avion_id = ? ");
        if (societeId != null)
            sql.append("AND d.idSociete = ? ");
        if (dateDebut != null)
            sql.append("AND d.dateDiff >= ? ");
        if (dateFin != null)
            sql.append("AND d.dateDiff <= ? ");
        sql.append("GROUP BY s.id, s.nom ");
        sql.append("ORDER BY ca_societe DESC, s.nom ASC ");

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            // For subqueries in SELECT, repeat the filter params in the same order
            if (volOpereId != null)
                ps.setInt(idx++, volOpereId);
            if (avionId != null)
                ps.setInt(idx++, avionId);
            if (dateDebut != null)
                ps.setDate(idx++, dateDebut);
            if (dateFin != null)
                ps.setDate(idx++, dateFin);
            // For reste_a_payer subquery
            if (volOpereId != null)
                ps.setInt(idx++, volOpereId);
            if (avionId != null)
                ps.setInt(idx++, avionId);
            if (dateDebut != null)
                ps.setDate(idx++, dateDebut);
            if (dateFin != null)
                ps.setDate(idx++, dateFin);
            // For main WHERE clause
            if (volOpereId != null)
                ps.setInt(idx++, volOpereId);
            if (avionId != null)
                ps.setInt(idx++, avionId);
            if (societeId != null)
                ps.setInt(idx++, societeId);
            if (dateDebut != null)
                ps.setDate(idx++, dateDebut);
            if (dateFin != null)
                ps.setDate(idx++, dateFin);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                java.util.Map<String, Object> row = new java.util.HashMap<>();
                row.put("societe_id", rs.getInt("societe_id"));
                row.put("societe_nom", rs.getString("societe_nom"));
                row.put("total_diffusions", rs.getInt("total_diffusions"));
                row.put("ca_societe", rs.getDouble("ca_societe"));
                row.put("somme_payee", rs.getDouble("somme_payee"));
                row.put("reste_a_payer", rs.getDouble("reste_a_payer"));
                result.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
