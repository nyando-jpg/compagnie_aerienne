package dao;

import util.DatabaseConnection;
import model.Reservation;
import model.VolOpere;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChiffreAffaireDAO {

    /**
     * Calculer le chiffre d'affaires avec filtres optionnels
     * @param ligneVolId ID de la ligne (null = toutes)
     * @param avionId ID de l'avion (null = tous)
     * @param dateDebut Date de début (null = depuis toujours)
     * @param dateFin Date de fin (null = jusqu'à maintenant)
     * @return Map contenant le CA total et les détails
     */
    public Map<String, Object> calculerChiffreAffaire(Integer ligneVolId, Integer avionId, 
                                                       Date dateDebut, Date dateFin) {
        Map<String, Object> resultat = new HashMap<>();
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("    COUNT(DISTINCT r.id) as nombre_reservations, ");
        sql.append("    COUNT(b.id) as nombre_billets, ");
        sql.append("    COALESCE(SUM(b.prix), 0) as chiffre_affaire_total ");
        sql.append("FROM billet b ");
        sql.append("JOIN reservation r ON b.reservation_id = r.id ");
        sql.append("JOIN vol_opere vo ON r.vol_opere_id = vo.id ");
        sql.append("JOIN ligne_vol lv ON vo.ligne_vol_id = lv.id ");
        sql.append("WHERE r.statut IN ('CONFIRMEE', 'EN_ATTENTE') ");
        
        // Ajouter les filtres
        if (ligneVolId != null) {
            sql.append("AND vo.ligne_vol_id = ? ");
        }
        if (avionId != null) {
            sql.append("AND vo.avion_id = ? ");
        }
        if (dateDebut != null) {
            sql.append("AND vo.date_heure_depart >= ? ");
        }
        if (dateFin != null) {
            sql.append("AND vo.date_heure_depart <= ? ");
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (ligneVolId != null) {
                stmt.setInt(paramIndex++, ligneVolId);
            }
            if (avionId != null) {
                stmt.setInt(paramIndex++, avionId);
            }
            if (dateDebut != null) {
                stmt.setDate(paramIndex++, dateDebut);
            }
            if (dateFin != null) {
                stmt.setDate(paramIndex++, dateFin);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                resultat.put("nombreReservations", rs.getInt("nombre_reservations"));
                resultat.put("nombreBillets", rs.getInt("nombre_billets"));
                resultat.put("chiffreAffaireTotal", rs.getDouble("chiffre_affaire_total"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error in ChiffreAffaireDAO.calculerChiffreAffaire(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultat;
    }
    
    /**
     * Obtenir le détail du CA par classe
     */
    public Map<String, Double> getChiffreAffaireParClasse(Integer ligneVolId, Integer avionId, 
                                                           Date dateDebut, Date dateFin) {
        Map<String, Double> resultat = new HashMap<>();
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("    cs.libelle as classe, ");
        sql.append("    COALESCE(SUM(b.prix), 0) as ca ");
        sql.append("FROM billet b ");
        sql.append("JOIN reservation r ON b.reservation_id = r.id ");
        sql.append("JOIN siege_vol sv ON b.siege_vol_id = sv.id ");
        sql.append("JOIN siege s ON sv.siege_id = s.id ");
        sql.append("JOIN classe_siege cs ON s.classe_siege_id = cs.id ");
        sql.append("JOIN vol_opere vo ON r.vol_opere_id = vo.id ");
        sql.append("WHERE r.statut IN ('CONFIRMEE', 'EN_ATTENTE') ");
        
        if (ligneVolId != null) {
            sql.append("AND vo.ligne_vol_id = ? ");
        }
        if (avionId != null) {
            sql.append("AND vo.avion_id = ? ");
        }
        if (dateDebut != null) {
            sql.append("AND vo.date_heure_depart >= ? ");
        }
        if (dateFin != null) {
            sql.append("AND vo.date_heure_depart <= ? ");
        }
        
        sql.append("GROUP BY cs.libelle ORDER BY ca DESC");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (ligneVolId != null) {
                stmt.setInt(paramIndex++, ligneVolId);
            }
            if (avionId != null) {
                stmt.setInt(paramIndex++, avionId);
            }
            if (dateDebut != null) {
                stmt.setDate(paramIndex++, dateDebut);
            }
            if (dateFin != null) {
                stmt.setDate(paramIndex++, dateFin);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                resultat.put(rs.getString("classe"), rs.getDouble("ca"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error in ChiffreAffaireDAO.getChiffreAffaireParClasse(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultat;
    }
    
    /**
     * Obtenir la liste des réservations avec filtres
     */
    public List<Reservation> getReservations(Integer ligneVolId, Integer avionId, 
                                             Date dateDebut, Date dateFin) {
        List<Reservation> list = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT r.id, r.client_id, r.vol_opere_id, r.date_reservation, r.statut, ");
        sql.append("c.nom as client_nom, c.prenom as client_prenom, ");
        sql.append("lv.numero_vol, ad.nom as aeroport_depart, aa.nom as aeroport_arrivee, ");
        sql.append("vo.date_heure_depart, s.numero_siege, b.prix ");
        sql.append("FROM reservation r ");
        sql.append("JOIN client c ON r.client_id = c.id ");
        sql.append("JOIN vol_opere vo ON r.vol_opere_id = vo.id ");
        sql.append("JOIN ligne_vol lv ON vo.ligne_vol_id = lv.id ");
        sql.append("LEFT JOIN aeroport ad ON lv.aeroport_depart_id = ad.id ");
        sql.append("LEFT JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id ");
        sql.append("LEFT JOIN billet b ON r.id = b.reservation_id ");
        sql.append("LEFT JOIN siege_vol sv ON b.siege_vol_id = sv.id ");
        sql.append("LEFT JOIN siege s ON sv.siege_id = s.id ");
        sql.append("WHERE r.statut IN ('CONFIRMEE', 'EN_ATTENTE') ");
        
        if (ligneVolId != null) {
            sql.append("AND vo.ligne_vol_id = ? ");
        }
        if (avionId != null) {
            sql.append("AND vo.avion_id = ? ");
        }
        if (dateDebut != null) {
            sql.append("AND vo.date_heure_depart >= ? ");
        }
        if (dateFin != null) {
            sql.append("AND vo.date_heure_depart <= ? ");
        }
        
        sql.append("ORDER BY r.date_reservation DESC");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (ligneVolId != null) {
                stmt.setInt(paramIndex++, ligneVolId);
            }
            if (avionId != null) {
                stmt.setInt(paramIndex++, avionId);
            }
            if (dateDebut != null) {
                stmt.setDate(paramIndex++, dateDebut);
            }
            if (dateFin != null) {
                stmt.setDate(paramIndex++, dateFin);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
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
                r.setNumeroSiege(rs.getString("numero_siege"));
                r.setPrix(rs.getDouble("prix"));
                list.add(r);
            }
            
        } catch (SQLException e) {
            System.err.println("Error in ChiffreAffaireDAO.getReservations(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Obtenir la liste des vols opérés avec filtres
     */
    public List<VolOpere> getVolsOperes(Integer ligneVolId, Integer avionId, 
                                        Date dateDebut, Date dateFin) {
        List<VolOpere> list = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT vo.id, vo.ligne_vol_id, vo.avion_id, ");
        sql.append("vo.date_heure_depart, vo.date_heure_arrivee, ");
        sql.append("sv.libelle as status, vo.retard_minutes, vo.motif_annulation, ");
        sql.append("lv.numero_vol, ad.nom as depart_nom, aa.nom as arrivee_nom ");
        sql.append("FROM vol_opere vo ");
        sql.append("JOIN ligne_vol lv ON vo.ligne_vol_id = lv.id ");
        sql.append("LEFT JOIN status_vol sv ON vo.status_id = sv.id ");
        sql.append("LEFT JOIN aeroport ad ON lv.aeroport_depart_id = ad.id ");
        sql.append("LEFT JOIN aeroport aa ON lv.aeroport_arrivee_id = aa.id ");
        sql.append("WHERE 1=1 ");
        
        if (ligneVolId != null) {
            sql.append("AND vo.ligne_vol_id = ? ");
        }
        if (avionId != null) {
            sql.append("AND vo.avion_id = ? ");
        }
        if (dateDebut != null) {
            sql.append("AND vo.date_heure_depart >= ? ");
        }
        if (dateFin != null) {
            sql.append("AND vo.date_heure_depart <= ? ");
        }
        
        sql.append("ORDER BY vo.date_heure_depart DESC");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (ligneVolId != null) {
                stmt.setInt(paramIndex++, ligneVolId);
            }
            if (avionId != null) {
                stmt.setInt(paramIndex++, avionId);
            }
            if (dateDebut != null) {
                stmt.setDate(paramIndex++, dateDebut);
            }
            if (dateFin != null) {
                stmt.setDate(paramIndex++, dateFin);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                VolOpere vo = new VolOpere();
                vo.setId(rs.getInt("id"));
                vo.setLigneVolId(rs.getInt("ligne_vol_id"));
                vo.setAvionId(rs.getInt("avion_id"));
                vo.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
                vo.setDateHeureArrivee(rs.getTimestamp("date_heure_arrivee"));
                vo.setStatus(rs.getString("status"));
                vo.setRetardMinutes(rs.getInt("retard_minutes"));
                vo.setMotifAnnulation(rs.getString("motif_annulation"));
                vo.setNumeroVol(rs.getString("numero_vol"));
                vo.setAeroportDepartNom(rs.getString("depart_nom"));
                vo.setAeroportArriveeNom(rs.getString("arrivee_nom"));
                list.add(vo);
            }
            
        } catch (SQLException e) {
            System.err.println("Error in ChiffreAffaireDAO.getVolsOperes(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
}
