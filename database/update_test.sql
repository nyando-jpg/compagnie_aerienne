
UPDATE prix_vol
SET prix_base = 800000.00,
    updated_at = NOW()
WHERE vol_opere_id = 7
  AND classe_siege_id = 1;
  