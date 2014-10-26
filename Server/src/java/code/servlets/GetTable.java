/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package code.servlets;

import data.Settings;
import data.entity.Booking;
import data.entity.Dish;
import data.entity.Dishgroup;
import data.entity.Event;
import data.entity.Info;
import data.entity.Inventory;
import data.entity.RestaurantUser;
import data.entity.TableOrder;
import data.entity.Tablehasdish;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.ejb.Stateless;
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
 * This class gets information about the table in json format
 *
 * @author Johannes Lindén
 * @since 2014-10-07
 * @version 1.0
 */
@WebServlet(name = "GetTable", urlPatterns = {"/gettable"}, initParams = {
    @WebInitParam(name = "key", value = ""),
    @WebInitParam(name = "table", value = "")})
@Stateless
public class GetTable extends HttpServlet {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    /**
     * Processes requests for HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            //Checks if the request has the right authority 
            int authCode = Settings.isAutorised(request.getParameter("key"));
            if (authCode == Settings.AuthCode.accept) {
                String jsonString = "{\"data\": [";
                switch (request.getParameter("table")) {
                    case "dish":
                        //Gets data from dish table 
                        TypedQuery<Dish> dishQuery = em.createNamedQuery("Dish.findAll", Dish.class);
                        for (Dish d : dishQuery.getResultList()) {
                            jsonString += d.toJsonString() + ",";
                        }
                        break;
                    case "booking":
                        //Gets data from the booking table
                        TypedQuery<Booking> bookingQuery = em.createNamedQuery("Booking.findAll", Booking.class);
                        for (Booking d : bookingQuery.getResultList()) {
                            jsonString += d.toJsonString() + ",";
                        }
                        break;
                    case "dishgroup":
                        //Gets data from the dishgroup table
                        TypedQuery<Dishgroup> dishGroupQuery = em.createNamedQuery("Dishgroup.findAll", Dishgroup.class);
                        for (Dishgroup d : dishGroupQuery.getResultList()) {
                            jsonString += d.toJsonString() + ",";
                        }
                        break;
                    case "event":
                        //get data from event table
                        TypedQuery<Event> eventQuery = em.createNamedQuery("Event.findAll", Event.class);
                        for (Event d : eventQuery.getResultList()) {
                            jsonString += d.toJsonString() + ",";
                        }
                        break;
                    case "info":
                        //Gets data from info table
                        TypedQuery<Info> infoQuery = em.createNamedQuery("Info.findAll", Info.class);
                        for (Info d : infoQuery.getResultList()) {
                            jsonString += d.toJsonString() + ",";
                        }
                        break;
                    case "inventory":
                        //Gets data from inventory table
                        TypedQuery<Inventory> inventoryQuery = em.createNamedQuery("Inventory.findAll", Inventory.class);
                        for (Inventory d : inventoryQuery.getResultList()) {
                            jsonString += d.toJsonString() + ",";
                        }
                        break; 
                    case "scheme":

                        break;
                    case "tableorder":
                        //Gets data from tableorder table
                        TypedQuery<TableOrder> tableOrderQuery = em.createNamedQuery("TableOrder.findAll", TableOrder.class);
                        for (TableOrder d : tableOrderQuery.getResultList()) {
                            jsonString += d.toJsonString() + ",";
                        }
                        break;
                    case "tabledishrelation":
                        //Gets data from tabledishrelation table
                        TypedQuery<Tablehasdish> tableHasDishQuery = em.createNamedQuery("Tablehasdish.findAllOrders", Tablehasdish.class);
                        for (Tablehasdish d : tableHasDishQuery.getResultList()) {
                            jsonString += d.toJsonString() + ",";
                        }
                        break;
                    case "user":
                        //Gets data from user table
                        TypedQuery<RestaurantUser> userQuery = em.createNamedQuery("RestaurantUser.findAll", RestaurantUser.class);
                        for (RestaurantUser d : userQuery.getResultList()) {
                            jsonString += d.toJsonString() + ",";
                        }

                        break;
                    default:

                }
                if (jsonString.length() > "{\"data\": [".length()) {
                    jsonString = jsonString.substring(0, jsonString.length() - 1);
                }
                jsonString += "]}"; // ends the json array
                out.print(jsonString);//URLEncoder.encode(jsonString, "UTF-8"));
            } else if (authCode == Settings.AuthCode.expired) {
                out.print("expired_key");
            } else if (authCode == Settings.AuthCode.deny) {
                out.print("false");
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
        return "Displays information in a specified table";
    }// </editor-fold>

}
