<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.VolOpere, model.LigneVol, model.ClasseSiege, model.Avion, model.TypeClient, java.util.List, java.util.Map" %>
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
        .prix-grid {
            overflow-x: auto;
            margin: 20px 0;
        }
        .prix-table {
            width: 100%;
            border-collapse: collapse;
        }
        .prix-table th,
        .prix-table td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: center;
        }
        .prix-table th {
            background-color: #f8f9fa;
            font-weight: 600;
        }
        .prix-table input {
            width: 130px;
            padding: 5px;
            text-align: right;
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
                List<ClasseSiege> classes = (List<ClasseSiege>) request.getAttribute("classes");
                List<Map<String, Object>> statuses = (List<Map<String, Object>>) request.getAttribute("statuses");
                List<Avion> avions = (List<Avion>) request.getAttribute("avions");
                List<TypeClient> typesClients = (List<TypeClient>) request.getAttribute("typesClients");
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
                    <label for="avion_id">Avion *</label>
                    <select id="avion_id" name="avion_id" required>
                        <option value="">-- Sélectionner un avion --</option>
                        <% 
                            if (avions != null) {
                                for (Avion av : avions) {
                        %>
                            <option value="<%= av.getId() %>" 
                                    <%= isEdit && vo.getAvionId() == av.getId() ? "selected" : "" %>>
                                <%= av.getCodeAvion() != null ? av.getCodeAvion() : ("Avion #" + av.getId()) %>
                            </option>
                        <%      }
                            }
                        %>
                    </select>
                </div>
                
                <% if (!isEdit) { %>
                    <!-- Prix par classe et type de client (uniquement pour l'ajout) -->
                    <h3 style="margin-top: 30px; margin-bottom: 15px; color: #333;">💰 Prix par Classe de Siège et Type de Client</h3>
                    <p style="color: #666; margin-bottom: 15px;">
                        Saisissez le <strong>prix fixe</strong> pour ADULTE et ENFANT, 
                        ou le <strong>pourcentage du prix adulte</strong> pour BÉBÉ.
                    </p>
                    
                    <div class="prix-grid">
                        <table class="prix-table">
                            <thead>
                                <tr>
                                    <th>Classe</th>
                                    <% if (typesClients != null) {
                                        for (TypeClient tc : typesClients) { %>
                                            <th>
                                                <%= tc.getLibelle() %>
                                                <% if ("POURCENTAGE".equals(tc.getModeCalcul())) { %>
                                                    <br><small style="color: #28a745;">(% du tarif adulte)</small>
                                                <% } else { %>
                                                    <br><small style="color: #666;">(Prix fixe)</small>
                                                <% } %>
                                            </th>
                                    <%  }
                                    } %>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                if (classes != null && typesClients != null) {
                                    for (ClasseSiege cs : classes) { 
                                %>
                                    <tr>
                                        <td><strong><%= cs.getLibelle() %></strong></td>
                                        <% for (TypeClient tc : typesClients) { %>
                                            <td>
                                                <% if ("POURCENTAGE".equals(tc.getModeCalcul())) { %>
                                                    <!-- Input pour pourcentage -->
                                                    <div style="display: flex; align-items: center; gap: 5px; justify-content: center;">
                                                        <input type="number" 
                                                               name="pourcent_classe_<%= cs.getId() %>_type_<%= tc.getId() %>" 
                                                               step="0.01" 
                                                               min="0"
                                                               max="100"
                                                               placeholder="Ex: 10"
                                                               required
                                                               style="width: 80px;">
                                                        <span style="font-weight: bold; color: #28a745; font-size: 18px;">%</span>
                                                    </div>
                                                <% } else { %>
                                                    <!-- Input pour prix fixe -->
                                                    <input type="number" 
                                                           name="prix_classe_<%= cs.getId() %>_type_<%= tc.getId() %>" 
                                                           step="0.01" 
                                                           min="0" 
                                                           placeholder="Prix"
                                                           required>
                                                <% } %>
                                            </td>
                                        <% } %>
                                    </tr>
                                <% 
                                    }
                                } 
                                %>
                            </tbody>
                        </table>
                        
                        <div style="margin-top: 20px; padding: 15px; background: #e7f3ff; border-radius: 5px; border-left: 4px solid #0066cc;">
                            <strong>💡 Exemple :</strong>
                            <ul style="margin: 10px 0 0 20px;">
                                <li>Économique ADULTE : <code>500000</code> Ar (prix fixe)</li>
                                <li>Économique ENFANT : <code>350000</code> Ar (prix fixe)</li>
                                <li>Économique BÉBÉ : <code>10</code> % → Prix final = 500000 × 10% = <strong>50000 Ar</strong></li>
                            </ul>
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
