-- Vérifier si une catégorie existe
SELECT * FROM CATEGORIE WHERE nomCat = 'Électronique';

-- Insérer une nouvelle catégorie si elle n'existe pas
INSERT INTO CATEGORIE (nomCat, DescriptionCat)
VALUES ('NouvelleCatégorie', 'Description de la catégorie')
ON CONFLICT (nomCat) DO NOTHING;


-- Vérifier si un produit existe
SELECT * FROM PRODUIT WHERE IdProduit = 1;


-- Ajouter un produit
INSERT INTO PRODUIT (IdProduit, nomCat, nomProduit, PrixRevient, Stock)
VALUES (9, 'Électronique', 'Casque Bluetooth', 50.00, 20)
ON CONFLICT (IdProduit) DO NOTHING;

-- Vérifier les contraintes (en amont via des triggers ou scripts SQL supplémentaires)
-- Exemple : Vérification que le prix et le stock soient positifs
-- Sinon lever une erreur via des CHECK ou des fonctions PL/pgSQL.

-- Vérifier si le stock est suffisant
SELECT Stock FROM PRODUIT WHERE IdProduit = 1;

-- Réduire le stock d’un produit
UPDATE PRODUIT
SET Stock = Stock - 5
WHERE IdProduit = 1
AND Stock >= 5;


-- Vérifier si une salle existe
SELECT * FROM SALLE WHERE IdSalle = 1;

-- Ajouter une salle
INSERT INTO SALLE (IdSalle, nomCat)
VALUES (7, 'Électronique')
ON CONFLICT (IdSalle) DO NOTHING;



-- Vérifier la correspondance des catégories
SELECT p.nomCat AS CatProduit, s.nomCat AS CatSalle
FROM PRODUIT p
JOIN SALLE s ON p.nomCat = s.nomCat
WHERE p.IdProduit = 1 AND s.IdSalle = 1;

-- Ajouter une vente
INSERT INTO VENTE (IdVente, IdSalle, IdProduit, PrixDépart, TypeVente, VenteRevocable, DateHeureDebutVente, LimiteOffre)
VALUES (10, 1, 1, 100.00, 'croissante', TRUE, '2024-12-10 10:00:00', TRUE);



-- Type d'enchère croissante : Plus haute offre
SELECT *
FROM OFFRE
WHERE IdVente = 1
ORDER BY PrixOffreProposé DESC
LIMIT 1;

-- Type d'enchère décroissante : Dernière offre placée
SELECT *
FROM OFFRE
WHERE IdVente = 1
ORDER BY DateHeureDépotOffre DESC
LIMIT 1;



-- Récupérer le gagnant
SELECT o.EmailUser, o.QuantitéProduit, p.IdProduit
FROM OFFRE o
JOIN VENTE v ON o.IdVente = v.IdVente
WHERE v.IdVente = 1
ORDER BY o.PrixOffreProposé DESC
LIMIT 1;

-- Réduire le stock du produit en fonction de l'offre gagnante
UPDATE PRODUIT
SET Stock = Stock - (SELECT QuantitéProduit FROM OFFRE WHERE IdVente = 1 ORDER BY PrixOffreProposé DESC LIMIT 1)
WHERE IdProduit = (SELECT IdProduit FROM VENTE WHERE IdVente = 1);




-- Vérifier si l'utilisateur existe
SELECT * FROM UTILISATEUR WHERE EmailUser = 'john.doe@example.com';

-- Ajouter un nouvel utilisateur
INSERT INTO UTILISATEUR (EmailUser, NomUser, PrenomUser, AdressePostale)
VALUES ('john.doe@example.com', 'John', 'Doe', '123 Main Street')
ON CONFLICT (EmailUser) DO NOTHING;



-- Générer un rapport d'enchère
SELECT v.IdVente, p.nomProduit AS Produit, o.EmailUser AS Utilisateur, o.PrixOffreProposé AS Prix, o.QuantitéProduit AS Quantité
FROM VENTE v
JOIN OFFRE o ON v.IdVente = o.IdVente
JOIN PRODUIT p ON v.IdProduit = p.IdProduit
WHERE v.IdVente = 1
ORDER BY o.PrixOffreProposé DESC;
