package com.example.kolokvijum1;

// =============================================
// Zadatak.java - MODEL KLASA
// =============================================
// Predstavlja jedan zadatak u listi.
// Nema Android logike - cista Java klasa.
// ZadatakAdapter.java koristi ovu klasu za prikaz.
// =============================================

public class Zadatak {

    private String naziv;   // Naziv zadatka (unosi se u formi)
    private String vreme;   // Vreme izvrsenja (bira se TimePicker-om)

    // Konstruktor - poziva se kada se kreira novi zadatak
    public Zadatak(String naziv, String vreme) {
        this.naziv = naziv;
        this.vreme = vreme;
    }

    // Getteri - adapter ih koristi za citanje podataka
    public String getNaziv() {
        return naziv;
    }

    public String getVreme() {
        return vreme;
    }
}
