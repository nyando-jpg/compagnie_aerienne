package model;

import java.sql.Timestamp;

public class Diffusion {
    int id;
    int idSociete;
    int idVolOpere;
    Timestamp date;
    int nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSociete() {
        return idSociete;
    }

    public void setIdSociete(int idSociete) {
        this.idSociete = idSociete;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public int getIdVolOpere() {
        return idVolOpere;
    }

    public void setIdVolOpere(int idVolOpere) {
        this.idVolOpere = idVolOpere;
    }
}
