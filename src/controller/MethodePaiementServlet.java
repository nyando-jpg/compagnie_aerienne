package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.MethodePaiementDAO;
import model.MethodePaiement;

public class MethodePaiementServlet extends HttpServlet {
    private MethodePaiementDAO dao;

    @Override
    public void init() throws ServletException {
        super.init();
        dao = new MethodePaiementDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteMethode(request, response);
                break;
            default:
                listMethodes(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("insert".equals(action)) {
            insertMethode(request, response);
        } else if ("update".equals(action)) {
            updateMethode(request, response);
        }
    }

    private void listMethodes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MethodePaiement> methodes = dao.getAll();
        request.setAttribute("methodes", methodes);
        request.getRequestDispatcher("/jsp/methode_paiement/list.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/methode_paiement/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        MethodePaiement m = dao.getById(id);
        request.setAttribute("methode", m);
        request.getRequestDispatcher("/jsp/methode_paiement/form.jsp").forward(request, response);
    }

    private void insertMethode(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String libelle = request.getParameter("libelle");
        String description = request.getParameter("description");

        MethodePaiement m = new MethodePaiement(libelle, description);
        boolean success = dao.insert(m);

        if (success) {
            request.setAttribute("message", "Méthode de paiement ajoutée avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de l'ajout");
        }

        listMethodes(request, response);
    }

    private void updateMethode(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String libelle = request.getParameter("libelle");
        String description = request.getParameter("description");

        MethodePaiement m = new MethodePaiement(id, libelle, description);
        boolean success = dao.update(m);

        if (success) {
            request.setAttribute("message", "Méthode de paiement modifiée avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la modification");
        }

        listMethodes(request, response);
    }

    private void deleteMethode(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = dao.delete(id);

        if (success) {
            request.setAttribute("message", "Méthode de paiement supprimée avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la suppression");
        }

        listMethodes(request, response);
    }
}