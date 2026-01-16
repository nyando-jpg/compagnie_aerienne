<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="model.Reservation" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Paiements</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
    <style>
        .statut-paiement-badge {
            padding: 4px 8px;
            border-radius: 3px;
            font-size: 0.85em;
            font-weight: 600;
        }
        .paiement-NON_PAYE { background: #f8d7da; color: #721c24; }
        .paiement-PARTIEL { background: #fff3cd; color: #856404; }
        .paiement-PAYE { background: #d4edda; color: #155724; }
        .paiement-NON_DETERMINE { background: #e2e3e5; color: #383d41; }
        
        .clickable-row {
            cursor: pointer;
            transition: background-color 0.2s;
        }
        .clickable-row:hover {
            background-color: #f8f9fa;
        }
    </style>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>Paiements par Réservation</h1>
        </div>
            
            <% if (request.getAttribute("message") != null) { %>
                <div class="message success">${message}</div>
            <% } %>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="message error">${error}</div>
            <% } %>
            
            <!-- Filtres -->
            <div class="filter-panel">
                <form method="get" action="${pageContext.request.contextPath}/paiement" style="display: flex; gap: 10px; align-items: flex-end; flex-wrap: wrap;">
                    <div>
                        <label for="numero_vol">Numéro Vol:</label>
                        <input type="text" id="numero_vol" name="numero_vol" value="${numero_vol}" placeholder="Ex: AF123" style="padding: 8px; border: 1px solid #ddd; border-radius: 4px;" />
                    </div>
                    <div>
                        <label for="vol_opere_id">ID Vol Opéré:</label>
                        <input type="number" id="vol_opere_id" name="vol_opere_id" value="${vol_opere_id}" placeholder="ID" style="padding: 8px; border: 1px solid #ddd; border-radius: 4px; width: 100px;" />
                    </div>
                    <div>
                        <label for="statut_paiement">Statut Paiement:</label>
                        <select id="statut_paiement" name="statut_paiement" style="padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                            <option value="">Tous</option>
                            <option value="NON_PAYE" ${statut_paiement == 'NON_PAYE' ? 'selected' : ''}>NON_PAYE</option>
                            <option value="PARTIEL" ${statut_paiement == 'PARTIEL' ? 'selected' : ''}>PARTIEL</option>
                            <option value="PAYE" ${statut_paiement == 'PAYE' ? 'selected' : ''}>PAYE</option>
                            <option value="NON_DETERMINE" ${statut_paiement == 'NON_DETERMINE' ? 'selected' : ''}>NON_DETERMINE</option>
                        </select>
                    </div>
                    <div>
                        <button type="submit" style="padding: 8px 16px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;">🔍 Filtrer</button>
                        <a href="${pageContext.request.contextPath}/paiement" style="padding: 8px 16px; background-color: #6c757d; color: white; text-decoration: none; border-radius: 4px; display: inline-block;">🔄 Réinitialiser</a>
                    </div>
                </form>
            </div>
            
            <%
                List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                if (reservations != null && !reservations.isEmpty()) {
            %>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Réservation</th>
                            <th>Client</th>
                            <th>Vol</th>
                            <th>ID Vol Opéré</th>
                            <th>Trajet</th>
                            <th>Date Vol</th>
                            <th>Montant total</th>
                            <th>Montant payé</th>
                            <th>Statut paiement</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Reservation r : reservations) { %>
                            <tr class="clickable-row" onclick="window.location.href='${pageContext.request.contextPath}/paiement?action=details&reservation_id=<%= r.getId() %>'">
                                <td><strong>#<%= r.getId() %></strong></td>
                                <td><%= r.getClientFullName() %></td>
                                <td><strong><%= r.getNumeroVol() %></strong></td>
                                <td>#<%= r.getVolOpereId() %></td>
                                <td><%= r.getAeroportDepart() %> → <%= r.getAeroportArrivee() %></td>
                                <td><%= sdf.format(r.getDateHeureDepart()) %></td>
                                <td><%= String.format("%.2f €", r.getMontantTotal() != null ? r.getMontantTotal() : 0.0) %></td>
                                <td><%= String.format("%.2f €", r.getMontantPaye() != null ? r.getMontantPaye() : 0.0) %></td>
                                <td>
                                    <% String sp = r.getStatutPaiement(); %>
                                    <span class="statut-paiement-badge paiement-<%= sp %>">
                                        <%= sp %>
                                    </span>
                                </td>
                                <td onclick="event.stopPropagation();">
                                    <a href="${pageContext.request.contextPath}/paiement?action=add&reservation_id=<%= r.getId() %>" class="btn-link">💵 Enregistrer un paiement</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <p>Aucune réservation trouvée.</p>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>