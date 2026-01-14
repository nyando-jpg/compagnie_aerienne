package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class HomeServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("HomeServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("appName", "Compagnie Aérienne");
        request.setAttribute("welcomeMessage", "Système de Gestion Compagnie Aérienne");
        
        request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
    }
}
