package controller;

import dao.SocieteDao;
import model.Societe;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SocieteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if (action == null)
                action = "list";

            switch (action) {
                case "add":
                    req.getRequestDispatcher("/jsp/societe/form.jsp").forward(req, resp);
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
        List<Societe> societes = SocieteDao.getAll();
        req.setAttribute("societes", societes);
        req.getRequestDispatcher("/jsp/societe/list.jsp").forward(req, resp);
    }

    private void editForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendRedirect(req.getContextPath() + "/societe?action=list");
            return;
        }
        int id = Integer.parseInt(idStr);
        Societe s = SocieteDao.getById(id);
        if (s == null) {
            resp.sendRedirect(req.getContextPath() + "/societe?action=list");
            return;
        }
        req.setAttribute("societe", s);
        req.getRequestDispatcher("/jsp/societe/form.jsp").forward(req, resp);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        if (idStr != null) {
            int id = Integer.parseInt(idStr);
            SocieteDao.delete(id);
        }
        resp.sendRedirect(req.getContextPath() + "/societe?action=list");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idStr = req.getParameter("id");
            String nom = req.getParameter("nom");

            Societe s = new Societe();
            s.setNom(nom);
            if (idStr != null && !idStr.trim().isEmpty() && !idStr.equals("0")) {
                s.setId(Integer.parseInt(idStr));
                SocieteDao.update(s);
            } else {
                SocieteDao.insert(s);
            }
            resp.sendRedirect(req.getContextPath() + "/societe?action=list");
        } catch (Exception e) {

        }
    }
}
