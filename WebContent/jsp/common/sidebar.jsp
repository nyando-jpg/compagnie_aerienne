<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
/* CSS SPECIFIQUE AU SIDEBAR (INCLUS DIRECTEMENT) */
.sidebar {
    position: fixed;
    left: 0;
    top: 0;
    width: 250px;
    height: 100vh;
    background-color: #ffffff;
    border-right: 2px solid #dddddd;
    overflow-y: auto;
    z-index: 1000;
}

.sidebar-header {
    background-color: #2c3e50;
    color: #ffffff;
    padding: 20px;
    text-align: center;
}

.sidebar-header h1 {
    font-size: 20px;
    margin: 0 0 6px 0;
}

.sidebar-header p {
    font-size: 13px;
    margin: 0;
}

.menu-list {
    padding: 10px 0;
}

.menu-item {
    display: block;
    padding: 12px 20px;
    color: #333333;
    text-decoration: none;
    border-left: 3px solid transparent;
}

.menu-item:hover {
    background-color: #f5f5f5;
    border-left-color: #2c3e50;
}

.menu-item-icon {
    display: inline-block;
    width: 24px;
    margin-right: 8px;
}

.menu-item-text {
    font-size: 14px;
}

/* Contenu principal décalé à droite de la sidebar */
.main-content {
    margin-left: 250px;
    padding: 20px 30px;
}
</style>

<div class="sidebar">
    <div class="sidebar-header">
        <h1>✈️ Compagnie</h1>
        <p>Système de gestion</p>
    </div>
    
    <div class="menu-list">
        <a href="${pageContext.request.contextPath}/home" class="menu-item">
            <span class="menu-item-icon">🏠</span>
            <span class="menu-item-text">Accueil</span>
        </a>
        <a href="${pageContext.request.contextPath}/classe-siege" class="menu-item">
            <span class="menu-item-icon">💺</span>
            <span class="menu-item-text">Classes de siège</span>
        </a>
        <a href="${pageContext.request.contextPath}/aeroport" class="menu-item">
            <span class="menu-item-icon">🛫</span>
            <span class="menu-item-text">Aéroports</span>
        </a>
        <a href="${pageContext.request.contextPath}/avion" class="menu-item">
            <span class="menu-item-icon">🛩️</span>
            <span class="menu-item-text">Avions</span>
        </a>
        <a href="${pageContext.request.contextPath}/modele" class="menu-item">
            <span class="menu-item-icon">🛫</span>
            <span class="menu-item-text">Modèles d'avion</span>
        </a>
        <a href="${pageContext.request.contextPath}/client" class="menu-item">
            <span class="menu-item-icon">👥</span>
            <span class="menu-item-text">Clients</span>
        </a>
        <a href="${pageContext.request.contextPath}/ligne-vol" class="menu-item">
            <span class="menu-item-icon">📋</span>
            <span class="menu-item-text">Lignes de vol</span>
        </a>
        <a href="${pageContext.request.contextPath}/vol-opere" class="menu-item">
            <span class="menu-item-icon">✈️</span>
            <span class="menu-item-text">Vols opérés</span>
        </a>
        <a href="${pageContext.request.contextPath}/reservation" class="menu-item">
            <span class="menu-item-icon">🎫</span>
            <span class="menu-item-text">Réservations</span>
        </a>
        <a href="${pageContext.request.contextPath}/chiffre-affaire" class="menu-item">
            <span class="menu-item-icon">💰</span>
            <span class="menu-item-text">Chiffre d'affaires</span>
        </a>
    </div>
</div>
