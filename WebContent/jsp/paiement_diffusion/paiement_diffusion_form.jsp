<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Nouveau Paiement Diffusion</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    <div class="main-content">
        <h1>Ajouter un paiement diffusion</h1>
        <form action="${pageContext.request.contextPath}/paiement-diffusion/create" method="post" class="form">
            <div class="form-group">
                <label for="idDiffusion">Diffusion</label>
                <select name="idDiffusion" id="idDiffusion" required>
                    <% List<Map<String, Object>> diffusions = (List<Map<String, Object>>) request.getAttribute("diffusions");
                       if (diffusions != null) {
                           for (Map<String, Object> d : diffusions) { %>
                        <option value="<%= d.get("id") %>"><%= d.get("info") %></option>
                    <%   }
                       } %>
                </select>
            </div>
            <div class="form-group">
                <label for="montant">Montant (€)</label>
                <input type="number" step="0.01" name="montant" id="montant" required>
            </div>
            <div class="form-group">
                <label for="datePaiement">Date paiement</label>
                <input type="datetime-local" name="datePaiement" id="datePaiement" required>
            </div>
            <div class="form-group">
                <label for="methodePaiement">Méthode de paiement</label>
                <select name="methodePaiement" id="methodePaiement" required>
                    <% List<Map<String, Object>> methodes = (List<Map<String, Object>>) request.getAttribute("methodesPaiement");
                       if (methodes != null) {
                           for (Map<String, Object> m : methodes) { %>
                        <option value="<%= m.get("id") %>"><%= m.get("libelle") %></option>
                    <%   }
                       } %>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Enregistrer</button>
            <a href="${pageContext.request.contextPath}/paiement-diffusion" class="btn btn-secondary">Annuler</a>
        </form>
    </div>
</body>
</html>
