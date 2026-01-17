
-- Mise à jour prix avec système de types de clients
-- IMPORTANT: Maintenant il faut spécifier aussi le type_client_id

-- Exemple: Mettre à jour le prix pour vol 1, classe Économique (1), type Adulte (1)
UPDATE prix_vol
SET prix_base = 550000.00,
    updated_at = NOW()
WHERE vol_opere_id = 1
  AND classe_siege_id = 1
  AND type_client_id = 1;

-- Exemple: Mettre à jour le prix pour vol 1, classe Économique (1), type Enfant (2)
UPDATE prix_vol
SET prix_base = 385000.00,
    updated_at = NOW()
WHERE vol_opere_id = 1
  AND classe_siege_id = 1
  AND type_client_id = 2;

-- Exemple: Mettre à jour le prix pour vol 1, classe Première (2), type Adulte (1)
UPDATE prix_vol
SET prix_base = 2700000.00,
    updated_at = NOW()
WHERE vol_opere_id = 1
  AND classe_siege_id = 2
  AND type_client_id = 1;

-- Exemple: Mettre à jour le prix pour vol 1, classe Première (2), type Enfant (2)
UPDATE prix_vol
SET prix_base = 1890000.00,
    updated_at = NOW()
WHERE vol_opere_id = 1
  AND classe_siege_id = 2
  AND type_client_id = 2;
  