package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity: Reservation
 */
public class Reservation implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int clientId;
    private int volOpereId;
    private Timestamp dateReservation;
    private String statut;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Pour affichage (jointures)
    private String clientNom;
    private String clientPrenom;
    private String numeroVol;
    private String aeroportDepart;
    private String aeroportArrivee;
    private Timestamp dateHeureDepart;
    private String numeroSiege;
    private Double prix;
    
    public Reservation() {
    }
    
    public Reservation(int clientId, int volOpereId, String statut) {
        this.clientId = clientId;
        this.volOpereId = volOpereId;
        this.statut = statut;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getClientId() {
        return clientId;
    }
    
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    
    public int getVolOpereId() {
        return volOpereId;
    }
    
    public void setVolOpereId(int volOpereId) {
        this.volOpereId = volOpereId;
    }
    
    public Timestamp getDateReservation() {
        return dateReservation;
    }
    
    public void setDateReservation(Timestamp dateReservation) {
        this.dateReservation = dateReservation;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getClientNom() {
        return clientNom;
    }
    
    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }
    
    public String getClientPrenom() {
        return clientPrenom;
    }
    
    public void setClientPrenom(String clientPrenom) {
        this.clientPrenom = clientPrenom;
    }
    
    public String getNumeroVol() {
        return numeroVol;
    }
    
    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
    }
    
    public String getAeroportDepart() {
        return aeroportDepart;
    }
    
    public void setAeroportDepart(String aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }
    
    public String getAeroportArrivee() {
        return aeroportArrivee;
    }
    
    public void setAeroportArrivee(String aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
    }
    
    public Timestamp getDateHeureDepart() {
        return dateHeureDepart;
    }
    
    public void setDateHeureDepart(Timestamp dateHeureDepart) {
        this.dateHeureDepart = dateHeureDepart;
    }
    
    public String getClientFullName() {
        return (clientNom != null ? clientNom : "") + " " + (clientPrenom != null ? clientPrenom : "");
    }
    
    public String getNumeroSiege() {
        return numeroSiege;
    }
    
    public void setNumeroSiege(String numeroSiege) {
        this.numeroSiege = numeroSiege;
    }
    
    public Double getPrix() {
        return prix;
    }
    
    public void setPrix(Double prix) {
        this.prix = prix;
    }
}
