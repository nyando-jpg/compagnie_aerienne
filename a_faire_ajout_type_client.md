================================================================================
               GUIDE - AJOUT SYSTÈME DE TYPES DE CLIENTS
================================================================================

DATE DE CRÉATION : 16 janvier 2026
OBJECTIF : Implémenter la tarification différenciée par type de client
           (Adulte, Enfant) avec impact sur le chiffre d'affaires

================================================================================
1. TABLES CONCERNÉES
================================================================================

NOUVELLE TABLE :
  - type_client (id, libelle, description, created_at, updated_at)

TABLES MODIFIÉES DIRECTEMENT :
  - client : AJOUT type_client_id (FK vers type_client)
             Chaque client doit avoir un type (Adulte, Enfant, etc.)
  
  - prix_vol : AJOUT type_client_id (FK vers type_client)
               MODIFICATION contrainte UNIQUE vers (vol_opere_id, classe_siege_id, type_client_id)
               Chaque combinaison vol×classe a maintenant un prix par type de client

TABLES LIÉES (utilisées dans le processus) :
  - vol_opere : Les prix sont définis pour chaque vol opéré
                Lié à prix_vol via vol_opere_id
  
  - classe_siege : Les prix sont définis par classe de siège
                   Lié à prix_vol via classe_siege_id
  
  - reservation : Utilise le type du client pour calculer le prix du billet
                  Lié à client (qui a type_client_id)
  
  - billet : Le prix stocké dépend du type de client de la réservation
             Montant calculé via getPrix(vol, classe, type_client)
  
  - ligne_vol : Identifie la route du vol (utilisé dans formulaire vol_opere)
  
  - avion : Identifie l'avion du vol (utilisé dans formulaire vol_opere)
  
  - siege : Définit la classe du siège réservé
            Lié à classe_siege_id
  
  - siege_vol : Relie les sièges aux vols opérés
                Utilisé pour déterminer la classe lors de la réservation

STRUCTURE PRIX AVANT/APRÈS :
  AVANT : prix_vol(vol_opere_id, classe_siege_id, prix_base)
          → 1 prix par vol × classe
  APRÈS : prix_vol(vol_opere_id, classe_siege_id, type_client_id, prix_base)
          → 1 prix par vol × classe × type_client

EXEMPLE DONNÉES :
  Vol 1, Économique, Adulte : 500.00
  Vol 1, Économique, Enfant : 350.00
  Vol 1, Première, Adulte : 2500.00
  Vol 1, Première, Enfant : 1750.00

================================================================================
2. MODÈLES ET DAO
================================================================================

NOUVEAU MODEL :
  src/model/TypeClient.java
    Champs : id, libelle, description, createdAt, updatedAt
    Note : PAS de champ pourcentageReduction (prix fixes)

NOUVEAU DAO :
  src/dao/TypeClientDAO.java
    Méthodes : getAll(), getById(id), insert(TypeClient), 
               update(TypeClient), delete(id)

DAO MODIFIÉS :
  src/dao/PrixVolDAO.java

    SIGNATURE MODIFIÉE - getPrix() :
      AVANT : getPrix(volOpereId : int, classeSiegeId : int) : Double
      APRÈS : getPrix(volOpereId : int, classeSiegeId : int, typeClientId : int) : Double
      SQL   : WHERE vol_opere_id = ? AND classe_siege_id = ? AND type_client_id = ?

    SIGNATURE MODIFIÉE - insertPrixPourVol() :
      AVANT : insertPrixPourVol(volOpereId : int, prixParClasse : Map<Integer, Double>)
              Key = classeSiegeId, Value = prix
      APRÈS : insertPrixPourVol(volOpereId : int, prixParClasseEtType : Map<String, Double>)
              Key = "classeSiegeId_typeClientId", Value = prix
      
      Exemple Map :
        "1_1" → 500.00   (Classe 1 Économique, Type 1 Adulte)
        "1_2" → 350.00   (Classe 1 Économique, Type 2 Enfant)
        "2_1" → 2500.00  (Classe 2 Première, Type 1 Adulte)
        "2_2" → 1750.00  (Classe 2 Première, Type 2 Enfant)

    NOUVELLE MÉTHODE - getPrixByVolOpere() :
      getPrixByVolOpere(volOpereId : int) : Map<String, Double>
      Retourne tous les prix d'un vol
      Key = "classeSiegeId_typeClientId", Value = prix

