# Rapport SAÉ 2.01 & 2.02 – Application APPLI

## 1. Introduction

L'application **APPLI** est un projet Java réalisé dans le cadre des SAÉ 2.01 (Développement) et 2.02 (Algorithmique). Elle vise à optimiser les livraisons de cartes Pokémon entre les membres d'une association, l’**APPLI** (Association Publique des Pokémonistes Libres et Indépendants).

Le président de l'association, résidant à Vélizy, souhaite effectuer toutes les livraisons en personne, en minimisant les distances parcourues. L'application permet donc de charger un scénario de ventes, de choisir un algorithme de calcul de l’itinéraire, et de visualiser le parcours et sa distance totale.


## 2. Conception

### 2.1 Architecture MVC

L’application suit le modèle **MVC** (Modèle - Vue - Contrôleur) :

- **Modèle** : Représente les données (membres, ventes, distances) et implémente les algorithmes de calcul d'itinéraire.
- **Vue** : Interface JavaFX qui permet à l'utilisateur d'interagir avec l'application.
- **Contrôleur** : Lie la vue et le modèle, déclenche les traitements en fonction des actions utilisateur.


### 2.2 Détails des composants

#### 2.2.1 Modèle

- **`DistanceMap`**  
  Gère les distances entre les villes.  
  Utilise une structure de type `Map<String, Map<String, Integer>>`.  
  Permet l’ajout, la récupération et la vérification de distances. Chargé depuis un fichier texte.

- **`Vente`** *(présumée)*  
  Représente une transaction entre un vendeur et un acheteur, chacun associé à une ville.

- **`Membre`** *(présumé)*  
  Contient les informations de pseudonyme et ville d’un membre.

#### 2.2.2 Vue (JavaFX)

- **`VBoxRoot`**  
  Racine de l’interface. Contient tous les composants visuels. Singleton.

- **`MenuAlgoScenario`**  
  Menu horizontal avec :
  - Choix du scénario
  - Choix de l’algorithme
  - Sélection de la méthode heuristique (greedy)
  - Paramètre *k* pour les k meilleures solutions
  - Bouton "Valider"

- **`AffichageChemin`**  
  Affiche un tableau avec les étapes du parcours (position, ville, distance).

- **`VBoxParcours`**  
  Liste les villes parcourues avec un label indiquant la distance totale.

- **`HBoxResultat`**  
  Contient côte à côte `AffichageChemin` et `VBoxParcours`.

#### 2.2.3 Contrôleur

- **`ControleurAppli`**  
  Gère les événements liés au bouton "Valider".  
  En fonction des choix de l’utilisateur, appelle le bon algorithme et met à jour l’affichage.



### 2.3 Structures de données

- **HashMap** : utilisée pour stocker les distances et les membres.
- **ArrayList** : utilisée pour les parcours et les ventes.
- **ObservableList** : pour lier les données aux composants JavaFX (`ListView`, `TableView`).
- **Map imbriquée** : pour représenter les distances entre villes efficacement.


### 2.4 Algorithmes

#### Algorithme de base (cours de graphes)
- Produit un parcours valide (vendeur avant acheteur), sans garantie d’optimalité.
- Simple et rapide pour les petits scénarios.

#### Algorithme heuristique
- Repose sur des choix locaux pour construire un parcours :
  - Ville la plus proche
  - Ville la plus éloignée
  - Ordre alphabétique
  - Ville qui débloque le plus de livraisons
  - Ville la moins visitée
- Ne donne pas toujours le meilleur résultat, mais très efficace pour de grands scénarios.

#### Algorithme des k meilleures solutions
- Calcule les *k* meilleurs itinéraires selon la distance.
- Enumère toutes les solutions possibles respectant les contraintes.
- Nécessite une exploration partiellement exhaustive, optimisée pour limiter le temps de calcul.

#### Modélisation en graphe
- Pour chaque ville A :
  - Sommet A+ (départ de vente)
  - Sommet A– (arrivée)
- Chaque vente A→B devient un arc de A+ vers B–
- On recherche un ordre de parcours respectant les contraintes (A+ avant B–) tout en minimisant la distance.



## 3. Conclusion

### Bilan

- Application fonctionnelle avec interface complète.
- Prise en charge des scénarios, affichage du parcours et de la distance.
- Architecture propre et modulable (MVC).
- Trois approches algorithmiques disponibles.

### Limites et axes d’amélioration

- Performances à optimiser pour les grands scénarios.
- Ajouter la création/modification de scénarios depuis l’interface.
- Ajouter des messages d’erreur et feedbacks utilisateur.

### Perspectives

- Ajout d’une carte graphique pour visualiser le trajet.
- Optimisation des algorithmes heuristiques avec pondérations.
- Sauvegarde des parcours et export en format CSV ou image.
- Mode simulation ou pas-à-pas du parcours.


## 4. Annexes
- **Fichiers CSS** : le répertoire `/css` contient le fichier `style.css` utilisé pour la mise en forme de l’interface JavaFX.
- **Tests unitaires** : présents dans le dossier `/tests` 
- **Dépôt Git** :  
https://github.com/JUIEG/SAE-ALGO-2.02.git


