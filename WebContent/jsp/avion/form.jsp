<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Avion" %>
<%@ page import="model.ClasseSiege" %>
<%@ page import="model.ModelAvion" %>
<%@ page import="java.util.List" %>
<%
    Avion avion = (Avion) request.getAttribute("avion");
    List<ClasseSiege> classes = (List<ClasseSiege>) request.getAttribute("classes");
    List<ModelAvion> modeles = (List<ModelAvion>) request.getAttribute("modeles");
    boolean isEdit = (avion != null);
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= isEdit ? "Modifier" : "Ajouter" %> un avion</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1><%= isEdit ? "Modifier" : "Ajouter" %> un avion</h1>
            <div class="toolbar-actions">
                <a href="?action=list" class="btn btn-secondary">← Retour à la liste</a>
            </div>
        </div>
        
        <div class="form-container">
            <form method="post" action="?action=<%= isEdit ? "update" : "insert" %>">
                <% if (isEdit) { %>
                    <input type="hidden" name="id" value="<%= avion.getId() %>">
                <% } %>
                
                <div class="form-group">
                    <label for="code_avion">Code Avion *</label>
                    <input type="text" 
                           id="code_avion" 
                           name="code_avion" 
                           value="<%= isEdit ? avion.getCodeAvion() : "" %>"
                           required
                           placeholder="Ex: A320-001">
                </div>
                
                <div class="form-group">
                    <label for="model_avion_id">Modèle d'Avion *</label>
                    <select id="model_avion_id" 
                            name="model_avion_id" 
                            required>
                        <option value="">-- Sélectionnez un modèle --</option>
                        <% for (ModelAvion modele : modeles) { %>
                            <option value="<%= modele.getId() %>" 
                                    <%= isEdit && avion.getModelAvionId() == modele.getId() ? "selected" : "" %>>
                                <%= modele.getDesignation() %> - <%= modele.getFabricant() %> (Capacité: <%= modele.getCapacite() %>)
                            </option>
                        <% } %>
                    </select>
                    <small class="form-help">Choisissez le modèle d'avion dans la liste</small>
                </div>
                
                <div class="form-group">
                    <label for="etat_avion_id">État de l'Avion (ID) *</label>
                    <input type="number" 
                           id="etat_avion_id" 
                           name="etat_avion_id" 
                           value="<%= isEdit ? avion.getEtatAvionId() : "" %>"
                           required
                           min="1"
                           placeholder="ID de l'état">
                    <small class="form-help">Entrez l'ID de l'état (ex: 1 pour En service, 2 pour Maintenance)</small>
                </div>
                
                <div class="form-group">
                    <label for="capacite_totale">Capacité Totale *</label>
                    <input type="number" 
                           id="capacite_totale" 
                           name="capacite_totale" 
                           value="<%= isEdit ? avion.getCapaciteTotale() : "" %>"
                           required
                           min="1"
                           placeholder="Nombre total de sièges">
                    <small class="form-help">Ce nombre doit correspondre à la somme des sièges par classe ci-dessous</small>
                </div>
                
                <% if (!isEdit && classes != null && !classes.isEmpty()) { %>
                    <fieldset style="border: 2px solid #ddd; padding: 20px; border-radius: 5px; margin: 20px 0;">
                        <legend style="font-weight: bold; padding: 0 10px;">📊 Nombre de sièges par classe</legend>
                        
                        <% for (ClasseSiege classe : classes) { %>
                            <div class="form-group">
                                <label for="nbr_sieges_<%= classe.getId() %>">
                                    <%= classe.getLibelle() %>
                                    <% if (classe.getDescription() != null && !classe.getDescription().isEmpty()) { %>
                                        <small style="color: #666;">(<%= classe.getDescription() %>)</small>
                                    <% } %>
                                </label>
                                <input type="number" 
                                       id="nbr_sieges_<%= classe.getId() %>" 
                                       name="nbr_sieges_<%= classe.getId() %>" 
                                       value="0"
                                       min="0"
                                       placeholder="Nombre de sièges">
                            </div>
                        <% } %>
                    </fieldset>
                <% } %>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <%= isEdit ? "💾 Enregistrer les modifications" : "➕ Ajouter l'avion" %>
                    </button>
                    <a href="?action=list" class="btn btn-secondary">Annuler</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
