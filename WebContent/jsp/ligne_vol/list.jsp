<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.LigneVol" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lignes de Vol</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>Lignes de Vol</h1>
            <div class="toolbar-actions">
                <a href="?action=add" class="btn btn-primary">+ Ajouter une ligne</a>
            </div>
        </div>
            
            <form method="get" class="search-form" style="margin: 20px 0;">
                <input type="hidden" name="action" value="list">
                <div style="display: flex; gap: 10px; max-width: 500px;">
                    <input type="text" name="search" placeholder="🔍 Rechercher par numéro ou aéroport..." 
                           value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>"
                           style="flex: 1; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                    <button type="submit" class="btn" style="white-space: nowrap;">Rechercher</button>
                    <% if (request.getParameter("search") != null && !request.getParameter("search").isEmpty()) { %>
                        <a href="?action=list" class="btn btn-secondary">Effacer</a>
                    <% } %>
                </div>
            </form>
            
            <% if (request.getAttribute("message") != null) { %>
                <div class="message success">${message}</div>
            <% } %>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="message error">${error}</div>
            <% } %>
            
            <% 
                List<LigneVol> lignes = (List<LigneVol>) request.getAttribute("lignes");
                if (lignes != null && !lignes.isEmpty()) {
            %>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>N° Vol</th>
                            <th>Départ</th>
                            <th>Arrivée</th>
                            <th>Durée (min)</th>
                            <th>Distance (km)</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (LigneVol lv : lignes) { %>
                            <tr>
                                <td><strong><%= lv.getNumeroVol() %></strong></td>
                                <td><%= lv.getAeroportDepartNom() != null ? lv.getAeroportDepartNom() : "ID: " + lv.getAeroportDepartId() %></td>
                                <td><%= lv.getAeroportArriveeNom() != null ? lv.getAeroportArriveeNom() : "ID: " + lv.getAeroportArriveeId() %></td>
                                <td><%= lv.getDureeEstimeeMinutes() %> min</td>
                                <td><%= lv.getDistanceKm() != null ? lv.getDistanceKm() + " km" : "-" %></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/vol-opere?action=list&ligne_vol_id=<%= lv.getId() %>" class="btn-link">📋 Vols opérés</a>
                                    <a href="?action=edit&id=<%= lv.getId() %>" class="btn-link">✏️ Modifier</a>
                                    <a href="?action=delete&id=<%= lv.getId() %>" 
                                       onclick="return confirm('Supprimer cette ligne ?')" 
                                       class="btn-link danger">🗑️ Supprimer</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <p>Aucune ligne de vol trouvée.</p>
                    <a href="?action=add" class="btn">+ Ajouter la première ligne</a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