================================================================================
3. SERVLETS (CONTROLLERS)
================================================================================

NOUVEAU SERVLET :
  src/controller/TypeClientServlet.java
    doGet()  : actions list, add, edit, delete
    doPost() : insert, update
    Mapping : /type-client (dans web.xml)

SERVLETS MODIFIÉS :

  src/controller/VolOpereServlet.java
    AJOUT CHAMPS :
      private TypeClientDAO typeClientDAO;
      private PrixVolDAO prixVolDAO;
    
    MODIFICATION showAddForm() et showEditForm() :
      List<TypeClient> typesClients = typeClientDAO.getAll();
      request.setAttribute("typesClients", typesClients);

    MODIFICATION insertVolOpere() :
      // Boucle imbriquée classes × types
      for (ClasseSiege cs : classes) {
          for (TypeClient tc : typesClients) {
              String paramName = "prix_classe_" + cs.getId() + "_type_" + tc.getId();
              String prixStr = request.getParameter(paramName);
              double prix = Double.parseDouble(prixStr);
              String key = cs.getId() + "_" + tc.getId();
              prixMap.put(key, prix);
          }
      }
      prixVolDAO.insertPrixPourVol(volOpereId, prixMap);
    
    CONVENTION PARAMÈTRES FORM :
      prix_classe_1_type_1  → Classe 1 Type 1
      prix_classe_1_type_2  → Classe 1 Type 2
      prix_classe_2_type_1  → Classe 2 Type 1
      prix_classe_2_type_2  → Classe 2 Type 2

  src/controller/ReservationServlet.java
    MODIFICATION insertReservation() :
      int typeClientId = Integer.parseInt(request.getParameter("type_client_id"));
      Double prix = prixVolDAO.getPrix(volOpereId, classeSiegeId, typeClientId);

================================================================================
4. PAGES JSP
================================================================================

NOUVELLES PAGES :
  [ ] WebContent/jsp/type_client/list.jsp
      Affichage : id, libelle, description, actions (Modifier, Supprimer)
      Bouton : Ajouter un type

  [ ] WebContent/jsp/type_client/form.jsp
      Champs : libelle (required), description (textarea)
      Mode : add ou edit (avec id hidden)

PAGES MODIFIÉES :

  [ ] WebContent/jsp/vol_opere/form.jsp
      AJOUT : import model.TypeClient
      
      AJOUT SECTION : Grille de prix (table classes × types)
        <table class="prix-table">
          <thead>
            <tr>
              <th>Classe</th>
              <th>ADULTE</th>
              <th>ENFANT</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>ECONOMIQUE</td>
              <td><input name="prix_classe_1_type_1" type="number" step="0.01" required></td>
              <td><input name="prix_classe_1_type_2" type="number" step="0.01" required></td>
            </tr>
            <tr>
              <td>PREMIERE</td>
              <td><input name="prix_classe_2_type_1" type="number" step="0.01" required></td>
              <td><input name="prix_classe_2_type_2" type="number" step="0.01" required></td>
            </tr>
          </tbody>
        </table>
      
      Génération dynamique avec <c:forEach var="cs" items="${classes}">
                                <c:forEach var="tc" items="${typesClients}">

  [ ] WebContent/jsp/reservation/form.jsp
      AJOUT : import model.TypeClient, dao.TypeClientDAO
      AJOUT : Chargement typesClients avec TypeClientDAO.getAll()
      AJOUT : Select dropdown type_client_id (required)
              <select name="type_client_id" required>
                <c:forEach var="tc" items="${typesClients}">
                  <option value="${tc.id}">${tc.libelle}</option>
                </c:forEach>
              </select>
      Position : Entre sélection client et sélection siège

  [ ] WebContent/jsp/common/sidebar.jsp
      AJOUT : Lien menu "🏷️ Types de Client" → /type-client

