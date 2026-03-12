# PROJET BASE DE DONNÉES – SYSTÈME DE VENTE AUX ENCHÈRES

Projet réalisé dans le cadre du module Bases de Données à Grenoble INP – Ensimag en équipe de 5.

## DESCRIPTION

Ce projet consiste en la conception et l’implémentation d’une base de données relationnelle modélisant une plateforme de ventes aux enchères.
Le système permet de gérer des utilisateurs, des produits, des salles de vente et les offres déposées lors d’enchères.

L’objectif principal était de concevoir un modèle de données cohérent, normalisé et capable de supporter différents utilisateurs envoyant des requêtes au même moment.


## SCHÉMA RELATIONNEL

Le modèle conceptuel a été traduit en relations SQL comprenant notamment :

### UTILISATEUR
EmailUser (clé primaire), NomUser, PrenomUser, AdressePostale

### PRODUIT
IdProduit (clé primaire), NomProduit, PrixRevient, Stock, NomCat

### VENTE
IdVente (clé primaire), IdProduit, IdSalle, PrixDepart, TypeVente, DateHeureDebutVente, LimiteOffre, VenteRevocable

### OFFRE
DateHeureDepotOffre (clé primaire), EmailUser, IdVente, PrixOffrePropose, QuantiteProduit

Un choix de conception important a été de modéliser l’entité OFFRE comme une entité faible, dépendant d’un utilisateur et d’une vente.

### CATÉGORIE
NomCat (clé primaire), DescriptionCat

### SALLE
IdSalle (clé primaire), NomCat

### CARACTÉRISTIQUE
NomCaractéristique (clé primaire), IdProduit, ValeurCaractéristique

Contraintes de clés primaires et étrangères assurent la cohérence entre les relations.

## NORMALISATION

Le schéma relationnel respecte les propriétés suivantes :

- Première forme normale (1FN) : attributs atomiques
- Deuxième forme normale (2FN) : dépendance complète à la clé
- Troisième forme normale (3FN)
- Forme normale de Boyce-Codd (BCNF)

Ces propriétés garantissent l’absence de redondances inutiles et améliorent la cohérence des données.

## RÈGLES SUPPLÉMENTAIRES ET CONTRAINTES

Plusieurs contraintes ont été intégrées au système :

* Le prix de départ d’une vente doit être strictement positif
* Le prix de revient d’un produit doit être positif
* Une offre doit concerner une quantité strictement positive
* Une vente peut être annulée si le prix de revient n’est pas atteint
* Dans les enchères descendantes, le premier utilisateur proposant d’acheter tout le lot remporte la vente

## EXEMPLES DE FONCTIONNALITÉS

* Création d’une salle de vente et lancement d’une vente
Insertion d’un produit dans une salle et configuration des paramètres d’enchère.
* Dépôt d’une offre par un utilisateur
* Vérification préalable de la validité de l’offre selon le type d’enchère (croissante ou descendante).
* Détermination du gagnant d’une enchère
* Requête SQL identifiant l’utilisateur ayant proposé l’offre valide la plus avantageuse selon les règles définies.

## POINTS TECHNIQUES INTÉRESSANTS

* Gestion de différents types d’enchères (croissantes et descendantes)
* Vérification de validité des offres via requêtes SQL
* Utilisation de sous-requêtes pour déterminer les meilleures offres
* Implémentation de contraintes métier dans les requêtes


## COMPÉTENCES DÉVELOPPÉES

* Conception de schémas relationnels complexes
* Raisonnement sur les dépendances fonctionnelles
* Normalisation avancée des bases de données
* Écriture de requêtes SQL avancées
* Traduction de règles métier en contraintes de données
