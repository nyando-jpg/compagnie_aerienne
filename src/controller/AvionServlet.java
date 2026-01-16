package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.AvionDAO;
import dao.ClasseSiegeDAO;
import dao.ModelAvionDAO;
import dao.SiegeDAO;
import model.Avion;
import model.ClasseSiege;
import model.ModelAvion;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AvionServlet extends HttpServlet {
    private AvionDAO dao;
    private ClasseSiegeDAO classeSiegeDAO;
    private ModelAvionDAO modelAvionDAO;
    private SiegeDAO siegeDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        dao = new AvionDAO();
        classeSiegeDAO = new ClasseSiegeDAO();
        modelAvionDAO = new ModelAvionDAO();
        siegeDAO = new SiegeDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listAvions(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteAvion(request, response);
                break;
            default:
                listAvions(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("insert".equals(action)) {
            insertAvion(request, response);
        } else if ("update".equals(action)) {
            updateAvion(request, response);
        }
    }
    
    private void listAvions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Avion> avions = dao.getAll();
        request.setAttribute("avions", avions);
        request.getRequestDispatcher("/jsp/avion/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<ClasseSiege> classes = classeSiegeDAO.getAll();
            List<ModelAvion> modeles = modelAvionDAO.getAll();
            
            if (classes == null || classes.isEmpty()) {
                throw new ServletException("Aucune classe de siège trouvée. Vérifiez la table classe_siege.");
            }
            if (modeles == null || modeles.isEmpty()) {
                throw new ServletException("Aucun modèle d'avion trouvé. Vérifiez la table model_avion.");
            }
            
            request.setAttribute("classes", classes);
            request.setAttribute("modeles", modeles);
            request.getRequestDispatcher("/jsp/avion/form.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Erreur SQL lors du chargement des données: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Erreur lors du chargement du formulaire: " + e.getMessage(), e);
        }
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                throw new ServletException("Paramètre 'id' manquant pour modifier un avion");
            }
            
            int id = Integer.parseInt(idParam);
            Avion avion = dao.getById(id);
            
            if (avion == null) {
                throw new ServletException("Avion introuvable avec l'ID: " + id);
            }
            
            List<ClasseSiege> classes = classeSiegeDAO.getAll();
            List<ModelAvion> modeles = modelAvionDAO.getAll();
            
            if (classes == null || classes.isEmpty()) {
                throw new ServletException("Aucune classe de siège trouvée. Vérifiez la table classe_siege.");
            }
            if (modeles == null || modeles.isEmpty()) {
                throw new ServletException("Aucun modèle d'avion trouvé. Vérifiez la table model_avion.");
            }
            
            request.setAttribute("avion", avion);
            request.setAttribute("classes", classes);
            request.setAttribute("modeles", modeles);
            request.getRequestDispatcher("/jsp/avion/form.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Erreur SQL lors du chargement des données: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new ServletException("ID d'avion invalide: " + request.getParameter("id"), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Erreur lors du chargement du formulaire: " + e.getMessage(), e);
        }
    }
    
    private void insertAvion(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String codeAvion = request.getParameter("code_avion");
            int modelAvionId = Integer.parseInt(request.getParameter("model_avion_id"));
            int etatAvionId = Integer.parseInt(request.getParameter("etat_avion_id"));
            int capaciteTotale = Integer.parseInt(request.getParameter("capacite_totale"));
            
            Avion avion = new Avion(codeAvion, modelAvionId, etatAvionId, capaciteTotale);
            
            int avionId = dao.insert(avion);
            
            if (avionId > 0) {
                // Créer les sièges pour chaque classe
                List<ClasseSiege> classes = classeSiegeDAO.getAll();
                boolean siegesCreated = true;
                int currentRow = 1; // Commencer à la rangée 1
                
                for (ClasseSiege classe : classes) {
                    String paramName = "nbr_sieges_" + classe.getId();
                    String paramValue = request.getParameter(paramName);
                    if (paramValue != null && !paramValue.isEmpty()) {
                        int nbr = Integer.parseInt(paramValue);
                        if (nbr > 0) {
                            int nextRow = siegeDAO.insertSiegesForAvion(avionId, classe.getId(), nbr, currentRow);
                            if (nextRow < 0) {
                                siegesCreated = false;
                                break;
                            }
                            currentRow = nextRow; // Continuer avec la prochaine rangée
                        }
                    }
                }
                
                if (siegesCreated) {
                    request.getSession().setAttribute("success", "Avion et sièges ajoutés avec succès");
                } else {
                    request.getSession().setAttribute("error", "Avion ajouté mais erreur lors de la création des sièges");
                }
            } else {
                request.getSession().setAttribute("error", "Erreur lors de l'ajout de l'avion");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Erreur: " + e.getMessage());
            e.printStackTrace();
        }
        
        response.sendRedirect(request.getContextPath() + "/avion?action=list");
    }
    
    private void updateAvion(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String codeAvion = request.getParameter("code_avion");
            int modelAvionId = Integer.parseInt(request.getParameter("model_avion_id"));
            int etatAvionId = Integer.parseInt(request.getParameter("etat_avion_id"));
            int capaciteTotale = Integer.parseInt(request.getParameter("capacite_totale"));
            
            Avion avion = new Avion(codeAvion, modelAvionId, etatAvionId, capaciteTotale);
            avion.setId(id);
            
            if (dao.update(avion)) {
                request.getSession().setAttribute("success", "Avion modifié avec succès");
            } else {
                request.getSession().setAttribute("error", "Erreur lors de la modification de l'avion");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Erreur: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/avion?action=list");
    }
    
    private void deleteAvion(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            if (dao.delete(id)) {
                request.getSession().setAttribute("success", "Avion supprimé avec succès");
            } else {
                request.getSession().setAttribute("error", "Erreur lors de la suppression de l'avion");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Erreur: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/avion?action=list");
    }
}
