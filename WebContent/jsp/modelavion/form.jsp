<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.ModelAvion" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("model") == null ? "Ajouter" : "Modifier" %> un Modèle d'Avion</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1><%= request.getAttribute("model") == null ? "Ajouter" : "Modifier" %> un Modèle d'Avion</h1>
        </div>
        
        <% 
            ModelAvion model = (ModelAvion) request.getAttribute("model");
            boolean isEdit = (model != null);
        %>
        
        <form method="post" action="${pageContext.request.contextPath}/modele" class="form-card">
            <input type="hidden" name="action" value="<%= isEdit ? "update" : "insert" %>">
            <% if (isEdit) { %>
                <input type="hidden" name="id" value="<%= model.getId() %>">
            <% } %>
            
            <div class="form-group">
                <label for="designation">Désignation *</label>
                <input type="text" 
                       id="designation" 
                       name="designation" 
                       value="<%= isEdit ? model.getDesignation() : "" %>" 
                       required 
                       placeholder="Ex: Boeing 747-8">
                <small>Nom complet du modèle</small>
            </div>
            
            <div class="form-group">
                <label for="fabricant">Fabricant *</label>
                <input type="text" 
                       id="fabricant" 
                       name="fabricant" 
                       value="<%= isEdit ? model.getFabricant() : "" %>" 
                       required 
                       placeholder="Ex: Boeing">
                <small>Constructeur de l'avion</small>
            </div>
            
            <div class="form-group">
                <label for="capacite">Capacité *</label>
                <input type="number" 
                       id="capacite" 
                       name="capacite" 
                       value="<%= isEdit ? model.getCapacite() : "" %>" 
                       required 
                       min="1"
                       placeholder="Ex: 416">
                <small>Nombre maximum de passagers</small>
            </div>
            
            <div class="form-group">
                <label for="autonomie_km">Autonomie (km)</label>
                <input type="number" 
                       id="autonomie_km" 
                       name="autonomie_km" 
                       value="<%= isEdit && model.getAutonomieKm() != null ? model.getAutonomieKm() : "" %>" 
                       min="0"
                       placeholder="Ex: 14685">
                <small>Distance maximale sans ravitaillement</small>
            </div>
            
            <div class="form-group">
                <label for="vitesse_km_h">Vitesse de croisière (km/h)</label>
                <input type="number" 
                       id="vitesse_km_h" 
                       name="vitesse_km_h" 
                       value="<%= isEdit && model.getVitesseKmH() != null ? model.getVitesseKmH() : "" %>" 
                       min="0"
                       placeholder="Ex: 908">
                <small>Vitesse de croisière typique</small>
            </div>
            
            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" 
                          name="description" 
                          rows="3"
                          placeholder="Ex: Gros porteur long-courrier"><%= isEdit && model.getDescription() != null ? model.getDescription() : "" %></textarea>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary"><%= isEdit ? "Modifier" : "Ajouter" %></button>
                <a href="?action=list" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
</body>
</html>
