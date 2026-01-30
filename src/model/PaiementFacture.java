package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PaiementFacture {
    private Timestamp datePaiement;
    private BigDecimal montant;
    private String diffusionInfo;

    public Timestamp getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Timestamp datePaiement) {
        this.datePaiement = datePaiement;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getDiffusionInfo() {
        return diffusionInfo;
    }

    public void setDiffusionInfo(String diffusionInfo) {
        this.diffusionInfo = diffusionInfo;
    }
}
