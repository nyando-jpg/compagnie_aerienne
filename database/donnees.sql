
-- =====================================================
-- 3) INSERTIONS DE DONNÉES DE TEST
-- =====================================================

-- Classes de sièges
INSERT INTO classe_siege (libelle, description) VALUES
('ECONOMIQUE', 'Classe économique standard'),
('PREMIUM', 'Classe économique premium'),
('AFFAIRES', 'Classe affaires'),
('PREMIERE', 'Première classe');

-- Rôles équipage
INSERT INTO role_equipage (libelle, description) VALUES
('PILOTE', 'Pilote de ligne'),
('COPILOTE', 'Copilote'),
('HOTESSE', 'Hôtesse de l''air'),
('STEWARD', 'Steward');

-- Statut de vol
INSERT INTO status_vol (libelle, description) VALUES
('PLANIFIE', 'Vol planifié'),
('DECOLLE', 'Vol décollé'),
('EN_VOL', 'Vol en cours'),
('ATTERRI', 'Vol atterri'),
('ANNULE', 'Vol annulé'),
('RETARDE', 'Vol retardé');

-- États avion
INSERT INTO etat_avion (libelle, description) VALUES
('OPERATIONNEL', 'Avion opérationnel'),
('EN_MAINTENANCE', 'Avion en maintenance'),
('HORS_SERVICE', 'Avion hors service');

-- Modèles d'avion
INSERT INTO model_avion (designation, fabricant, capacite, autonomie_km, vitesse_km_h, description) VALUES
('Boeing 747-8', 'Boeing', 416, 14685, 908, 'Gros porteur long-courrier'),
('Airbus A380', 'Airbus', 555, 15000, 903, 'Double pont long-courrier'),
('Boeing 777-300ER', 'Boeing', 365, 14685, 905, 'Gros porteur long-courrier biréacteur'),
('Airbus A350-900', 'Airbus', 325, 15000, 903, 'Long-courrier biréacteur'),
('Boeing 787-9', 'Boeing', 296, 14010, 903, 'Moyen-courrier long-distance');

-- États avion déjà insérés, récupérons leurs IDs
-- Avions (capacité réduite à 50 pour simplification)
INSERT INTO avion (code_avion, model_avion_id, etat_avion_id, capacite_totale) VALUES
('AV001', 1, 1, 50),
('AV002', 2, 1, 50),
('AV003', 3, 2, 50);

-- Sièges explicites pour chaque avion (50 sièges par avion)
-- Répartition: 25 Economique, 10 Premium, 10 Affaires, 5 Première

-- AVION 1 (AV001) - 50 sièges
-- Première classe (5 sièges): rangées 1-2
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
('1A', 4, 1), ('1B', 4, 1), ('1C', 4, 1),
('2A', 4, 1), ('2B', 4, 1);

-- Affaires (10 sièges): rangées 3-4
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
('3A', 3, 1), ('3B', 3, 1), ('3C', 3, 1), ('3D', 3, 1), ('3E', 3, 1),
('4A', 3, 1), ('4B', 3, 1), ('4C', 3, 1), ('4D', 3, 1), ('4E', 3, 1);

-- Premium (10 sièges): rangées 5-6
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
('5A', 2, 1), ('5B', 2, 1), ('5C', 2, 1), ('5D', 2, 1), ('5E', 2, 1),
('6A', 2, 1), ('6B', 2, 1), ('6C', 2, 1), ('6D', 2, 1), ('6E', 2, 1);

-- Economique (25 sièges): rangées 7-11
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
('7A', 1, 1), ('7B', 1, 1), ('7C', 1, 1), ('7D', 1, 1), ('7E', 1, 1),
('8A', 1, 1), ('8B', 1, 1), ('8C', 1, 1), ('8D', 1, 1), ('8E', 1, 1),
('9A', 1, 1), ('9B', 1, 1), ('9C', 1, 1), ('9D', 1, 1), ('9E', 1, 1),
('10A', 1, 1), ('10B', 1, 1), ('10C', 1, 1), ('10D', 1, 1), ('10E', 1, 1),
('11A', 1, 1), ('11B', 1, 1), ('11C', 1, 1), ('11D', 1, 1), ('11E', 1, 1);

-- AVION 2 (AV002) - 50 sièges
-- Première classe (5 sièges): rangées 1-2
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
('1A', 4, 2), ('1B', 4, 2), ('1C', 4, 2),
('2A', 4, 2), ('2B', 4, 2);

-- Affaires (10 sièges): rangées 3-4
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
('3A', 3, 2), ('3B', 3, 2), ('3C', 3, 2), ('3D', 3, 2), ('3E', 3, 2),
('4A', 3, 2), ('4B', 3, 2), ('4C', 3, 2), ('4D', 3, 2), ('4E', 3, 2);

