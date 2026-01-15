<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Avion" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Avions</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>Avions</h1>
            <div class="toolbar-actions">
                <a href="?action=add" class="btn btn-primary">+ Ajouter un avion</a>
            </div>
        </div>
        
        <% 
            String success = (String) session.getAttribute("success");
            String error = (String) session.getAttribute("error");
            
            if (success != null) { 
                session.removeAttribute("success");
        %>
            <div class="alert alert-success"><%= success %></div>
        <% } %>
        
        <% if (error != null) { 
                session.removeAttribute("error");
        %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>
        
        <% 
            List<Avion> avions = (List<Avion>) request.getAttribute("avions");
            if (avions != null && !avions.isEmpty()) {
        %>
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Code Avion</th>
                        <th>Modèle</th>
                        <th>État</th>
                        <th>Capacité Totale</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Avion avion : avions) { %>
                        <tr>
                            <td><%= avion.getId() %></td>
                            <td><strong><%= avion.getCodeAvion() %></strong></td>
                            <td><%= avion.getDesignationModel() != null ? avion.getDesignationModel() : "N/A" %></td>
                            <td>
                                <span class="badge <%= avion.getLibelleEtat() != null ? "badge-" + avion.getLibelleEtat().toLowerCase().replace(" ", "-") : "" %>">
                                    <%= avion.getLibelleEtat() != null ? avion.getLibelleEtat() : "N/A" %>
                                </span>
                            </td>
                            <td><%= avion.getCapaciteTotale() %> sièges</td>
                            <td>
                                <a href="?action=edit&id=<%= avion.getId() %>" class="btn btn-sm">✏️ Modifier</a>
                                <a href="?action=delete&id=<%= avion.getId() %>" 
                                   class="btn btn-sm btn-danger"
                                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet avion ?')">
                                    🗑️ Supprimer
                                </a>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } else { %>
            <div class="empty-state">
                <p>🛩️ Aucun avion enregistré</p>
                <a href="?action=add" class="btn btn-primary">Ajouter le premier avion</a>
            </div>
        <% } %>
    </div>
</body>
</html>
