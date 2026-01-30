package model;

import java.math.BigDecimal;

public class CATotal {
    private String aeroportDepart;
    private String aeroportArrivee;
    private String avion;
    private String dateDepart;
    private String heureDepart;
    private BigDecimal caPlace;
    private BigDecimal caDiffusion;
    private BigDecimal caTotal;
    private BigDecimal montantPayeDiffusion;
    private BigDecimal resteAPayerDiffusion;
    private BigDecimal caExtra;

    public BigDecimal getCaExtra() {
        return caExtra;
    }

    public void setCaExtra(BigDecimal caExtra) {
        this.caExtra = caExtra;
    }

    public String getAeroportDepart() {
        return aeroportDepart;
    }

    public void setAeroportDepart(String aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }

    public String getAeroportArrivee() {
        return aeroportArrivee;
    }

    public void setAeroportArrivee(String aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
    }

    public String getAvion() {
        return avion;
    }

    public void setAvion(String avion) {
        this.avion = avion;
    }

    public String getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public String getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(String heureDepart) {
        this.heureDepart = heureDepart;
    }

    public BigDecimal getCaPlace() {
        return caPlace;
    }

    public void setCaPlace(BigDecimal caPlace) {
        this.caPlace = caPlace;
    }

    public BigDecimal getCaDiffusion() {
        return caDiffusion;
    }

    public void setCaDiffusion(BigDecimal caDiffusion) {
        this.caDiffusion = caDiffusion;
    }

    public BigDecimal getCaTotal() {
        return caTotal;
    }

    public void setCaTotal(BigDecimal caTotal) {
        this.caTotal = caTotal;
    }

    public BigDecimal getMontantPayeDiffusion() {
        return montantPayeDiffusion;
    }

    public void setMontantPayeDiffusion(BigDecimal montantPayeDiffusion) {
        this.montantPayeDiffusion = montantPayeDiffusion;
    }

    public BigDecimal getResteAPayerDiffusion() {
        return resteAPayerDiffusion;
    }

    public void setResteAPayerDiffusion(BigDecimal resteAPayerDiffusion) {
        this.resteAPayerDiffusion = resteAPayerDiffusion;
    }
}
