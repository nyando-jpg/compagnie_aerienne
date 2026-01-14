package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity: LigneVol (Route abstraite de vol)
 */
public class LigneVol implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String numeroVol;
    private int aeroportDepartId;
    private int aeroportArriveeId;
    private int dureeEstimeeMinutes;
    private Integer distanceKm;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Pour affichage (jointures)
    private String aeroportDepartNom;
    private String aeroportArriveeNom;
    
    public LigneVol() {
    }
    
    public LigneVol(String numeroVol, int aeroportDepartId, int aeroportArriveeId, int dureeEstimeeMinutes) {
        this.numeroVol = numeroVol;
        this.aeroportDepartId = aeroportDepartId;
        this.aeroportArriveeId = aeroportArriveeId;
        this.dureeEstimeeMinutes = dureeEstimeeMinutes;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNumeroVol() {
        return numeroVol;
    }
    
    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
    }
    
    public int getAeroportDepartId() {
        return aeroportDepartId;
    }
    
    public void setAeroportDepartId(int aeroportDepartId) {
        this.aeroportDepartId = aeroportDepartId;
    }
    
    public int getAeroportArriveeId() {
        return aeroportArriveeId;
    }
    
    public void setAeroportArriveeId(int aeroportArriveeId) {
        this.aeroportArriveeId = aeroportArriveeId;
    }
    
    public int getDureeEstimeeMinutes() {
        return dureeEstimeeMinutes;
    }
    
    public void setDureeEstimeeMinutes(int dureeEstimeeMinutes) {
        this.dureeEstimeeMinutes = dureeEstimeeMinutes;
    }
    
    public Integer getDistanceKm() {
        return distanceKm;
    }
    
    public void setDistanceKm(Integer distanceKm) {
        this.distanceKm = distanceKm;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    @Override
    public String toString() {
        return "LigneVol{" +
                "id=" + id +
                ", numeroVol='" + numeroVol + '\'' +
                ", aeroportDepart=" + aeroportDepartNom +
                ", aeroportArrivee=" + aeroportArriveeNom +
                ", duree=" + dureeEstimeeMinutes + "min" +
                '}';
    }
}
