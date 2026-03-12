INSERT INTO UTILISATEUR (EmailUser, NomUser, PrenomUser, AdressePostale) VALUES ('alice@example.com', 'Alice', 'Dupont', '123 Rue des Fleurs, Paris');
INSERT INTO UTILISATEUR (EmailUser, NomUser, PrenomUser, AdressePostale) VALUES ('bob@example.com', 'Bob', 'Martin', '45 Avenue des Champs, Lyon');
INSERT INTO UTILISATEUR (EmailUser, NomUser, PrenomUser, AdressePostale) VALUES ('charlie@example.com', 'Charlie', 'Durand', '78 Boulevard Haussmann, Marseille');
INSERT INTO UTILISATEUR (EmailUser, NomUser, PrenomUser, AdressePostale) VALUES ('david@example.com', 'David', 'Moreau', '12 Rue Saint-Honore, Paris');
INSERT INTO UTILISATEUR (EmailUser, NomUser, PrenomUser, AdressePostale) VALUES ('eve@example.com', 'Eve', 'Lemoine', '67 Allee des Acacias, Lille');
INSERT INTO UTILISATEUR (EmailUser, NomUser, PrenomUser, AdressePostale) VALUES ('frank@example.com', 'Frank', 'Petit', '23 Boulevard Carnot, Bordeaux');
INSERT INTO UTILISATEUR (EmailUser, NomUser, PrenomUser, AdressePostale) VALUES ('grace@example.com', 'Grace', 'Rousseau', '89 Rue Victor Hugo, Nantes');
INSERT INTO UTILISATEUR (EmailUser, NomUser, PrenomUser, AdressePostale) VALUES ('hannah@example.com', 'Hannah', 'Blanc', '14 Rue de Strasbourg, Toulouse');


INSERT INTO CATEGORIE (nomCat, DescriptionCat) VALUES ('electronique', 'Appareils electroniques comme smartphones, ordinateurs, tablettes');
INSERT INTO CATEGORIE (nomCat, DescriptionCat) VALUES  ('Mobilier', 'Meubles pour la maison, le bureau ou les espaces exterieurs');
INSERT INTO CATEGORIE (nomCat, DescriptionCat) VALUES  ('Vetements', 'Articles de mode pour hommes, femmes et enfants');
INSERT INTO CATEGORIE (nomCat, DescriptionCat) VALUES  ('Sport', 'equipements et vetements sportifs');
INSERT INTO CATEGORIE (nomCat, DescriptionCat) VALUES  ('Automobile', 'Accessoires et pieces pour vehicules');
INSERT INTO CATEGORIE (nomCat, DescriptionCat) VALUES  ('Alimentation', 'Produits alimentaires et boissons');


INSERT INTO SALLE (IdSalle, nomCat) VALUES (1, 'electronique');
INSERT INTO SALLE (IdSalle, nomCat) VALUES (2, 'Mobilier');
INSERT INTO SALLE (IdSalle, nomCat) VALUES (3, 'Vetements');
INSERT INTO SALLE (IdSalle, nomCat) VALUES (4, 'Sport');
INSERT INTO SALLE (IdSalle, nomCat) VALUES (5, 'Automobile');
INSERT INTO SALLE (IdSalle, nomCat) VALUES (6, 'Alimentation');

INSERT INTO PRODUIT (IdProduit, nomCat, nomProduit, PrixRevient, Stock) VALUES (1, 'electronique', 'Smartphone XYZ', 400.00, 15);
INSERT INTO PRODUIT (IdProduit, nomCat, nomProduit, PrixRevient, Stock) VALUES (2, 'electronique', 'Ordinateur Portable ABC', 800.00, 5);
INSERT INTO PRODUIT (IdProduit, nomCat, nomProduit, PrixRevient, Stock) VALUES (3, 'Mobilier', 'Table en chene massif', 120.00, 8);
INSERT INTO PRODUIT (IdProduit, nomCat, nomProduit, PrixRevient, Stock) VALUES (4, 'Vetements', 'Robe de soiree', 40.00, 20);
INSERT INTO PRODUIT (IdProduit, nomCat, nomProduit, PrixRevient, Stock) VALUES (5, 'Sport', 'Tapis de course electrique', 180.00, 4);
INSERT INTO PRODUIT (IdProduit, nomCat, nomProduit, PrixRevient, Stock) VALUES (6, 'Automobile', 'Ensemble de pneus hiver', 80.00, 10);
INSERT INTO PRODUIT (IdProduit, nomCat, nomProduit, PrixRevient, Stock) VALUES (7, 'Alimentation', 'Coffret de chocolats fins', 25.00, 50);
INSERT INTO PRODUIT (IdProduit, nomCat, nomProduit, PrixRevient, Stock) VALUES (8, 'electronique', 'TV Ultra HD 65 pouces', 1000.00, 3);


