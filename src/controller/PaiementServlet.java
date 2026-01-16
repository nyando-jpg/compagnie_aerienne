package controller;

import dao.MethodePaiementDAO;
import dao.PaiementDAO;
import model.MethodePaiement;
import model.Paiement;
import model.Reservation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaiementServlet extends HttpServlet {

    private PaiementDAO paiementDAO;
    private MethodePaiementDAO methodePaiementDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        paiementDAO = new PaiementDAO();
        methodePaiementDAO = new MethodePaiementDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "add":
                showAddForm(request, response);
                break;
            case "details":
                showDetails(request, response);
                break;
            default:
                listPaiements(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("insert".equals(action)) {
            insertPaiement(request, response);
        } else {
            listPaiements(request, response);
        }
    }

    private void listPaiements(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numeroVol = request.getParameter("numero_vol");
        String volOpereId = request.getParameter("vol_opere_id");
        String statutPaiement = request.getParameter("statut_paiement");

        List<Reservation> reservations = paiementDAO.getReservationsAvecStatutPaiement(numeroVol, volOpereId, statutPaiement);
        request.setAttribute("reservations", reservations);
        request.setAttribute("numero_vol", numeroVol != null ? numeroVol : "");
        request.setAttribute("vol_opere_id", volOpereId != null ? volOpereId : "");
        request.setAttribute("statut_paiement", statutPaiement != null ? statutPaiement : "");
        request.getRequestDispatcher("/jsp/paiement/list.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reservationIdStr = request.getParameter("reservation_id");
        if (reservationIdStr == null || reservationIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/paiement");
            return;
        }

        int reservationId = Integer.parseInt(reservationIdStr);
        Reservation reservation = paiementDAO.getReservationPaiementById(reservationId);
        if (reservation == null) {
            response.sendRedirect(request.getContextPath() + "/paiement");
            return;
        }

        List<MethodePaiement> methodes = methodePaiementDAO.getAll();
        request.setAttribute("reservation", reservation);
        request.setAttribute("methodes", methodes);
        request.getRequestDispatcher("/jsp/paiement/form.jsp").forward(request, response);
    }

    private void showDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reservationIdStr = request.getParameter("reservation_id");
        if (reservationIdStr == null || reservationIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/paiement");
            return;
        }

        int reservationId = Integer.parseInt(reservationIdStr);
        
        // Récupérer les informations de la réservation
        Reservation reservation = paiementDAO.getReservationPaiementById(reservationId);
        if (reservation == null) {
            response.sendRedirect(request.getContextPath() + "/paiement");
            return;
        }

        // Récupérer l'historique des paiements
        List<Paiement> paiements = paiementDAO.getPaiementsByReservationId(reservationId);
        
        request.setAttribute("reservation", reservation);
        request.setAttribute("paiements", paiements);
        request.getRequestDispatcher("/jsp/paiement/details.jsp").forward(request, response);
    }

    private void insertPaiement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int reservationId = Integer.parseInt(request.getParameter("reservation_id"));

            String[] methodeIds = request.getParameterValues("methode_id");
            String[] montants = request.getParameterValues("montant");

            Map<Integer, Double> montantsParMethode = new HashMap<>();

            if (methodeIds != null && montants != null) {
                for (int i = 0; i < methodeIds.length && i < montants.length; i++) {
                    String midStr = methodeIds[i];
                    String montantStr = montants[i];
                    if (midStr == null || midStr.isEmpty() || montantStr == null || montantStr.isEmpty()) {
                        continue;
                    }
                    int methodeId = Integer.parseInt(midStr);
                    double montant = Double.parseDouble(montantStr);
                    if (montant <= 0) continue;

                    montantsParMethode.merge(methodeId, montant, Double::sum);
                }
            }

            if (montantsParMethode.isEmpty()) {
                request.setAttribute("error", "Veuillez saisir au moins un montant de paiement valide.");
                showAddForm(request, response);
                return;
            }

            boolean success = paiementDAO.insererPaiement(reservationId, montantsParMethode);

            if (success) {
                request.setAttribute("message", "Paiement enregistré avec succès.");
            } else {
                request.setAttribute("error", "Erreur lors de l'enregistrement du paiement.");
            }

            listPaiements(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
            listPaiements(request, response);
        }
    }
}
