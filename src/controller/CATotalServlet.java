package controller;

import dao.CATotalDAO;
import model.CATotal;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CATotalServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mois = req.getParameter("mois");
        String annee = req.getParameter("annee");
        List<CATotal> caTotalList;
        if (mois != null && annee != null) {
            caTotalList = CATotalDAO.getCATotalListByMoisAnnee(mois, annee);
        } else {
            caTotalList = CATotalDAO.getCATotalList();
        }
        req.setAttribute("caTotalList", caTotalList);
        req.getRequestDispatcher("/jsp/ca_total.jsp").forward(req, resp);
    }
}
