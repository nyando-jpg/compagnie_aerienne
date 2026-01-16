package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ChiffreAffaireDAO;
import dao.LigneVolDAO;
import dao.AvionDAO;
import model.LigneVol;
import model.VolOpere;
import model.Avion;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChiffreAffaireServlet extends HttpServlet {
    
    private ChiffreAffaireDAO chiffreAffaireDAO;
    private LigneVolDAO ligneVolDAO;
    private AvionDAO avionDAO;
    
    @Override
    public void init() {
        chiffreAffaireDAO = new ChiffreAffaireDAO();
        ligneVolDAO = new LigneVolDAO();
        avionDAO = new AvionDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Récupérer les filtres
        String ligneVolIdStr = request.getParameter("ligne_vol_id");
        String avionIdStr = request.getParameter("avion_id");
        String dateDebutStr = request.getParameter("date_debut");
        String dateFinStr = request.getParameter("date_fin");
        
        Integer ligneVolId = (ligneVolIdStr != null && !ligneVolIdStr.isEmpty()) ? Integer.parseInt(ligneVolIdStr) : null;
        Integer avionId = (avionIdStr != null && !avionIdStr.isEmpty()) ? Integer.parseInt(avionIdStr) : null;
        Date dateDebut = (dateDebutStr != null && !dateDebutStr.isEmpty()) ? Date.valueOf(dateDebutStr) : null;
        Date dateFin = (dateFinStr != null && !dateFinStr.isEmpty()) ? Date.valueOf(dateFinStr) : null;
        
        // Calculer le chiffre d'affaires
        Map<String, Object> resultat = chiffreAffaireDAO.calculerChiffreAffaire(ligneVolId, avionId, dateDebut, dateFin);
        Map<String, Double> caParClasse = chiffreAffaireDAO.getChiffreAffaireParClasse(ligneVolId, avionId, dateDebut, dateFin);
        
        // Récupérer les réservations et vols opérés
        var reservations = chiffreAffaireDAO.getReservations(ligneVolId, avionId, dateDebut, dateFin);
        var volsOperes = chiffreAffaireDAO.getVolsOperes(ligneVolId, avionId, dateDebut, dateFin);
        
        // Calculer le prix max pour chaque vol (stocké dans une Map)
        Map<Integer, Double> prixMaxParVol = new HashMap<>();
        for (VolOpere vol : volsOperes) {
            double prixMax = avionDAO.getPrixTotalMax(vol.getAvionId(), vol.getId());
            prixMaxParVol.put(vol.getId(), prixMax);
        }
        
        // Récupérer les listes pour les filtres
        List<LigneVol> lignes = ligneVolDAO.getAll();
        List<Avion> avions = avionDAO.getAll();
        
        // Passer les données à la JSP
        request.setAttribute("resultat", resultat);
        request.setAttribute("caParClasse", caParClasse);
        request.setAttribute("reservations", reservations);
        request.setAttribute("volsOperes", volsOperes);
        request.setAttribute("prixMaxParVol", prixMaxParVol);
        request.setAttribute("lignes", lignes);
        request.setAttribute("avions", avions);
        request.setAttribute("filtres", Map.of(
            "ligneVolId", ligneVolId != null ? ligneVolId : "",
            "avionId", avionId != null ? avionId : "",
            "dateDebut", dateDebutStr != null ? dateDebutStr : "",
            "dateFin", dateFinStr != null ? dateFinStr : ""
        ));
        
        request.getRequestDispatcher("/jsp/chiffre_affaire.jsp").forward(request, response);
    }
}
