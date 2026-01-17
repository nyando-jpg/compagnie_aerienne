package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.TypeClientDAO;
import model.TypeClient;

public class TypeClientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TypeClientDAO typeClientDAO;

    @Override
    public void init() throws ServletException {
        typeClientDAO = new TypeClientDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "add":
                showForm(request, response, null);
                break;
            case "edit":
                editForm(request, response);
                break;
            case "delete":
                delete(request, response);
                break;
            default:
                list(request, response);
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<TypeClient> list = typeClientDAO.getAll();
        request.setAttribute("typesClients", list);
        request.getRequestDispatcher("/jsp/type_client/list.jsp").forward(request, response);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, TypeClient tc)
            throws ServletException, IOException {
        if (tc != null) {
            request.setAttribute("typeClient", tc);
        }
        request.getRequestDispatcher("/jsp/type_client/form.jsp").forward(request, response);
    }

    private void editForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/type-client?action=list");
            return;
        }
        int id = Integer.parseInt(idStr);
        TypeClient tc = typeClientDAO.getById(id);
        if (tc == null) {
            response.sendRedirect(request.getContextPath() + "/type-client?action=list");
            return;
        }
        showForm(request, response, tc);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            int id = Integer.parseInt(idStr);
            typeClientDAO.delete(id);
        }
        response.sendRedirect(request.getContextPath() + "/type-client?action=list");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String libelle = request.getParameter("libelle");
        String description = request.getParameter("description");

        TypeClient tc = new TypeClient();
        tc.setLibelle(libelle);
        tc.setDescription(description);

        if (idStr == null || idStr.isEmpty()) {
            typeClientDAO.insert(tc);
        } else {
            tc.setId(Integer.parseInt(idStr));
            typeClientDAO.update(tc);
        }

        response.sendRedirect(request.getContextPath() + "/type-client?action=list");
    }
}
