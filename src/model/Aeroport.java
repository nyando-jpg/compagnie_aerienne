package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity: Aéroport
 */
public class Aeroport implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String codeAeroport;
    private String nom;
    private String ville;
    private String pays;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    public Aeroport() {
    }
    
    public Aeroport(int id, String codeAeroport, String nom, String ville, String pays) {
        this.id = id;
        this.codeAeroport = codeAeroport;
        this.nom = nom;
        this.ville = ville;
        this.pays = pays;
    }
    
    public Aeroport(String codeAeroport, String nom, String ville, String pays) {
        this.codeAeroport = codeAeroport;
        this.nom = nom;
        this.ville = ville;
        this.pays = pays;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCodeAeroport() {
        return codeAeroport;
    }
    
    public void setCodeAeroport(String codeAeroport) {
        this.codeAeroport = codeAeroport;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getVille() {
        return ville;
    }
    
    public void setVille(String ville) {
        this.ville = ville;
    }
    
    public String getPays() {
        return pays;
    }
    
    public void setPays(String pays) {
        this.pays = pays;
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
        return "Aeroport{" +
                "id=" + id +
                ", codeAeroport='" + codeAeroport + '\'' +
                ", nom='" + nom + '\'' +
                ", ville='" + ville + '\'' +
                ", pays='" + pays + '\'' +
                '}';
    }
}
