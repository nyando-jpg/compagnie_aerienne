<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.TypeClient" %>

<!DOCTYPE html>
<html>
<head>
    <title>Types de Client</title>
    <%@ include file="../common/base_style.jsp" %>
</head>
<body>
    <%@ include file="../common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>👥 Types de Client</h1>
            <a href="<%=request.getContextPath()%>/type-client?action=add" class="btn btn-primary">
                + Ajouter un type
            </a>
        </div>

        <%
            List<TypeClient> typesClients = (List<TypeClient>) request.getAttribute("typesClients");
        %>

        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Libellé</th>
                    <th>Description</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (typesClients != null && !typesClients.isEmpty()) {
                        for (TypeClient tc : typesClients) {
                %>
                <tr>
                    <td><%= tc.getId() %></td>
                    <td><strong><%= tc.getLibelle() %></strong></td>
                    <td><%= tc.getDescription() != null ? tc.getDescription() : "-" %></td>
                    <td>
                        <a href="<%=request.getContextPath()%>/type-client?action=edit&id=<%=tc.getId()%>" 
                           class="btn-link">✏️ Modifier</a>
                        <a href="<%=request.getContextPath()%>/type-client?action=delete&id=<%=tc.getId()%>" 
                           class="btn-link"
                           style="color: #dc3545;"
                           onclick="return confirm('Supprimer ce type de client ?');">🗑️ Supprimer</a>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="4" style="text-align:center;">Aucun type de client trouvé</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
