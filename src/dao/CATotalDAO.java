package dao;

import java.sql.*;
import java.util.*;
import model.CATotal;

public class CATotalDAO {

        public static List<CATotal> getCATotalListByMoisAnnee(String mois, String annee) {
                List<CATotal> list = new ArrayList<>();
                String query = "SELECT " +
                                " ad.nom AS aeroportDepart, " +
                                " aa.nom AS aeroportArrivee, " +
                                " av.code_avion AS avion, " +
                                " v.date_heure_depart AS dateDepart, " +
                                " EXTRACT(HOUR FROM v.date_heure_depart) || ':' || LPAD(EXTRACT(MINUTE FROM v.date_heure_depart)::text, 2, '0') AS heureDepart, "
                                +

                                " COALESCE(b.ca_place, 0) AS caPlace, " +
                                " COALESCE(d.ca_diffusion, 0) AS caDiffusion, " +
                                " COALESCE(e.ca_extra, 0) AS caExtra, " +

                                " (COALESCE(b.ca_place, 0) + COALESCE(d.ca_diffusion, 0) + COALESCE(e.ca_extra, 0)) AS caTotal, "
                                +

                                " COALESCE(pd.montant_paye, 0) AS montantPayeDiffusion, " +
                                " (COALESCE(d.ca_diffusion, 0) - COALESCE(pd.montant_paye, 0)) AS resteAPayerDiffusion "
                                +

                                "FROM vol_opere v " +
                                "JOIN ligne_vol lv ON v.ligne_vol_id = lv.id " +
                                "JOIN aeroport ad ON lv.aeroport_depart_id = ad.id " +
                                "JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id " +
                                "JOIN avion av ON v.avion_id = av.id " +

                                "LEFT JOIN ( " +
                                "   SELECT r.vol_opere_id, SUM(b.prix) AS ca_place " +
                                "   FROM reservation r " +
                                "   JOIN billet b ON r.id = b.reservation_id " +
                                "   GROUP BY r.vol_opere_id " +
                                ") b ON v.id = b.vol_opere_id " +

                                "LEFT JOIN ( " +
                                "   SELECT d.idVolOpere, SUM(d.nombre * ( " +
                                "       SELECT p.valeur FROM prixDiffusion p " +
                                "       WHERE p.idSociete = d.idSociete " +
                                "       AND p.datePrixDiff <= d.dateDiff " +
                                "       ORDER BY p.datePrixDiff DESC LIMIT 1 " +
                                "   )) AS ca_diffusion " +
                                "   FROM diffusion d " +
                                "   GROUP BY d.idVolOpere " +
                                ") d ON v.id = d.idVolOpere " +

                                "LEFT JOIN ( " +
                                "   SELECT ve.idVolOpere, SUM(ve.nombre * e.prix) AS ca_extra " +
                                "   FROM vente_extra ve " +
                                "   JOIN extra e ON ve.idExtra = e.id " +
                                "   GROUP BY ve.idVolOpere " +
                                ") e ON e.idVolOpere = v.id " +

                                "LEFT JOIN ( " +
                                "   SELECT d.idVolOpere, SUM(pd.montant) AS montant_paye " +
                                "   FROM diffusion d " +
                                "   JOIN paiementDiff pd ON pd.idDiffusion = d.id " +
                                "   GROUP BY d.idVolOpere " +
                                ") pd ON v.id = pd.idVolOpere " +

                                "WHERE to_char(v.date_heure_depart, 'YYYY-MM') = ?;";

                try (Connection conn = util.DatabaseConnection.getConnection();
                                PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, annee + "-" + mois);
                        ResultSet rs = stmt.executeQuery();
                        while (rs.next()) {
                                CATotal ca = new CATotal();
                                ca.setAeroportDepart(rs.getString("aeroportDepart"));
                                ca.setAeroportArrivee(rs.getString("aeroportArrivee"));
                                ca.setAvion(rs.getString("avion"));
                                ca.setDateDepart(rs.getString("dateDepart"));
                                ca.setHeureDepart(rs.getString("heureDepart"));
                                ca.setCaPlace(rs.getBigDecimal("caPlace"));
                                ca.setCaDiffusion(rs.getBigDecimal("caDiffusion"));
                                ca.setCaTotal(rs.getBigDecimal("caTotal"));
                                ca.setMontantPayeDiffusion(rs.getBigDecimal("montantPayeDiffusion"));
                                ca.setResteAPayerDiffusion(rs.getBigDecimal("resteAPayerDiffusion"));
                                ca.setCaExtra(rs.getBigDecimal("caExtra"));
                                list.add(ca);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return list;
        }

        public static List<CATotal> getCATotalList() {
                List<CATotal> list = new ArrayList<>();
                String query = "SELECT " +
                                " ad.nom AS aeroportDepart, " +
                                " aa.nom AS aeroportArrivee, " +
                                " av.code_avion AS avion, " +
                                " v.date_heure_depart AS dateDepart, " +
                                " EXTRACT(HOUR FROM v.date_heure_depart) || ':' || LPAD(EXTRACT(MINUTE FROM v.date_heure_depart)::text, 2, '0') AS heureDepart, "
                                +

                                " COALESCE(b.ca_place, 0) AS caPlace, " +
                                " COALESCE(d.ca_diffusion, 0) AS caDiffusion, " +
                                " COALESCE(e.ca_extra, 0) AS caExtra, " +

                                " (COALESCE(b.ca_place, 0) + COALESCE(d.ca_diffusion, 0) + COALESCE(e.ca_extra, 0)) AS caTotal, "
                                +

                                " COALESCE(pd.montant_paye, 0) AS montantPayeDiffusion, " +
                                " (COALESCE(d.ca_diffusion, 0) - COALESCE(pd.montant_paye, 0)) AS resteAPayerDiffusion "
                                +

                                "FROM vol_opere v " +
                                "JOIN ligne_vol lv ON v.ligne_vol_id = lv.id " +
                                "JOIN aeroport ad ON lv.aeroport_depart_id = ad.id " +
                                "JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id " +
                                "JOIN avion av ON v.avion_id = av.id " +

                                "LEFT JOIN ( " +
                                "   SELECT r.vol_opere_id, SUM(b.prix) AS ca_place " +
                                "   FROM reservation r " +
                                "   JOIN billet b ON r.id = b.reservation_id " +
                                "   GROUP BY r.vol_opere_id " +
                                ") b ON v.id = b.vol_opere_id " +

                                "LEFT JOIN ( " +
                                "   SELECT d.idVolOpere, SUM(d.nombre * ( " +
                                "       SELECT p.valeur FROM prixDiffusion p " +
                                "       WHERE p.idSociete = d.idSociete " +
                                "       AND p.datePrixDiff <= d.dateDiff " +
                                "       ORDER BY p.datePrixDiff DESC LIMIT 1 " +
                                "   )) AS ca_diffusion " +
                                "   FROM diffusion d " +
                                "   GROUP BY d.idVolOpere " +
                                ") d ON v.id = d.idVolOpere " +

                                "LEFT JOIN ( " +
                                "   SELECT ve.idVolOpere, SUM(ve.nombre * e.prix) AS ca_extra " +
                                "   FROM vente_extra ve " +
                                "   JOIN extra e ON ve.idExtra = e.id " +
                                "   GROUP BY ve.idVolOpere " +
                                ") e ON e.idVolOpere = v.id " +

                                "LEFT JOIN ( " +
                                "   SELECT d.idVolOpere, SUM(pd.montant) AS montant_paye " +
                                "   FROM diffusion d " +
                                "   JOIN paiementDiff pd ON pd.idDiffusion = d.id " +
                                "   GROUP BY d.idVolOpere " +
                                ") pd ON v.id = pd.idVolOpere ";
                try (Connection conn = util.DatabaseConnection.getConnection();
                                Statement stmt = conn.createStatement();
                                ResultSet rs = stmt.executeQuery(query)) {
                        while (rs.next()) {
                                CATotal ca = new CATotal();
                                ca.setAeroportDepart(rs.getString("aeroportDepart"));
                                ca.setAeroportArrivee(rs.getString("aeroportArrivee"));
                                ca.setAvion(rs.getString("avion"));
                                ca.setDateDepart(rs.getString("dateDepart"));
                                ca.setHeureDepart(rs.getString("heureDepart"));
                                ca.setCaPlace(rs.getBigDecimal("caPlace"));
                                ca.setCaDiffusion(rs.getBigDecimal("caDiffusion"));
                                ca.setCaTotal(rs.getBigDecimal("caTotal"));
                                ca.setMontantPayeDiffusion(rs.getBigDecimal("montantPayeDiffusion"));
                                ca.setResteAPayerDiffusion(rs.getBigDecimal("resteAPayerDiffusion"));
                                ca.setCaExtra(rs.getBigDecimal("caExtra"));
                                list.add(ca);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return list;
        }
}
