package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity: Type Client
 */
public class TypeClient implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String libelle;
    private String description;
    private String modeCalcul; // FIXE ou POURCENTAGE
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public TypeClient() {
    }

    public TypeClient(String libelle, String description) {
        this.libelle = libelle;
        this.description = description;
        this.modeCalcul = "FIXE"; // Par défaut
    }

    // Getters et Setters
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

    public String getModeCalcul() {
        return modeCalcul;
    }

    public void setModeCalcul(String modeCalcul) {
        this.modeCalcul = modeCalcul;
    }

    @Override
    public String toString() {
        return "TypeClient{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
