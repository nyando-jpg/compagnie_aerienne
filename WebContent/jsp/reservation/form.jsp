<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Reservation, model.Client, model.VolOpere, model.LigneVol, java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="fr">
<head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><%= request.getAttribute("reservation") != null ? "Modifier" : "Nouvelle" %> Réservation</title>
        <%@ include file="/jsp/common/base_style.jsp" %>
    <style>
        .search-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            border: 2px solid #667eea;
        }
        .search-section h3 {
            margin-top: 0;
            color: #667eea;
        }
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        .info-box {
            background: #e3f2fd;
            padding: 12px;
            border-radius: 5px;
            margin: 10px 0;
            color: #1976d2;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1><%= request.getAttribute("reservation") != null ? "Modifier" : "Nouvelle" %> Réservation</h1>
        </div>
            <% 
                Reservation reservation = (Reservation) request.getAttribute("reservation");
                List<Client> clients = (List<Client>) request.getAttribute("clients");
                List<VolOpere> vols = (List<VolOpere>) request.getAttribute("vols");
                List<LigneVol> lignes = (List<LigneVol>) request.getAttribute("lignes");
                boolean isEdit = (reservation != null);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                
                String selectedLigneId = request.getParameter("ligne_vol_id");
            %>
            
            <% if (!isEdit) { %>
            <!-- Section de recherche de vol -->
            <div class="search-section">
                <h3>🔍 1. Rechercher un vol</h3>
                <form method="get" action="${pageContext.request.contextPath}/reservation">
                    <input type="hidden" name="action" value="add">
                    
                    <div class="form-group">
                        <label for="ligne_vol_id">Ligne de Vol *</label>
                        <select id="ligne_vol_id" name="ligne_vol_id" required>
                            <option value="">-- Choisir une ligne de vol --</option>
                            <% if (lignes != null) {
                                for (LigneVol lv : lignes) { %>
                                    <option value="<%= lv.getId() %>" <%= String.valueOf(lv.getId()).equals(selectedLigneId) ? "selected" : "" %>>
                                        <%= lv.getNumeroVol() %> - <%= lv.getAeroportDepartNom() %> → <%= lv.getAeroportArriveeNom() %>
                                    </option>
                            <%  }
                            } %>
                        </select>
                    </div>
                    
                    <% 
                        String selectedAvionId = request.getParameter("avion_id");
                        String selectedDateDebut = request.getParameter("date_debut");
                        String selectedDateFin = request.getParameter("date_fin");
                    %>
                    
                    <div class="form-row" style="margin-top: 15px;">
                        <div class="form-group">
                            <label for="avion_id">Avion (ID)</label>
                            <input type="number" id="avion_id" name="avion_id" min="1"
                                   value="<%= selectedAvionId != null ? selectedAvionId : "" %>" 
                                   placeholder="ID de l'avion (optionnel)">
                        </div>
                    </div>
                    
                    <div class="form-row" style="margin-top: 15px;">
                        <div class="form-group">
                            <label for="date_debut">Date Début</label>
                            <input type="date" id="date_debut" name="date_debut" 
                                   value="<%= selectedDateDebut != null ? selectedDateDebut : "" %>">
                        </div>
                        
                        <div class="form-group">
                            <label for="date_fin">Date Fin</label>
                            <input type="date" id="date_fin" name="date_fin" 
                                   value="<%= selectedDateFin != null ? selectedDateFin : "" %>">
                        </div>
                    </div>
                    
                    <button type="submit" class="btn" style="margin-top: 10px;">🔍 Rechercher les vols disponibles</button>
                </form>
                
                <% if (selectedLigneId != null) { %>
                    <div class="info-box">
                        ✅ Recherche effectuée. <%= vols != null ? vols.size() : 0 %> vol(s) disponible(s) pour ce trajet.
                    </div>
                <% } %>
            </div>
            <% } %>
            
            <!-- Formulaire de réservation -->
            <% if (isEdit || (vols != null && !vols.isEmpty())) { %>
            <div style="<%= !isEdit ? "background: #fff; padding: 20px; border-radius: 8px; border: 2px solid #667eea;" : "" %>">
                <% if (!isEdit) { %>
                    <h3 style="margin-top: 0; color: #667eea;">🎫 2. Compléter la réservation</h3>
                <% } %>
                
                <form action="${pageContext.request.contextPath}/reservation" method="post" class="form">
                    <input type="hidden" name="action" value="<%= isEdit ? "update" : "insert" %>">
                    <% if (isEdit) { %>
                        <input type="hidden" name="id" value="<%= reservation.getId() %>">
                    <% } %>
                    
                    <div class="form-group">
                        <label for="client_id">Client *</label>
                        <div style="display: flex; gap: 10px; align-items: flex-end;">
                            <div style="flex: 1;">
                                <select id="client_id" name="client_id" required>
                                    <option value="">-- Sélectionner un client --</option>
                                    <% if (clients != null) {
                                        for (Client c : clients) { %>
                                            <option value="<%= c.getId() %>" 
                                                    <%= isEdit && reservation.getClientId() == c.getId() ? "selected" : "" %>>
                                                <%= c.getNom() %> <%= c.getPrenom() %> (<%= c.getEmail() %>)
                                            </option>
                                    <%  }
                                    } %>
                                </select>
                            </div>
                            <a href="${pageContext.request.contextPath}/client?action=add" 
                               class="btn btn-secondary" 
                               target="_blank"
                               style="white-space: nowrap;">
                                👤 Nouveau Client
                            </a>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="vol_opere_id">Vol Disponible *</label>
                        <select id="vol_opere_id" name="vol_opere_id" required>
                            <option value="">-- Sélectionner un vol --</option>
                            <% if (vols != null) {
                                for (VolOpere vo : vols) { %>
                                    <option value="<%= vo.getId() %>" 
                                            <%= isEdit && reservation.getVolOpereId() == vo.getId() ? "selected" : "" %>>
                                        <%= vo.getNumeroVol() != null ? vo.getNumeroVol() : "Vol #" + vo.getId() %> - 
                                        <%= vo.getAeroportDepartNom() %> → <%= vo.getAeroportArriveeNom() %> 
                                        [✈️ <%= vo.getCodeAvion() != null ? vo.getCodeAvion() : "Avion #" + vo.getAvionId() %>] 
                                        (<%= sdf.format(vo.getDateHeureDepart()) %>)
                                    </option>
                            <%  }
                            } %>
                        </select>
                    </div>
                    
                    <% if (!isEdit) { %>
                    <div class="form-group">
                        <label for="siege_vol_id">Siège Disponible *</label>
                        <select id="siege_vol_id" name="siege_vol_id" required>
                            <option value="">-- D'abord sélectionner un vol --</option>
                        </select>
                    </div>
                    <% } %>
                    
                    <% if (isEdit) { %>
                    <div class="form-group">
                        <label for="statut">Statut *</label>
                        <select id="statut" name="statut" required>
                            <option value="EN_ATTENTE" <%= "EN_ATTENTE".equals(reservation.getStatut()) ? "selected" : "" %>>EN_ATTENTE</option>
                            <option value="CONFIRMEE" <%= "CONFIRMEE".equals(reservation.getStatut()) ? "selected" : "" %>>CONFIRMEE</option>
                            <option value="ANNULEE" <%= "ANNULEE".equals(reservation.getStatut()) ? "selected" : "" %>>ANNULEE</option>
                            <option value="REMBOURSEE" <%= "REMBOURSEE".equals(reservation.getStatut()) ? "selected" : "" %>>REMBOURSEE</option>
                        </select>
                    </div>
                    <% } else { %>
                    <input type="hidden" name="statut" value="EN_ATTENTE">
                    <% } %>
                    
                    <script>
                    // Charger sièges quand vol change
                    document.getElementById('vol_opere_id').addEventListener('change', function() {
                        var volId = this.value;
                        var siegeSelect = document.getElementById('siege_vol_id');
                        
                        if (!volId) {
                            siegeSelect.innerHTML = '<option value="">-- D\'abord sélectionner un vol --</option>';
                            return;
                        }
                        
                        siegeSelect.innerHTML = '<option value="">Chargement...</option>';
                        
                        fetch('${pageContext.request.contextPath}/reservation?action=sieges&vol_opere_id=' + volId)
                            .then(response => response.json())
                            .then(data => {
                                siegeSelect.innerHTML = '<option value="">-- Choisir un siège --</option>';
                                data.forEach(function(siege) {
                                    var option = document.createElement('option');
                                    option.value = siege.id;
                                    option.textContent = siege.numeroSiege + ' (' + siege.classeLibelle + ')';
                                    siegeSelect.appendChild(option);
                                });
                                if (data.length === 0) {
                                    siegeSelect.innerHTML = '<option value="">Aucun siège disponible</option>';
                                }
                            })
                            .catch(error => {
                                siegeSelect.innerHTML = '<option value="">Erreur de chargement</option>';
                                console.error('Erreur:', error);
                            });
                    });
                    </script>
                    
                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/reservation" class="btn btn-secondary">Annuler</a>
                        <button type="submit" class="btn"><%= isEdit ? "Modifier" : "Créer la réservation" %></button>
                    </div>
                </form>
            </div>
            <% } else if (!isEdit && selectedLigneId != null) { %>
                <div class="info-box" style="background: #fff3cd; color: #856404;">
                    ⚠️ Aucun vol disponible pour ce trajet. Veuillez essayer d'autres filtres.
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
