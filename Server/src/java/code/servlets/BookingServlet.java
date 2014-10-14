/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package code.servlets;

import data.entity.Booking;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
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
 *
 * @author Johannes
 */
@WebServlet(name = "BookingServlet", urlPatterns = {"/newbooking"}, initParams = {
    @WebInitParam(name = "name", value = ""),
    @WebInitParam(name = "tel", value = ""),
    @WebInitParam(name = "sdate", value = ""),
    @WebInitParam(name = "count", value = "1")})
public class BookingServlet extends HttpServlet {
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
     * Processes requests for both HTTP <code>POST</code>
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
        String name = request.getParameter("name");
        String tel = request.getParameter("tel");
        String sdate = request.getParameter("sdate");
        int count;
        try {
            count = Integer.parseInt(request.getParameter("count"));
        }
        catch(NumberFormatException ex) {
            count = -1;
        }
        if(name.isEmpty() || tel.isEmpty() || sdate.isEmpty() || count <= 0 || count > 6)
            response.sendRedirect(response.encodeRedirectURL("/Server/faces/index.xhtml?page=bord&s=false") );
        Booking newBooking = new Booking();
        newBooking.setName(name);
        SimpleDateFormat ft = new SimpleDateFormat ("dd/MM-yy 'kl:' HH:mm");
        Date d = new Date(); 
        d.setTime(0);
        try {
            d = ft.parse(sdate);
        } catch (ParseException ex) {
            System.out.println("Error: " + ex.getMessage());
            System.out.println("In BookingServlet.java");
            return;
        }
        try {
            newBooking.setPhone(tel);
            newBooking.setDuration(2);
            newBooking.setStartDate(d.getTime());
            newBooking.setPersons(count);
        } catch(NumberFormatException ex) {
            System.out.println("Number not well formated in input field");
            
            return;
        }
        
        EntityManager em = null;
        try {
            utx.begin();
            // crea em to access database
            em = emf.createEntityManager();
            em.persist(newBooking);
            utx.commit();
        } catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | SecurityException |
                IllegalStateException ex) {
        }
        if(em != null) {
            em.clear();     // forget everything we did
            em.close();     // close the em
        }
        response.sendRedirect(response.encodeRedirectURL("/Server/faces/index.xhtml?page=bord&s=true") );
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
