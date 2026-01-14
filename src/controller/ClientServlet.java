package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ClientDAO;
import model.Client;

import java.io.IOException;
import java.util.List;

public class ClientServlet extends HttpServlet {
    private ClientDAO dao;
    
    @Override
    public void init() throws ServletException {
        super.init();
        dao = new ClientDAO();
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
                listClients(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteClient(request, response);
                break;
            default:
                listClients(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("insert".equals(action)) {
            insertClient(request, response);
        } else if ("update".equals(action)) {
            updateClient(request, response);
        }
    }
    
    private void listClients(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String search = request.getParameter("search");
        List<Client> clients = dao.search(search);
        request.setAttribute("clients", clients);
        request.getRequestDispatcher("/jsp/client/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/client/form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Client c = dao.getById(id);
        request.setAttribute("client", c);
        request.getRequestDispatcher("/jsp/client/form.jsp").forward(request, response);
    }
    
    private void insertClient(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        
        Client c = new Client(nom, prenom, email, telephone);
        boolean success = dao.insert(c);
        
        if (success) {
            request.setAttribute("message", "Client ajouté avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de l'ajout");
        }
        
        listClients(request, response);
    }
    
    private void updateClient(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        
        Client c = new Client(id, nom, prenom, email, telephone);
        boolean success = dao.update(c);
        
        if (success) {
            request.setAttribute("message", "Client modifié avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la modification");
        }
        
        listClients(request, response);
    }
    
    private void deleteClient(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = dao.delete(id);
        
        if (success) {
            request.setAttribute("message", "Client supprimé avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la suppression");
        }
        
        listClients(request, response);
    }
}
