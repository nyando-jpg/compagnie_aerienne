<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${appName}</title>
    <%@ include file="/jsp/common/base_style.jsp" %>
</head>
<body>
    <%@ include file="/jsp/common/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="welcome-card">
            <h2>Bienvenue dans le système de gestion</h2>
            <p>Gérez efficacement vos vols, réservations et clients</p>
        </div>
        
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">💺</div>
                <div class="stat-label">Classes</div>
                <div class="stat-value">4</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">🛫</div>
                <div class="stat-label">Aéroports</div>
                <div class="stat-value">4</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">✈️</div>
                <div class="stat-label">Vols actifs</div>
                <div class="stat-value">4</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">🎫</div>
                <div class="stat-label">Réservations</div>
                <div class="stat-value">4</div>
            </div>
        </div>
        
        <div class="quick-actions">
            <h3>Actions rapides</h3>
            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/reservation?action=add" class="action-btn">
                    <span class="action-btn-icon">➕</span>
                    <span>Nouvelle réservation</span>
                </a>
                <a href="${pageContext.request.contextPath}/vol-opere?action=add" class="action-btn">
                    <span class="action-btn-icon">✈️</span>
                    <span>Créer un vol</span>
                </a>
                <a href="${pageContext.request.contextPath}/client?action=add" class="action-btn">
                    <span class="action-btn-icon">👤</span>
                    <span>Ajouter un client</span>
                </a>
                <a href="${pageContext.request.contextPath}/reservation" class="action-btn">
                    <span class="action-btn-icon">📋</span>
                    <span>Voir les réservations</span>
                </a>
            </div>
        </div>
    </div>
</body>
</html>
