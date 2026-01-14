<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.ClasseSiege" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Classes de Siège</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>Classes de Sièges</h1>
            <div class="toolbar-actions">
                <a href="?action=add" class="btn btn-primary">+ Ajouter une classe</a>
            </div>
        </div>
            
            <form method="get" class="search-form" style="margin: 20px 0;">
                <input type="hidden" name="action" value="list">
                <div style="display: flex; gap: 10px; max-width: 500px;">
                    <input type="text" name="search" placeholder="🔍 Rechercher par libellé ou description..." 
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
                List<ClasseSiege> classes = (List<ClasseSiege>) request.getAttribute("classes");
                if (classes != null && !classes.isEmpty()) {
            %>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Libellé</th>
                            <th>Description</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (ClasseSiege cs : classes) { %>
                            <tr>
                                <td><%= cs.getId() %></td>
                                <td><strong><%= cs.getLibelle() %></strong></td>
                                <td><%= cs.getDescription() != null ? cs.getDescription() : "-" %></td>
                                <td>
                                    <a href="?action=edit&id=<%= cs.getId() %>" class="btn-link">✏️ Modifier</a>
                                    <a href="?action=delete&id=<%= cs.getId() %>" 
                                       onclick="return confirm('Supprimer cette classe ?')" 
                                       class="btn-link danger">🗑️ Supprimer</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <p>Aucune classe de siège trouvée.</p>
                    <a href="?action=add" class="btn">+ Ajouter la première classe</a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
