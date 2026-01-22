<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Societe" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sociétés</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>

    <div class="main-content">
        <div class="toolbar">
            <h1>🏢 Sociétés</h1>
            <a href="<%=request.getContextPath()%>/societe?action=add" class="btn btn-primary">+ Ajouter</a>
        </div>

        <table class="data-table">
    <tr>
        <th>ID</th>
        <th>Nom</th>
        <th>Actions</th>
    </tr>
    <%
        List<Societe> societes = (List<Societe>) request.getAttribute("societes");
        if (societes != null && !societes.isEmpty()) {
            for (Societe s : societes) {
    %>
    <tr>
        <td><%= s.getId() %></td>
        <td><%= s.getNom() %></td>
        <td>
            <a href="<%= request.getContextPath() %>/societe?action=edit&id=<%= s.getId() %>" class="btn btn-link">✏️ Modifier</a>
            <a href="<%= request.getContextPath() %>/societe?action=delete&id=<%= s.getId() %>" onclick="return confirm('Supprimer ?')" class="btn btn-link danger">❌ Supprimer</a>
        </td>
    </tr>
    <%      }
        } else {
    %>
    <tr>
        <td colspan="3">Aucune société trouvée.</td>
    </tr>
    <%
        }
    %>
        </table>
    </div>
</body>
</html>
