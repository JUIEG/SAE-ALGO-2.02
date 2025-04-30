import java.io.*;
import java.util.*;

public class PokemonDelivery {
    public static List<Membre> chargerMembresAvecScanner(String cheminFichier) throws FileNotFoundException {
        List<Membre> membres = new ArrayList<>();
        Scanner scanner = new Scanner(new File(cheminFichier));
        while (scanner.hasNextLine()) {
            String ligne = scanner.nextLine();
            String[] parties = ligne.split(" ");
            membres.add(new Membre(parties[0], parties[1]));
        }
        scanner.close();
        return membres;
    }

}

