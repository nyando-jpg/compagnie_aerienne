<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Paiements Diffusions</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    <div class="main-content">
        <div class="toolbar">
            <h1>💸 Paiements Diffusions</h1>
            <a href="${pageContext.request.contextPath}/paiement-diffusion/new" class="btn btn-success">+ Nouveau paiement diffusion</a>
        </div>
        <div class="result-box">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Date</th>
                        <th>Diffusion</th>
                        <th>Montant</th>
                        <th>Méthode paiement</th>
                    </tr>
                </thead>
                <tbody>
                <% List<Map<String, Object>> paiements = (List<Map<String, Object>>) request.getAttribute("paiements");
                   if (paiements != null && !paiements.isEmpty()) {
                       for (Map<String, Object> p : paiements) { %>
                    <tr>
                        <td><%= p.get("id") %></td>
                        <td><%= p.get("date") %></td>
                        <td><%= p.get("diffusion_info") %></td>
                        <td><%= String.format("%.2f", p.get("montant")) %> €</td>
                        <td><%= p.get("methode_paiement") != null ? p.get("methode_paiement") : "-" %></td>
                    </tr>
                <%   }
                   } else { %>
                    <tr><td colspan="5" style="text-align:center;">Aucun paiement diffusion trouvé</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
