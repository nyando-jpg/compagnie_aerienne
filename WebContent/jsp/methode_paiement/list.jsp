<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.MethodePaiement" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Méthodes de Paiement</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>Méthodes de Paiement</h1>
            <div class="toolbar-actions">
                <a href="?action=add" class="btn btn-primary">+ Ajouter une méthode</a>
            </div>
        </div>
            
            <% if (request.getAttribute("message") != null) { %>
                <div class="message success">${message}</div>
            <% } %>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="message error">${error}</div>
            <% } %>
            
            <%
                List<MethodePaiement> methodes = (List<MethodePaiement>) request.getAttribute("methodes");
                if (methodes != null && !methodes.isEmpty()) {
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
                        <% for (MethodePaiement m : methodes) { %>
                            <tr>
                                <td><%= m.getId() %></td>
                                <td><strong><%= m.getLibelle() %></strong></td>
                                <td><%= m.getDescription() != null ? m.getDescription() : "-" %></td>
                                <td>
                                    <a href="?action=edit&id=<%= m.getId() %>" class="btn-link">✏️ Modifier</a>
                                    <a href="?action=delete&id=<%= m.getId() %>" 
                                       onclick="return confirm('Supprimer cette méthode ?')" 
                                       class="btn-link danger">🗑️ Supprimer</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <p>Aucune méthode de paiement trouvée.</p>
                    <a href="?action=add" class="btn">+ Ajouter la première méthode</a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>