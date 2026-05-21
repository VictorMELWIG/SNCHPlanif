package snchbilletterie.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Trajet {

    private int idTrajet;
    private LocalDate dateDepart;
    private LocalTime heureDepart;
    private LocalTime heureArriveePrevue;

    private Train train;
    private Itineraire itineraire;

    private int idArretDepart;
    private int idArretArrive;

    public Trajet() {}

    // ✅ constructeur SANS ID (utilisé pour INSERT)
    public Trajet(LocalDate dateDepart,
                  LocalTime heureDepart,
                  LocalTime heureArriveePrevue,
                  Train train,
                  Itineraire itineraire) {

        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.heureArriveePrevue = heureArriveePrevue;
        this.train = train;
        this.itineraire = itineraire;
    }

    // ✅ constructeur AVEC ID (utilisé pour SELECT)
    public Trajet(int idTrajet,
                  LocalDate dateDepart,
                  LocalTime heureDepart,
                  LocalTime heureArriveePrevue,
                  Train train,
                  Itineraire itineraire) {

        this.idTrajet = idTrajet;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.heureArriveePrevue = heureArriveePrevue;
        this.train = train;
        this.itineraire = itineraire;
    }

    // GETTERS

    public int getIdTrajet() {
        return idTrajet;
    }

    public LocalDate getDateDepart() {
        return dateDepart;
    }

    public LocalTime getHeureDepart() {
        return heureDepart;
    }

    public LocalTime getHeureArriveePrevue() {
        return heureArriveePrevue;
    }

    public Train getTrain() {
        return train;
    }

    public Itineraire getItineraire() {
        return itineraire;
    }

    public int getIdArretDepart() {
        return idArretDepart;
    }

    public int getIdArretArrive() {
        return idArretArrive;
    }

    // SETTERS

    public void setIdTrajet(int idTrajet) {
        this.idTrajet = idTrajet;
    }

    public void setDateDepart(LocalDate dateDepart) {
        this.dateDepart = dateDepart;
    }

    public void setHeureDepart(LocalTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public void setHeureArriveePrevue(LocalTime heureArriveePrevue) {
        this.heureArriveePrevue = heureArriveePrevue;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public void setItineraire(Itineraire itineraire) {
        this.itineraire = itineraire;
    }

    public void setIdArretDepart(int idArretDepart) {
        this.idArretDepart = idArretDepart;
    }

    public void setIdArretArrive(int idArretArrive) {
        this.idArretArrive = idArretArrive;
    }

    @Override
    public String toString() {
        return "Trajet " + idTrajet + " (" + dateDepart + " " + heureDepart + ")";
    }
}