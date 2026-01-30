-- ============================================
-- Database: compagnie
-- PostgreSQL Schema complet pour Compagnie Aérienne
-- Compatible PostgreSQL 15/16
-- ============================================
-- =====================================================
-- 0) DROP EXISTING TABLES & TYPES (Clean Setup)
-- =====================================================
DROP TABLE IF EXISTS paiement_methode CASCADE;
DROP TABLE IF EXISTS methode_paiement CASCADE;
DROP TABLE IF EXISTS paiement CASCADE;
DROP TABLE IF EXISTS maintenance_avion CASCADE;
DROP TABLE IF EXISTS billet CASCADE;
DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS client CASCADE;
DROP TABLE IF EXISTS vol_opere_equipage CASCADE;
DROP TABLE IF EXISTS siege_vol CASCADE;
DROP TABLE IF EXISTS prix_vol CASCADE;
DROP TABLE IF EXISTS vol_opere CASCADE;
DROP TABLE IF EXISTS ligne_vol CASCADE;
DROP TABLE IF EXISTS aeroport CASCADE;
DROP TABLE IF EXISTS equipage CASCADE;
DROP TABLE IF EXISTS role_equipage CASCADE;
DROP TABLE IF EXISTS siege CASCADE;
DROP TABLE IF EXISTS avion CASCADE;
DROP TABLE IF EXISTS etat_avion CASCADE;
DROP TABLE IF EXISTS model_avion CASCADE;
DROP TABLE IF EXISTS classe_siege CASCADE;
DROP TABLE IF EXISTS status_vol CASCADE;
DROP TABLE IF EXISTS type_client CASCADE;
DROP TABLE IF EXISTS diffusion CASCADE;
DROP TABLE IF EXISTS prixdiffusion CASCADE; 
DROP TABLE IF EXISTS paiementdiff CASCADE;
DROP TABLE IF EXISTS societe CASCADE;
DROP TABLE IF EXISTS paiement_diff_methode CASCADE;

DROP TYPE IF EXISTS statut_reservation_enum CASCADE;
DROP TYPE IF EXISTS statut_billet_enum CASCADE;
DROP TYPE IF EXISTS statut_paiement_enum CASCADE;

-- =====================================================
-- 1) TYPES ENUM
-- =====================================================
CREATE TYPE statut_reservation_enum AS ENUM ('EN_ATTENTE', 'CONFIRMEE', 'ANNULEE', 'REMBOURSEE');
CREATE TYPE statut_billet_enum AS ENUM ('EMIS', 'ANNULE', 'UTILISE', 'REMBOURSE');
CREATE TYPE statut_paiement_enum AS ENUM ('EN_ATTENTE', 'VALIDE', 'REFUSE');

-- =====================================================
-- 2) TABLES
-- =====================================================

