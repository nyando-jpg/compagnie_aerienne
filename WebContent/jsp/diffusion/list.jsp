<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Diffusion" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Diffusions</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
    <style>
        .toolbar { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
    </style>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>

    <div class="main-content">
        <div class="toolbar">
            <h1>📺 Diffusions</h1>
            <a href="<%=request.getContextPath()%>/diffusion?action=add" class="btn btn-primary">+ Ajouter</a>
        </div>

        <table class="data-table">
    <tr>
        <th>ID</th>
        <th>ID Societe</th>
        <th>ID Vol Opere</th>
        <th>Date</th>
        <th>Nombre</th>
        <th>Actions</th>
    </tr>
    <%
        List<Diffusion> diffusions = (List<Diffusion>) request.getAttribute("diffusions");
        if (diffusions != null && !diffusions.isEmpty()) {
            for (Diffusion d : diffusions) {
    %>
    <tr>
        <td><%= d.getId() %></td>
        <td><%= d.getIdSociete() %></td>
        <td><%= d.getIdVolOpere() %></td>
        <td><%= d.getDate() %></td>
        <td><%= d.getNombre() %></td>
        <td>
            <a href="<%= request.getContextPath() %>/diffusion?action=edit&id=<%= d.getId() %>">✏️</a>
            &nbsp;
            <a href="<%= request.getContextPath() %>/diffusion?action=delete&id=<%= d.getId() %>" onclick="return confirm('Supprimer ?')">❌</a>
        </td>
    </tr>
    <%      }
        } else {
    %>
    <tr>
        <td colspan="6">Aucune diffusion trouvée.</td>
    </tr>
    <%
        }
    %>
        </table>
    </div>
</body>
</html>
