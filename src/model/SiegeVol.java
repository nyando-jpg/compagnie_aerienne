package model;

public class SiegeVol {
    private int id;
    private int volOpereId;
    private int siegeId;
    private String statut;
    private String numeroSiege;
    private String classeLibelle;
    private int classeSiegeId;

    public SiegeVol() {}

    public SiegeVol(int id, int volOpereId, int siegeId, String statut) {
        this.id = id;
        this.volOpereId = volOpereId;
        this.siegeId = siegeId;
        this.statut = statut;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVolOpereId() { return volOpereId; }
    public void setVolOpereId(int volOpereId) { this.volOpereId = volOpereId; }

    public int getSiegeId() { return siegeId; }
    public void setSiegeId(int siegeId) { this.siegeId = siegeId; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getNumeroSiege() { return numeroSiege; }
    public void setNumeroSiege(String numeroSiege) { this.numeroSiege = numeroSiege; }

    public String getClasseLibelle() { return classeLibelle; }
    public void setClasseLibelle(String classeLibelle) { this.classeLibelle = classeLibelle; }

    public int getClasseSiegeId() { return classeSiegeId; }
    public void setClasseSiegeId(int classeSiegeId) { this.classeSiegeId = classeSiegeId; }
}
