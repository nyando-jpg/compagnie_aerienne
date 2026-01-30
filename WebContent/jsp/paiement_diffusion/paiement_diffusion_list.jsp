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
        <div class="filter-box" style="margin-bottom:20px;">
            <form method="get" action="${pageContext.request.contextPath}/paiement-diffusion">
                <label for="societe">Société :</label>
                <select name="societeId" id="societe">
                    <option value="">-- Toutes --</option>
                    <% List<model.Societe> societes = (List<model.Societe>) request.getAttribute("societes");
                       String selectedSociete = request.getParameter("societeId");
                       if (societes != null) for (model.Societe s : societes) { %>
                        <option value="<%= s.getId() %>" <%= (selectedSociete != null && selectedSociete.equals(String.valueOf(s.getId()))) ? "selected" : "" %>><%= s.getNom() %></option>
                    <% } %>
                </select>
                <label for="dateDebut">Date début :</label>
                <input type="date" name="dateDebut" id="dateDebut" value="<%= request.getParameter("dateDebut") != null ? request.getParameter("dateDebut") : "" %>" />
                <label for="dateFin">Date fin :</label>
                <input type="date" name="dateFin" id="dateFin" value="<%= request.getParameter("dateFin") != null ? request.getParameter("dateFin") : "" %>" />
                <button type="submit" class="btn btn-primary">Filtrer</button>
            </form>
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
                <% 
                   List<Map<String, Object>> paiements = (List<Map<String, Object>>) request.getAttribute("paiements");
                   Double sommePayee = (Double) request.getAttribute("sommePayee");
                   Double sommeDue = (Double) request.getAttribute("sommeDue");
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
            <% if (selectedSociete != null && !selectedSociete.isEmpty()) { %>
            <div style="margin-top:20px; font-weight:bold;">
                <span>Total payé par la société : </span>
                <%= String.format("%.2f", sommePayee != null ? sommePayee : 0.0) + " €" %><br/>
                <span>Total dû par la société : </span>
                <%= String.format("%.2f", sommeDue != null ? sommeDue : 0.0) + " €" %>
            </div>
            <% } %>
        </div>
    </div>
</body>
</html>
