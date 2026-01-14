package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ClasseSiegeDAO;
import model.ClasseSiege;

import java.io.IOException;
import java.util.List;

public class ClasseSiegeServlet extends HttpServlet {
    private ClasseSiegeDAO dao;
    
    @Override
    public void init() throws ServletException {
        super.init();
        dao = new ClasseSiegeDAO();
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
                listClasses(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteClasse(request, response);
                break;
            default:
                listClasses(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("insert".equals(action)) {
            insertClasse(request, response);
        } else if ("update".equals(action)) {
            updateClasse(request, response);
        }
    }
    
    private void listClasses(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String search = request.getParameter("search");
        List<ClasseSiege> classes = dao.search(search);
        request.setAttribute("classes", classes);
        request.getRequestDispatcher("/jsp/classe_siege/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/classe_siege/form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ClasseSiege cs = dao.getById(id);
        request.setAttribute("classe", cs);
        request.getRequestDispatcher("/jsp/classe_siege/form.jsp").forward(request, response);
    }
    
    private void insertClasse(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String libelle = request.getParameter("libelle");
        String description = request.getParameter("description");
        
        ClasseSiege cs = new ClasseSiege(libelle, description);
        boolean success = dao.insert(cs);
        
        if (success) {
            request.setAttribute("message", "Classe de siège ajoutée avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de l'ajout");
        }
        
        listClasses(request, response);
    }
    
    private void updateClasse(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String libelle = request.getParameter("libelle");
        String description = request.getParameter("description");
        
        ClasseSiege cs = new ClasseSiege(id, libelle, description);
        boolean success = dao.update(cs);
        
        if (success) {
            request.setAttribute("message", "Classe de siège modifiée avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la modification");
        }
        
        listClasses(request, response);
    }
    
    private void deleteClasse(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = dao.delete(id);
        
        if (success) {
            request.setAttribute("message", "Classe de siège supprimée avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la suppression");
        }
        
        listClasses(request, response);
    }
}
