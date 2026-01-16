package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.VolOpereDAO;
import dao.LigneVolDAO;
import dao.StatusVolDAO;
import dao.ClasseSiegeDAO;
import dao.AvionDAO;
import model.VolOpere;
import model.LigneVol;
import model.ClasseSiege;
import model.Avion;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class VolOpereServlet extends HttpServlet {
    private VolOpereDAO volOpereDAO;
    private LigneVolDAO ligneVolDAO;
    private StatusVolDAO statusVolDAO;
    private ClasseSiegeDAO classeSiegeDAO;
    private AvionDAO avionDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        volOpereDAO = new VolOpereDAO();
        ligneVolDAO = new LigneVolDAO();
        statusVolDAO = new StatusVolDAO();
        classeSiegeDAO = new ClasseSiegeDAO();
        avionDAO = new AvionDAO();
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
                listVolsOperes(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteVolOpere(request, response);
                break;
            case "chiffre-affaire":
                showChiffreAffaireForm(request, response);
                break;
            default:
                listVolsOperes(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("insert".equals(action)) {
            insertVolOpere(request, response);
        } else if ("update".equals(action)) {
            updateVolOpere(request, response);
        }
    }
    
    private void listVolsOperes(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String ligneVolIdStr = request.getParameter("ligne_vol_id");
        String avionIdStr = request.getParameter("avion_id");
        String dateDebutStr = request.getParameter("date_debut");
        String dateFinStr = request.getParameter("date_fin");
        
        Integer ligneVolId = (ligneVolIdStr != null && !ligneVolIdStr.isEmpty()) ? Integer.parseInt(ligneVolIdStr) : null;
        Integer avionId = (avionIdStr != null && !avionIdStr.isEmpty()) ? Integer.parseInt(avionIdStr) : null;
        java.sql.Date dateDebut = null;
        java.sql.Date dateFin = null;
        
        if (dateDebutStr != null && !dateDebutStr.isEmpty()) {
            try {
                dateDebut = java.sql.Date.valueOf(dateDebutStr);
            } catch (Exception e) {
                // Date invalide
            }
        }
        
        if (dateFinStr != null && !dateFinStr.isEmpty()) {
            try {
                dateFin = java.sql.Date.valueOf(dateFinStr);
            } catch (Exception e) {
                // Date invalide
            }
        }
        
        List<VolOpere> vols = volOpereDAO.search(search, status, ligneVolId, avionId, dateDebut, dateFin);
        List<Avion> avions = avionDAO.getAll();
        
        request.setAttribute("vols", vols);
        request.setAttribute("avions", avions);
        request.getRequestDispatcher("/jsp/vol_opere/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<LigneVol> lignes = ligneVolDAO.getAll();
        List<Map<String, Object>> statuses = statusVolDAO.getAll();
        List<ClasseSiege> classes = classeSiegeDAO.getAll();
        List<Avion> avions = avionDAO.getAll();
        request.setAttribute("lignes", lignes);
        request.setAttribute("statuses", statuses);
        request.setAttribute("classes", classes);
        request.setAttribute("avions", avions);
        request.getRequestDispatcher("/jsp/vol_opere/form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        VolOpere vo = volOpereDAO.getById(id);
        List<LigneVol> lignes = ligneVolDAO.getAll();
        List<Map<String, Object>> statuses = statusVolDAO.getAll();
        List<ClasseSiege> classes = classeSiegeDAO.getAll();
        List<Avion> avions = avionDAO.getAll();
        request.setAttribute("vol", vo);
        request.setAttribute("lignes", lignes);
        request.setAttribute("statuses", statuses);
        request.setAttribute("classes", classes);
        request.setAttribute("avions", avions);
        request.getRequestDispatcher("/jsp/vol_opere/form.jsp").forward(request, response);
    }
    
    private void insertVolOpere(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int ligneVolId = Integer.parseInt(request.getParameter("ligne_vol_id"));
            int avionId = Integer.parseInt(request.getParameter("avion_id"));
            String dateDepart = request.getParameter("date_heure_depart");
            String dateArrivee = request.getParameter("date_heure_arrivee");
            
            Timestamp tsDepart = Timestamp.valueOf(dateDepart.replace("T", " ") + ":00");
            Timestamp tsArrivee = Timestamp.valueOf(dateArrivee.replace("T", " ") + ":00");
            
            VolOpere vo = new VolOpere(ligneVolId, avionId, tsDepart, tsArrivee);
            int volOpereId = volOpereDAO.insertAndGetId(vo);
            
            if (volOpereId > 0) {
                // Récupérer dynamiquement les prix pour chaque classe
                List<ClasseSiege> classes = classeSiegeDAO.getAll();
                java.util.Map<Integer, Double> prixParClasse = new java.util.HashMap<>();
                
                for (ClasseSiege classe : classes) {
                    String prixParam = request.getParameter("prix_" + classe.getId());
                    if (prixParam != null && !prixParam.isEmpty()) {
                        double prix = Double.parseDouble(prixParam);
                        prixParClasse.put(classe.getId(), prix);
                    }
                }
                
                dao.PrixVolDAO prixVolDAO = new dao.PrixVolDAO();
                int prixInseres = prixVolDAO.insertPrixPourVol(volOpereId, prixParClasse);
                int nbClasses = classes.size();
                
                if (prixInseres == nbClasses) {
                    request.setAttribute("message", "Vol opéré et prix ajoutés avec succès");
                } else {
                    request.setAttribute("message", "Vol opéré ajouté mais erreur sur les prix (" + prixInseres + "/" + nbClasses + " insérés)");
                }
            } else {
                request.setAttribute("error", "Erreur lors de l'ajout du vol");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
            e.printStackTrace();
        }
        
        listVolsOperes(request, response);
    }
    
    private void updateVolOpere(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int ligneVolId = Integer.parseInt(request.getParameter("ligne_vol_id"));
            int avionId = Integer.parseInt(request.getParameter("avion_id"));
            String dateDepart = request.getParameter("date_heure_depart");
            String dateArrivee = request.getParameter("date_heure_arrivee");
            String statusParam = request.getParameter("status");
            int retardMinutes = Integer.parseInt(request.getParameter("retard_minutes"));
            String motifAnnulation = request.getParameter("motif_annulation");
            
            Timestamp tsDepart = Timestamp.valueOf(dateDepart.replace("T", " ") + ":00");
            Timestamp tsArrivee = Timestamp.valueOf(dateArrivee.replace("T", " ") + ":00");
            
            // Convertir status libelle en ID
            int statusId = statusVolDAO.getIdByLibelle(statusParam);
            
            VolOpere vo = new VolOpere(ligneVolId, avionId, tsDepart, tsArrivee);
            vo.setId(id);
            vo.setStatusId(statusId);
            vo.setRetardMinutes(retardMinutes);
            vo.setMotifAnnulation(motifAnnulation);
            
            boolean success = volOpereDAO.update(vo);
            
            if (success) {
                request.setAttribute("message", "Vol opéré modifié avec succès");
            } else {
                request.setAttribute("error", "Erreur lors de la modification");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }
        
        listVolsOperes(request, response);
    }
    
    private void showChiffreAffaireForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Récupérer tous les vols pour le dropdown
        List<VolOpere> vols = volOpereDAO.getAll();
        request.setAttribute("vols", vols);
        
        // Afficher le formulaire (le calcul sera implémenté plus tard)
        request.getRequestDispatcher("/jsp/vol_opere/chiffre_affaire.jsp").forward(request, response);
    }
    
    private void deleteVolOpere(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = volOpereDAO.delete(id);
        
        if (success) {
            request.setAttribute("message", "Vol opéré supprimé avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la suppression");
        }
        
        listVolsOperes(request, response);
    }
}
