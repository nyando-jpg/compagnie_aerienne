<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Client" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("client") != null ? "Modifier" : "Ajouter" %> un Client</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1><%= request.getAttribute("client") != null ? "Modifier" : "Ajouter" %> un Client</h1>
        </div>
            <% 
                Client c = (Client) request.getAttribute("client");
                boolean isEdit = (c != null);
            %>
            
            <form action="${pageContext.request.contextPath}/client" method="post" class="form">
                <input type="hidden" name="action" value="<%= isEdit ? "update" : "insert" %>">
                <% if (isEdit) { %>
                    <input type="hidden" name="id" value="<%= c.getId() %>">
                <% } %>
                
                <div class="form-group">
                    <label for="nom">Nom *</label>
                    <input type="text" id="nom" name="nom" 
                           value="<%= isEdit ? c.getNom() : "" %>" 
                           required placeholder="Nom de famille">
                </div>
                
                <div class="form-group">
                    <label for="prenom">Prénom *</label>
                    <input type="text" id="prenom" name="prenom" 
                           value="<%= isEdit ? c.getPrenom() : "" %>" 
                           required placeholder="Prénom">
                </div>
                
                <div class="form-group">
                    <label for="email">Email *</label>
                    <input type="email" id="email" name="email" 
                           value="<%= isEdit ? c.getEmail() : "" %>" 
                           required placeholder="exemple@email.com">
                </div>
                
                <div class="form-group">
                    <label for="telephone">Téléphone</label>
                    <input type="tel" id="telephone" name="telephone" 
                           value="<%= isEdit && c.getTelephone() != null ? c.getTelephone() : "" %>" 
                           placeholder="+33 6 12 34 56 78">
                </div>
                
                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/client" class="btn btn-secondary">Annuler</a>
                    <button type="submit" class="btn"><%= isEdit ? "Modifier" : "Ajouter" %></button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
