<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="model.VolOpere" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vols Opérés</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
    <style>
        .status-badge {
            padding: 4px 8px;
            border-radius: 3px;
            font-size: 0.85em;
            font-weight: 600;
        }
        .status-PLANIFIE { background: #cce5ff; color: #004085; }
        .status-DECOLLE { background: #d4edda; color: #155724; }
        .status-EN_VOL { background: #d1ecf1; color: #0c5460; }
        .status-ATTERRI { background: #d4edda; color: #155724; }
        .status-ANNULE { background: #f8d7da; color: #721c24; }
        .status-RETARDE { background: #fff3cd; color: #856404; }
    </style>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1>Vols Opérés</h1>
            <div class="toolbar-actions">

                <a href="?action=add" class="btn btn-primary">+ Ajouter un vol opéré</a>
            </div>
        </div>
            
            <form method="get" class="search-form" style="margin: 20px 0;">
                <input type="hidden" name="action" value="list">
                
                <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 15px; margin-bottom: 15px;">
                    <div>
                        <label style="display: block; margin-bottom: 5px; font-weight: 500;">Recherche</label>
                        <input type="text" name="search" placeholder="Numéro de vol ou aéroport..." 
                               value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>"
                               style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    
                    <div>
                        <label style="display: block; margin-bottom: 5px; font-weight: 500;">Statut</label>
                        <select name="status" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                            <option value="">Tous les statuts</option>
                            <% 
                                String selectedStatus = request.getParameter("status");
                                String[] statuses = {"PLANIFIE", "DECOLLE", "EN_VOL", "ATTERRI", "ANNULE", "RETARDE"};
                                for (String st : statuses) {
                            %>
                                <option value="<%= st %>" <%= st.equals(selectedStatus) ? "selected" : "" %>><%= st %></option>
                            <% } %>
                        </select>
                    </div>
                </div>
                
                <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 15px; margin-bottom: 15px;">
                    <div>
                        <label style="display: block; margin-bottom: 5px; font-weight: 500;">Avion (ID)</label>
                        <input type="number" name="avion_id" placeholder="ID de l'avion..." 
                               value="<%= request.getParameter("avion_id") != null ? request.getParameter("avion_id") : "" %>"
                               style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;" min="1">
                    </div>
                    
                    <div>
                        <label style="display: block; margin-bottom: 5px; font-weight: 500;">Date Début</label>
                        <input type="date" name="date_debut" 
                               value="<%= request.getParameter("date_debut") != null ? request.getParameter("date_debut") : "" %>"
                               style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    
                    <div>
                        <label style="display: block; margin-bottom: 5px; font-weight: 500;">Date Fin</label>
                        <input type="date" name="date_fin" 
                               value="<%= request.getParameter("date_fin") != null ? request.getParameter("date_fin") : "" %>"
                               style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                </div>
                
                <div style="display: flex; gap: 10px;">
                    <button type="submit" class="btn" style="white-space: nowrap;">🔍 Rechercher</button>
                    <% if ((request.getParameter("search") != null && !request.getParameter("search").isEmpty()) || 
                           (request.getParameter("status") != null && !request.getParameter("status").isEmpty()) ||
                           (request.getParameter("avion_id") != null && !request.getParameter("avion_id").isEmpty()) ||
                           (request.getParameter("date_debut") != null && !request.getParameter("date_debut").isEmpty()) ||
                           (request.getParameter("date_fin") != null && !request.getParameter("date_fin").isEmpty())) { %>
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
                List<VolOpere> vols = (List<VolOpere>) request.getAttribute("vols");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                if (vols != null && !vols.isEmpty()) {
            %>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>N° Vol</th>
                            <th>Départ</th>
                            <th>Arrivée</th>
                            <th>Date Départ</th>
                            <th>Date Arrivée</th>
                            <th>Avion</th>
                            <th>Statut</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (VolOpere vo : vols) { %>
                            <tr>
                                <td><strong><%= vo.getNumeroVol() != null ? vo.getNumeroVol() : "ID: " + vo.getLigneVolId() %></strong></td>
                                <td><%= vo.getAeroportDepartNom() != null ? vo.getAeroportDepartNom() : "-" %></td>
                                <td><%= vo.getAeroportArriveeNom() != null ? vo.getAeroportArriveeNom() : "-" %></td>
                                <td><%= sdf.format(vo.getDateHeureDepart()) %></td>
                                <td><%= sdf.format(vo.getDateHeureArrivee()) %></td>
                                <td><%= vo.getCodeAvion() != null ? vo.getCodeAvion() : "ID: " + vo.getAvionId() %></td>
                                <td>
                                    <span class="status-badge status-<%= vo.getStatus() %>">
                                        <%= vo.getStatus() %>
                                    </span>
                                </td>
                                <td>
                                    <a href="?action=edit&id=<%= vo.getId() %>" class="btn-link">✏️ Modifier</a>
                                    <a href="?action=delete&id=<%= vo.getId() %>" 
                                       onclick="return confirm('Supprimer ce vol opéré ?')" 
                                       class="btn-link danger">🗑️ Supprimer</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <p>Aucun vol opéré trouvé.</p>
                    <a href="?action=add" class="btn">+ Ajouter le premier vol opéré</a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