INSERT INTO VENTE (IdVente, IdSalle, IdProduit, PrixDepart, TypeVente, VenteRevocable, DateHeureDebutVente, LimiteOffre) VALUES (1, 1, 1, 500.00, 'croissante', 1, TO_TIMESTAMP('2024-11-25 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO VENTE (IdVente, IdSalle, IdProduit, PrixDepart, TypeVente, VenteRevocable, DateHeureDebutVente, LimiteOffre) VALUES (2, 1, 2, 300.00, 'decroissante', 0, TO_TIMESTAMP('2024-11-26 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO VENTE (IdVente, IdSalle, IdProduit, PrixDepart, TypeVente, VenteRevocable, DateHeureDebutVente, LimiteOffre) VALUES (3, 2, 3, 150.00, 'croissante', 1, TO_TIMESTAMP('2024-11-27 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 0);
INSERT INTO VENTE (IdVente, IdSalle, IdProduit, PrixDepart, TypeVente, VenteRevocable, DateHeureDebutVente, LimiteOffre) VALUES (4, 3, 4, 70.00, 'decroissante', 1, TO_TIMESTAMP('2024-11-28 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO VENTE (IdVente, IdSalle, IdProduit, PrixDepart, TypeVente, VenteRevocable, DateHeureDebutVente, LimiteOffre) VALUES (5, 4, 5, 200.00, 'croissante', 0, TO_TIMESTAMP('2024-11-29 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO VENTE (IdVente, IdSalle, IdProduit, PrixDepart, TypeVente, VenteRevocable, DateHeureDebutVente, LimiteOffre) VALUES (6, 5, 6, 100.00, 'croissante', 1, TO_TIMESTAMP('2024-12-01 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 0);
INSERT INTO VENTE (IdVente, IdSalle, IdProduit, PrixDepart, TypeVente, VenteRevocable, DateHeureDebutVente, LimiteOffre) VALUES (7, 6, 7, 50.00, 'decroissante', 1, TO_TIMESTAMP('2024-12-02 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 0);
INSERT INTO VENTE (IdVente, IdSalle, IdProduit, PrixDepart, TypeVente, VenteRevocable, DateHeureDebutVente, LimiteOffre) VALUES (8, 1, 8, 1200.00, 'croissante', 0, TO_TIMESTAMP('2024-12-03 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);

INSERT INTO VENTELIMITEE (IdVente, DateHeureFinVente) VALUES (1, TO_TIMESTAMP('2024-11-30 23:59:59', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO VENTELIMITEE (IdVente, DateHeureFinVente) VALUES (3, TO_TIMESTAMP('2024-12-01 20:00:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO VENTELIMITEE (IdVente, DateHeureFinVente) VALUES (5, TO_TIMESTAMP('2024-12-03 18:00:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO VENTELIMITEE (IdVente, DateHeureFinVente) VALUES (6, TO_TIMESTAMP('2024-12-05 21:00:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO VENTELIMITEE (IdVente, DateHeureFinVente) VALUES (8, TO_TIMESTAMP('2024-12-07 23:59:59', 'YYYY-MM-DD HH24:MI:SS'));



INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('Processeur', 1, 'Octa-Core 3GHz');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('Memoire', 1, '128GB');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('ecran', 1, '6.5 pouces AMOLED');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('RAM', 2, '16GB DDR4');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('Stockage', 2, '1TB SSD');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('Materiau', 3, 'Chene massif');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('Couleur', 3, 'Marron');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('Taille', 4, 'M');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('Textile', 4, 'Soie');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('Vitesse', 5, '12km/h max');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('Poids supporte', 5, '150kg');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('Diametre', 6, '16 pouces');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('Goût', 7, 'Chocolat noir et lait');
INSERT INTO CARACTERISTIQUE (nomCaracteristique, IdProduit, valeurCaracteristique) VALUES ('Resolution', 8, '3840x2160 UHD');

INSERT INTO OFFRE (EmailUser, IdVente, PrixOffrePropose, DateHeureDepotOffre, QuantiteProduit) VALUES ('alice@example.com', 1, 520.00, TO_TIMESTAMP('2024-11-25 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO OFFRE (EmailUser, IdVente, PrixOffrePropose, DateHeureDepotOffre, QuantiteProduit) VALUES ('bob@example.com', 1, 530.00, TO_TIMESTAMP('2024-11-25 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO OFFRE (EmailUser, IdVente, PrixOffrePropose, DateHeureDepotOffre, QuantiteProduit) VALUES ('charlie@example.com', 2, 310.00, TO_TIMESTAMP('2024-11-26 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO OFFRE (EmailUser, IdVente, PrixOffrePropose, DateHeureDepotOffre, QuantiteProduit) VALUES ('david@example.com', 3, 155.00, TO_TIMESTAMP('2024-11-27 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO OFFRE (EmailUser, IdVente, PrixOffrePropose, DateHeureDepotOffre, QuantiteProduit) VALUES ('eve@example.com', 3, 160.00, TO_TIMESTAMP('2024-11-27 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO OFFRE (EmailUser, IdVente, PrixOffrePropose, DateHeureDepotOffre, QuantiteProduit) VALUES ('frank@example.com', 5, 210.00, TO_TIMESTAMP('2024-11-29 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO OFFRE (EmailUser, IdVente, PrixOffrePropose, DateHeureDepotOffre, QuantiteProduit) VALUES ('grace@example.com', 6, 105.00, TO_TIMESTAMP('2024-12-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO OFFRE (EmailUser, IdVente, PrixOffrePropose, DateHeureDepotOffre, QuantiteProduit) VALUES ('hannah@example.com', 8, 1250.00, TO_TIMESTAMP('2024-12-03 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO OFFRE (EmailUser, IdVente, PrixOffrePropose, DateHeureDepotOffre, QuantiteProduit) VALUES ('alice@example.com', 1, 520.00, TO_TIMESTAMP('2024-11-25 19:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);