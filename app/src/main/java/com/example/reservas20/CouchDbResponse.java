package com.example.reservas20;

public class CouchDbResponse {
    private String id;  // ID del documento en CouchDB
    private String rev; // Revisión del documento en CouchDB

    // Constructor vacío
    public CouchDbResponse() {
    }

    // Constructor que inicializa los campos
    public CouchDbResponse(String id, String rev) {
        this.id = id;
        this.rev = rev;
    }

    // Getter para id
    public String getId() {
        return id;
    }

    // Setter para id
    public void setId(String id) {
        this.id = id;
    }

    // Getter para rev
    public String getRev() {
        return rev;
    }

    // Setter para rev
    public void setRev(String rev) {
        this.rev = rev;
    }


}


