<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Client" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clients</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>Clients</h1>
            <div class="toolbar-actions">
                <a href="?action=add" class="btn btn-primary">+ Ajouter un client</a>
            </div>
        </div>
            
            <form method="get" class="search-form" style="margin: 20px 0;">
                <input type="hidden" name="action" value="list">
                <div style="display: flex; gap: 10px; max-width: 500px;">
                    <input type="text" name="search" placeholder="🔍 Rechercher par nom, prénom ou email..." 
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
                List<Client> clients = (List<Client>) request.getAttribute("clients");
                if (clients != null && !clients.isEmpty()) {
            %>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Email</th>
                            <th>Téléphone</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Client c : clients) { %>
                            <tr>
                                <td><%= c.getId() %></td>
                                <td><strong><%= c.getNom() %></strong></td>
                                <td><%= c.getPrenom() %></td>
                                <td><%= c.getEmail() %></td>
                                <td><%= c.getTelephone() != null ? c.getTelephone() : "-" %></td>
                                <td>
                                    <a href="?action=edit&id=<%= c.getId() %>" class="btn-link">✏️ Modifier</a>
                                    <a href="?action=delete&id=<%= c.getId() %>" 
                                       onclick="return confirm('Supprimer ce client ?')" 
                                       class="btn-link danger">🗑️ Supprimer</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <p>Aucun client trouvé.</p>
                    <a href="?action=add" class="btn">+ Ajouter le premier client</a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
