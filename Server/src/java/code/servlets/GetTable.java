/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.servlets;

import data.entity.Dish;
import java.io.IOException;
import java.io.PrintWriter;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ulf
 */
@WebServlet(name = "GetTable", urlPatterns = {"/gettable"}, initParams = {
    @WebInitParam(name = "key", value = ""),
    @WebInitParam(name = "table", value = "")})
public class GetTable extends HttpServlet {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if (request.getParameter("key").equals("enroligtemporarnyckel")) {
                switch (request.getParameter("table")) {
                    case "dish":
                        TypedQuery<Dish> query = em.createNamedQuery("Dish.findAll", Dish.class);
                        String jsonString = "{[";
                        for(Dish d : query.getResultList()){
                            jsonString+=d.toJsonString()+",";
                        }
                        jsonString = jsonString.substring(0, jsonString.length()-2);
                        jsonString += "]}";
                        out.print(jsonString);
                        break; 
                    case "booking":
                        
                        break; 
                    case "dishdroup": 
                        
                        break; 
                    case "event":
                        
                        break; 
                    case "info":
                        
                        break; 
                    case "inventory":
                        
                        break; 
                    case "scheme": 
                        
                        break; 
                    default: 
                        
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
