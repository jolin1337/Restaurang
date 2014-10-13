/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package code.servlets;

import data.Settings;
import data.entity.Dish;
import data.entity.Event;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Johannes
 */
@WebServlet(name = "UploadImage", urlPatterns = {"/upload"})
public class UploadImage extends HttpServlet {

    final int BUFFER_SIZE = 4096;

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

        String pkId = request.getParameter("eventId");
        /**
         * This code could be used later for detecting multiple uses of same image...
        if (!pkId.equals("")) {

            TypedQuery<Event> eventQuery = em.createNamedQuery("Event.findById", Event.class);
            eventQuery.setParameter("id", Integer.parseInt(pkId));
            String oldImg = eventQuery.getSingleResult().getImgsrc();
            if (!oldImg.equals("") && !oldImg.contains("/")) {
                eventQuery = em.createNamedQuery("Event.findByImgsrc", Event.class);
                eventQuery.setParameter("imgsrc", oldImg);

                //Is this image used by any other event??
                if (eventQuery.getResultList().size() > 1) {
                    try {
                        File file = new File(Settings.imagePath, oldImg);
                        file.delete();
                    } catch (SecurityException ex) {
                    }
                }
            }
        }
        */

        // Gets file name for HTTP header
        String fileName = request.getHeader("fileName");
        File saveFile = new File(Settings.imagePath + fileName);

        // prints out all header values
        System.out.println("===== Begin headers =====");
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String headerName = names.nextElement();
            System.out.println(headerName + " = " + request.getHeader(headerName));
        }
        System.out.println("===== End headers =====\n");

        // opens input stream of the request for reading data
        InputStream inputStream = request.getInputStream();

        // opens an output stream for writing file
        FileOutputStream outputStream = new FileOutputStream(saveFile);

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        System.out.println("Receiving data...");

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        System.out.println("Data received.");
        outputStream.close();
        inputStream.close();

        System.out.println("File written to: " + saveFile.getAbsolutePath());

        // sends response to client
        response.getWriter().print("UPLOAD DONE");
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
        //processRequest(request, response);
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
