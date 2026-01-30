<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Diffusion" %>
<%@ page import="java.util.List" %>
<%
    Diffusion d = (Diffusion) request.getAttribute("diffusion");
    if (d == null) d = new Diffusion();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Diffusion</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>

    <div class="main-content">
        <h1>Ajouter / Modifier Diffusion</h1>
        <%
            String formError = (String) request.getAttribute("formError");
            if (formError != null) {
        %>
        <div class="message error"><%= formError %></div>
        <%
            }
        %>
        <form method="post" action="<%= request.getContextPath() %>/diffusion" class="form-grid">
            <input type="hidden" name="id" value="<%= d.getId() %>" />
            <label>Société:
                <select name="idSociete">
                <% 
                    List<model.Societe> societes = (List<model.Societe>) request.getAttribute("societes");
                    int selectedSociete = d.getIdSociete();
                    if (societes != null) {
                        for (model.Societe s : societes) {
                %>
                    <option value="<%= s.getId() %>" <%= (s.getId() == selectedSociete) ? "selected" : "" %>><%= s.getNom() %></option>
                <%      }
                    }
                %>
                </select>
            </label>
            <label>Vol Opéré:
                <select name="idVolOpere">
                <% 
                    List<model.VolOpere> vols = (List<model.VolOpere>) request.getAttribute("volsOperes");
                    int selectedVol = d.getIdVolOpere();
                    if (vols != null) {
                        for (model.VolOpere v : vols) {
                %>
                    <option value="<%= v.getId() %>" <%= (v.getId() == selectedVol) ? "selected" : "" %>>Vol #<%= v.getId() %> (<%= v.getNumeroVol() != null ? v.getNumeroVol() : "" %>)</option>
                <%      }
                    }
                %>
                </select>
            </label>
            <label>Date: <input type="datetime-local" name="date" value="<%= d.getDate() != null ? d.getDate().toString() : "" %>"/></label>
            <label>Nombre: <input type="number" name="nombre" value="<%= d.getNombre() %>"/></label>
            <div></div>
            <div>
                <button type="submit" class="btn btn-primary">Enregistrer</button>
                <a href="<%=request.getContextPath()%>/diffusion?action=list" class="btn">Annuler</a>
            </div>
        </form>
    </div>
</body>
</html>
