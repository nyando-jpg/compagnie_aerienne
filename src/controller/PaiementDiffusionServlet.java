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
            // Load diffusions for select
            List<Map<String, Object>> diffusions = PaiementDiffusionDAO.getAllDiffusions();
            req.setAttribute("diffusions", diffusions);
            // Load payment methods
            List<Map<String, Object>> methodes = PaiementDiffusionDAO.getAllMethodesPaiement();
            req.setAttribute("methodesPaiement", methodes);
            req.getRequestDispatcher("/jsp/paiement_diffusion/paiement_diffusion_form.jsp").forward(req, resp);
        } else {
            List<Map<String, Object>> paiements = PaiementDiffusionDAO.getAll();
            req.setAttribute("paiements", paiements);
            req.getRequestDispatcher("/jsp/paiement_diffusion/paiement_diffusion_list.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path != null && path.equals("/create")) {
            String idDiffusionStr = req.getParameter("idDiffusion");
            String montantStr = req.getParameter("montant");
            String datePaiementStr = req.getParameter("datePaiement");
            boolean success = false;
            String idMethodePaiementStr = req.getParameter("methodePaiement");
            if (idDiffusionStr != null && montantStr != null && datePaiementStr != null
                    && idMethodePaiementStr != null) {
                try {
                    int idDiffusion = Integer.parseInt(idDiffusionStr);
                    double montant = Double.parseDouble(montantStr);
                    java.sql.Timestamp datePaiement = java.sql.Timestamp
                            .valueOf(datePaiementStr.replace("T", " ") + ":00");
                    int idMethodePaiement = Integer.parseInt(idMethodePaiementStr);
                    success = PaiementDiffusionDAO.insert(idDiffusion, montant, datePaiement, idMethodePaiement);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (success) {
                resp.sendRedirect(req.getContextPath() + "/paiement-diffusion");
            } else {
                req.setAttribute("error", "Erreur lors de l'insertion du paiement diffusion.");
                List<Map<String, Object>> diffusions = PaiementDiffusionDAO.getAllDiffusions();
                req.setAttribute("diffusions", diffusions);
                List<Map<String, Object>> methodes = PaiementDiffusionDAO.getAllMethodesPaiement();
                req.setAttribute("methodesPaiement", methodes);
                req.getRequestDispatcher("/jsp/paiement_diffusion/paiement_diffusion_form.jsp").forward(req, resp);
            }
        }
    }
}
