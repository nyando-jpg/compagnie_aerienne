<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.ClasseSiege" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("classe") != null ? "Modifier" : "Ajouter" %> une Classe de Siège</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1><%= request.getAttribute("classe") != null ? "Modifier" : "Ajouter" %> une Classe de Siège</h1>
        </div>
            <% 
                ClasseSiege cs = (ClasseSiege) request.getAttribute("classe");
                boolean isEdit = (cs != null);
            %>
            
            <form action="${pageContext.request.contextPath}/classe-siege" method="post" class="form">
                <input type="hidden" name="action" value="<%= isEdit ? "update" : "insert" %>">
                <% if (isEdit) { %>
                    <input type="hidden" name="id" value="<%= cs.getId() %>">
                <% } %>
                
                <div class="form-group">
                    <label for="libelle">Libellé *</label>
                    <input type="text" id="libelle" name="libelle" 
                           value="<%= isEdit ? cs.getLibelle() : "" %>" 
                           required maxlength="100" placeholder="Ex: Économique, Affaires, Première">
                </div>
                
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" rows="4" 
                              placeholder="Description de la classe de siège"><%= isEdit && cs.getDescription() != null ? cs.getDescription() : "" %></textarea>
                </div>
                
                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/classe-siege" class="btn btn-secondary">Annuler</a>
                    <button type="submit" class="btn"><%= isEdit ? "Modifier" : "Ajouter" %></button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
