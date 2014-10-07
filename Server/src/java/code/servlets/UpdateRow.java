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
import data.entity.JsonEntity;
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
import javax.transaction.RollbackException;
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
    @PersistenceUnit(name = "WebApplication1")
    private EntityManagerFactory emf;
    // This injects a user transaction object.
    @Resource
    private UserTransaction utx;

    <T> boolean updateTable(JsonObject obj, Object id,  Class<T> tableClass) {
        boolean edited = false;
        try {
            utx.begin();
            EntityManager em = emf.createEntityManager();
            T dishEntity = em.find(tableClass, id);
            T modEntity = dishEntity;
            if (dishEntity == null) {
                modEntity = tableClass.newInstance();
                edited = true;
            }
            if (dishEntity != null && obj.containsKey("remove")) {
                em.remove(dishEntity);
                edited = true;
            } else {
                // Should always be true, but just in case!
                if (modEntity instanceof JsonEntity) { 
                    edited = ((JsonEntity)modEntity).setEntityByJson(obj, em);
                    if(edited) {
                        if (dishEntity == null) {
                            em.persist(modEntity);
                        } else {
                            em.merge(modEntity);
                        }
                    }
                }
            }
            try {
                em.flush();
            } catch (Exception e) {
            } finally {
                if(edited)
                   utx.commit();
                else
                    utx.rollback();
                em.clear();
                em.close(); 
            }

        } catch (Exception ex) {
            Logger.getLogger(UpdateRow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return edited;
    }

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
            if (authCode == Settings.AuthCode.accept) {
                StringReader sr = new StringReader(request.getParameter("data"));
                JsonReader rdr = Json.createReader(sr);
                JsonArray data = rdr.readObject().getJsonArray("data");
                boolean edited = false;
                for (JsonValue objVal : data) {
                    JsonObject obj = (JsonObject) objVal;
                    switch (request.getParameter("table")) {
                        case "dish":
                            edited |= updateTable(obj, obj.getInt("id", -1), Dish.class);
                            break; 
                         case "booking":

                         break; 
                         case "dishdroup": 
                            edited |= updateTable(obj, obj.getString("name", ""), Dishgroup.class);
                         break; 
                         case "event":
                            edited |= updateTable(obj, obj.getInt("id", -1), Event.class);
                         break; 
                         case "info":
                            edited |= updateTable(obj, obj.getString("what", ""), Info.class);
                         break; 
                         case "inventory":
                            edited |= updateTable(obj, obj.getInt("id", -1), Inventory.class);
                         break; 
                         case "scheme": 

                         break; 
                    }
                }
                if(edited)
                    out.print("Success");
                else out.print("Something wrong with the data");
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
        return "Set and update a rows information";
    }// </editor-fold>

}
