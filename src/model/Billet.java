package model;

public class Billet {
    private int id;
    private int reservationId;
    private int siegeVolId;
    private double prix;
    private String statut;

    public Billet() {}

    public Billet(int reservationId, int siegeVolId, double prix, String statut) {
        this.reservationId = reservationId;
        this.siegeVolId = siegeVolId;
        this.prix = prix;
        this.statut = statut;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getSiegeVolId() { return siegeVolId; }
    public void setSiegeVolId(int siegeVolId) { this.siegeVolId = siegeVolId; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
