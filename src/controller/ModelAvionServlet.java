package controller;

import dao.ModelAvionDAO;
import model.ModelAvion;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet: ModelAvion
 */
public class ModelAvionServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private ModelAvionDAO modelAvionDAO;
    
    @Override
    public void init() {
        modelAvionDAO = new ModelAvionDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        try {
            switch (action) {
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteModelAvion(request, response);
                    break;
                default:
                    listModelAvions(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            if ("insert".equals(action)) {
                insertModelAvion(request, response);
            } else if ("update".equals(action)) {
                updateModelAvion(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
    
    /**
     * List all model avions
     */
    private void listModelAvions(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        List<ModelAvion> models = modelAvionDAO.getAll();
        request.setAttribute("models", models);
        
        request.getRequestDispatcher("/jsp/modelavion/list.jsp").forward(request, response);
    }
    
    /**
     * Show add form
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/jsp/modelavion/form.jsp").forward(request, response);
    }
    
    /**
     * Show edit form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        ModelAvion model = modelAvionDAO.getById(id);
        
        request.setAttribute("model", model);
        request.getRequestDispatcher("/jsp/modelavion/form.jsp").forward(request, response);
    }
    
    /**
     * Insert a new model avion
     */
    private void insertModelAvion(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        
        String designation = request.getParameter("designation");
        String fabricant = request.getParameter("fabricant");
        int capacite = Integer.parseInt(request.getParameter("capacite"));
        
        String autonomieStr = request.getParameter("autonomie_km");
        Integer autonomieKm = (autonomieStr != null && !autonomieStr.trim().isEmpty()) 
                ? Integer.parseInt(autonomieStr) : null;
        
        String vitesseStr = request.getParameter("vitesse_km_h");
        Integer vitesseKmH = (vitesseStr != null && !vitesseStr.trim().isEmpty()) 
                ? Integer.parseInt(vitesseStr) : null;
        
        String description = request.getParameter("description");
        
        ModelAvion model = new ModelAvion(designation, fabricant, capacite);
        model.setAutonomieKm(autonomieKm);
        model.setVitesseKmH(vitesseKmH);
        model.setDescription(description);
        
        boolean success = modelAvionDAO.insert(model);
        
        HttpSession session = request.getSession();
        if (success) {
            session.setAttribute("message", "Modèle d'avion ajouté avec succès");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Erreur lors de l'ajout du modèle");
            session.setAttribute("messageType", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/modele");
    }
    
    /**
     * Update an existing model avion
     */
    private void updateModelAvion(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String designation = request.getParameter("designation");
        String fabricant = request.getParameter("fabricant");
        int capacite = Integer.parseInt(request.getParameter("capacite"));
        
        String autonomieStr = request.getParameter("autonomie_km");
        Integer autonomieKm = (autonomieStr != null && !autonomieStr.trim().isEmpty()) 
                ? Integer.parseInt(autonomieStr) : null;
        
        String vitesseStr = request.getParameter("vitesse_km_h");
        Integer vitesseKmH = (vitesseStr != null && !vitesseStr.trim().isEmpty()) 
                ? Integer.parseInt(vitesseStr) : null;
        
        String description = request.getParameter("description");
        
        ModelAvion model = new ModelAvion(designation, fabricant, capacite);
        model.setId(id);
        model.setAutonomieKm(autonomieKm);
        model.setVitesseKmH(vitesseKmH);
        model.setDescription(description);
        
        boolean success = modelAvionDAO.update(model);
        
        HttpSession session = request.getSession();
        if (success) {
            session.setAttribute("message", "Modèle d'avion modifié avec succès");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Erreur lors de la modification du modèle");
            session.setAttribute("messageType", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/modele");
    }
    
    /**
     * Delete a model avion
     */
    private void deleteModelAvion(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = modelAvionDAO.delete(id);
        
        HttpSession session = request.getSession();
        if (success) {
            session.setAttribute("message", "Modèle d'avion supprimé avec succès");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Erreur lors de la suppression du modèle");
            session.setAttribute("messageType", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/modele");
    }
}
