package controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import modele.Vente;
import modele.creation.CreateScenario;
import vue.creation.VBoxCreation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControleurCreation implements EventHandler<ActionEvent> {

    private final VBoxCreation vue;

    public ControleurCreation(VBoxCreation vue) {
        this.vue = vue;
    }

    @Override
    public void handle(ActionEvent event) {
        System.out.println("Bouton intéragie  " + event.getSource());
        Object source = event.getSource();

        if (source instanceof Button button) {
            String id = button.getId();

            switch (id) {
                case "ajouterVente" -> ajouterVentesParPaires();
                case "creerScenario" -> creerOuModifierScenario();
                default -> System.err.println("Bouton non reconnu : " + id);
            }
        }
    }

    /** Convertit les villes sélectionnées en paires vente (départ → arrivée) */
    private void ajouterVentesParPaires() {
        List<String> pseudos = vue.getSelectionVerticale(); // Liste des pseudos sélectionnés
        if (pseudos.size() < 2) {
            System.out.println("Sélectionnez au moins deux entrées.");
            return;
        }

        List<Vente> nouvelles = new ArrayList<>();
        for (int i = 0; i < pseudos.size() - 1; i += 2) {
            String pseudoDepart = pseudos.get(i);
            String pseudoArrivee = pseudos.get(i + 1);
            nouvelles.add(new Vente(pseudoDepart, pseudoArrivee));
        }

        vue.ajouterVentes(nouvelles);
    }

    /** Crée ou modifie un scénario selon le mode */
    private void creerOuModifierScenario() {
        try {
            List<Vente> ventes = vue.getListeVentes();
            if (ventes.isEmpty()) {
                System.out.println("Aucune vente à enregistrer.");
                return;
            }

            if (vue.isModeCreation()) {
                CreateScenario.enregistrerScenario(ventes);
                System.out.println("Nouveau scénario créé.");
            } else {
                // TODO : implémenter l'écrasement d'un scénario existant
                System.out.println("Mode modification non encore implémenté.");
            }

            vue.reinitialiserFormulaire();
            vue.rafraichirListeScenario();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }
}
