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
import data.entity.JsonEntity;
import data.entity.RestaurantUser;
import data.entity.TableOrder;
import data.entity.Tablehasdish;
import data.entity.TablehasdishPK;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * This class manages all updates of data in the tables
 *
 * @author Johannes Lindén
 * @since 2014-10-07
 * @version 1.0
 */
@WebServlet(name = "UpdateRow", urlPatterns = {"/updaterow"}, initParams = {
    @WebInitParam(name = "key", value = ""),
    @WebInitParam(name = "table", value = ""),
    @WebInitParam(name = "id", value = ""),
    @WebInitParam(name = "data", value = "")})
@Named
@Stateless
public class UpdateRow extends HttpServlet {

    /**
     * This injects the default persistence unit.
     */
    @PersistenceUnit(name = "WebApplication1")
    private EntityManagerFactory emf;
    /**
     * This injects a user transaction object.
     */
    @Resource
    private UserTransaction utx;

    /**
     * Processes requests for <code>POST</code> method.
     *
     * Example of a request:
     * /path/to/server/updaterow?table=tablealias&data=jsonobject
     *
     * @see https://github.com/jolin1337/Restaurang/wiki for layouts examples
     * goes here....
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set the json mimetype
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Try to access output stream 
        try (PrintWriter out = response.getWriter()) {

            int authCode = Settings.isAutorised(request.getParameter("key"));
            if (authCode == Settings.AuthCode.accept) {
                // Parse the json object
                StringReader sr = new StringReader(request.getParameter("data"));
                // read the string from the stringreader
                JsonReader rdr = Json.createReader(sr);
                // the array of the rows to edit
                JsonArray data = rdr.readObject().getJsonArray("data");
                boolean edited = false; // indicates if the database has changed

                // Iterate the rows to modify
                for (JsonValue objVal : data) {
                    if (objVal.getValueType() != JsonValue.ValueType.OBJECT) {
                        continue;
                    }
                    JsonObject obj = (JsonObject) objVal;
                    if (obj.get("data") != null && obj.get("data") instanceof JsonObject) {
                        obj = obj.getJsonObject("data");
                    }
                    // get the table to alter

                    switch (request.getParameter("table")) {
                        case "dish": // if dish is the table we want to alter
                            // try to alter the table
                            edited |= updateTable(obj, obj.getInt(Dish.getPK(), -1), Dish.class);
                            break;
                        case "booking":
                            // try to alter the table
                            edited |= updateTable(obj, obj.getInt(Booking.getPK(), -1), Booking.class);
                            break;
                        case "dishgroup":
                            // try to alter the table
                            edited |= updateTable(obj, obj.getString(Dishgroup.getPK(), ""), Dishgroup.class);
                            break;
                        case "event":
                            // try to alter the table
                            edited |= updateTable(obj, obj.getInt(Event.getPK(), -1), Event.class);
                            break;
                        case "info":
                            // try to alter the table
                            edited |= updateTable(obj, obj.getString(Info.getPK(), ""), Info.class);
                            break;
                        // try to alter the table
                        case "inventory":
                            // try to alter the table
                            edited |= updateTable(obj, obj.getInt(Inventory.getPK(), -1), Inventory.class);
                            break;
                        case "tabledishrelation":
                            // try to alter the table
                            EntityManager em = emf.createEntityManager();
                            TablehasdishPK pk = Tablehasdish.getPKOf(obj.getInt("tableNr"), obj.getInt("id"), obj.getInt("dishId"), em);
                            
                            em.clear();
                            em.close();
                            
                            if(pk == null) {
                                edited |= newTableHasDish(obj);
                            }
                            else {
                                edited |= updateTable(obj, pk, Tablehasdish.class);
                            }
                            break;
                        case "tableorder":
                            // try to alter the table
                            edited |= updateTable(obj, obj.getInt(TableOrder.getPK()), Tablehasdish.class);
                            break;
                        case "scheme":

                            break;
                        case "user":
                            edited |= updateTable(obj, obj.getInt(RestaurantUser.getPK(), -1), RestaurantUser.class);
                            break;
                    }
                }
                if (edited) // if some table has been edited
                {
                    out.print("Success"); // print success to the requester
                } else {
                    out.print("Something wrong with the data"); // otherwise print an error message
                }
            } else if (authCode == Settings.AuthCode.expired) {
                out.print("expired_key");
            } else if (authCode == Settings.AuthCode.deny) {
                out.print("false");
            }
        }
    }

    /**
     * This method alter the table with classtype T.
     *
     * @param <T> - The class of the entity we want to alter
     * @param obj - The json object that contains the information
     * @param id - The pk object value of the row to alter
     * @param tableClass - The class type of the entity we create
     * @return return true if we altered an table row otherwise false.
     */
    private <T> boolean updateTable(JsonObject obj, Object id, Class<T> tableClass) {
        boolean edited = false;
        try {
            // Start transaction to edit the table row
            utx.begin();
            // crea em to access database
            EntityManager em = emf.createEntityManager();
            // find the required row in the table
            T dishEntity = em.find(tableClass, id);
            T modEntity = dishEntity;
            if (dishEntity == null) { // a tablerow not wasn't found
                modEntity = tableClass.newInstance(); // create a new instance of the entity
                edited = true;      // now we have edited the table
            }
            if (dishEntity == null || !obj.containsKey("remove")) {
                // Should always be true, but just in case!
                if (modEntity instanceof JsonEntity) {
                    // update the entity to for new values
                    edited = ((JsonEntity) modEntity).setEntityByJson(obj, em);
                    if (edited) { // if we succeded to edit them
                        if (dishEntity == null) {   // if we created a new row
                            em.persist(modEntity);  // add the row to the table
                        } else {
                            em.merge(modEntity);    // modify the table row
                        }
                    }
                }
            } else {
                em.remove(dishEntity); // remove the table and it exists? sure why not?
                edited = true;      // well then we set the edit falg to true
            }
            try {
                em.flush();
            } catch (Exception e) {
                edited = false;
                return false; 
            } finally {
                if (edited) {
                    utx.commit(); // confirm the changes
                } else {
                    utx.rollback(); // do not make any changes
                }
                em.clear();     // forget everything we did
                em.close();     // close the em
            }

        } catch (NotSupportedException | SystemException | InstantiationException | RollbackException | IllegalAccessException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            //Logger.getLogger(UpdateRow.class.getName()).log(Level.SEVERE, null, ex);

            edited = false;
            return false;
        }
        finally {
            return edited;  // return if we have altered a table
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
        return "Set and update all rows information";
    }// </editor-fold>

    private boolean newTableHasDish(JsonObject obj) {
        try {
            utx.begin();
            EntityManager em = emf.createEntityManager();
            int tableNr = obj.getInt("tableNr");
            TypedQuery<TableOrder> query = em.createNamedQuery("TableOrder.findByTableNr", TableOrder.class);
            query.setParameter("tableNr", tableNr);
            List<TableOrder> t = query.getResultList();
            //em.clear();
            if(t.size() > 0) {
                TablehasdishPK pk = Tablehasdish.getPKOf(tableNr, t.get(0).getId(), obj.getInt("dishId"), em);

                    if (obj.containsKey("remove")) {
                        Tablehasdish hd = em.find(Tablehasdish.class, pk);
                        if(hd != null) {
                            em.remove(hd); 
                        }
                    }
                    else {
                        if(pk == null) {
                            Tablehasdish hd = new Tablehasdish(obj.getInt("dishId"), t.get(0).getId());
                            hd.setTableOrder(t.get(0));
                            hd.setEntityByJson(obj, em);
                            em.persist(hd);
                        }
                        else {
                            Tablehasdish hd = em.find(Tablehasdish.class, pk);
                            
                            hd.setTableOrder(t.get(0));
                            hd.setEntityByJson(obj, em);
                            
                            em.merge(hd);
                        }
                    }
                    try {
                        em.flush();
                    } catch (Exception e) {
                        return false; 
                    }
                    utx.commit();
                    em.clear();     // forget everything we did
                    em.close();     // close the em
                    return true;

            }
        } catch (NotSupportedException | SystemException |RollbackException | HeuristicMixedException |HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            Logger.getLogger(UpdateRow.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return false;
    }

}
