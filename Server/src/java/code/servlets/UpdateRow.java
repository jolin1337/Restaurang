/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.servlets;

import data.Settings;
import data.entity.Dish;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

/**
 *
 * @author Johannes
 */

@WebServlet(name = "UpdateRow", urlPatterns = {"/updaterow"}, initParams = {
    @WebInitParam(name = "key", value = ""),
    @WebInitParam(name = "table", value = ""),
    @WebInitParam(name = "id", value = ""),
    @WebInitParam(name = "data", value = "")})
@Named
@Stateful
public class UpdateRow extends HttpServlet { 

    // This injects the default persistence unit.
    @PersistenceUnit(name = "WebApplication1") private EntityManagerFactory emf;
    // This injects a user transaction object.
    @Resource private UserTransaction utx; 
    

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
        response.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            int authCode = Settings.isAutorised(request.getParameter("key"));
            if(authCode == Settings.AuthCode.accept) {
                StringReader sr = new StringReader(request.getParameter("data"));
                JsonReader rdr = Json.createReader(sr);
                JsonArray data = rdr.readObject().getJsonArray("data"); 
                switch (request.getParameter("table")) {
                    case "dish":
                        for(JsonValue objVal : data) {
                            JsonObject obj = (JsonObject) objVal; 
                            try {
                                utx.begin();
                                EntityManager em = emf.createEntityManager();
                                Dish dishEntity = em.find(Dish.class, obj.getInt("id", -1));
                                Dish modEntity = dishEntity;
                                if(dishEntity == null) {
                                    modEntity = new Dish();
                                }
                                if(dishEntity != null && obj.containsKey("remove"))
                                    em.remove(dishEntity); 
                                else {
                                    modEntity.setName(obj.getString("name", null));
                                    JsonNumber price = obj.getJsonNumber("price");
                                    if(price != null)
                                        modEntity.setPrice(price.doubleValue());
                                    if(dishEntity == null)
                                        em.persist(modEntity);
                                    else  
                                        em.merge(modEntity);
                                }
                                try {
                                    em.flush();
                                }catch(Exception e) {
                                }
                                finally {
                                utx.commit();
                                em.clear();
                                em.close();
                                }
                            } catch (Exception ex) { 
                                Logger.getLogger(UpdateRow.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        break; /*
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
                        */
                }
            }
            else if(authCode == Settings.AuthCode.expired)
                out.print("expired_key");
            else if(authCode == Settings.AuthCode.deny)
                out.print("false");
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
        return "Set and update a rows information";
    }// </editor-fold>

}
