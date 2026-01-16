package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class MethodePaiement implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String libelle;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public MethodePaiement() {
    }

    public MethodePaiement(String libelle, String description) {
        this.libelle = libelle;
        this.description = description;
    }

    public MethodePaiement(int id, String libelle, String description) {
        this.id = id;
        this.libelle = libelle;
        this.description = description;
    }

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
}