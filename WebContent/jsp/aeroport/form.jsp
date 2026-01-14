<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Aeroport" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("aeroport") != null ? "Modifier" : "Ajouter" %> un Aéroport</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1><%= request.getAttribute("aeroport") != null ? "Modifier" : "Ajouter" %> un Aéroport</h1>
        </div>
            <% 
                Aeroport a = (Aeroport) request.getAttribute("aeroport");
                boolean isEdit = (a != null);
            %>
            
            <form action="${pageContext.request.contextPath}/aeroport" method="post" class="form">
                <input type="hidden" name="action" value="<%= isEdit ? "update" : "insert" %>">
                <% if (isEdit) { %>
                    <input type="hidden" name="id" value="<%= a.getId() %>">
                <% } %>
                
                <div class="form-group">
                    <label for="code_aeroport">Code Aéroport (IATA) *</label>
                    <input type="text" id="code_aeroport" name="code_aeroport" 
                           value="<%= isEdit ? a.getCodeAeroport() : "" %>" 
                           required maxlength="10" placeholder="Ex: CDG, JFK, DXB">
                </div>
                
                <div class="form-group">
                    <label for="nom">Nom *</label>
                    <input type="text" id="nom" name="nom" 
                           value="<%= isEdit ? a.getNom() : "" %>" 
                           required placeholder="Ex: Aéroport Charles de Gaulle">
                </div>
                
                <div class="form-group">
                    <label for="ville">Ville *</label>
                    <input type="text" id="ville" name="ville" 
                           value="<%= isEdit ? a.getVille() : "" %>" 
                           required placeholder="Ex: Paris">
                </div>
                
                <div class="form-group">
                    <label for="pays">Pays *</label>
                    <input type="text" id="pays" name="pays" 
                           value="<%= isEdit ? a.getPays() : "" %>" 
                           required placeholder="Ex: France">
                </div>
                
                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/aeroport" class="btn btn-secondary">Annuler</a>
                    <button type="submit" class="btn"><%= isEdit ? "Modifier" : "Ajouter" %></button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
