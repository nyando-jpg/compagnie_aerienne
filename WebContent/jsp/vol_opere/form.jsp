<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.VolOpere, model.LigneVol, java.util.List, java.util.Map" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("vol") != null ? "Modifier" : "Ajouter" %> un Vol Opéré</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
    <style>
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
    </style>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="toolbar">
            <h1><%= request.getAttribute("vol") != null ? "Modifier" : "Ajouter" %> un Vol Opéré</h1>
        </div>
            <% 
                VolOpere vo = (VolOpere) request.getAttribute("vol");
                List<LigneVol> lignes = (List<LigneVol>) request.getAttribute("lignes");
                List<Map<String, Object>> statuses = (List<Map<String, Object>>) request.getAttribute("statuses");
                boolean isEdit = (vo != null);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            %>
            
            <form action="${pageContext.request.contextPath}/vol-opere" method="post" class="form">
                <input type="hidden" name="action" value="<%= isEdit ? "update" : "insert" %>">
                <% if (isEdit) { %>
                    <input type="hidden" name="id" value="<%= vo.getId() %>">
                <% } %>
                
                <div class="form-group">
                    <label for="ligne_vol_id">Ligne de Vol *</label>
                    <select id="ligne_vol_id" name="ligne_vol_id" required>
                        <option value="">-- Sélectionner une ligne --</option>
                        <% if (lignes != null) {
                            for (LigneVol lv : lignes) { %>
                                <option value="<%= lv.getId() %>" 
                                        <%= isEdit && vo.getLigneVolId() == lv.getId() ? "selected" : "" %>>
                                    <%= lv.getNumeroVol() %> (<%= lv.getAeroportDepartNom() %> → <%= lv.getAeroportArriveeNom() %>)
                                </option>
                        <%  }
                        } %>
                    </select>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="date_heure_depart">Date et Heure de Départ *</label>
                        <input type="datetime-local" id="date_heure_depart" name="date_heure_depart" 
                               value="<%= isEdit ? sdf.format(vo.getDateHeureDepart()) : "" %>" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="date_heure_arrivee">Date et Heure d'Arrivée *</label>
                        <input type="datetime-local" id="date_heure_arrivee" name="date_heure_arrivee" 
                               value="<%= isEdit ? sdf.format(vo.getDateHeureArrivee()) : "" %>" required>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="avion_id">ID Avion * (temporaire)</label>
                    <input type="number" id="avion_id" name="avion_id" 
                           value="<%= isEdit ? vo.getAvionId() : "1" %>" 
                           required min="1" placeholder="ID de l'avion">
                </div>
                
                <% if (!isEdit) { %>
                    <!-- Prix par classe (uniquement pour l'ajout) -->
                    <h3 style="margin-top: 30px; margin-bottom: 15px; color: #333;">💰 Prix par Classe</h3>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="prix_economique">Prix Économique (€) *</label>
                            <input type="number" id="prix_economique" name="prix_economique" 
                                   step="0.01" min="0" value="500.00" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="prix_premium">Prix Premium (€) *</label>
                            <input type="number" id="prix_premium" name="prix_premium" 
                                   step="0.01" min="0" value="1200.00" required>
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="prix_affaires">Prix Affaires (€) *</label>
                            <input type="number" id="prix_affaires" name="prix_affaires" 
                                   step="0.01" min="0" value="1800.00" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="prix_premiere">Prix Première (€) *</label>
                            <input type="number" id="prix_premiere" name="prix_premiere" 
                                   step="0.01" min="0" value="2500.00" required>
                        </div>
                    </div>
                <% } %>
                
                <% if (isEdit) { %>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="status">Statut</label>
                            <select id="status" name="status">
                                <% if (statuses != null) {
                                    for (Map<String, Object> st : statuses) { 
                                        String libelle = (String) st.get("libelle");
                                %>
                                    <option value="<%= libelle %>" <%= libelle.equals(vo.getStatus()) ? "selected" : "" %>><%= libelle %></option>
                                <% 
                                    }
                                } %>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="retard_minutes">Retard (minutes)</label>
                            <input type="number" id="retard_minutes" name="retard_minutes" 
                                   value="<%= vo.getRetardMinutes() %>" min="0">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="motif_annulation">Motif d'Annulation</label>
                        <textarea id="motif_annulation" name="motif_annulation" rows="2"><%= vo.getMotifAnnulation() != null ? vo.getMotifAnnulation() : "" %></textarea>
                    </div>
                <% } %>
                
                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/vol-opere" class="btn btn-secondary">Annuler</a>
                    <button type="submit" class="btn"><%= isEdit ? "Modifier" : "Ajouter" %></button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
