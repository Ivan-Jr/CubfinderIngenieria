package com.example.reservas20;

public class Reserva {
    private String _id; // ID del documento en CouchDB
    private String _rev; // Revisión del documento
    private String fecha;
    private String hora;
    private int numPersonas;

    // Constructor
    public Reserva(String fecha, String hora, int numPersonas) {
        this.fecha = fecha;
        this.hora = hora;
        this.numPersonas = numPersonas;
    }

    // Getters y setters
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getRev() {
        return _rev;
    }

    public void setRev(String rev) {
        this._rev = rev;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }
}
