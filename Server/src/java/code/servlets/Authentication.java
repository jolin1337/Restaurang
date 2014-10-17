/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.servlets;

import data.Settings;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.Key;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author ulf
 */
@WebServlet(name = "Authentication", urlPatterns = {"/login"}, initParams = {
    @WebInitParam(name = "key", value = "")})
public class Authentication extends HttpServlet {

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
            String k = request.getParameter("key");
            if(k == null) {
                System.out.println("djfhdsjkfhsdjkfhdsjkfh");
                return;
            }
            for (String key : Settings.allowedKeys) {
                if (k.equals(key)) {
                    String responseKey = Settings.tempPrefixKey + Long.toString(new Date().getTime());

                    try {
                        // Create key and cipher
                        Key aesKey = new SecretKeySpec(Settings.cryptionValue.getBytes(), "AES");
                        Cipher cipher;
                        cipher = Cipher.getInstance("AES");
                        // encrypt the text
                        cipher.init(Cipher.ENCRYPT_MODE, aesKey);

                        String encrypted = Settings.asHex(cipher.doFinal(responseKey.getBytes()));

                        out.println(encrypted);
                    } catch (Exception ex) {
                        out.print("error");
                    }
                    return;
                }
            }
            out.print("Wrong key");
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
