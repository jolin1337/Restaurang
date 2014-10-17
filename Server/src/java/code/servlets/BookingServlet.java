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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
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
        String syear = request.getParameter("syear");
        String smonth = request.getParameter("smonth");
        String sday = request.getParameter("sday");
        String shour = request.getParameter("shour");
        String smin = request.getParameter("smin");
        String sdate = getDateString(syear, smonth, sday, shour, smin);
        
        int count;
        try {
            count = Integer.parseInt(request.getParameter("count"));
        }
        catch(NumberFormatException ex) {
            count = -1;
        }
        
        try {
            utx.begin();
            EntityManager em = emf.createEntityManager();
            //This commented code is for checking the total amount of persons booked in this period...
            // Getting error: java.lang.IllegalArgumentException, Syntax error parsing [SELECT SUM(b) FROM Booking b]. 
            Query query = em.createQuery("SELECT count(b.id) FROM Booking b");
            Long sum = (Long)query.getSingleResult();
            if(name.isEmpty() || tel.isEmpty() || sdate.isEmpty() || sdate.length() != "20141017:0130".length()
                    || count <= 0 || count+sum > 6){
                    response.sendRedirect(response.encodeRedirectURL("/Server/?page=bord&s=false") );
                    return;
            }
            Booking newBooking = new Booking();
            newBooking.setName(name);
            SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd:HHmm");
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
                newBooking.setStartDate(Long.toString(d.getTime()));
                newBooking.setPersons(count);
            } catch(NumberFormatException ex) {
                System.out.println("Number not well formated in input field");

                return;
            }

            em.persist(newBooking);
            utx.commit();
        
            if(em != null) {
                em.clear();     // forget everything we did
                em.close();     // close the em
            }
        } catch(NotSupportedException | SystemException | RollbackException | 
            HeuristicMixedException | HeuristicRollbackException | SecurityException |
            IllegalStateException ex) {
        }
        response.sendRedirect(response.encodeRedirectURL("/Server/?page=bord&s=true") );
    }

    private String getDateString(String... dates) {
        for(int i = 1; i < 5; i++) 
            if(dates[i].length() != 2)
                dates[i] = "0" + dates[i];
        return "20" + dates[0] + dates[1] + dates[2] + ":" + dates[3] + dates[4];
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
