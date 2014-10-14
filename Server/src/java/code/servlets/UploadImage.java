/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package code.servlets;

import data.Settings;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.pdfbox.io.IOUtils;

/**
 *
 * @author Johannes
 */
@WebServlet(name = "UploadImage", urlPatterns = {"/upload"})
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
                 maxFileSize=1024*1024*10,      // 10MB
                 maxRequestSize=1024*1024*50)   // 50MB
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
         * This code could be used later for detecting multiple uses of same
         * image... if (!pkId.equals("")) {
         *
         * TypedQuery<Event> eventQuery = em.createNamedQuery("Event.findById",
         * Event.class); eventQuery.setParameter("id", Integer.parseInt(pkId));
         * String oldImg = eventQuery.getSingleResult().getImgsrc(); if
         * (!oldImg.equals("") && !oldImg.contains("/")) { eventQuery =
         * em.createNamedQuery("Event.findByImgsrc", Event.class);
         * eventQuery.setParameter("imgsrc", oldImg);
         *
         * //Is this image used by any other event?? if
         * (eventQuery.getResultList().size() > 1) { try { File file = new
         * File(Settings.imagePath, oldImg); file.delete(); } catch
         * (SecurityException ex) { } } } }
         */

        System.out.println("hjsadkasd");
        // Gets file name for HTTP header
        String fileName = request.getHeader("fileName");
        File saveFile = new File(Settings.imagePath);
        if (!saveFile.exists()) {
            System.out.println("creating directory: " + Settings.imagePath);
            boolean result = false;

            try {
                saveFile.mkdir();
                result = true;
            } catch (SecurityException se) {
                return;
            }
            if (result) {
                System.out.println("DIR created");
            }
            else
                return;
        }

        // gets absolute path of the web application
        // constructs path of the directory to save uploaded file
        saveFile = new File(Settings.imagePath + File.separator + fileName);
        System.out.println("Save dir=" + Settings.imagePath);
         
        // creates the save directory if it does not exists
        File fileSaveDir = new File(Settings.imagePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
 
        
        // prints out all header values
        System.out.println("===== Begin headers =====");
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String headerName = names.nextElement();
            System.out.println(headerName + " = " + request.getHeader(headerName));
        }
        System.out.println("===== End headers =====\n");

        // opens input stream of the request for reading data
        for (Part part : request.getParts()) {
            fileName = getFileName(part);
            saveFile = new File(Settings.imagePath + File.separator + fileName);
            // opens an output stream for writing file
            FileOutputStream outputStream = new FileOutputStream(saveFile);
            IOUtils.copy(part.getInputStream(), outputStream);
            part.delete();
            outputStream.close();
            //System.out.println(fileName);
            System.out.println("File written to: " + saveFile.getAbsolutePath());
        }


        // sends response to client
        response.getWriter().print("UPLOAD DONE");
    }
    
    /**
     * Utility method to get file name from HTTP header content-disposition
     */
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        System.out.println("content-disposition header= "+contentDisp);
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length()-1);
            }
        }
        return "";
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
