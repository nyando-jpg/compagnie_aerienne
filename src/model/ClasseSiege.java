package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity: Classe de Siège (Economique, Affaires, Première)
 */
public class ClasseSiege implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String libelle;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    public ClasseSiege() {
    }
    
    public ClasseSiege(int id, String libelle, String description) {
        this.id = id;
        this.libelle = libelle;
        this.description = description;
    }
    
    public ClasseSiege(String libelle, String description) {
        this.libelle = libelle;
        this.description = description;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
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
        return "ClasseSiege{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
