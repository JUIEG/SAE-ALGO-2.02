package modele;

public class Membre {
    private String pseudo;
    private String ville;

    public Membre(String pseudo, String ville) {
        this.pseudo = pseudo;
        this.ville = ville;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getVille() {
        return ville;
    }

    @Override
    public String toString() {
        return pseudo + " (" + ville + ")";
    }
}