package controller;

import dao.SocieteDao;
import dao.PaiementFactureDAO;
import model.Societe;
import model.PaiementFacture;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FactureServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Societe> societes = SocieteDao.getAll();
        req.setAttribute("societes", societes);
        String societeIdStr = req.getParameter("societeId");
        List<PaiementFacture> paiements = null;
        if (societeIdStr != null && !societeIdStr.isEmpty()) {
            try {
                int societeId = Integer.parseInt(societeIdStr);
                paiements = PaiementFactureDAO.getPaiementsBySociete(societeId);
            } catch (NumberFormatException e) {
                // ignore, no paiements
            }
        }
        req.setAttribute("paiements", paiements);
        req.getRequestDispatcher("/jsp/facture.jsp").forward(req, resp);
    }
}