-- Premium (10 sièges): rangées 5-6
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
('5A', 2, 2), ('5B', 2, 2), ('5C', 2, 2), ('5D', 2, 2), ('5E', 2, 2),
('6A', 2, 2), ('6B', 2, 2), ('6C', 2, 2), ('6D', 2, 2), ('6E', 2, 2);

-- Economique (25 sièges): rangées 7-11
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
('7A', 1, 2), ('7B', 1, 2), ('7C', 1, 2), ('7D', 1, 2), ('7E', 1, 2),
('8A', 1, 2), ('8B', 1, 2), ('8C', 1, 2), ('8D', 1, 2), ('8E', 1, 2),
('9A', 1, 2), ('9B', 1, 2), ('9C', 1, 2), ('9D', 1, 2), ('9E', 1, 2),
('10A', 1, 2), ('10B', 1, 2), ('10C', 1, 2), ('10D', 1, 2), ('10E', 1, 2),
('11A', 1, 2), ('11B', 1, 2), ('11C', 1, 2), ('11D', 1, 2), ('11E', 1, 2);

-- AVION 3 (AV003) - 50 sièges
-- Première classe (5 sièges): rangées 1-2
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
('1A', 4, 3), ('1B', 4, 3), ('1C', 4, 3),
('2A', 4, 3), ('2B', 4, 3);

-- Affaires (10 sièges): rangées 3-4
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
('3A', 3, 3), ('3B', 3, 3), ('3C', 3, 3), ('3D', 3, 3), ('3E', 3, 3),
('4A', 3, 3), ('4B', 3, 3), ('4C', 3, 3), ('4D', 3, 3), ('4E', 3, 3);

-- Premium (10 sièges): rangées 5-6
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
('5A', 2, 3), ('5B', 2, 3), ('5C', 2, 3), ('5D', 2, 3), ('5E', 2, 3),
('6A', 2, 3), ('6B', 2, 3), ('6C', 2, 3), ('6D', 2, 3), ('6E', 2, 3);

-- Economique (25 sièges): rangées 7-11
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
('7A', 1, 3), ('7B', 1, 3), ('7C', 1, 3), ('7D', 1, 3), ('7E', 1, 3),
('8A', 1, 3), ('8B', 1, 3), ('8C', 1, 3), ('8D', 1, 3), ('8E', 1, 3),
('9A', 1, 3), ('9B', 1, 3), ('9C', 1, 3), ('9D', 1, 3), ('9E', 1, 3),
('10A', 1, 3), ('10B', 1, 3), ('10C', 1, 3), ('10D', 1, 3), ('10E', 1, 3),
('11A', 1, 3), ('11B', 1, 3), ('11C', 1, 3), ('11D', 1, 3), ('11E', 1, 3);

-- Aéroports
INSERT INTO aeroport (code_aeroport, nom, ville, pays) VALUES
('TNR', 'Aéroport International Ivato', 'Antananarivo', 'Madagascar'),
('JNB', 'O.R. Tambo International', 'Johannesburg', 'Afrique du Sud'),
('CDG', 'Aéroport Charles de Gaulle', 'Paris', 'France'),
('LHR', 'Heathrow', 'Londres', 'Royaume-Uni');

-- Equipage
INSERT INTO equipage (nom, prenom, role_id) VALUES
('Rakoto', 'Jean', 1),
('Rabe', 'Marie', 2),
('Andrian', 'Lala', 3),
('Ranaivo', 'Hery', 4);

-- Lignes de Vol (routes abstraites)
INSERT INTO ligne_vol (numero_vol, aeroport_depart_id, aeroport_arrivee_id, duree_estimee_minutes, distance_km, description) VALUES
('AF1001', 1, 2, 240, 2500, 'Ligne régulière Antananarivo - Johannesburg'),
('AF1002', 2, 3, 480, 8500, 'Ligne long-courrier Johannesburg - Paris'),
('AF1003', 3, 4, 120, 350, 'Ligne courte distance Paris - Londres');

-- Vols Opérés (instances concrètes avec dates et avions)
INSERT INTO vol_opere (ligne_vol_id, avion_id, date_heure_depart, date_heure_arrivee, status_id, retard_minutes) VALUES
(1, 1, '2026-01-15 08:00', '2026-01-15 12:00', 1, 0),
(2, 2, '2026-01-16 10:00', '2026-01-16 18:00', 1, 0),
(3, 1, '2026-01-17 14:00', '2026-01-17 16:00', 1, 0),
-- Même ligne, autre avion, autre horaire (démontre la flexibilité)
(1, 2, '2026-01-15 18:00', '2026-01-15 22:00', 1, 0);

