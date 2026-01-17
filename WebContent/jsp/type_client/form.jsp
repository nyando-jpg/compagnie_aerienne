<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="model.TypeClient" %>

<%
    TypeClient tc = (TypeClient) request.getAttribute("typeClient");
    boolean isEdit = (tc != null && tc.getId() > 0);
%>

<!DOCTYPE html>
<html>
<head>
    <title><%= isEdit ? "Modifier" : "Ajouter" %> un type de client</title>
    <%@ include file="../common/base_style.jsp" %>
</head>
<body>
    <%@ include file="../common/sidebar.jsp" %>
    
    <div class="main-content">
        <h1><%= isEdit ? "✏️ Modifier" : "➕ Ajouter" %> un type de client</h1>

        <form method="post" action="<%=request.getContextPath()%>/type-client" class="form-card">
            <% if (isEdit) { %>
                <input type="hidden" name="id" value="<%= tc.getId() %>"/>
            <% } %>

            <div class="form-group">
                <label for="libelle">Libellé *</label>
                <input type="text" id="libelle" name="libelle" required
                       value="<%= isEdit ? tc.getLibelle() : "" %>"
                       placeholder="Ex: Adulte, Enfant, Senior, Étudiant">
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="3"
                          placeholder="Description du type de client"><%= isEdit && tc.getDescription() != null ? tc.getDescription() : "" %></textarea>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-success">
                    <%= isEdit ? "💾 Mettre à jour" : "➕ Ajouter" %>
                </button>
                <a href="<%=request.getContextPath()%>/type-client?action=list" class="btn btn-secondary">
                    ❌ Annuler
                </a>
            </div>
        </form>
    </div>
</body>
</html>
