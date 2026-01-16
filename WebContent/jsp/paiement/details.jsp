<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="model.Reservation" %>
<%@ page import="model.Paiement" %>
<%@ page import="model.Paiement.PaiementMethodeDetail" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Détails des Paiements - Réservation #${reservation.id}</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .details-container {
            max-width: 1200px;
            margin: 0 auto;
        }
        .reservation-summary {
            background: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 6px;
            padding: 20px;
            margin-bottom: 30px;
        }
        .summary-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 15px;
        }
        .summary-item {
            padding: 10px;
        }
        .summary-label {
            font-weight: 600;
            color: #6c757d;
            font-size: 0.875rem;
            margin-bottom: 5px;
        }
        .summary-value {
            font-size: 1.1rem;
            color: #212529;
        }
        .payment-card {
            background: white;
            border: 1px solid #dee2e6;
            border-radius: 6px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
        .payment-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 2px solid #e9ecef;
        }
        .payment-id {
            font-size: 1.2rem;
            font-weight: 600;
            color: #495057;
        }
        .payment-date {
            font-size: 0.95rem;
            color: #6c757d;
        }
        .payment-amount {
            font-size: 1.5rem;
            font-weight: 700;
            color: #28a745;
        }
        .payment-status {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 4px;
            font-size: 0.875rem;
            font-weight: 600;
        }
        .status-VALIDE {
            background-color: #d4edda;
            color: #155724;
        }
        .status-ANNULE {
            background-color: #f8d7da;
            color: #721c24;
        }
        .methodes-list {
            background: #f8f9fa;
            border-radius: 4px;
            padding: 15px;
            margin-top: 15px;
        }
        .methode-item {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #e9ecef;
        }
        .methode-item:last-child {
            border-bottom: none;
        }
        .methode-name {
            font-weight: 500;
            color: #495057;
        }
        .methode-amount {
            font-weight: 600;
            color: #28a745;
        }
        .empty-message {
            text-align: center;
            padding: 40px;
            color: #6c757d;
            font-size: 1.1rem;
        }
        .back-button {
            display: inline-block;
            margin-bottom: 20px;
            padding: 10px 20px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            transition: background-color 0.3s;
        }
        .back-button:hover {
            background-color: #5a6268;
        }
        .total-summary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 6px;
            padding: 20px;
            margin-bottom: 30px;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
        }
        .total-item {
            text-align: center;
        }
        .total-label {
            font-size: 0.875rem;
            opacity: 0.9;
            margin-bottom: 5px;
        }
        .total-value {
            font-size: 1.8rem;
            font-weight: 700;
        }
    </style>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="details-container">
            <a href="${pageContext.request.contextPath}/paiement" class="back-button">← Retour à la liste</a>
            
            <h1>Historique des Paiements</h1>
            
            <%
                Reservation reservation = (Reservation) request.getAttribute("reservation");
                List<Paiement> paiements = (List<Paiement>) request.getAttribute("paiements");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                
                if (reservation != null) {
            %>
            
            <!-- Résumé de la réservation -->
            <div class="reservation-summary">
                <h2>Réservation #<%= reservation.getId() %></h2>
                <div class="summary-grid">
                    <div class="summary-item">
                        <div class="summary-label">Client</div>
                        <div class="summary-value"><%= reservation.getClientFullName() %></div>
                    </div>
                    <div class="summary-item">
                        <div class="summary-label">Vol</div>
                        <div class="summary-value"><%= reservation.getNumeroVol() %> (ID VO: #<%= reservation.getVolOpereId() %>)</div>
                    </div>
                    <div class="summary-item">
                        <div class="summary-label">Trajet</div>
                        <div class="summary-value"><%= reservation.getAeroportDepart() %> → <%= reservation.getAeroportArrivee() %></div>
                    </div>
                    <div class="summary-item">
                        <div class="summary-label">Date du vol</div>
                        <div class="summary-value"><%= sdf.format(reservation.getDateHeureDepart()) %></div>
                    </div>
                </div>
            </div>
            
            <!-- Résumé des totaux -->
            <div class="total-summary">
                <div class="total-item">
                    <div class="total-label">Montant Total</div>
                    <div class="total-value"><%= String.format("%.2f €", reservation.getMontantTotal() != null ? reservation.getMontantTotal() : 0.0) %></div>
                </div>
                <div class="total-item">
                    <div class="total-label">Montant Payé</div>
                    <div class="total-value"><%= String.format("%.2f €", reservation.getMontantPaye() != null ? reservation.getMontantPaye() : 0.0) %></div>
                </div>
                <div class="total-item">
                    <div class="total-label">Reste à Payer</div>
                    <div class="total-value">
                        <% 
                            double reste = (reservation.getMontantTotal() != null ? reservation.getMontantTotal() : 0.0) 
                                         - (reservation.getMontantPaye() != null ? reservation.getMontantPaye() : 0.0);
                        %>
                        <%= String.format("%.2f €", reste) %>
                    </div>
                </div>
                <div class="total-item">
                    <div class="total-label">Statut</div>
                    <div class="total-value">
                        <span style="background: rgba(255,255,255,0.2); padding: 5px 15px; border-radius: 20px;">
                            <%= reservation.getStatutPaiement() %>
                        </span>
                    </div>
                </div>
            </div>
            
            <!-- Liste des paiements -->
            <h2>Historique des Paiements (<%= paiements != null ? paiements.size() : 0 %>)</h2>
            
            <%
                if (paiements != null && !paiements.isEmpty()) {
                    for (Paiement p : paiements) {
            %>
                        <div class="payment-card">
                            <div class="payment-header">
                                <div>
                                    <div class="payment-id">Paiement #<%= p.getId() %></div>
                                    <div class="payment-date">
                                        📅 <%= sdf.format(p.getDatePaiement()) %>
                                    </div>
                                </div>
                                <div style="text-align: right;">
                                    <div class="payment-amount"><%= String.format("%.2f €", p.getMontant()) %></div>
                                    <span class="payment-status status-<%= p.getStatut() %>"><%= p.getStatut() %></span>
                                </div>
                            </div>
                            
                            <% if (p.getMethodeDetails() != null && !p.getMethodeDetails().isEmpty()) { %>
                                <div class="methodes-list">
                                    <strong>💳 Méthodes de paiement utilisées:</strong>
                                    <% for (PaiementMethodeDetail detail : p.getMethodeDetails()) { %>
                                        <div class="methode-item">
                                            <span class="methode-name"><%= detail.getMethodePaiementLibelle() %></span>
                                            <span class="methode-amount"><%= String.format("%.2f €", detail.getMontant()) %></span>
                                        </div>
                                    <% } %>
                                </div>
                            <% } %>
                        </div>
            <%
                    }
                } else {
            %>
                    <div class="empty-message">
                        <p>Aucun paiement enregistré pour cette réservation.</p>
                        <a href="${pageContext.request.contextPath}/paiement?action=add&reservation_id=<%= reservation.getId() %>" 
                           style="display: inline-block; margin-top: 15px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px;">
                            💵 Enregistrer un paiement
                        </a>
                    </div>
            <%
                }
            %>
            
            <%
                } else {
            %>
                <div class="empty-message">
                    <p>Réservation introuvable.</p>
                </div>
            <%
                }
            %>
        </div>
    </div>
</body>
</html>
