CREATE TABLE Utilisateur (
    Id       INT          NOT NULL AUTO_INCREMENT,
    Prenom   VARCHAR(50)  NOT NULL,
    Nom      VARCHAR(50)  NOT NULL,
    Email    VARCHAR(255) NOT NULL UNIQUE,

    CONSTRAINT PK_Utilisateur PRIMARY KEY (Id)
);