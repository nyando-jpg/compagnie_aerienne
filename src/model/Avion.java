package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity: Avion
 */
public class Avion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String codeAvion;
    private int modelAvionId;
    private int etatAvionId;
    private int capaciteTotale;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // For display (from JOINs)
    private String designationModel;
    private String libelleEtat;
    
    public Avion() {
    }
    
    public Avion(String codeAvion, int modelAvionId, int etatAvionId, int capaciteTotale) {
        this.codeAvion = codeAvion;
        this.modelAvionId = modelAvionId;
        this.etatAvionId = etatAvionId;
        this.capaciteTotale = capaciteTotale;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCodeAvion() {
        return codeAvion;
    }
    
    public void setCodeAvion(String codeAvion) {
        this.codeAvion = codeAvion;
    }
    
    public int getModelAvionId() {
        return modelAvionId;
    }
    
    public void setModelAvionId(int modelAvionId) {
        this.modelAvionId = modelAvionId;
    }
    
    public int getEtatAvionId() {
        return etatAvionId;
    }
    
    public void setEtatAvionId(int etatAvionId) {
        this.etatAvionId = etatAvionId;
    }
    
    public int getCapaciteTotale() {
        return capaciteTotale;
    }
    
    public void setCapaciteTotale(int capaciteTotale) {
        this.capaciteTotale = capaciteTotale;
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
    
    public String getDesignationModel() {
        return designationModel;
    }
    
    public void setDesignationModel(String designationModel) {
        this.designationModel = designationModel;
    }
    
    public String getLibelleEtat() {
        return libelleEtat;
    }
    
    public void setLibelleEtat(String libelleEtat) {
        this.libelleEtat = libelleEtat;
    }
    
    @Override
    public String toString() {
        return "Avion{" +
                "id=" + id +
                ", codeAvion='" + codeAvion + '\'' +
                ", modelAvionId=" + modelAvionId +
                ", etatAvionId=" + etatAvionId +
                ", capaciteTotale=" + capaciteTotale +
                '}';
    }
}
