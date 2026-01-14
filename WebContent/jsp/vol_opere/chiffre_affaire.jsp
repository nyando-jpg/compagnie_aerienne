<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.VolOpere" %>
<!DOCTYPE html>
<html lang="fr">
<head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chiffre d'Affaires - Vols Opérés</title>
        <%@ include file="/jsp/common/base_style.jsp" %>
    <style>
        .result-box {
            margin-top: 30px;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 8px;
            border: 2px solid #dee2e6;
        }
        .ca-amount {
            font-size: 2em;
            font-weight: bold;
            color: #28a745;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>💰 Chiffre d'Affaires des Vols</h1>
            <div class="toolbar-actions">
                <a href="${pageContext.request.contextPath}/vol-opere?action=list" class="btn btn-secondary">← Retour aux vols</a>
            </div>
        </div>
            
            <form method="get" action="#" class="form-container" style="margin-top: 30px;">
                <h2>Sélectionner un vol</h2>
                
                <div class="form-group">
                    <label for="vol_opere_id">Vol opéré <span class="required">*</span></label>
                    <select name="vol_opere_id" id="vol_opere_id" required 
                            style="padding: 10px; border: 1px solid #ddd; border-radius: 5px; width: 100%;">
                        <option value="">-- Choisir un vol --</option>
                        <% 
                        List<VolOpere> vols = (List<VolOpere>) request.getAttribute("vols");
                        if (vols != null) {
                            for (VolOpere vol : vols) {
                        %>
                            <option value="<%= vol.getId() %>">
                                <%= vol.getNumeroVol() %> - 
                                <%= vol.getAeroportDepartNom() %> → <%= vol.getAeroportArriveeNom() %> 
                                (<%= vol.getDateHeureDepart() != null ? 
                                    new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(vol.getDateHeureDepart()) : "" %>)
                            </option>
                        <% 
                            }
                        }
                        %>
                    </select>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn">📊 Afficher Chiffre d'Affaires</button>
                </div>
            </form>
            
            <%-- Zone pour afficher le résultat (vide pour l'instant) --%>
            <% 
            String volOpereId = request.getParameter("vol_opere_id");
            if (volOpereId != null && !volOpereId.isEmpty()) {
            %>
                <div class="result-box">
                    <h3>Résultat</h3>
                    <p><strong>Vol sélectionné:</strong> #<%= volOpereId %></p>
                    <p class="ca-amount">Chiffre d'affaires: [À calculer]</p>
                    <p style="color: #6c757d; font-size: 0.9em; margin-top: 15px;">
                        ⚠️ Fonctionnalité en développement - Backend non implémenté
                    </p>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
