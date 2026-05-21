package snchbilletterie.model;

public class Itineraire {

    private int idItineraire;
    private int dureePrevue;

    private String villeDepart;
    private String villeArrivee;

    public Itineraire() {
    }

    public Itineraire(int idItineraire, int dureePrevue) {
        this.idItineraire = idItineraire;
        this.dureePrevue = dureePrevue;
    }

    public int getIdItineraire() {
        return idItineraire;
    }

    public void setIdItineraire(int idItineraire) {
        this.idItineraire = idItineraire;
    }

    public int getDureePrevue() {
        return dureePrevue;
    }

    public void setDureePrevue(int dureePrevue) {
        this.dureePrevue = dureePrevue;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    @Override
    public String toString() {
        if (villeDepart != null && villeArrivee != null
                && !villeDepart.isBlank() && !villeArrivee.isBlank()) {
            return villeDepart + " → " + villeArrivee + " (" + dureePrevue + " min)";
        }

        return "Itinéraire " + idItineraire + " (" + dureePrevue + " min)";
    }
}