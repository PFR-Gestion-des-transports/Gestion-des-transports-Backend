CREATE TABLE Adresse (
    Id        INT          NOT NULL AUTO_INCREMENT,
    Ville     VARCHAR(100) NOT NULL,
    Rue       VARCHAR(150) NOT NULL,
    NumeroRue VARCHAR(10)  NOT NULL,

    CONSTRAINT PK_Adresse PRIMARY KEY (Id)
);

CREATE TABLE Utilisateur (
    Id          INT          NOT NULL AUTO_INCREMENT,
    Prenom      VARCHAR(50)  NOT NULL,
    Nom         VARCHAR(50)  NOT NULL,
    Email       VARCHAR(255) NOT NULL UNIQUE,
    MotDePasse  CHAR(64)     NOT NULL,
    AdresseId   INT          NOT NULL,

    CONSTRAINT PK_Utilisateur PRIMARY KEY (Id),
    CONSTRAINT FK_Utilisateur_Adresse FOREIGN KEY (AdresseId)
        REFERENCES Adresse(Id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);