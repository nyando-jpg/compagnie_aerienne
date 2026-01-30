package controller;

import dao.DiffusionDAO;
import model.Diffusion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class DiffusionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if (action == null)
                action = "list";

            // Charger sociétés et vols opérés pour le formulaire
            List<model.Societe> societes = dao.SocieteDao.getAll();
            List<model.VolOpere> vols = new dao.VolOpereDAO().getAll();
            req.setAttribute("societes", societes);
            req.setAttribute("volsOperes", vols);

            switch (action) {
                case "add":
                    req.getRequestDispatcher("/jsp/diffusion/form.jsp").forward(req, resp);
                    break;
                case "edit":
                    editForm(req, resp);
                    break;
                case "delete":
                    delete(req, resp);
                    break;
                default:
                    list(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.toString());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            req.setAttribute("stackTrace", sw.toString());
            req.getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Diffusion> diffusions = DiffusionDAO.getAll();
        req.setAttribute("diffusions", diffusions);
        req.getRequestDispatcher("/jsp/diffusion/list.jsp").forward(req, resp);
    }

    private void editForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendRedirect(req.getContextPath() + "/diffusion?action=list");
            return;
        }
        int id = Integer.parseInt(idStr);
        Diffusion d = DiffusionDAO.getById(id);
        if (d == null) {
            resp.sendRedirect(req.getContextPath() + "/diffusion?action=list");
            return;
        }
        req.setAttribute("diffusion", d);
        req.getRequestDispatcher("/jsp/diffusion/form.jsp").forward(req, resp);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        if (idStr != null) {
            int id = Integer.parseInt(idStr);
            DiffusionDAO.delete(id);
        }
        resp.sendRedirect(req.getContextPath() + "/diffusion?action=list");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idStr = req.getParameter("id");
            String idSocieteStr = req.getParameter("idSociete");
            String idVolOpereStr = req.getParameter("idVolOpere");
            String dateStr = req.getParameter("date");
            String nombreStr = req.getParameter("nombre");

            String formError = null;
            Diffusion d = new Diffusion();
            try {
                d.setIdSociete(Integer.parseInt(idSocieteStr));
            } catch (Exception ex) {
                formError = "ID Société invalide";
            }
            try {
                d.setIdVolOpere(Integer.parseInt(idVolOpereStr));
            } catch (Exception ex) {
                formError = "ID Vol Opéré invalide";
            }
            try {
                // Handle datetime-local input (e.g., 2026-01-23T15:30)
                if (dateStr != null && !dateStr.isEmpty()) {
                    String dateTimeStr = dateStr.replace('T', ' ');
                    if (dateTimeStr.length() == 16)
                        dateTimeStr += ":00"; // add seconds if missing
                    d.setDate(Timestamp.valueOf(dateTimeStr));
                } else {
                    d.setDate(null);
                }
            } catch (Exception ex) {
                formError = "Date invalide (format attendu: yyyy-MM-ddTHH:mm)";
            }
            try {
                d.setNombre(Integer.parseInt(nombreStr));
            } catch (Exception ex) {
                formError = "Nombre invalide";
            }

            if (formError != null) {
                req.setAttribute("formError", formError);
                req.setAttribute("diffusion", d);
                req.getRequestDispatcher("/jsp/diffusion/form.jsp").forward(req, resp);
                return;
            }

            boolean ok;
            if (idStr != null && !idStr.trim().isEmpty() && !idStr.equals("0")) {
                d.setId(Integer.parseInt(idStr));
                ok = DiffusionDAO.update(d);
            } else {
                ok = DiffusionDAO.insert(d);
            }
            if (!ok) {
                req.setAttribute("formError", "Erreur lors de l'enregistrement en base (voir logs)");
                req.setAttribute("diffusion", d);
                req.getRequestDispatcher("/jsp/diffusion/form.jsp").forward(req, resp);
                return;
            }

            resp.sendRedirect(req.getContextPath() + "/diffusion?action=list");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.toString());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            req.setAttribute("stackTrace", sw.toString());
            req.getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
        }
    }
}
