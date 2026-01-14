<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Aeroport" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Aéroports</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>Aéroports</h1>
            <div class="toolbar-actions">
                <a href="?action=add" class="btn btn-primary">+ Ajouter un aéroport</a>
            </div>
        </div>
            
            <form method="get" class="search-form" style="margin: 20px 0;">
                <input type="hidden" name="action" value="list">
                <div style="display: flex; gap: 10px; max-width: 500px;">
                    <input type="text" name="search" placeholder="🔍 Rechercher par code, nom, ville ou pays..." 
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
                List<Aeroport> aeroports = (List<Aeroport>) request.getAttribute("aeroports");
                if (aeroports != null && !aeroports.isEmpty()) {
            %>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Code</th>
                            <th>Nom</th>
                            <th>Ville</th>
                            <th>Pays</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Aeroport a : aeroports) { %>
                            <tr>
                                <td><strong><%= a.getCodeAeroport() %></strong></td>
                                <td><%= a.getNom() %></td>
                                <td><%= a.getVille() %></td>
                                <td><%= a.getPays() %></td>
                                <td>
                                    <a href="?action=edit&id=<%= a.getId() %>" class="btn-link">✏️ Modifier</a>
                                    <a href="?action=delete&id=<%= a.getId() %>" 
                                       onclick="return confirm('Supprimer cet aéroport ?')" 
                                       class="btn-link danger">🗑️ Supprimer</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <p>Aucun aéroport trouvé.</p>
                    <a href="?action=add" class="btn">+ Ajouter le premier aéroport</a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
