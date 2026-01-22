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
import dao.TypeClientDAO;
import dao.PrixVolDAO;
import model.VolOpere;
import model.LigneVol;
import model.ClasseSiege;
import model.Avion;
import model.TypeClient;

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
    private TypeClientDAO typeClientDAO;
    private PrixVolDAO prixVolDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        volOpereDAO = new VolOpereDAO();
        ligneVolDAO = new LigneVolDAO();
        statusVolDAO = new StatusVolDAO();
        classeSiegeDAO = new ClasseSiegeDAO();
        avionDAO = new AvionDAO();
        typeClientDAO = new TypeClientDAO();
        prixVolDAO = new PrixVolDAO();
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

        Integer ligneVolId = (ligneVolIdStr != null && !ligneVolIdStr.isEmpty()) ? Integer.parseInt(ligneVolIdStr)
                : null;
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
        List<TypeClient> typesClients = typeClientDAO.getAll();
        request.setAttribute("lignes", lignes);
        request.setAttribute("statuses", statuses);
        request.setAttribute("classes", classes);
        request.setAttribute("avions", avions);
        request.setAttribute("typesClients", typesClients);
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
        List<TypeClient> typesClients = typeClientDAO.getAll();
        request.setAttribute("vol", vo);
        request.setAttribute("lignes", lignes);
        request.setAttribute("statuses", statuses);
        request.setAttribute("classes", classes);
        request.setAttribute("avions", avions);
        request.setAttribute("typesClients", typesClients);
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
                // Récupérer les prix par classe et type de client
                List<ClasseSiege> classes = classeSiegeDAO.getAll();
                List<TypeClient> typesClients = typeClientDAO.getAll();

                java.util.Map<String, Object> prixEtPourcentages = new java.util.HashMap<>();

                for (ClasseSiege cs : classes) {
                    for (TypeClient tc : typesClients) {
                        String key = cs.getId() + "_" + tc.getId();

                        if ("POURCENTAGE".equals(tc.getModeCalcul())) {
                            // Récupérer le pourcentage
                            String paramName = "pourcent_classe_" + cs.getId() + "_type_" + tc.getId();
                            String pourcentStr = request.getParameter(paramName);
                            if (pourcentStr != null && !pourcentStr.isEmpty()) {
                                double pourcent = Double.parseDouble(pourcentStr);
                                prixEtPourcentages.put(key, pourcent);
                            }
                        } else {
                            // Récupérer le prix fixe
                            String paramName = "prix_classe_" + cs.getId() + "_type_" + tc.getId();
                            String prixStr = request.getParameter(paramName);
                            if (prixStr != null && !prixStr.isEmpty()) {
                                double prix = Double.parseDouble(prixStr);
                                prixEtPourcentages.put(key, prix);
                            }
                        }
                    }
                }

                // Insérer les prix et pourcentages
                prixVolDAO.insertPrixPourVol(volOpereId, prixEtPourcentages);

                request.setAttribute("message", "Vol opéré et prix ajoutés avec succès");
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
