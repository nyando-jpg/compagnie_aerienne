package controller;

import dao.PaiementDiffusionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PaiementDiffusionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path != null && path.equals("/new")) {
            // Charger les sociétés pour le select
            List<model.Societe> societes = dao.SocieteDao.getAll();
            req.setAttribute("societes", societes);
            // Charger les méthodes de paiement
            List<Map<String, Object>> methodes = PaiementDiffusionDAO.getAllMethodesPaiement();
            req.setAttribute("methodesPaiement", methodes);
            req.getRequestDispatcher("/jsp/paiement_diffusion/paiement_diffusion_form.jsp").forward(req, resp);
        } else {
            // Lecture des filtres
            String societeIdStr = req.getParameter("societeId");
            String dateDebutStr = req.getParameter("dateDebut");
            String dateFinStr = req.getParameter("dateFin");
            Integer societeId = null;
            java.sql.Date dateDebut = null;
            java.sql.Date dateFin = null;
            try {
                if (societeIdStr != null && !societeIdStr.isEmpty())
                    societeId = Integer.parseInt(societeIdStr);
                if (dateDebutStr != null && !dateDebutStr.isEmpty())
                    dateDebut = java.sql.Date.valueOf(dateDebutStr);
                if (dateFinStr != null && !dateFinStr.isEmpty())
                    dateFin = java.sql.Date.valueOf(dateFinStr);
            } catch (Exception e) {
                /* ignore parse errors, fallback to null */ }
            List<Map<String, Object>> paiements = PaiementDiffusionDAO.getFiltered(null, null, societeId, dateDebut,
                    dateFin);
            req.setAttribute("paiements", paiements);
            // Toujours passer la liste des sociétés pour le select
            List<model.Societe> societes = dao.SocieteDao.getAll();
            req.setAttribute("societes", societes);
            req.getRequestDispatcher("/jsp/paiement_diffusion/paiement_diffusion_list.jsp").forward(req, resp);
            // Calcul des totaux si société filtrée
            Double sommePayee = null;
            Double sommeDue = null;
            if (societeId != null) {
                sommePayee = 0.0;
                for (Map<String, Object> p : paiements) {
                    Object montant = p.get("montant");
                    if (montant instanceof Number) {
                        sommePayee += ((Number) montant).doubleValue();
                    }
                }
                sommeDue = dao.PaiementDiffusionDAO.getTotalDuPourSociete(societeId);
            }
            req.setAttribute("sommePayee", sommePayee);
            req.setAttribute("sommeDue", sommeDue);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path != null && path.equals("/create")) {
            String idSocieteStr = req.getParameter("idSociete");
            String moisStr = req.getParameter("mois"); // format yyyy-MM
            String montantStr = req.getParameter("montant");
            String datePaiementStr = req.getParameter("datePaiement");
            String idMethodePaiementStr = req.getParameter("methodePaiement");
            boolean success = false;
            String errorMsg = null;
            if (idSocieteStr != null && moisStr != null && montantStr != null && datePaiementStr != null
                    && idMethodePaiementStr != null) {
                try {
                    int idSociete = Integer.parseInt(idSocieteStr);
                    double montantTotal = Double.parseDouble(montantStr);
                    java.sql.Timestamp datePaiement = java.sql.Timestamp
                            .valueOf(datePaiementStr.replace("T", " ") + ":00");
                    int idMethodePaiement = Integer.parseInt(idMethodePaiementStr);
                    // Appel DAO pour paiement groupé
                    StringBuilder errorBuilder = new StringBuilder();
                    success = PaiementDiffusionDAO.insertPaiementParMoisSociete(idSociete, moisStr, montantTotal,
                            datePaiement, idMethodePaiement, errorBuilder);
                    if (!success && errorBuilder.length() > 0) {
                        errorMsg = errorBuilder.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = e.getMessage();
                }
            }
            if (success) {
                resp.sendRedirect(req.getContextPath() + "/paiement-diffusion");
            } else {
                req.setAttribute("error",
                        errorMsg != null ? errorMsg : "Erreur lors de l'insertion du paiement diffusion.");
                List<model.Societe> societes = dao.SocieteDao.getAll();
                req.setAttribute("societes", societes);
                List<Map<String, Object>> methodes = PaiementDiffusionDAO.getAllMethodesPaiement();
                req.setAttribute("methodesPaiement", methodes);
                req.getRequestDispatcher("/jsp/paiement_diffusion/paiement_diffusion_form.jsp").forward(req, resp);
            }
        }
    }
}