-- Prix par vol opéré et classe (prix spécifiques à chaque vol)
INSERT INTO prix_vol (vol_opere_id, classe_siege_id, prix_base) VALUES
-- Vol 1 (TNR-JNB du 15/01 à 08h)
(1, 1, 500.00),   -- Economique
(1, 2, 1200.00),  -- Premium
(1, 3, 1800.00),  -- Affaires
(1, 4, 2500.00),  -- Premiere
-- Vol 2 (JNB-CDG du 16/01 à 10h)
(2, 1, 600.00),   -- Economique
(2, 2, 1400.00),  -- Premium
(2, 3, 2000.00),  -- Affaires
(2, 4, 3000.00),  -- Premiere
-- Vol 3 (CDG-LHR du 17/01 à 14h)
(3, 1, 550.00),   -- Economique
(3, 2, 900.00),   -- Premium
(3, 3, 1200.00),  -- Affaires
(3, 4, 2000.00),  -- Premiere
-- Vol 4 (TNR-JNB du 15/01 à 18h)
(4, 1, 520.00),   -- Economique (légèrement différent du vol 1)
(4, 2, 1250.00),  -- Premium
(4, 3, 1850.00),  -- Affaires
(4, 4, 2600.00);  -- Premiere

-- Siege_vol (UNIQUEMENT les sièges RESERVE - les autres sont LIBRE par défaut)
-- Vol 1: quelques sièges réservés
INSERT INTO siege_vol (vol_opere_id, siege_id, statut) VALUES
(1, 1, 'RESERVE'),   -- 1A Première
(1, 16, 'RESERVE'),  -- 5A Premium
(1, 26, 'RESERVE');  -- 7A Economique

-- Vol 2: quelques sièges réservés
INSERT INTO siege_vol (vol_opere_id, siege_id, statut) VALUES
(2, 51, 'RESERVE'),  -- 1A Première (avion 2)
(2, 66, 'RESERVE'),  -- 5A Premium (avion 2)
(2, 76, 'RESERVE');  -- 7A Economique (avion 2)

-- Vol 3: quelques sièges réservés (utilise avion 1)
INSERT INTO siege_vol (vol_opere_id, siege_id, statut) VALUES
(3, 2, 'RESERVE'),   -- 1B Première
(3, 27, 'RESERVE');  -- 7B Economique

-- Vol 4: quelques sièges réservés (utilise avion 2)
INSERT INTO siege_vol (vol_opere_id, siege_id, statut) VALUES
(4, 52, 'RESERVE'),  -- 1B Première (avion 2)
(4, 77, 'RESERVE');  -- 7B Economique (avion 2)

-- Vol_Opere_Equipage
INSERT INTO vol_opere_equipage (vol_opere_id, equipage_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 2),
(2, 3),
(2, 4),
(3, 1),
(3, 4),
(4, 1),
(4, 3);

-- Clients
INSERT INTO client (nom, prenom, email, telephone) VALUES
('Rakotomalala', 'Jean', 'jean.rakoto@example.com', '+261330000001'),
('Rasoamanana', 'Lala', 'lala.raso@example.com', '+261330000002'),
('Andriantsitohaina', 'Hery', 'hery.andi@example.com', '+261330000003');

-- Réservations (avec vol_opere_id au lieu de vol_id)
INSERT INTO reservation (client_id, vol_opere_id, statut) VALUES
(1, 1, 'CONFIRMEE'),
(2, 2, 'EN_ATTENTE'),
(3, 4, 'CONFIRMEE');

-- Billets (liés aux sièges réservés dans siege_vol)
INSERT INTO billet (reservation_id, siege_vol_id, prix) VALUES
(1, 1, 2000.00),  -- Réservation 1, siege_vol 1 (vol 1, siège 1A Première)
(2, 4, 600.00),   -- Réservation 2, siege_vol 4 (vol 2, siège 1A Première)
(3, 7, 500.00);   -- Réservation 3, siege_vol 7 (vol 4, siège 1B Première)

-- Méthodes de paiement
INSERT INTO methode_paiement (libelle, description) VALUES
('CARTE_BANCAIRE', 'Paiement par carte bancaire'),
('ESPECES', 'Paiement en espèces'),
('VIREMENT', 'Virement bancaire'),
('PAYPAL', 'Paiement PayPal');

-- Paiements
INSERT INTO paiement (reservation_id, montant, statut) VALUES
(1, 2000.00, 'VALIDE'),
(2, 600.00, 'EN_ATTENTE'),
(3, 500.00, 'VALIDE');

-- Paiement_methode
INSERT INTO paiement_methode (paiement_id, methode_paiement_id, montant) VALUES
(1, 1, 2000.00),
(2, 2, 600.00),
(3, 3, 500.00);

-- Maintenance avion
INSERT INTO maintenance_avion (avion_id, date_debut, date_fin, description) VALUES
(3, '2026-01-01 08:00', '2026-01-10 18:00', 'Maintenance générale');

-- Message succès
SELECT 'Toutes les données ont été créées avec succès !' AS status;
