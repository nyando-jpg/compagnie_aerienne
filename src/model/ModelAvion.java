package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity: ModelAvion (Modèle d'avion)
 */
public class ModelAvion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String designation;
    private String fabricant;
    private int capacite;
    private Integer autonomieKm;
    private Integer vitesseKmH;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    public ModelAvion() {
    }
    
    public ModelAvion(String designation, String fabricant, int capacite) {
        this.designation = designation;
        this.fabricant = fabricant;
        this.capacite = capacite;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getDesignation() {
        return designation;
    }
    
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    
    public String getFabricant() {
        return fabricant;
    }
    
    public void setFabricant(String fabricant) {
        this.fabricant = fabricant;
    }
    
    public int getCapacite() {
        return capacite;
    }
    
    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }
    
    public Integer getAutonomieKm() {
        return autonomieKm;
    }
    
    public void setAutonomieKm(Integer autonomieKm) {
        this.autonomieKm = autonomieKm;
    }
    
    public Integer getVitesseKmH() {
        return vitesseKmH;
    }
    
    public void setVitesseKmH(Integer vitesseKmH) {
        this.vitesseKmH = vitesseKmH;
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
    
    @Override
    public String toString() {
        return "ModelAvion{" +
                "id=" + id +
                ", designation='" + designation + '\'' +
                ", fabricant='" + fabricant + '\'' +
                ", capacite=" + capacite +
                ", autonomieKm=" + autonomieKm +
                ", vitesseKmH=" + vitesseKmH +
                '}';
    }
}
