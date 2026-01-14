<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="model.Reservation, model.LigneVol" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réservations</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
    <style>
        .statut-badge {
            padding: 4px 8px;
            border-radius: 3px;
            font-size: 0.85em;
            font-weight: 600;
        }
        .statut-EN_ATTENTE { background: #fff3cd; color: #856404; }
        .statut-CONFIRMEE { background: #d4edda; color: #155724; }
        .statut-ANNULEE { background: #f8d7da; color: #721c24; }
        .statut-REMBOURSEE { background: #d1ecf1; color: #0c5460; }
    </style>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>Réservations</h1>
            <div class="toolbar-actions">
                <a href="?action=add" class="btn btn-primary">+ Nouvelle Réservation</a>
            </div>
        </div>
            
            <form method="get" class="search-form" style="margin: 20px 0;">
                <input type="hidden" name="action" value="list">
                
                <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 15px; margin-bottom: 15px;">
                    <div>
                        <label style="display: block; margin-bottom: 5px; font-weight: 500;">Recherche</label>
                        <input type="text" name="search" placeholder="Client ou n° vol..." 
                               value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>"
                               style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    
                    <div>
                        <label style="display: block; margin-bottom: 5px; font-weight: 500;">Statut</label>
                        <select name="statut" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                            <option value="">Tous les statuts</option>
                            <% 
                                String selectedStatut = request.getParameter("statut");
                                String[] statuts = {"EN_ATTENTE", "CONFIRMEE", "ANNULEE", "REMBOURSEE"};
                                for (String st : statuts) {
                            %>
                                <option value="<%= st %>" <%= st.equals(selectedStatut) ? "selected" : "" %>><%= st %></option>
                            <% } %>
                        </select>
                    </div>
                    
                    <div>
                        <label style="display: block; margin-bottom: 5px; font-weight: 500;">Ligne de Vol</label>
                        <select name="ligne_vol_id" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                            <option value="">Toutes les lignes</option>
                            <% 
                                List<LigneVol> lignes = (List<LigneVol>) request.getAttribute("lignes");
                                String selectedLigneId = request.getParameter("ligne_vol_id");
                                if (lignes != null) {
                                    for (LigneVol lv : lignes) {
                            %>
                                <option value="<%= lv.getId() %>" <%= String.valueOf(lv.getId()).equals(selectedLigneId) ? "selected" : "" %>>
                                    <%= lv.getNumeroVol() %>
                                </option>
                            <%  }
                            } %>
                        </select>
                    </div>
                </div>
                
                <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 15px; margin-bottom: 15px;">
                    <div>
                        <label style="display: block; margin-bottom: 5px; font-weight: 500;">Date Début</label>
                        <input type="date" name="date_min" 
                               value="<%= request.getParameter("date_min") != null ? request.getParameter("date_min") : "" %>"
                               style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    
                    <div>
                        <label style="display: block; margin-bottom: 5px; font-weight: 500;">Date Fin</label>
                        <input type="date" name="date_max" 
                               value="<%= request.getParameter("date_max") != null ? request.getParameter("date_max") : "" %>"
                               style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                </div>
                
                <div style="display: flex; gap: 10px;">
                    <button type="submit" class="btn" style="white-space: nowrap;">🔍 Rechercher</button>
                    <% if ((request.getParameter("search") != null && !request.getParameter("search").isEmpty()) || 
                           (request.getParameter("statut") != null && !request.getParameter("statut").isEmpty()) ||
                           (request.getParameter("ligne_vol_id") != null && !request.getParameter("ligne_vol_id").isEmpty()) ||
                           (request.getParameter("date_min") != null && !request.getParameter("date_min").isEmpty()) ||
                           (request.getParameter("date_max") != null && !request.getParameter("date_max").isEmpty())) { %>
                        <a href="?action=list" class="btn btn-secondary">Effacer</a>
                    <% } %>
                </div>
            </form>
            
            <% if (request.getAttribute("message") != null) { %>
                <div class="message success">${message}</div>
            <% } %>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="message error">${error}</div>
            <% } %>
            
            <% 
                List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                if (reservations != null && !reservations.isEmpty()) {
            %>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Client</th>
                            <th>Vol</th>
                            <th>Siège</th>
                            <th>Trajet</th>
                            <th>Date Vol</th>
                            <th>Date Réservation</th>
                            <th>Statut</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Reservation r : reservations) { %>
                            <tr>
                                <td><strong>#<%= r.getId() %></strong></td>
                                <td><%= r.getClientFullName() %></td>
                                <td><strong><%= r.getNumeroVol() %></strong></td>
                                <td><strong><%= r.getNumeroSiege() != null ? r.getNumeroSiege() : "-" %></strong></td>
                                <td><%= r.getAeroportDepart() %> → <%= r.getAeroportArrivee() %></td>
                                <td><%= sdf.format(r.getDateHeureDepart()) %></td>
                                <td><%= sdf.format(r.getDateReservation()) %></td>
                                <td>
                                    <span class="statut-badge statut-<%= r.getStatut() %>">
                                        <%= r.getStatut() %>
                                    </span>
                                </td>
                                <td>
                                    <a href="?action=edit&id=<%= r.getId() %>" class="btn-link">✏️ Modifier</a>
                                    <a href="?action=delete&id=<%= r.getId() %>" 
                                       onclick="return confirm('Supprimer cette réservation ?')" 
                                       class="btn-link danger">🗑️ Supprimer</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <p>Aucune réservation trouvée.</p>
                    <a href="?action=add" class="btn">+ Créer la première réservation</a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
