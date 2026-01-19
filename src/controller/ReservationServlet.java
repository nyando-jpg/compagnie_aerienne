package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ReservationDAO;
import dao.ClientDAO;
import dao.VolOpereDAO;
import dao.LigneVolDAO;
import dao.AeroportDAO;
import dao.SiegeVolDAO;
import dao.BilletDAO;
import dao.PrixVolDAO;
import model.Reservation;
import model.Client;
import model.VolOpere;
import model.Aeroport;
import model.SiegeVol;
import model.Billet;

import java.io.IOException;
import java.util.List;

public class ReservationServlet extends HttpServlet {
    private ReservationDAO reservationDAO;
    private ClientDAO clientDAO;
    private VolOpereDAO volOpereDAO;
    private AeroportDAO aeroportDAO;
    private SiegeVolDAO siegeVolDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        reservationDAO = new ReservationDAO();
        clientDAO = new ClientDAO();
        volOpereDAO = new VolOpereDAO();
        aeroportDAO = new AeroportDAO();
        siegeVolDAO = new SiegeVolDAO();
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
                listReservations(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteReservation(request, response);
                break;
            case "sieges":
                getSiegesDisponibles(request, response);
                break;
            default:
                listReservations(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("insert".equals(action)) {
            insertReservation(request, response);
        } else if ("update".equals(action)) {
            updateReservation(request, response);
        }
    }
    
    private void listReservations(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String search = request.getParameter("search");
        String statut = request.getParameter("statut");
        String ligneVolIdStr = request.getParameter("ligne_vol_id");
        String dateMinStr = request.getParameter("date_min");
        String dateMaxStr = request.getParameter("date_max");
        
        Integer ligneVolId = (ligneVolIdStr != null && !ligneVolIdStr.isEmpty()) ? Integer.parseInt(ligneVolIdStr) : null;
        java.sql.Date dateMin = null;
        java.sql.Date dateMax = null;
        
        if (dateMinStr != null && !dateMinStr.isEmpty()) {
            try {
                dateMin = java.sql.Date.valueOf(dateMinStr);
            } catch (Exception e) {
                // Date invalide
            }
        }
        
        if (dateMaxStr != null && !dateMaxStr.isEmpty()) {
            try {
                dateMax = java.sql.Date.valueOf(dateMaxStr);
            } catch (Exception e) {
                // Date invalide
            }
        }
        
        List<Reservation> reservations = reservationDAO.search(search, statut, ligneVolId, dateMin, dateMax);
        
        // Get lignes for filter
        LigneVolDAO ligneVolDAO = new LigneVolDAO();
        List<model.LigneVol> lignes = ligneVolDAO.getAll();
        
        request.setAttribute("reservations", reservations);
        request.setAttribute("lignes", lignes);
        request.getRequestDispatcher("/jsp/reservation/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Client> clients = clientDAO.getAll();
        LigneVolDAO ligneVolDAO = new LigneVolDAO();
        List<model.LigneVol> lignes = ligneVolDAO.getAll();
        
        // Filtrer les vols selon les paramètres
        String ligneVolIdStr = request.getParameter("ligne_vol_id");
        String avionIdStr = request.getParameter("avion_id");
        String dateDebutStr = request.getParameter("date_debut");
        String dateFinStr = request.getParameter("date_fin");
        
        List<VolOpere> vols = null;
        
        if (ligneVolIdStr != null && !ligneVolIdStr.isEmpty()) {
            
            Integer ligneVolId = Integer.parseInt(ligneVolIdStr);
            Integer avionId = (avionIdStr != null && !avionIdStr.isEmpty()) ? Integer.parseInt(avionIdStr) : null;
            java.sql.Date dateDebut = null;
            java.sql.Date dateFin = null;
            
            if (dateDebutStr != null && !dateDebutStr.isEmpty()) {
                try {
                    dateDebut = java.sql.Date.valueOf(dateDebutStr);
                } catch (Exception e) {
                    // Date invalide, on ignore
                }
            }
            
            if (dateFinStr != null && !dateFinStr.isEmpty()) {
                try {
                    dateFin = java.sql.Date.valueOf(dateFinStr);
                } catch (Exception e) {
                    // Date invalide, on ignore
                }
            }
            
            // Utiliser la méthode avec filtres
            vols = volOpereDAO.getByLigneVolFilters(ligneVolId, avionId, dateDebut, dateFin);
        }
        
        request.setAttribute("clients", clients);
        request.setAttribute("lignes", lignes);
        request.setAttribute("vols", vols);
        request.getRequestDispatcher("/jsp/reservation/form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Reservation reservation = reservationDAO.getById(id);
        List<Client> clients = clientDAO.getAll();
        List<Aeroport> aeroports = aeroportDAO.getAll();
        List<VolOpere> vols = volOpereDAO.getAll();
        request.setAttribute("reservation", reservation);
        request.setAttribute("clients", clients);
        request.setAttribute("aeroports", aeroports);
        request.setAttribute("vols", vols);
        request.getRequestDispatcher("/jsp/reservation/form.jsp").forward(request, response);
    }
    
    private void insertReservation(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int clientId = Integer.parseInt(request.getParameter("client_id"));
            int volOpereId = Integer.parseInt(request.getParameter("vol_opere_id"));
            String statut = request.getParameter("statut");
            
            // Récupérer les lignes (type client + siège) potentielles
            String[] typeClientIds = request.getParameterValues("type_client_id[]");
            String[] siegeIds = request.getParameterValues("siege_id[]");

            if (typeClientIds == null || siegeIds == null || typeClientIds.length == 0 || typeClientIds.length != siegeIds.length) {
                request.setAttribute("error", "Aucune ligne de billet valide n'a été fournie");
                listReservations(request, response);
                return;
            }

            // 1. Créer une seule réservation pour ce client et ce vol
            Reservation r = new Reservation(clientId, volOpereId, statut);
            int reservationId = reservationDAO.insertAndGetId(r);

            if (reservationId <= 0) {
                request.setAttribute("error", "Erreur lors de la création de la réservation");
                listReservations(request, response);
                return;
            }

            PrixVolDAO prixVolDAO = new PrixVolDAO();
            BilletDAO billetDAO = new BilletDAO();

            int billetsCrees = 0;

            // 2. Pour chaque ligne, créer un siège réservé + un billet
            for (int i = 0; i < typeClientIds.length; i++) {
                int typeClientId = Integer.parseInt(typeClientIds[i]);
                int siegeId = Integer.parseInt(siegeIds[i]);

                // Récupérer infos siège depuis la table siege
                SiegeVol siegeInfo = siegeVolDAO.getSiegeInfoById(siegeId);
                if (siegeInfo == null) {
                    continue; // Siège introuvable, on ignore cette ligne
                }

                // Calculer prix basé sur le vol opéré, classe et type de client
                double prix = prixVolDAO.getPrix(volOpereId, siegeInfo.getClasseSiegeId(), typeClientId);

                if (prix <= 0) {
                    continue; // Prix introuvable, on ignore cette ligne
                }

                // Créer siege_vol avec statut RESERVE pour ce siège
                int siegeVolId = siegeVolDAO.insertSiegeVol(volOpereId, siegeId, "RESERVE");
                if (siegeVolId <= 0) {
                    continue; // Impossible de réserver ce siège (probablement déjà pris)
                }

                // Créer billet lié à cette réservation et ce siège
                Billet billet = new Billet(reservationId, siegeVolId, prix, "EMIS");
                int billetId = billetDAO.insert(billet);
                if (billetId > 0) {
                    billetsCrees++;
                }
            }

            if (billetsCrees > 0) {
                request.setAttribute("message", billetsCrees + " billet(s) créé(s) avec succès pour cette réservation");
            } else {
                request.setAttribute("error", "Aucun billet n'a pu être créé pour cette réservation (sièges ou prix introuvables)");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
            e.printStackTrace();
        }
        
        listReservations(request, response);
    }
    
    private void updateReservation(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int clientId = Integer.parseInt(request.getParameter("client_id"));
            int volOpereId = Integer.parseInt(request.getParameter("vol_opere_id"));
            String statut = request.getParameter("statut");
            
            Reservation r = new Reservation(clientId, volOpereId, statut);
            r.setId(id);
            
            boolean success = reservationDAO.update(r);
            
            if (success) {
                request.setAttribute("message", "Réservation modifiée avec succès");
            } else {
                request.setAttribute("error", "Erreur lors de la modification");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }
        
        listReservations(request, response);
    }
    
    private void getSiegesDisponibles(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int volOpereId = Integer.parseInt(request.getParameter("vol_opere_id"));
            List<SiegeVol> sieges = siegeVolDAO.getSiegesDisponibles(volOpereId);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < sieges.size(); i++) {
                SiegeVol s = sieges.get(i);
                json.append("{");
                json.append("\"id\":").append(s.getSiegeId()).append(","); // Utiliser siege_id
                json.append("\"numeroSiege\":\"").append(s.getNumeroSiege()).append("\",");
                json.append("\"classeLibelle\":\"").append(s.getClasseLibelle()).append("\"");
                json.append("}");
                if (i < sieges.size() - 1) json.append(",");
            }
            json.append("]");
            
            response.getWriter().write(json.toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    private void deleteReservation(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = reservationDAO.delete(id);
        
        if (success) {
            request.setAttribute("message", "Réservation supprimée avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la suppression");
        }
        
        listReservations(request, response);
    }
}
