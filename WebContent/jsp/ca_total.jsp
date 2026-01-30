<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.CATotal" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chiffre d'Affaires Total</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    <div class="main-content">
        <h1>Chiffre d'Affaires Total</h1>
        <form method="get" style="margin-bottom:20px;">
            <label for="mois">Mois :</label>
            <select name="mois" id="mois">
                <% for(int m=1;m<=12;m++){ String sm = (m<10?"0":"")+m; %>
                <option value="<%=sm%>" <%= request.getParameter("mois")!=null && request.getParameter("mois").equals(sm)?"selected":"" %>><%=sm%></option>
                <% } %>
            </select>
            <label for="annee">Année :</label>
            <input type="number" name="annee" id="annee" min="2000" max="2100" value="<%= request.getParameter("annee")!=null?request.getParameter("annee"):java.time.Year.now() %>" />
            <button type="submit">Filtrer</button>
        </form>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Aéroport départ</th>
                    <th>Aéroport arrivée</th>
                    <th>Avion</th>
                    <th>Date départ</th>
                    <th>Heure départ</th>
                    <th>CA place</th>
                    <th>CA Extra</th>
                    <th>Montant généré par diffusion</th>
                    <th>Montant Total CA</th>
                    <th>Déjà payé diffusion</th>
                    <th>Reste à payer diffusion</th>
                </tr>
            </thead>
            <tbody>
                <% 
                List<CATotal> caList = (List<CATotal>) request.getAttribute("caTotalList");
                java.math.BigDecimal totalGeneral = java.math.BigDecimal.ZERO;
                if (caList != null) {
                    for (CATotal ca : caList) {
                        totalGeneral = totalGeneral.add(ca.getCaTotal());
                %>
                <tr>
                    <td><%= ca.getAeroportDepart() %></td>
                    <td><%= ca.getAeroportArrivee() %></td>
                    <td><%= ca.getAvion() %></td>
                    <td><%= ca.getDateDepart() %></td>
                    <td><%= ca.getHeureDepart() %></td>
                    <td><%= ca.getCaPlace() %></td>
                    <td><%= ca.getCaExtra() %></td>
                    <td><%= ca.getCaDiffusion() %></td>
                    <td><%= ca.getCaTotal() %></td>
                    <td><%= ca.getMontantPayeDiffusion() %></td>
                    <td><%= ca.getResteAPayerDiffusion() %></td>
                </tr>
                <%  }
            } else { %>
                <tr><td colspan="10">Aucune donnée</td></tr>
                <% } %>
                <tr style="font-weight:bold; background:#f0f0f0;">
                    <td colspan="8" style="text-align:right;">TOTAL GÉNÉRAL :</td>
                    <td><%= totalGeneral %></td>
                    <td colspan="2"></td>
                </tr>
            </tbody>
        </table>
    </div>
</body>
</html>
