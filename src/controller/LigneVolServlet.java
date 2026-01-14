package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.LigneVolDAO;
import dao.AeroportDAO;
import model.LigneVol;
import model.Aeroport;

import java.io.IOException;
import java.util.List;

public class LigneVolServlet extends HttpServlet {
    private LigneVolDAO ligneVolDAO;
    private AeroportDAO aeroportDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        ligneVolDAO = new LigneVolDAO();
        aeroportDAO = new AeroportDAO();
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
                listLignesVol(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteLigneVol(request, response);
                break;
            default:
                listLignesVol(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("insert".equals(action)) {
            insertLigneVol(request, response);
        } else if ("update".equals(action)) {
            updateLigneVol(request, response);
        }
    }
    
    private void listLignesVol(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String search = request.getParameter("search");
        List<LigneVol> lignes = ligneVolDAO.search(search);
        request.setAttribute("lignes", lignes);
        request.getRequestDispatcher("/jsp/ligne_vol/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Aeroport> aeroports = aeroportDAO.getAll();
        request.setAttribute("aeroports", aeroports);
        request.getRequestDispatcher("/jsp/ligne_vol/form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        LigneVol lv = ligneVolDAO.getById(id);
        List<Aeroport> aeroports = aeroportDAO.getAll();
        request.setAttribute("ligne", lv);
        request.setAttribute("aeroports", aeroports);
        request.getRequestDispatcher("/jsp/ligne_vol/form.jsp").forward(request, response);
    }
    
    private void insertLigneVol(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String numeroVol = request.getParameter("numero_vol");
            int aeroportDepartId = Integer.parseInt(request.getParameter("aeroport_depart_id"));
            int aeroportArriveeId = Integer.parseInt(request.getParameter("aeroport_arrivee_id"));
            int dureeEstimee = Integer.parseInt(request.getParameter("duree_estimee_minutes"));
            String distanceStr = request.getParameter("distance_km");
            Integer distanceKm = (distanceStr != null && !distanceStr.isEmpty()) ? Integer.parseInt(distanceStr) : null;
            String description = request.getParameter("description");
            
            LigneVol lv = new LigneVol(numeroVol, aeroportDepartId, aeroportArriveeId, dureeEstimee);
            lv.setDistanceKm(distanceKm);
            lv.setDescription(description);
            
            boolean success = ligneVolDAO.insert(lv);
            
            if (success) {
                request.setAttribute("message", "Ligne de vol ajoutée avec succès");
            } else {
                request.setAttribute("error", "Erreur lors de l'ajout");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }
        
        listLignesVol(request, response);
    }
    
    private void updateLigneVol(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String numeroVol = request.getParameter("numero_vol");
            int aeroportDepartId = Integer.parseInt(request.getParameter("aeroport_depart_id"));
            int aeroportArriveeId = Integer.parseInt(request.getParameter("aeroport_arrivee_id"));
            int dureeEstimee = Integer.parseInt(request.getParameter("duree_estimee_minutes"));
            String distanceStr = request.getParameter("distance_km");
            Integer distanceKm = (distanceStr != null && !distanceStr.isEmpty()) ? Integer.parseInt(distanceStr) : null;
            String description = request.getParameter("description");
            
            LigneVol lv = new LigneVol(numeroVol, aeroportDepartId, aeroportArriveeId, dureeEstimee);
            lv.setId(id);
            lv.setDistanceKm(distanceKm);
            lv.setDescription(description);
            
            boolean success = ligneVolDAO.update(lv);
            
            if (success) {
                request.setAttribute("message", "Ligne de vol modifiée avec succès");
            } else {
                request.setAttribute("error", "Erreur lors de la modification");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }
        
        listLignesVol(request, response);
    }
    
    private void deleteLigneVol(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = ligneVolDAO.delete(id);
        
        if (success) {
            request.setAttribute("message", "Ligne de vol supprimée avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la suppression");
        }
        
        listLignesVol(request, response);
    }
}