================================================================================
5. IMPACT CHIFFRE D'AFFAIRES
================================================================================

CALCUL PRIX :
  AVANT : getPrix(volOpereId, classeSiegeId) → prix unique
  APRÈS : getPrix(volOpereId, classeSiegeId, typeClientId) → prix différencié

EXEMPLE TARIFICATION :
  Vol TNR-JNB, Économique :
    Adulte : 500 000 Ar
    Enfant : 350 000 Ar (70% du tarif adulte)
  
  Réservation famille (1 adulte + 1 enfant) :
    CA = 500 000 + 350 000 = 850 000 Ar
    VS ancien système (2 × 500 000) = 1 000 000 Ar
    Différence : -150 000 Ar (mais reflet réel de la politique tarifaire)

REQUÊTE SQL - CA PAR TYPE CLIENT :
  SELECT 
      tc.libelle AS type_client,
      COUNT(r.id) AS nombre_reservations,
      SUM(b.prix) AS chiffre_affaires
  FROM reservation r
  JOIN client c ON r.client_id = c.id
  JOIN type_client tc ON c.type_client_id = tc.id
  JOIN billet b ON b.reservation_id = r.id
  WHERE r.statut = 'CONFIRMEE'
  GROUP BY tc.id, tc.libelle
  ORDER BY chiffre_affaires DESC;

RÉSULTAT ATTENDU :
  type_client | nombre_reservations | chiffre_affaires
  ------------|---------------------|------------------
  ADULTE      | 150                 | 75 000 000
  ENFANT      | 80                  | 28 000 000
  TOTAL       | 230                 | 103 000 000

REQUÊTE SQL - CA PAR VOL ET TYPE :
  SELECT 
      lv.numero_vol,
      tc.libelle AS type_client,
      cs.libelle AS classe,
      COUNT(b.id) AS nb_billets,
      AVG(b.prix) AS prix_moyen,
      SUM(b.prix) AS ca_total
  FROM billet b
  JOIN reservation r ON b.reservation_id = r.id
  JOIN client c ON r.client_id = c.id
  JOIN type_client tc ON c.type_client_id = tc.id
  JOIN vol_opere vo ON r.vol_opere_id = vo.id
  JOIN ligne_vol lv ON vo.ligne_vol_id = lv.id
  JOIN siege_vol sv ON b.siege_vol_id = sv.id
  JOIN siege s ON sv.siege_id = s.id
  JOIN classe_siege cs ON s.classe_siege_id = cs.id
  WHERE r.statut = 'CONFIRMEE'
  GROUP BY lv.numero_vol, tc.libelle, cs.libelle
  ORDER BY lv.numero_vol, ca_total DESC;

REQUÊTE SQL - CA MENSUEL PAR TYPE :
  SELECT 
      DATE_TRUNC('month', r.date_reservation) AS mois,
      tc.libelle,
      SUM(b.prix) AS ca
  FROM reservation r
  JOIN client c ON r.client_id = c.id
  JOIN type_client tc ON c.type_client_id = tc.id
  JOIN billet b ON b.reservation_id = r.id
  WHERE r.statut = 'CONFIRMEE'
  GROUP BY mois, tc.libelle
  ORDER BY mois DESC, ca DESC;

DASHBOARD CA - SUGGESTIONS :
  Widget 1 : Pie Chart - Répartition CA par type client
  Widget 2 : Tableau comparatif avec % du CA total
  Widget 3 : Graphique ligne - Évolution CA mensuel par type
  Widget 4 : KPIs - Prix moyen Adulte vs Enfant

