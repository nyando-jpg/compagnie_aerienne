<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.ModelAvion" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modèles d'Avion</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>Modèles d'Avion</h1>
            <div class="toolbar-actions">
                <a href="?action=add" class="btn btn-primary">+ Ajouter un modèle</a>
            </div>
        </div>
        
        <% if (request.getAttribute("message") != null) { %>
            <div class="message success"><%= request.getAttribute("message") %></div>
        <% } %>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="message error"><%= request.getAttribute("error") %></div>
        <% } %>
        
        <% 
            @SuppressWarnings("unchecked")
            List<ModelAvion> models = (List<ModelAvion>) request.getAttribute("models");
            if (models != null && !models.isEmpty()) {
        %>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Désignation</th>
                        <th>Fabricant</th>
                        <th>Capacité</th>
                        <th>Autonomie (km)</th>
                        <th>Vitesse (km/h)</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (ModelAvion model : models) { %>
                        <tr>
                            <td><%= model.getId() %></td>
                            <td><strong><%= model.getDesignation() %></strong></td>
                            <td><%= model.getFabricant() %></td>
                            <td><%= model.getCapacite() %></td>
                            <td>
                                <% if (model.getAutonomieKm() != null) { %>
                                    <%= model.getAutonomieKm() %>
                                <% } else { %>
                                    <span style="color: #999;">-</span>
                                <% } %>
                            </td>
                            <td>
                                <% if (model.getVitesseKmH() != null) { %>
                                    <%= model.getVitesseKmH() %>
                                <% } else { %>
                                    <span style="color: #999;">-</span>
                                <% } %>
                            </td>
                            <td class="action-buttons">
                                <a href="?action=edit&id=<%= model.getId() %>" class="btn btn-warning">✏️</a>
                                <a href="?action=delete&id=<%= model.getId() %>" class="btn btn-danger" 
                                   onclick="return confirm('Voulez-vous vraiment supprimer ce modèle ?')">🗑️</a>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } else { %>
            <div style="text-align: center; padding: 40px; background: white; border-radius: 8px; margin-top: 20px;">
                <p style="color: #666; font-size: 16px; margin-bottom: 20px;">Aucun modèle d'avion trouvé</p>
                <a href="?action=add" class="btn btn-primary">Ajouter le premier modèle</a>
            </div>
        <% } %>
    </div>
</body>
</html>
