package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Reservation;
import util.DatabaseConnection;

public class PaiementDAO {

    public List<Reservation> getReservationsAvecStatutPaiement() {
        List<Reservation> list = new ArrayList<>();

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
                "GROUP BY r.id, r.client_id, r.vol_opere_id, r.date_reservation, r.statut, " +
                "c.nom, c.prenom, lv.numero_vol, ad.nom, aa.nom, vo.date_heure_depart " +
                "ORDER BY r.date_reservation DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reservation r = mapReservationPaiement(rs);
                list.add(r);
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
}
