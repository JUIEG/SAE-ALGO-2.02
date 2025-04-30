public class Ville {
    private String nom;

    public Ville(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ville)) return false;
        Ville autre = (Ville) obj;
        return nom.equalsIgnoreCase(autre.nom);
    }

    @Override
    public String toString() {
        return nom;
    }
}

