CREATE TABLE CATEGORIE (
    nomCat VARCHAR2(255) PRIMARY KEY,
    DescriptionCat CLOB
);

CREATE TABLE SALLE (
    IdSalle NUMBER PRIMARY KEY,
    nomCat VARCHAR2(255),
    CONSTRAINT fk_salle_categorie FOREIGN KEY (nomCat) REFERENCES CATEGORIE (nomCat)
);

CREATE TABLE PRODUIT (
    IdProduit NUMBER PRIMARY KEY,
    nomCat VARCHAR2(255),
    nomProduit VARCHAR2(255) NOT NULL,
    PrixRevient NUMBER(10, 2) NOT NULL,
    Stock NUMBER NOT NULL CHECK (Stock >= 0),
    CONSTRAINT fk_produit_categorie FOREIGN KEY (nomCat) REFERENCES CATEGORIE (nomCat)
);

CREATE TABLE VENTE (
    IdVente NUMBER PRIMARY KEY,
    IdSalle NUMBER NOT NULL,
    IdProduit NUMBER NOT NULL,
    PrixDepart NUMBER(10, 2) NOT NULL,
    TypeVente VARCHAR2(20) NOT NULL CHECK (TypeVente IN ('croissante', 'decroissante')),
    VenteRevocable NUMBER(1) DEFAULT 0 CHECK (VenteRevocable IN (0, 1)), 
    DateHeureDebutVente TIMESTAMP NOT NULL,
    LimiteOffre NUMBER(1) DEFAULT 0 CHECK (LimiteOffre IN (0, 1)), 
    CONSTRAINT fk_vente_salle FOREIGN KEY (IdSalle) REFERENCES SALLE (IdSalle),
    CONSTRAINT fk_vente_produit FOREIGN KEY (IdProduit) REFERENCES PRODUIT (IdProduit)
);

CREATE TABLE VENTELIMITEE (
    IdVente NUMBER PRIMARY KEY,
    DateHeureFinVente TIMESTAMP NOT NULL,
    CONSTRAINT fk_ventelimitee_vente FOREIGN KEY (IdVente) REFERENCES VENTE (IdVente)
);

CREATE TABLE CARACTERISTIQUE (
    nomCaracteristique VARCHAR2(255) PRIMARY KEY,
    IdProduit NUMBER NOT NULL,
    valeurCaracteristique CLOB NOT NULL,
    CONSTRAINT fk_caracteristique_produit FOREIGN KEY (IdProduit) REFERENCES PRODUIT (IdProduit)
);


CREATE TABLE UTILISATEUR (
    EmailUser VARCHAR2(255) PRIMARY KEY,
    NomUser VARCHAR2(255) NOT NULL,
    PrenomUser VARCHAR2(255) NOT NULL,
    AdressePostale CLOB NOT NULL 
);

CREATE TABLE OFFRE (
    EmailUser VARCHAR2(255),
    IdVente NUMBER,
    PrixOffrePropose NUMBER(10, 2) NOT NULL,
    DateHeureDepotOffre TIMESTAMP NOT NULL,
    QuantiteProduit NUMBER NOT NULL CHECK (QuantiteProduit > 0),
    CONSTRAINT pk_offre PRIMARY KEY (DateHeureDepotOffre, EmailUser, IdVente),
    CONSTRAINT fk_offre_utilisateur FOREIGN KEY (EmailUser) REFERENCES UTILISATEUR (EmailUser),
    CONSTRAINT fk_offre_vente FOREIGN KEY (IdVente) REFERENCES VENTE (IdVente)
);