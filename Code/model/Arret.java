package snchbilletterie.model;

public class Arret {

    private int idArret;
    private String nom;

    public Arret() {
    }

    public Arret(int idArret, String nom) {
        this.idArret = idArret;
        this.nom = nom;
    }

    public int getIdArret() {
        return idArret;
    }

    public void setIdArret(int idArret) {
        this.idArret = idArret;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}