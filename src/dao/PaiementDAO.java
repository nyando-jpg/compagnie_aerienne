package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Paiement;
import model.Paiement.PaiementMethodeDetail;
import model.Reservation;
import util.DatabaseConnection;

public class PaiementDAO {

    public List<Reservation> getReservationsAvecStatutPaiement(String numeroVol, String volOpereId, String statutPaiement) {
        List<Reservation> list = new ArrayList<>();
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        String sql = "SELECT r.id, r.client_id, r.vol_opere_id, r.date_reservation, r.statut, " +
                "c.nom AS client_nom, c.prenom AS client_prenom, " +
                "lv.numero_vol, ad.nom AS aeroport_depart, aa.nom AS aeroport_arrivee, vo.date_heure_depart, " +
                "COALESCE(SUM(b.prix), 0) AS montant_total, " +
                "COALESCE(SUM(pm.montant), 0) AS montant_paye " +
                "FROM reservation r " +
                "JOIN client c ON r.client_id = c.id " +
                "JOIN vol_opere vo ON r.vol_opere_id = vo.id " +
                "JOIN ligne_vol lv ON vo.ligne_vol_id = lv.id " +
                "LEFT JOIN aeroport ad ON lv.aeroport_depart_id = ad.id " +
                "LEFT JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id " +
                "LEFT JOIN billet b ON r.id = b.reservation_id " +
                "LEFT JOIN paiement p ON p.reservation_id = r.id AND p.statut = 'VALIDE'::statut_paiement_enum " +
                "LEFT JOIN paiement_methode pm ON pm.paiement_id = p.id ";

        // Filtres dynamiques
        if (numeroVol != null && !numeroVol.trim().isEmpty()) {
            conditions.add("lv.numero_vol ILIKE ?");
            params.add("%" + numeroVol.trim() + "%");
        }
        if (volOpereId != null && !volOpereId.trim().isEmpty()) {
            try {
                int voId = Integer.parseInt(volOpereId.trim());
                conditions.add("vo.id = ?");
                params.add(voId);
            } catch (NumberFormatException e) {
                // Ignorer si ce n'est pas un nombre valide
            }
        }

        if (!conditions.isEmpty()) {
            sql += "WHERE " + String.join(" AND ", conditions) + " ";
        }

        sql += "GROUP BY r.id, r.client_id, r.vol_opere_id, r.date_reservation, r.statut, " +
                "c.nom, c.prenom, lv.numero_vol, ad.nom, aa.nom, vo.date_heure_depart " +
                "ORDER BY r.date_reservation DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) param);
                } else {
                    stmt.setString(i + 1, (String) param);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservation r = mapReservationPaiement(rs);
                    // Filtrer par statut paiement après calcul (car calculé en Java)
                    if (statutPaiement == null || statutPaiement.trim().isEmpty() || 
                        statutPaiement.equals(r.getStatutPaiement())) {
                        list.add(r);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in PaiementDAO.getReservationsAvecStatutPaiement(): " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public Reservation getReservationPaiementById(int reservationId) {
        String sql = "SELECT r.id, r.client_id, r.vol_opere_id, r.date_reservation, r.statut, " +
                "c.nom AS client_nom, c.prenom AS client_prenom, " +
                "lv.numero_vol, ad.nom AS aeroport_depart, aa.nom AS aeroport_arrivee, vo.date_heure_depart, " +
                "COALESCE(SUM(b.prix), 0) AS montant_total, " +
                "COALESCE(SUM(pm.montant), 0) AS montant_paye " +
                "FROM reservation r " +
                "JOIN client c ON r.client_id = c.id " +
                "JOIN vol_opere vo ON r.vol_opere_id = vo.id " +
                "JOIN ligne_vol lv ON vo.ligne_vol_id = lv.id " +
                "LEFT JOIN aeroport ad ON lv.aeroport_depart_id = ad.id " +
                "LEFT JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id " +
                "LEFT JOIN billet b ON r.id = b.reservation_id " +
                "LEFT JOIN paiement p ON p.reservation_id = r.id AND p.statut = 'VALIDE'::statut_paiement_enum " +
                "LEFT JOIN paiement_methode pm ON pm.paiement_id = p.id " +
                "WHERE r.id = ? " +
                "GROUP BY r.id, r.client_id, r.vol_opere_id, r.date_reservation, r.statut, " +
                "c.nom, c.prenom, lv.numero_vol, ad.nom, aa.nom, vo.date_heure_depart";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapReservationPaiement(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in PaiementDAO.getReservationPaiementById(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private Reservation mapReservationPaiement(ResultSet rs) throws SQLException {
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

        double montantTotal = rs.getDouble("montant_total");
        double montantPaye = rs.getDouble("montant_paye");
        r.setMontantTotal(montantTotal);
        r.setMontantPaye(montantPaye);

        String statutPaiement;
        if (montantTotal <= 0.0001) {
            statutPaiement = "NON_DETERMINE";
        } else if (montantPaye <= 0.0001) {
            statutPaiement = "NON_PAYE";
        } else if (montantPaye + 0.0001 < montantTotal) {
            statutPaiement = "PARTIEL";
        } else {
            statutPaiement = "PAYE";
        }
        r.setStatutPaiement(statutPaiement);

        return r;
    }

    public boolean insererPaiement(int reservationId, Map<Integer, Double> montantsParMethode) {
        if (montantsParMethode == null || montantsParMethode.isEmpty()) {
            return false;
        }

        Connection conn = null;
        PreparedStatement paiementStmt = null;
        PreparedStatement methodeStmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            double montantTotal = montantsParMethode.values().stream().mapToDouble(Double::doubleValue).sum();

            String paiementSql = "INSERT INTO paiement (reservation_id, montant, statut, date_paiement) " +
                    "VALUES (?, ?, 'VALIDE'::statut_paiement_enum, CURRENT_TIMESTAMP) RETURNING id";
            paiementStmt = conn.prepareStatement(paiementSql);
            paiementStmt.setInt(1, reservationId);
            paiementStmt.setDouble(2, montantTotal);

            ResultSet rs = paiementStmt.executeQuery();
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            int paiementId = rs.getInt("id");

            String methodeSql = "INSERT INTO paiement_methode (paiement_id, methode_paiement_id, montant) VALUES (?, ?, ?)";
            methodeStmt = conn.prepareStatement(methodeSql);

            for (Map.Entry<Integer, Double> entry : montantsParMethode.entrySet()) {
                methodeStmt.setInt(1, paiementId);
                methodeStmt.setInt(2, entry.getKey());
                methodeStmt.setDouble(3, entry.getValue());
                methodeStmt.addBatch();
            }

            int[] results = methodeStmt.executeBatch();
            for (int res : results) {
                if (res == PreparedStatement.EXECUTE_FAILED) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error in PaiementDAO.insererPaiement(): " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            if (methodeStmt != null) {
                try { methodeStmt.close(); } catch (SQLException ignored) {}
            }
            if (paiementStmt != null) {
                try { paiementStmt.close(); } catch (SQLException ignored) {}
            }
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }

        return false;
    }

    /**
     * Récupère tous les paiements effectués pour une réservation donnée
     */
    public List<Paiement> getPaiementsByReservationId(int reservationId) {
        List<Paiement> paiements = new ArrayList<>();
        
        String sql = "SELECT p.id, p.reservation_id, p.montant, p.statut, p.date_paiement, " +
                "p.created_at, p.updated_at, " +
                "pm.id as pm_id, pm.methode_paiement_id, pm.montant as pm_montant, " +
                "mp.libelle as methode_libelle " +
                "FROM paiement p " +
                "LEFT JOIN paiement_methode pm ON p.id = pm.paiement_id " +
                "LEFT JOIN methode_paiement mp ON pm.methode_paiement_id = mp.id " +
                "WHERE p.reservation_id = ? " +
                "ORDER BY p.date_paiement DESC, pm.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservationId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                Paiement currentPaiement = null;
                int lastPaiementId = -1;
                
                while (rs.next()) {
                    int paiementId = rs.getInt("id");
                    
                    // Nouveau paiement
                    if (paiementId != lastPaiementId) {
                        currentPaiement = new Paiement();
                        currentPaiement.setId(paiementId);
                        currentPaiement.setReservationId(rs.getInt("reservation_id"));
                        currentPaiement.setMontant(rs.getDouble("montant"));
                        currentPaiement.setStatut(rs.getString("statut"));
                        currentPaiement.setDatePaiement(rs.getTimestamp("date_paiement"));
                        currentPaiement.setCreatedAt(rs.getTimestamp("created_at"));
                        currentPaiement.setUpdatedAt(rs.getTimestamp("updated_at"));
                        
                        paiements.add(currentPaiement);
                        lastPaiementId = paiementId;
                    }
                    
                    // Ajouter les détails de la méthode de paiement
                    int pmId = rs.getInt("pm_id");
                    if (pmId > 0 && currentPaiement != null) {
                        PaiementMethodeDetail detail = new PaiementMethodeDetail();
                        detail.setId(pmId);
                        detail.setPaiementId(paiementId);
                        detail.setMethodePaiementId(rs.getInt("methode_paiement_id"));
                        detail.setMethodePaiementLibelle(rs.getString("methode_libelle"));
                        detail.setMontant(rs.getDouble("pm_montant"));
                        
                        currentPaiement.addMethodeDetail(detail);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in PaiementDAO.getPaiementsByReservationId(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return paiements;
    }
}
