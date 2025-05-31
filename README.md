SAÉ 2.02 — Recherche des k meilleurs itinéraires avec contraintes de livraison
==========================================================================

## Introduction

Ce projet a pour objectif de trouver les k meilleurs parcours de livraison dans un réseau de villes, en respectant la contrainte que le vendeur doit toujours être visité avant l’acheteur dans le parcours. Le point de départ et d’arrivée est la ville de Vélizy.

Les données utilisées sont :
- Une liste de membres identifiés par un pseudo, associés à une ville.
- Une liste de ventes représentant les contraintes vendeur → acheteur.
- Une matrice des distances entre villes.

Le but est d’implémenter un algorithme capable de générer les k itinéraires les plus courts, valides selon ces contraintes.

---

## Classes principales

### Classe AlgoBase

La classe `AlgoBase` est la classe de base fournissant des fonctionnalités communes à tous les algorithmes de recherche d’itinéraires. Elle offre notamment des méthodes pour vérifier les contraintes de livraison, calculer la distance totale d’un parcours, et générer des permutations possibles. 

---

### Classe AlgoKpossibilite

La classe `AlgoKpossibilite` implémente la recherche des k meilleurs parcours valides à l’aide d’une exploration en profondeur (`dfs`) combinée à une structure `PriorityQueue` pour conserver les meilleurs résultats.

#### Fonctionnement

- **Préparation des données** : à partir des ventes, on déduit les villes concernées et on prépare la liste des contraintes.
- **Exploration** : l’algorithme effectue un parcours en profondeur à partir de la ville "Velizy", en respectant les contraintes de visite (le vendeur doit être visité avant l’acheteur).
- **Validation** : dès qu’un parcours couvre toutes les villes, il est validé et ajouté dans une `PriorityQueue` (un tas prioritaire) qui garde uniquement les k meilleurs (les plus courts).
- **Retour** : la liste finale contient les k parcours ordonnés du plus court au plus long.

#### Détails techniques

- Le `PriorityQueue<Trajet>` est utilisé en tant que max-heap inversé afin de toujours retirer le trajet le plus long lorsque la taille dépasse k.
- Le parcours utilise un ensemble `visited` pour éviter les visites répétées de villes.
- Les contraintes sont vérifiées à chaque étape pour garantir la validité du chemin.

---

### Classe Util

La classe `Util` fournit des méthodes utilitaires pour charger les données depuis des fichiers et calculer des distances.

#### Méthodes

- `chargerMembres(String chemin)` : charge une map pseudo → ville à partir d’un fichier texte.
- `chargerVentes(String chemin)` : charge la liste des ventes (vendeur → acheteur) à partir d’un fichier.
- `chargerDistances(String chemin)` : lit une matrice des distances et construit une structure `DistanceMap`.
- `calculerDistance(List<String> parcours, DistanceMap distances)` : calcule la distance totale d’un parcours donné.

#### Rôle

Cette classe centralise la gestion des données, permettant d’isoler la lecture des fichiers et de préparer les données sous forme exploitable pour les algorithmes.

---

## Conclusion

Ce projet combine la gestion de données relationnelles avec des contraintes de précédence et un problème d’optimisation classique (parcours du voyageur). L’utilisation de la programmation récursive et des structures comme `PriorityQueue` permet d’obtenir efficacement les meilleurs parcours valides. La modularité des classes facilite l’évolution du projet pour intégrer d’autres algorithmes ou d’autres contraintes.

-
