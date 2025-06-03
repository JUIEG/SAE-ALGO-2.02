package controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import modele.Vente;
import modele.creation.CreateScenario;
import vue.creation.VBoxCreation;

import java.io.IOException;
import java.util.List;
public class ControleurCreation implements EventHandler<ActionEvent> {
    private final VBoxCreation vue;
    // champ pour mémoriser le pseudo choisi comme 'départ'
    private String pseudoDepartCourant = null;
    private String selectionDepart = null;


    public ControleurCreation(VBoxCreation vue) {
        this.vue = vue;
    }

    @Override
    public void handle(ActionEvent event) {
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

    /** Sélection séquentielle :
     * 1. Premier clic → on mémorise le pseudo comme « départ »
     * 2. Deuxième clic → on vérifie qu’il est différent de « départ »,
     *    on crée la Vente et on l’ajoute au tableau.
     */
    private void ajouterVentesParPaires() {
        List<String> selection = vue.getSelectionVerticale();
        if (selection.size() != 1) {
            System.out.println("Sélectionnez exactement un élément à chaque fois.");
            return;
        }
        String pseudoSélectionné = selection.get(0);

        // Si le champ 'départ' n’est pas encore rempli, on l’affecte
        if (pseudoDepartCourant == null) {
            pseudoDepartCourant = pseudoSélectionné;
            System.out.println("Ville départ définie sur : " + pseudoDepartCourant);
            return;
        }

        // Sinon, on est au deuxième choix : on l’utilise comme arrivée
        String pseudoArrivée = pseudoSélectionné;
        // Vérification qu’on ne reprenne pas le même pseudo
        if (pseudoArrivée.equals(pseudoDepartCourant)) {
            System.out.println("Le pseudo de départ et d’arrivée doivent être différents.");
            return;
        }

        // Tout est bon : on crée la vente et on l’ajoute
        Vente vente = new Vente(pseudoDepartCourant, pseudoArrivée);
        vue.ajouterVentes(List.of(vente));

        // On réinitialise pour la prochaine paire
        pseudoDepartCourant = null;
        System.out.println("Vente ajoutée : " + vente.getVendeur() + " → " + vente.getAcheteur());
    }

    private void creerOuModifierScenario() {
        // Si on est en mode modification, il faut un scénario sélectionné
        if (!vue.isModeCreation() && !vue.isScenarioSelectionne()) {
            System.out.println("⚠ Aucun scénario sélectionné.");
            return;
        }

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
                int numero = vue.getNumeroScenarioActuel();
                CreateScenario.modifierScenario(numero, ventes);
                System.out.println("Scénario modifié.");
            }

            vue.reinitialiserFormulaire();
            vue.rafraichirListeScenario();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }



}

