<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Societe" %>
<%@ page import="model.PaiementFacture" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Factures par Société</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    <div class="main-content">
        <h1>Factures par Société</h1>
        <form method="get" action="facture">
            <label for="societe">Société :</label>
            <select name="societeId" id="societe" onchange="this.form.submit()">
                <option value="">-- Sélectionner --</option>
                <% List<Societe> societes = (List<Societe>) request.getAttribute("societes");
                   String selectedId = request.getParameter("societeId");
                   if (societes != null) {
                       for (Societe s : societes) { %>
                <option value="<%= s.getId() %>" <%= (s.getId()+"").equals(selectedId) ? "selected" : "" %>><%= s.getNom() %></option>
                <%   }
                   } %>
            </select>
        </form>
        <br/>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Date paiement</th>
                    <th>Montant</th>
                    <th>Diffusion</th>
                </tr>
            </thead>
            <tbody>
                <% List<PaiementFacture> paiements = (List<PaiementFacture>) request.getAttribute("paiements");
                   if (paiements != null && !paiements.isEmpty()) {
                       for (PaiementFacture p : paiements) { %>
                <tr>
                    <td><%= p.getDatePaiement() %></td>
                    <td><%= p.getMontant() %></td>
                    <td><%= p.getDiffusionInfo() %></td>
                </tr>
                <%   }
                   } else { %>
                <tr><td colspan="3">Aucun paiement trouvé</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>
</body>
</html>
