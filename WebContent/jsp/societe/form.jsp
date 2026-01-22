<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Societe" %>
<%
    Societe s = (Societe) request.getAttribute("societe");
    if (s == null) s = new Societe();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Société</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>

    <div class="main-content">
        <h1>Ajouter / Modifier Société</h1>
        <%
            String formError = (String) request.getAttribute("formError");
            if (formError != null) {
        %>
        <div class="message error"><%= formError %></div>
        <%
            }
        %>
        <form method="post" action="<%= request.getContextPath() %>/societe" class="form-grid">
            <input type="hidden" name="id" value="<%= s.getId() %>" />
            <label>Nom: <input type="text" name="nom" value="<%= s.getNom() != null ? s.getNom() : "" %>" /></label>
            <div></div>
            <div>
                <button type="submit" class="btn btn-primary">Enregistrer</button>
                <a href="<%=request.getContextPath()%>/societe?action=list" class="btn">Annuler</a>
            </div>
        </form>
    </div>
</body>
</html>
