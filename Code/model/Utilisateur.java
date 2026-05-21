package snchbilletterie.model;
public class Utilisateur {
    private final int id;
    private final String login;
    private final String nom;
    private final String prenom;
    private final Role role;

    public Utilisateur(int id, String login, String nom, String prenom, Role role) {
        this.id = id;
        this.login = login;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
    }
    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public Role getRole() { return role; }
}
