package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.AeroportDAO;
import model.Aeroport;

import java.io.IOException;
import java.util.List;

public class AeroportServlet extends HttpServlet {
    private AeroportDAO dao;
    
    @Override
    public void init() throws ServletException {
        super.init();
        dao = new AeroportDAO();
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
                listAeroports(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteAeroport(request, response);
                break;
            default:
                listAeroports(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("insert".equals(action)) {
            insertAeroport(request, response);
        } else if ("update".equals(action)) {
            updateAeroport(request, response);
        }
    }
    
    private void listAeroports(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String search = request.getParameter("search");
        List<Aeroport> aeroports = dao.search(search);
        request.setAttribute("aeroports", aeroports);
        request.getRequestDispatcher("/jsp/aeroport/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/aeroport/form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Aeroport a = dao.getById(id);
        request.setAttribute("aeroport", a);
        request.getRequestDispatcher("/jsp/aeroport/form.jsp").forward(request, response);
    }
    
    private void insertAeroport(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String code = request.getParameter("code_aeroport");
        String nom = request.getParameter("nom");
        String ville = request.getParameter("ville");
        String pays = request.getParameter("pays");
        
        Aeroport a = new Aeroport(code, nom, ville, pays);
        boolean success = dao.insert(a);
        
        if (success) {
            request.setAttribute("message", "Aéroport ajouté avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de l'ajout");
        }
        
        listAeroports(request, response);
    }
    
    private void updateAeroport(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String code = request.getParameter("code_aeroport");
        String nom = request.getParameter("nom");
        String ville = request.getParameter("ville");
        String pays = request.getParameter("pays");
        
        Aeroport a = new Aeroport(id, code, nom, ville, pays);
        boolean success = dao.update(a);
        
        if (success) {
            request.setAttribute("message", "Aéroport modifié avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la modification");
        }
        
        listAeroports(request, response);
    }
    
    private void deleteAeroport(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = dao.delete(id);
        
        if (success) {
            request.setAttribute("message", "Aéroport supprimé avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la suppression");
        }
        
        listAeroports(request, response);
    }
}
