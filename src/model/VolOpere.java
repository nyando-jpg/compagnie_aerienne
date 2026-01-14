package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity: VolOpere (Instance concrète d'un vol)
 */
public class VolOpere implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int ligneVolId;
    private int avionId;
    private Timestamp dateHeureDepart;
    private Timestamp dateHeureArrivee;
    private int statusId;
    private String status;
    private int retardMinutes;
    private String motifAnnulation;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Pour affichage (jointures)
    private String numeroVol;
    private String aeroportDepartNom;
    private String aeroportArriveeNom;
    private String codeAvion;
    
    public VolOpere() {
    }
    
    public VolOpere(int ligneVolId, int avionId, Timestamp dateHeureDepart, Timestamp dateHeureArrivee) {
        this.ligneVolId = ligneVolId;
        this.avionId = avionId;
        this.dateHeureDepart = dateHeureDepart;
        this.dateHeureArrivee = dateHeureArrivee;
        this.status = "PLANIFIE";
        this.retardMinutes = 0;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getLigneVolId() {
        return ligneVolId;
    }
    
    public void setLigneVolId(int ligneVolId) {
        this.ligneVolId = ligneVolId;
    }
    
    public int getAvionId() {
        return avionId;
    }
    
    public void setAvionId(int avionId) {
        this.avionId = avionId;
    }
    
    public Timestamp getDateHeureDepart() {
        return dateHeureDepart;
    }
    
    public void setDateHeureDepart(Timestamp dateHeureDepart) {
        this.dateHeureDepart = dateHeureDepart;
    }
    
    public Timestamp getDateHeureArrivee() {
        return dateHeureArrivee;
    }
    
    public void setDateHeureArrivee(Timestamp dateHeureArrivee) {
        this.dateHeureArrivee = dateHeureArrivee;
    }
    
    public int getStatusId() {
        return statusId;
    }
    
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getRetardMinutes() {
        return retardMinutes;
    }
    
    public void setRetardMinutes(int retardMinutes) {
        this.retardMinutes = retardMinutes;
    }
    
    public String getMotifAnnulation() {
        return motifAnnulation;
    }
    
    public void setMotifAnnulation(String motifAnnulation) {
        this.motifAnnulation = motifAnnulation;
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
    
    public String getNumeroVol() {
        return numeroVol;
    }
    
    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
    }
    
    public String getAeroportDepartNom() {
        return aeroportDepartNom;
    }
    
    public void setAeroportDepartNom(String aeroportDepartNom) {
        this.aeroportDepartNom = aeroportDepartNom;
    }
    
    public String getAeroportArriveeNom() {
        return aeroportArriveeNom;
    }
    
    public void setAeroportArriveeNom(String aeroportArriveeNom) {
        this.aeroportArriveeNom = aeroportArriveeNom;
    }
    
    public String getCodeAvion() {
        return codeAvion;
    }
    
    public void setCodeAvion(String codeAvion) {
        this.codeAvion = codeAvion;
    }
    
    @Override
    public String toString() {
        return "VolOpere{" +
                "id=" + id +
                ", numeroVol='" + numeroVol + '\'' +
                ", dateDepart=" + dateHeureDepart +
                ", status='" + status + '\'' +
                '}';
    }
}