================================================================================
6. FICHIERS À CRÉER/MODIFIER
================================================================================

BACKEND (Java) :
  [X] src/model/TypeClient.java
  [X] src/dao/TypeClientDAO.java
  [X] src/dao/PrixVolDAO.java (MODIFIER getPrix, insertPrixPourVol)
  [X] src/controller/TypeClientServlet.java
  [X] src/controller/VolOpereServlet.java (MODIFIER)
  [X] src/controller/ReservationServlet.java (MODIFIER)
  [X] WebContent/WEB-INF/web.xml (mapping /type-client)

FRONTEND (JSP) :
  [X] WebContent/jsp/type_client/list.jsp
  [X] WebContent/jsp/type_client/form.jsp
  [X] WebContent/jsp/vol_opere/form.jsp (MODIFIER - grille prix)
  [X] WebContent/jsp/reservation/form.jsp (MODIFIER - select type)
  [X] WebContent/jsp/common/sidebar.jsp (MODIFIER - menu)

BASE DE DONNÉES :
  [X] database/schema.sql (table type_client + modifications)
  [X] database/donnees.sql (INSERT types + prix avec 3 colonnes)

================================================================================
7. TESTS REQUIS
================================================================================

TEST 1 - CRUD Type Client :
  1. Ajouter type "ADULTE"
  2. Ajouter type "ENFANT"
  3. Modifier description
  4. Lister les types
  5. Supprimer un type (vérifier cascade/restrict)

TEST 2 - Création Vol avec Prix :
  1. Créer vol opéré
  2. Remplir grille : 4 prix (2 classes × 2 types)
  3. Vérifier insertion dans prix_vol (4 lignes attendues)
  4. Vérifier contrainte UNIQUE (vol, classe, type)

TEST 3 - Réservation et Prix :
  1. Créer client type ADULTE
  2. Réserver vol → vérifier prix adulte appliqué
  3. Créer client type ENFANT
  4. Réserver même vol → vérifier prix enfant appliqué
  5. Comparer les 2 prix

TEST 4 - Calcul CA :
  1. Créer 10 réservations (5 adultes, 5 enfants)
  2. Exécuter requête CA par type
  3. Vérifier totaux : CA_adultes + CA_enfants = CA_total
  4. Vérifier moyenne : CA_adultes/5 > CA_enfants/5

================================================================================
8. MIGRATION DONNÉES EXISTANTES (SI NÉCESSAIRE)
================================================================================

Si base contient déjà des clients et prix :

  -- Étape 1 : Créer type ADULTE par défaut
  INSERT INTO type_client (libelle, description) 
  VALUES ('ADULTE', 'Client adulte standard');

  -- Étape 2 : Affecter tous les clients existants
  UPDATE client SET type_client_id = 1;

  -- Étape 3 : Ajouter contrainte NOT NULL
  ALTER TABLE client ALTER COLUMN type_client_id SET NOT NULL;

  -- Étape 4 : Dupliquer prix existants pour type ADULTE
  -- (nécessite script personnalisé selon vos données)

================================================================================
9. CHECKLIST DÉPLOIEMENT
================================================================================

  [ ] Exécuter schema.sql complet (DROP + CREATE avec type_client)
  [ ] Exécuter donnees.sql complet (INSERT types + prix 3 colonnes)
  [ ] Vérifier web.xml (mapping TypeClientServlet)
  [ ] Compiler projet (mvn clean package ou ant build)
  [ ] Déployer WAR sur Tomcat
  [ ] Accéder /type-client → vérifier liste
  [ ] Créer vol opéré → vérifier grille prix
  [ ] Créer réservation → vérifier select type
  [ ] Exécuter requêtes CA → vérifier résultats

================================================================================
FIN DU GUIDE
================================================================================
