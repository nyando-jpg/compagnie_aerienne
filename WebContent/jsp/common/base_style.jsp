<style>
/* CSS BRUT - SIMPLE (intégré directement dans les pages) */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: Arial, sans-serif;
    background: #f5f5f5;
    margin: 0;
    padding: 0;
}

/* TOOLBAR */
.toolbar {
    padding: 20px 0;
    border-bottom: 1px solid #ddd;
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 15px;
}

.toolbar h1 {
    font-size: 24px;
    margin: 0;
}

.toolbar-actions {
    display: flex;
    align-items: center;
    gap: 10px;
}

/* BOUTONS */
.btn {
    display: inline-block;
    padding: 10px 20px;
    background: #333;
    color: white;
    text-decoration: none;
    border-radius: 3px;
    border: none;
    cursor: pointer;
    font-size: 14px;
}

.btn:hover {
    background: #555;
}

.btn-primary {
    background: #333;
}

.btn-secondary {
    background: #6c757d;
}

.btn-success {
    background: #28a745;
}

.btn-danger {
    background: #dc3545;
}

.btn-link {
    border: none;
    background: transparent;
    color: #007bff;
    text-decoration: none;
    padding: 0;
    font-size: 14px;
}

.btn-link:hover {
    text-decoration: underline;
}

.btn-link.danger {
    color: #dc3545;
}

/* TABLEAUX */
table {
    width: 100%;
    background: white;
    border: 1px solid #ddd;
    border-collapse: collapse;
}

table th,
table td {
    padding: 10px;
    text-align: left;
    border-bottom: 1px solid #ddd;
}

table th {
    background: #f5f5f5;
    font-weight: bold;
}

.data-table tbody tr:hover {
    background: #fafafa;
}

/* FORMULAIRES */
.form-container {
    background: white;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 4px;
}

.form-group {
    margin-bottom: 15px;
}

.form-group label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
}

input,
textarea,
select {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 3px;
}

.form {
    background: white;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 4px;
    max-width: 700px;
}

.form-actions {
    margin-top: 20px;
    display: flex;
    gap: 10px;
    justify-content: flex-end;
}

.required {
    color: #dc3545;
}

/* MESSAGES */
.alert {
    padding: 10px;
    margin-bottom: 15px;
    border-radius: 3px;
}

.alert-success {
    background: #d4edda;
    color: #155724;
}

.alert-error {
    background: #f8d7da;
    color: #721c24;
}

.message.success {
    background: #d4edda;
    color: #155724;
    padding: 10px;
    margin-bottom: 15px;
}

.message.error {
    background: #f8d7da;
    color: #721c24;
    padding: 10px;
    margin-bottom: 15px;
}

/* AUTRES */
.search-form {
    background: white;
    padding: 15px;
    border: 1px solid #ddd;
    margin-bottom: 15px;
}

.empty-state {
    background: white;
    padding: 40px;
    text-align: center;
    border: 1px solid #ddd;
}

/* PAGE HOME */
.welcome-card {
    background: white;
    padding: 30px;
    margin-bottom: 20px;
    border: 1px solid #ddd;
}

.welcome-card h2 {
    font-size: 24px;
    margin-bottom: 10px;
}

.stats-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 15px;
    margin-bottom: 20px;
}

.stat-card {
    background: white;
    padding: 20px;
    border: 1px solid #ddd;
    text-align: center;
}

.stat-icon {
    font-size: 40px;
    margin-bottom: 10px;
}

.stat-label {
    font-size: 12px;
    color: #666;
    margin-bottom: 5px;
}

.stat-value {
    font-size: 28px;
    font-weight: bold;
}

.quick-actions {
    background: white;
    padding: 20px;
    border: 1px solid #ddd;
}

.quick-actions h3 {
    font-size: 18px;
    margin-bottom: 15px;
}

.action-buttons {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 10px;
}

.action-btn {
    display: block;
    padding: 15px;
    background: #f5f5f5;
    text-align: center;
    text-decoration: none;
    color: #333;
    border: 1px solid #ddd;
}

.action-btn:hover {
    background: #333;
    color: white;
}

/* CONTENU PRINCIPAL */
.main-content {
    background: #ffffff;
    border-radius: 4px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    min-height: 100vh;
}
</style>
