package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity: Paiement
 */
public class Paiement implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int reservationId;
    private double montant;
    private String statut;
    private Timestamp datePaiement;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Liste des méthodes de paiement utilisées pour ce paiement
    private List<PaiementMethodeDetail> methodeDetails;
    
    public Paiement() {
        this.methodeDetails = new ArrayList<>();
    }
    
    public Paiement(int reservationId, double montant, String statut, Timestamp datePaiement) {
        this.reservationId = reservationId;
        this.montant = montant;
        this.statut = statut;
        this.datePaiement = datePaiement;
        this.methodeDetails = new ArrayList<>();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    
    public double getMontant() {
        return montant;
    }
    
    public void setMontant(double montant) {
        this.montant = montant;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    public Timestamp getDatePaiement() {
        return datePaiement;
    }
    
    public void setDatePaiement(Timestamp datePaiement) {
        this.datePaiement = datePaiement;
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
    
    public List<PaiementMethodeDetail> getMethodeDetails() {
        return methodeDetails;
    }
    
    public void setMethodeDetails(List<PaiementMethodeDetail> methodeDetails) {
        this.methodeDetails = methodeDetails;
    }
    
    public void addMethodeDetail(PaiementMethodeDetail detail) {
        this.methodeDetails.add(detail);
    }
    
    /**
     * Classe interne pour stocker les détails d'une méthode de paiement utilisée
     */
    public static class PaiementMethodeDetail implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private int id;
        private int paiementId;
        private int methodePaiementId;
        private String methodePaiementLibelle;
        private double montant;
        
        public PaiementMethodeDetail() {
        }
        
        public PaiementMethodeDetail(int methodePaiementId, String methodePaiementLibelle, double montant) {
            this.methodePaiementId = methodePaiementId;
            this.methodePaiementLibelle = methodePaiementLibelle;
            this.montant = montant;
        }
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public int getPaiementId() {
            return paiementId;
        }
        
        public void setPaiementId(int paiementId) {
            this.paiementId = paiementId;
        }
        
        public int getMethodePaiementId() {
            return methodePaiementId;
        }
        
        public void setMethodePaiementId(int methodePaiementId) {
            this.methodePaiementId = methodePaiementId;
        }
        
        public String getMethodePaiementLibelle() {
            return methodePaiementLibelle;
        }
        
        public void setMethodePaiementLibelle(String methodePaiementLibelle) {
            this.methodePaiementLibelle = methodePaiementLibelle;
        }
        
        public double getMontant() {
            return montant;
        }
        
        public void setMontant(double montant) {
            this.montant = montant;
        }
    }
}