-- Statut de vol
CREATE TABLE status_vol (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Classe de sièges
CREATE TABLE classe_siege (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Types de clients
CREATE TABLE type_client (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libelle VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    mode_calcul VARCHAR(20) DEFAULT 'FIXE' CHECK (mode_calcul IN ('FIXE', 'POURCENTAGE')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN type_client.mode_calcul IS 'FIXE: prix fixe à saisir dans prix_vol, POURCENTAGE: pourcentage du prix adulte à saisir';

-- Rôles équipage
CREATE TABLE role_equipage (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Modèles d'avion
CREATE TABLE model_avion (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    designation VARCHAR(100) NOT NULL UNIQUE,
    fabricant VARCHAR(100) NOT NULL,
    capacite INT NOT NULL CHECK (capacite > 0),
    autonomie_km INT,
    vitesse_km_h INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- États d'avion
CREATE TABLE etat_avion (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Avions
CREATE TABLE avion (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code_avion VARCHAR(50) NOT NULL UNIQUE,
    model_avion_id INT NOT NULL,
    etat_avion_id INT NOT NULL,
    capacite_totale INT NOT NULL CHECK (capacite_totale > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (model_avion_id) REFERENCES model_avion(id) ON DELETE RESTRICT,
    FOREIGN KEY (etat_avion_id) REFERENCES etat_avion(id) ON DELETE RESTRICT
);

-- Sièges
CREATE TABLE siege (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero_siege VARCHAR(10) NOT NULL,
    classe_siege_id INT NOT NULL,
    avion_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (classe_siege_id) REFERENCES classe_siege(id) ON DELETE RESTRICT,
    FOREIGN KEY (avion_id) REFERENCES avion(id) ON DELETE CASCADE,
    UNIQUE (avion_id, numero_siege)
);

-- Equipage
CREATE TABLE equipage (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    role_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES role_equipage(id) ON DELETE RESTRICT
);

-- Aéroports
CREATE TABLE aeroport (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code_aeroport VARCHAR(10) NOT NULL UNIQUE,
    nom VARCHAR(150) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    pays VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Lignes de vol (Routes abstraites)
CREATE TABLE ligne_vol (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero_vol VARCHAR(50) NOT NULL UNIQUE,
    aeroport_depart_id INT NOT NULL,
    aeroport_arrivee_id INT NOT NULL,
    duree_estimee_minutes INT NOT NULL CHECK (duree_estimee_minutes > 0),
    distance_km INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (aeroport_depart_id) REFERENCES aeroport(id) ON DELETE RESTRICT,
    FOREIGN KEY (aeroport_arrivee_id) REFERENCES aeroport(id) ON DELETE RESTRICT,
    CHECK (aeroport_depart_id != aeroport_arrivee_id)
);

-- Vols opérés (Instances concrètes)
CREATE TABLE vol_opere (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ligne_vol_id INT NOT NULL,
    avion_id INT NOT NULL,
    date_heure_depart TIMESTAMP NOT NULL,
    date_heure_arrivee TIMESTAMP NOT NULL,
    status_id INT NOT NULL DEFAULT 1,
    retard_minutes INT DEFAULT 0,
    motif_annulation TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ligne_vol_id) REFERENCES ligne_vol(id) ON DELETE RESTRICT,
    FOREIGN KEY (avion_id) REFERENCES avion(id) ON DELETE RESTRICT,
    FOREIGN KEY (status_id) REFERENCES status_vol(id) ON DELETE RESTRICT,
    CHECK (date_heure_depart < date_heure_arrivee)
);

-- Prix par vol opéré, classe et type de client
CREATE TABLE prix_vol (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vol_opere_id INT NOT NULL,
    classe_siege_id INT NOT NULL,
    type_client_id INT NOT NULL,
    prix_base DECIMAL(10,2) CHECK (prix_base >= 0),
    pourcentage_base DECIMAL(5,2) CHECK (pourcentage_base >= 0 AND pourcentage_base <= 100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vol_opere_id) REFERENCES vol_opere(id) ON DELETE CASCADE,
    FOREIGN KEY (classe_siege_id) REFERENCES classe_siege(id) ON DELETE RESTRICT,
    FOREIGN KEY (type_client_id) REFERENCES type_client(id) ON DELETE RESTRICT,
    UNIQUE (vol_opere_id, classe_siege_id, type_client_id),
    CHECK ((prix_base IS NOT NULL AND pourcentage_base IS NULL) OR (prix_base IS NULL AND pourcentage_base IS NOT NULL))
);

COMMENT ON COLUMN prix_vol.prix_base IS 'Prix fixe en Ariary (pour types FIXE)';
COMMENT ON COLUMN prix_vol.pourcentage_base IS 'Pourcentage du prix adulte (pour types POURCENTAGE)';

-- Siege_vol (sièges disponibles pour un vol opéré)
CREATE TABLE siege_vol (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vol_opere_id INT NOT NULL,
    siege_id INT NOT NULL,
    statut VARCHAR(20) DEFAULT 'LIBRE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vol_opere_id) REFERENCES vol_opere(id) ON DELETE CASCADE,
    FOREIGN KEY (siege_id) REFERENCES siege(id) ON DELETE CASCADE,
    UNIQUE (vol_opere_id, siege_id),
    CHECK (statut IN ('LIBRE', 'RESERVE', 'OCCUPE'))
);

-- Vol_opere_equipage
CREATE TABLE vol_opere_equipage (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vol_opere_id INT NOT NULL,
    equipage_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vol_opere_id) REFERENCES vol_opere(id) ON DELETE CASCADE,
    FOREIGN KEY (equipage_id) REFERENCES equipage(id) ON DELETE CASCADE,
    UNIQUE (vol_opere_id, equipage_id)
);

-- Clients
CREATE TABLE client (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telephone VARCHAR(20),
    date_naissance DATE,
    adresse TEXT,
    type_client_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (type_client_id) REFERENCES type_client(id) ON DELETE RESTRICT
);

-- Réservations
CREATE TABLE reservation (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id INT NOT NULL,
    vol_opere_id INT NOT NULL,
    date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut statut_reservation_enum DEFAULT 'EN_ATTENTE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE RESTRICT,
    FOREIGN KEY (vol_opere_id) REFERENCES vol_opere(id) ON DELETE RESTRICT
);

-- Billets
CREATE TABLE billet (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    reservation_id INT NOT NULL,
    siege_vol_id INT NOT NULL,
    prix DECIMAL(10,2) NOT NULL CHECK (prix >= 0),
    statut statut_billet_enum DEFAULT 'EMIS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES reservation(id) ON DELETE CASCADE,
    FOREIGN KEY (siege_vol_id) REFERENCES siege_vol(id) ON DELETE RESTRICT
);

-- Méthodes de paiement
CREATE TABLE methode_paiement (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Paiements
CREATE TABLE paiement (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    reservation_id INT NOT NULL,
    montant DECIMAL(10,2) NOT NULL CHECK (montant > 0),
    date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut statut_paiement_enum DEFAULT 'EN_ATTENTE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES reservation(id) ON DELETE CASCADE
);

-- Paiement_methode
CREATE TABLE paiement_methode (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    paiement_id INT NOT NULL,
    methode_paiement_id INT NOT NULL,
    montant DECIMAL(10,2) NOT NULL CHECK (montant > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paiement_id) REFERENCES paiement(id) ON DELETE CASCADE,
    FOREIGN KEY (methode_paiement_id) REFERENCES methode_paiement(id) ON DELETE RESTRICT,
    UNIQUE (paiement_id, methode_paiement_id)
);

-- Maintenance avion
CREATE TABLE maintenance_avion (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    avion_id INT NOT NULL,
    date_debut TIMESTAMP NOT NULL,
    date_fin TIMESTAMP,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (avion_id) REFERENCES avion(id) ON DELETE CASCADE
);

CREATE TABLE societe (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nom VARCHAR(100)
);

CREATE TABLE diffusion (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    idSociete int,
    idVolOpere int,
    nombre int,
    dateDiff TIMESTAMP NOT NULL,
    FOREIGN KEY (idSociete) REFERENCES societe(id) ON DELETE CASCADE,
    FOREIGN KEY (idVolOpere) REFERENCES vol_opere(id) ON DELETE CASCADE
);

CREATE TABLE prixDiffusion (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    idSociete int,
    valeur DECIMAL(10, 2),
    datePrixDiff TIMESTAMP NOT NULL,
    FOREIGN KEY (idSociete) REFERENCES societe(id) ON DELETE CASCADE
);

CREATE TABLE paiementDiff(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    idDiffusion int,
    montant DECIMAL(10, 2),
    datePaiement TIMESTAMP NOT NULL,
    FOREIGN KEY (idDiffusion) REFERENCES diffusion(id) ON DELETE CASCADE
);

CREATE TABLE paiement_diff_methode (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    paiement_diff_id INT NOT NULL,
    methode_paiement_id INT NOT NULL,
    montant DECIMAL(10,2) NOT NULL CHECK (montant > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paiement_diff_id) REFERENCES paiementDiff(id) ON DELETE CASCADE,
    FOREIGN KEY (methode_paiement_id) REFERENCES methode_paiement(id) ON DELETE RESTRICT,
    UNIQUE (paiement_diff_id, methode_paiement_id)
);

CREATE TABLE extra (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nom VARCHAR(100),
    prix DECIMAL(10, 2)
);

CREATE TABLE vente_extra (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    idExtra int,
    idVolOpere int,
    nombre int,
    FOREIGN KEY(idExtra) REFERENCES extra(id) ON DELETE CASCADE
);