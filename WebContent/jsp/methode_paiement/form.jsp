<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.MethodePaiement" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("methode") != null ? "Modifier" : "Ajouter" %> une Méthode de Paiement</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1><%= request.getAttribute("methode") != null ? "Modifier" : "Ajouter" %> une Méthode de Paiement</h1>
        </div>
            <%
                MethodePaiement m = (MethodePaiement) request.getAttribute("methode");
                boolean isEdit = (m != null);
            %>
            
            <form action="${pageContext.request.contextPath}/methode-paiement" method="post" class="form">
                <input type="hidden" name="action" value="<%= isEdit ? "update" : "insert" %>">
                <% if (isEdit) { %>
                    <input type="hidden" name="id" value="<%= m.getId() %>">
                <% } %>
                
                <div class="form-group">
                    <label for="libelle">Libellé *</label>
                    <input type="text" id="libelle" name="libelle" 
                           value="<%= isEdit ? m.getLibelle() : "" %>" 
                           required maxlength="100" placeholder="Ex: Carte bancaire, PayPal, etc.">
                </div>
                
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" rows="4" 
                              placeholder="Description de la méthode de paiement"><%= isEdit && m.getDescription() != null ? m.getDescription() : "" %></textarea>
                </div>
                
                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/methode-paiement" class="btn btn-secondary">Annuler</a>
                    <button type="submit" class="btn"><%= isEdit ? "Modifier" : "Ajouter" %></button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>