<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.LigneVol, model.Aeroport, java.util.List" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("ligne") != null ? "Modifier" : "Ajouter" %> une Ligne de Vol</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
    <style>
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
    </style>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1><%= request.getAttribute("ligne") != null ? "Modifier" : "Ajouter" %> une Ligne de Vol</h1>
        </div>
            <% 
                LigneVol lv = (LigneVol) request.getAttribute("ligne");
                List<Aeroport> aeroports = (List<Aeroport>) request.getAttribute("aeroports");
                boolean isEdit = (lv != null);
            %>
            
            <form action="${pageContext.request.contextPath}/ligne-vol" method="post" class="form">
                <input type="hidden" name="action" value="<%= isEdit ? "update" : "insert" %>">
                <% if (isEdit) { %>
                    <input type="hidden" name="id" value="<%= lv.getId() %>">
                <% } %>
                
                <div class="form-group">
                    <label for="numero_vol">Numéro de Vol *</label>
                    <input type="text" id="numero_vol" name="numero_vol" 
                           value="<%= isEdit ? lv.getNumeroVol() : "" %>" 
                           required placeholder="Ex: AF1234">
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="aeroport_depart_id">Aéroport de Départ *</label>
                        <select id="aeroport_depart_id" name="aeroport_depart_id" required>
                            <option value="">-- Sélectionner --</option>
                            <% if (aeroports != null) {
                                for (Aeroport a : aeroports) { %>
                                    <option value="<%= a.getId() %>" 
                                            <%= isEdit && lv.getAeroportDepartId() == a.getId() ? "selected" : "" %>>
                                        <%= a.getCodeAeroport() %> - <%= a.getNom() %>
                                    </option>
                            <%  }
                            } %>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="aeroport_arrivee_id">Aéroport d'Arrivée *</label>
                        <select id="aeroport_arrivee_id" name="aeroport_arrivee_id" required>
                            <option value="">-- Sélectionner --</option>
                            <% if (aeroports != null) {
                                for (Aeroport a : aeroports) { %>
                                    <option value="<%= a.getId() %>" 
                                            <%= isEdit && lv.getAeroportArriveeId() == a.getId() ? "selected" : "" %>>
                                        <%= a.getCodeAeroport() %> - <%= a.getNom() %>
                                    </option>
                            <%  }
                            } %>
                        </select>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="duree_estimee_minutes">Durée Estimée (minutes) *</label>
                        <input type="number" id="duree_estimee_minutes" name="duree_estimee_minutes" 
                               value="<%= isEdit ? lv.getDureeEstimeeMinutes() : "" %>" 
                               required min="1" placeholder="Ex: 90">
                    </div>
                    
                    <div class="form-group">
                        <label for="distance_km">Distance (km)</label>
                        <input type="number" id="distance_km" name="distance_km" 
                               value="<%= isEdit && lv.getDistanceKm() != null ? lv.getDistanceKm() : "" %>" 
                               min="0" placeholder="Ex: 450">
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" rows="3" 
                              placeholder="Description de la ligne"><%= isEdit && lv.getDescription() != null ? lv.getDescription() : "" %></textarea>
                </div>
                
                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/ligne-vol" class="btn btn-secondary">Annuler</a>
                    <button type="submit" class="btn"><%= isEdit ? "Modifier" : "Ajouter" %></button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
