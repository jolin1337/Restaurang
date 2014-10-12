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
@WebServlet(name = "UploadImage", urlPatterns = {"/upload"}, initParams = {
    @WebInitParam(name = "key", value = ""),
    @WebInitParam(name = "oldImgSrc", value = "")})
@MultipartConfig
public class UploadImage extends HttpServlet {

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
            if(Settings.isAutorised(request.getParameter("key")) == Settings.AuthCode.accept) {
                
                String pkId = request.getParameter("eventId");
                if(!pkId.equals("")) {

                    TypedQuery<Event> eventQuery = em.createNamedQuery("Event.findById", Event.class);
                    eventQuery.setParameter("id", Integer.parseInt(pkId));
                    String oldImg = eventQuery.getSingleResult().getImgsrc();
                    if(!oldImg.equals("") && !oldImg.contains("/")) {
                        eventQuery = em.createNamedQuery("Event.findByImgsrc", Event.class);
                        eventQuery.setParameter("imgsrc", oldImg);
                        
                        //Is this image used by any other event??
                        if(eventQuery.getResultList().size() > 1) {
                            try {
                                File file = new File(Settings.imagePath, oldImg);
                                file.delete();
                            } catch(SecurityException ex) {}
                        }
                    }
                }

                // Create path components to save the file
                final String path = Settings.imagePath;
                final Part filePart = request.getPart("file");
                final String fileName = getFileName(filePart);

                OutputStream imgOut;
                InputStream filecontent = null;
                final PrintWriter writer = response.getWriter();

                try {
                    imgOut = new FileOutputStream(new File(path
                            , fileName));
                    filecontent = filePart.getInputStream();

                    int read = 0;
                    final byte[] bytes = new byte[1024];

                    while ((read = filecontent.read(bytes)) != -1) {
                        imgOut.write(bytes, 0, read);
                    }
                    writer.println("New file " + fileName + " created at " + path);
                    System.out.println("File " + fileName + " being uploaded to " + path);
                } catch (FileNotFoundException fne) {
                    writer.println("You either did not specify a file to upload or are "
                            + "trying to upload a file to a protected or nonexistent "
                            + "location.");
                    writer.println("<br/> ERROR: " + fne.getMessage());

                    System.out.println("Problems during file upload. Error: " + fne.getMessage());
                } finally {
                    if (out != null) {
                        out.close();
                    }
                    if (filecontent != null) {
                        filecontent.close();
                    }
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
        }
    }
    
    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        System.out.println("Part Header = " + partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
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
