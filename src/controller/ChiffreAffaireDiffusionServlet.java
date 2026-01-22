package controller;

import dao.ChiffreAffaireDiffusionDAO;
import dao.SocieteDao;
import dao.VolOpereDAO;
import dao.AvionDAO;
import model.Societe;
import model.VolOpere;
import model.Avion;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.Map;

public class ChiffreAffaireDiffusionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // read optional filters
        Integer volOpereId = parseIntOrNull(req.getParameter("volopere_id"));
        Integer avionId = parseIntOrNull(req.getParameter("avion_id"));
        Integer societeId = parseIntOrNull(req.getParameter("societe_id"));
        Date dateDebut = parseDateOrNull(req.getParameter("date_debut"));
        Date dateFin = parseDateOrNull(req.getParameter("date_fin"));

        Map<String, Object> resultat = ChiffreAffaireDiffusionDAO.calculerCA(volOpereId, avionId, societeId, dateDebut,
                dateFin);

        // Liste CA par société
        java.util.List<java.util.Map<String, Object>> caParSociete = ChiffreAffaireDiffusionDAO
                .getCaParSociete(volOpereId, avionId, societeId, dateDebut, dateFin);

        // Listes pour les filtres
        java.util.List<Societe> societes = SocieteDao.getAll();
        java.util.List<VolOpere> vols = new VolOpereDAO().getAll();
        java.util.List<Avion> avions = new AvionDAO().getAll();

        // Filtres pour garder l'état sélectionné
        java.util.Map<String, Object> filtres = new java.util.HashMap<>();
        filtres.put("volOpereId", volOpereId);
        filtres.put("avionId", avionId);
        filtres.put("societeId", societeId);
        filtres.put("dateDebut", dateDebut);
        filtres.put("dateFin", dateFin);

        // Paiements filtrés
        java.util.List<java.util.Map<String, Object>> paiements = dao.PaiementDiffusionDAO.getFiltered(volOpereId,
                avionId, societeId, dateDebut, dateFin);

        req.setAttribute("resultat", resultat);
        req.setAttribute("caParSociete", caParSociete);
        req.setAttribute("societes", societes);
        req.setAttribute("volsOperes", vols);
        req.setAttribute("avions", avions);
        req.setAttribute("filtres", filtres);
        req.setAttribute("paiementsDiffusion", paiements);

        req.getRequestDispatcher("/jsp/diffusion/ca/chiffre_affaire_diffusion.jsp").forward(req, resp);
    }

    private Integer parseIntOrNull(String s) {
        try {
            if (s == null || s.trim().isEmpty())
                return null;
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }

    private Date parseDateOrNull(String s) {
        try {
            if (s == null || s.trim().isEmpty())
                return null;
            return Date.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }
}
