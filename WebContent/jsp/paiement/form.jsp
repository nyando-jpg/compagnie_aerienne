<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Reservation, model.MethodePaiement, java.util.List" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Enregistrer un Paiement</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
    <style>
        .payment-summary {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            border: 1px solid #dee2e6;
        }
        .payment-summary h3 {
            margin-top: 0;
            margin-bottom: 10px;
        }
        .payment-summary-item {
            margin-bottom: 5px;
        }
        .payment-method-row {
            display: grid;
            grid-template-columns: 2fr 1fr auto;
            gap: 10px;
            margin-bottom: 10px;
        }
        .remove-row-btn {
            background: transparent;
            border: none;
            color: #dc3545;
            cursor: pointer;
            font-size: 18px;
        }
    </style>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>

    <div class="main-content">
        <div class="toolbar">
            <h1>Enregistrer un Paiement</h1>
        </div>
            <%
                Reservation reservation = (Reservation) request.getAttribute("reservation");
                List<MethodePaiement> methodes = (List<MethodePaiement>) request.getAttribute("methodes");
                double montantTotal = reservation.getMontantTotal() != null ? reservation.getMontantTotal() : 0.0;
                double montantPaye = reservation.getMontantPaye() != null ? reservation.getMontantPaye() : 0.0;
                double restant = montantTotal - montantPaye;
                if (restant < 0) restant = 0;
            %>

            <div class="payment-summary">
                <h3>Détails de la réservation</h3>
                <div class="payment-summary-item"><strong>Réservation :</strong> #<%= reservation.getId() %></div>
                <div class="payment-summary-item"><strong>Client :</strong> <%= reservation.getClientFullName() %></div>
                <div class="payment-summary-item"><strong>Vol :</strong> <%= reservation.getNumeroVol() %> - <%= reservation.getAeroportDepart() %> → <%= reservation.getAeroportArrivee() %></div>
                <div class="payment-summary-item"><strong>Montant total :</strong> <%= String.format("%.2f €", montantTotal) %></div>
                <div class="payment-summary-item"><strong>Déjà payé :</strong> <%= String.format("%.2f €", montantPaye) %></div>
                <div class="payment-summary-item"><strong>Reste à payer :</strong> <%= String.format("%.2f €", restant) %></div>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="message error">${error}</div>
            <% } %>

            <form action="${pageContext.request.contextPath}/paiement" method="post" class="form" id="paiement-form">
                <input type="hidden" name="action" value="insert">
                <input type="hidden" name="reservation_id" value="<%= reservation.getId() %>">

                <h3>Méthodes de paiement</h3>
                <div id="payment-methods-container">
                    <div class="payment-method-row">
                        <select name="methode_id" required>
                            <option value="">-- Sélectionner une méthode --</option>
                            <% if (methodes != null) {
                                for (MethodePaiement mp : methodes) { %>
                                    <option value="<%= mp.getId() %>"><%= mp.getLibelle() %></option>
                            <%  }
                            } %>
                        </select>
                        <input type="number" name="montant" step="0.01" min="0" placeholder="Montant" required>
                        <button type="button" class="remove-row-btn" onclick="removeRow(this)">×</button>
                    </div>
                </div>

                <button type="button" class="btn btn-secondary" style="margin-top: 10px;" onclick="addPaymentRow()">+ Ajouter une méthode</button>

                <div class="form-actions" style="margin-top: 20px;">
                    <a href="${pageContext.request.contextPath}/paiement" class="btn btn-secondary">Annuler</a>
                    <button type="submit" class="btn">Enregistrer le paiement</button>
                </div>
            </form>

            <script>
                function addPaymentRow() {
                    var container = document.getElementById('payment-methods-container');
                    var rows = container.getElementsByClassName('payment-method-row');
                    if (rows.length === 0) return;
                    var lastRow = rows[rows.length - 1];
                    var newRow = lastRow.cloneNode(true);

                    // Reset values
                    var selects = newRow.getElementsByTagName('select');
                    if (selects.length > 0) {
                        selects[0].selectedIndex = 0;
                    }
                    var inputs = newRow.getElementsByTagName('input');
                    if (inputs.length > 0) {
                        inputs[0].value = '';
                    }

                    container.appendChild(newRow);
                }

                function removeRow(button) {
                    var container = document.getElementById('payment-methods-container');
                    var rows = container.getElementsByClassName('payment-method-row');
                    if (rows.length <= 1) {
                        // Garder au moins une ligne
                        return;
                    }
                    var row = button.parentNode;
                    container.removeChild(row);
                }
            </script>
        </div>
    </div>
</body>
</html>