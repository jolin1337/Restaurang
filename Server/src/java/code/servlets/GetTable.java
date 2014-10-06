/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.servlets;

import data.Settings;
import data.entity.Dish;
import data.entity.Dishgroup;
import data.entity.Event;
import data.entity.Info;
import data.entity.Inventory;
import java.io.IOException;
import java.io.PrintWriter;
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
            if (Settings.isAutorised(request.getParameter("key"))) {
                String jsonString = "{\"data\": [";
                switch (request.getParameter("table")) {
                    case "dish":
                        TypedQuery<Dish> dishQuery = em.createNamedQuery("Dish.findAll", Dish.class);
                        for(Dish d : dishQuery.getResultList()){
                            jsonString += d.toJsonString() + ",";
                        }
                        break; 
                    case "booking":
                        
                        break; 
                    case "dishdroup": 
                        
                        TypedQuery<Dishgroup> dishGroupQuery = em.createNamedQuery("DishGroup.findAll", Dishgroup.class);
                        for(Dishgroup d : dishGroupQuery.getResultList()){
                            jsonString += d.toJsonString() + ",";
                        }
                        break; 
                    case "event":
                        
                        TypedQuery<Event> eventQuery = em.createNamedQuery("Event.findAll", Event.class);
                        for(Event d : eventQuery.getResultList()){
                            jsonString += d.toJsonString() + ",";
                        }
                        break; 
                    case "info":
                        
                        TypedQuery<Info> infoQuery = em.createNamedQuery("Info.findAll", Info.class);
                        for(Info d : infoQuery.getResultList()){
                            jsonString += d.toJsonString() + ",";
                        }
                        break; 
                    case "inventory":
                        
                        TypedQuery<Inventory> inventoryQuery = em.createNamedQuery("Inventory.findAll", Inventory.class);
                        for(Inventory d : inventoryQuery.getResultList()){
                            jsonString += d.toJsonString() + ",";
                        }
                        break; 
                    case "scheme": 
                        
                        break; 
                    default: 
                        
                }
                if(jsonString.length() > "{'data': [".length())
                    jsonString = jsonString.substring(0, jsonString.length()-2);
                jsonString += "]}";
                out.print(jsonString);
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
