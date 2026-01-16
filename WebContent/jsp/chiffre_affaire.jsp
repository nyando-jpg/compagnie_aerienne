<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map, model.LigneVol, model.Reservation, model.VolOpere, model.Avion" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chiffre d'Affaires</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
    <style>
        .filters-container {
            background: white;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .filters-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 15px;
            margin-bottom: 15px;
        }
        .result-box {
            background: white;
            padding: 30px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .ca-principal {
            text-align: center;
            padding: 40px 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        .ca-montant {
            font-size: 3em;
            font-weight: bold;
            margin: 10px 0;
        }
        .ca-label {
            font-size: 1.2em;
            opacity: 0.9;
        }
        .stats-row {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            margin-bottom: 30px;
        }
        .stat-box {
            background: #f5f5f5;
            padding: 20px;
            border-radius: 4px;
            text-align: center;
        }
        .stat-box-value {
            font-size: 2em;
            font-weight: bold;
            color: #333;
            margin-bottom: 5px;
        }
        .stat-box-label {
            color: #666;
            font-size: 0.9em;
        }
        .classe-row {
            display: flex;
            justify-content: space-between;
            padding: 15px;
            border-bottom: 1px solid #eee;
        }
        .classe-row:last-child {
            border-bottom: none;
        }
        .classe-nom {
            font-weight: bold;
            color: #333;
        }
        .classe-ca {
            font-size: 1.2em;
            color: #667eea;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>💰 Chiffre d'Affaires</h1>
        </div>
        
        <% 
            Map<String, Object> resultat = (Map<String, Object>) request.getAttribute("resultat");
            Map<String, Double> caParClasse = (Map<String, Double>) request.getAttribute("caParClasse");
            Map<Integer, Double> prixMaxParVol = (Map<Integer, Double>) request.getAttribute("prixMaxParVol");
            List<LigneVol> lignes = (List<LigneVol>) request.getAttribute("lignes");
            List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
            List<VolOpere> volsOperes = (List<VolOpere>) request.getAttribute("volsOperes");
            List<Avion> avions = (List<Avion>) request.getAttribute("avions");
            Map<String, Object> filtres = (Map<String, Object>) request.getAttribute("filtres");
        %>
        
        <!-- Filtres -->
        <div class="filters-container">
            <h3>🔍 Filtres</h3>
            <form method="get" action="${pageContext.request.contextPath}/chiffre-affaire">
                <div class="filters-grid">
                    <div class="form-group">
                        <label for="ligne_vol_id">Ligne de Vol</label>
                        <select id="ligne_vol_id" name="ligne_vol_id">
                            <option value="">-- Toutes les lignes --</option>
                            <% if (lignes != null) {
                                for (LigneVol lv : lignes) { %>
                                    <option value="<%= lv.getId() %>" 
                                            <%= String.valueOf(lv.getId()).equals(String.valueOf(filtres.get("ligneVolId"))) ? "selected" : "" %>>
                                        <%= lv.getNumeroVol() %>
                                    </option>
                            <%  }
                            } %>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="avion_id">Avion</label>
                        <select id="avion_id" name="avion_id">
                            <option value="">-- Tous les avions --</option>
                            <% 
                                if (avions != null) {
                                    String avionFiltre = String.valueOf(filtres.get("avionId"));
                                    for (Avion av : avions) { 
                            %>
                                <option value="<%= av.getId() %>" 
                                    <%= String.valueOf(av.getId()).equals(avionFiltre) ? "selected" : "" %>>
                                    <%= av.getCodeAvion() != null ? av.getCodeAvion() : ("Avion #" + av.getId()) %>
                                </option>
                            <%      }
                                }
                            %>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="date_debut">Date Début</label>
                        <input type="date" id="date_debut" name="date_debut" 
                               value="<%= filtres.get("dateDebut") %>">
                    </div>
                    
                    <div class="form-group">
                        <label for="date_fin">Date Fin</label>
                        <input type="date" id="date_fin" name="date_fin" 
                               value="<%= filtres.get("dateFin") %>">
                    </div>
                </div>
                
                <div style="display: flex; gap: 10px;">
                    <button type="submit" class="btn btn-primary">📊 Calculer</button>
                    <a href="${pageContext.request.contextPath}/chiffre-affaire" class="btn btn-secondary">🔄 Réinitialiser</a>
                </div>
            </form>
        </div>
        
        <!-- Résultats -->
        <% if (resultat != null && !resultat.isEmpty()) { %>
            <div class="result-box">
                <!-- CA Principal -->
                <div class="ca-principal">
                    <div class="ca-label">CHIFFRE D'AFFAIRES TOTAL</div>
                    <div class="ca-montant"><%= String.format("%.2f", resultat.get("chiffreAffaireTotal")) %> €</div>
                </div>
                
                <!-- Statistiques -->
                <div class="stats-row">
                    <div class="stat-box">
                        <div class="stat-box-value"><%= resultat.get("nombreReservations") %></div>
                        <div class="stat-box-label">Réservations</div>
                    </div>
                    <div class="stat-box">
                        <div class="stat-box-value"><%= resultat.get("nombreBillets") %></div>
                        <div class="stat-box-label">Billets vendus</div>
                    </div>
                </div>
                
                <!-- CA par Classe -->
                <% if (caParClasse != null && !caParClasse.isEmpty()) { %>
                    <h3 style="margin-bottom: 20px;">📊 Répartition par Classe</h3>
                    <div style="background: white; border: 1px solid #ddd; border-radius: 4px;">
                        <% for (Map.Entry<String, Double> entry : caParClasse.entrySet()) { %>
                            <div class="classe-row">
                                <span class="classe-nom"><%= entry.getKey() %></span>
                                <span class="classe-ca"><%= String.format("%.2f", entry.getValue()) %> €</span>
                            </div>
                        <% } %>
                    </div>
                <% } %>
            </div>
            
            <!-- Liste des Vols Opérés -->
            <% if (volsOperes != null && !volsOperes.isEmpty()) { %>
                <div class="result-box" style="margin-top: 20px;">
                    <h3 style="margin-bottom: 20px;">✈️ Vols Opérés (<%= volsOperes.size() %>)</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Numéro Vol</th>
                                <th>Départ</th>
                                <th>Arrivée</th>
                                <th>Date Départ</th>
                                <th>Statut</th>
                                <th>Avion</th>
                                <th>Prix Max Possible</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (VolOpere vo : volsOperes) { %>
                                <tr>
                                    <td><%= vo.getId() %></td>
                                    <td><strong><%= vo.getNumeroVol() %></strong></td>
                                    <td><%= vo.getAeroportDepartNom() %></td>
                                    <td><%= vo.getAeroportArriveeNom() %></td>
                                    <td><%= vo.getDateHeureDepart() %></td>
                                    <td><span class="badge badge-<%= vo.getStatus() != null && vo.getStatus().equals("A_L_HEURE") ? "success" : "warning" %>">
                                        <%= vo.getStatus() != null ? vo.getStatus() : "N/A" %>
                                    </span></td>
                                    <td>#<%= vo.getAvionId() %></td>
                                    <td><strong style="color: #667eea;"><%= String.format("%.2f €", prixMaxParVol.get(vo.getId())) %></strong></td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
            
            <!-- Liste des Réservations -->
            <% if (reservations != null && !reservations.isEmpty()) { %>
                <div class="result-box" style="margin-top: 20px;">
                    <h3 style="margin-bottom: 20px;">📋 Réservations (<%= reservations.size() %>)</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Client</th>
                                <th>Vol</th>
                                <th>Trajet</th>
                                <th>Date Réservation</th>
                                <th>Date Vol</th>
                                <th>Siège</th>
                                <th>Prix</th>
                                <th>Statut</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Reservation r : reservations) { %>
                                <tr>
                                    <td><%= r.getId() %></td>
                                    <td><%= r.getClientNom() %> <%= r.getClientPrenom() %></td>
                                    <td><strong><%= r.getNumeroVol() %></strong></td>
                                    <td><%= r.getAeroportDepart() %> → <%= r.getAeroportArrivee() %></td>
                                    <td><%= r.getDateReservation() %></td>
                                    <td><%= r.getDateHeureDepart() %></td>
                                    <td><%= r.getNumeroSiege() != null ? r.getNumeroSiege() : "-" %></td>
                                    <td><strong><%= r.getPrix() != null ? String.format("%.2f €", r.getPrix()) : "-" %></strong></td>
                                    <td><span class="badge badge-<%= r.getStatut().equals("CONFIRMEE") ? "success" : r.getStatut().equals("EN_ATTENTE") ? "warning" : "danger" %>">
                                        <%= r.getStatut() %>
                                    </span></td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        <% } else { %>
            <div class="empty-state">
                <p>📊 Sélectionnez des filtres et cliquez sur "Calculer" pour voir le chiffre d'affaires</p>
            </div>
        <% } %>
    </div>
</body>
</html>
