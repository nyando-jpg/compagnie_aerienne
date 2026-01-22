<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map, model.Societe, model.VolOpere, model.Avion" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chiffre d'Affaires Diffusions</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
    <style>
        .filters-container { background: white; padding: 20px; border: 1px solid #ddd; border-radius: 4px; margin-bottom: 20px; }
        .filters-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; margin-bottom: 15px; }
        .result-box { background: white; padding: 30px; border: 1px solid #ddd; border-radius: 4px; margin-bottom: 20px; }
        .ca-principal { text-align: center; padding: 40px 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border-radius: 8px; margin-bottom: 30px; }
        .ca-montant { font-size: 3em; font-weight: bold; margin: 10px 0; }
        .ca-label { font-size: 1.2em; opacity: 0.9; }
    </style>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    <div class="main-content">
        <div class="toolbar">
            <h1>📺 Chiffre d'Affaires Diffusions</h1>
        </div>
        <% Map<String, Object> resultat = (Map<String, Object>) request.getAttribute("resultat");
           List<Societe> societes = (List<Societe>) request.getAttribute("societes");
           List<VolOpere> vols = (List<VolOpere>) request.getAttribute("volsOperes");
           List<Avion> avions = (List<Avion>) request.getAttribute("avions");
           Map<String, Object> filtres = (Map<String, Object>) request.getAttribute("filtres"); %>
        <div class="filters-container">
            <h3>🔍 Filtres</h3>
            <form method="get" action="${pageContext.request.contextPath}/diffusion/ca">
                <div class="filters-grid">
                    <div class="form-group">
                        <label for="societe_id">Société</label>
                        <select id="societe_id" name="societe_id">
                            <option value="">-- Toutes --</option>
                            <% if (societes != null) for (Societe s : societes) { %>
                                <option value="<%= s.getId() %>" <%= String.valueOf(s.getId()).equals(String.valueOf(filtres.get("societeId"))) ? "selected" : "" %>><%= s.getNom() %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="volopere_id">Vol Opéré</label>
                        <select id="volopere_id" name="volopere_id">
                            <option value="">-- Tous --</option>
                            <% if (vols != null) for (VolOpere v : vols) { %>
                                <option value="<%= v.getId() %>" <%= String.valueOf(v.getId()).equals(String.valueOf(filtres.get("volOpereId"))) ? "selected" : "" %>>Vol #<%= v.getId() %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="avion_id">Avion</label>
                        <select id="avion_id" name="avion_id">
                            <option value="">-- Tous --</option>
                            <% if (avions != null) for (Avion av : avions) { %>
                                <option value="<%= av.getId() %>" <%= String.valueOf(av.getId()).equals(String.valueOf(filtres.get("avionId"))) ? "selected" : "" %>><%= av.getCodeAvion() != null ? av.getCodeAvion() : ("Avion #" + av.getId()) %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="date_debut">Date Début</label>
                        <input type="date" id="date_debut" name="date_debut" value="<%= filtres.get("dateDebut") %>">
                    </div>
                    <div class="form-group">
                        <label for="date_fin">Date Fin</label>
                        <input type="date" id="date_fin" name="date_fin" value="<%= filtres.get("dateFin") %>">
                    </div>
                </div>
                <div style="display: flex; gap: 10px;">
                    <button type="submit" class="btn btn-primary">📊 Calculer</button>
                    <a href="${pageContext.request.contextPath}/diffusion/ca" class="btn btn-secondary">🔄 Réinitialiser</a>
                </div>
            </form>
        </div>
        <% if (resultat != null) { %>
            <div class="result-box">
                <div class="ca-principal">
                    <div class="ca-label">CHIFFRE D'AFFAIRES DIFFUSIONS</div>
                    <div class="ca-montant"><%= String.format("%.2f", resultat.get("total")) %> €</div>
                </div>
                <div class="stat-box">
                    <div class="stat-box-value"><%= resultat.get("total_tickets") %></div>
                    <div class="stat-box-label">Nombre total de diffusions</div>
                </div>
            </div>
        <% } %>

        <%-- Tableau CA par société --%>
        <div class="result-box">
            <h2 style="margin-bottom: 20px;">Chiffre d'affaires par société</h2>
            <table class="table table-striped" style="width:100%;">
                <thead>
                    <tr>
                        <th>Société</th>
                        <th>Nombre de diffusions</th>
                        <th>Chiffre d'affaires (€)</th>
                        <th>Somme payée (€)</th>
                        <th>Reste à payer (€)</th>
                    </tr>
                </thead>
                <tbody>
                <% List<Map<String, Object>> caParSociete = (List<Map<String, Object>>) request.getAttribute("caParSociete");
                   if (caParSociete != null && !caParSociete.isEmpty()) {
                       for (Map<String, Object> row : caParSociete) { %>
                        <tr>
                            <td><%= row.get("societe_nom") %></td>
                            <td style="text-align:center;"><%= row.get("total_diffusions") %></td>
                            <td style="text-align:right; font-weight:bold;"> <%= String.format("%.2f", row.get("ca_societe")) %> </td>
                            <td style="text-align:right; color:green; font-weight:bold;"> <%= String.format("%.2f", row.get("somme_payee")) %> </td>
                            <td style="text-align:right; color:red; font-weight:bold;"> <%= String.format("%.2f", row.get("reste_a_payer")) %> </td>
                        </tr>
                <%   }
                   } else { %>
                        <tr><td colspan="5" style="text-align:center;">Aucune donnée à afficher</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <!-- Liste des paiements diffusion filtrés -->
        <div class="result-box">
            <h2 style="margin-bottom: 20px;">Liste des paiements diffusion (filtrés)</h2>
            <table class="table table-striped" style="width:100%;">
                <thead>
                    <tr>
                        <th>Date paiement</th>
                        <th>Montant (€)</th>
                        <th>Méthode paiement</th>
                        <th>Diffusion</th>
                    </tr>
                </thead>
                <tbody>
                <% List<Map<String, Object>> paiementsDiffusion = (List<Map<String, Object>>) request.getAttribute("paiementsDiffusion");
                   if (paiementsDiffusion != null && !paiementsDiffusion.isEmpty()) {
                       for (Map<String, Object> row : paiementsDiffusion) { %>
                        <tr>
                            <td><%= row.get("date") %></td>
                            <td style="text-align:right; font-weight:bold;"> <%= String.format("%.2f", row.get("montant")) %> </td>
                            <td><%= row.get("methode_paiement") %></td>
                            <td><%= row.get("diffusion_info") %></td>
                        </tr>
                <%   }
                   } else { %>
                        <tr><td colspan="4" style="text-align:center;">Aucun paiement à afficher</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
