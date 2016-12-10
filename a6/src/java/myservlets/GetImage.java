/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myservlets;

import static cs359db.db.PhotosDB.getPhotoBlobWithID;
import static cs359db.db.PhotosDB.getPhotoMetadataWithID;
import cs359db.model.Photo;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Blob;
import java.sql.SQLException;

/**
 *
 * @author redho
 */
public class GetImage extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GetImage</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetImage at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        
        String idC  = request.getParameter("id");
        String meta = request.getParameter("metadata"); 
        int id = -1;
        if(idC != null) {
            id = Integer.parseInt(idC);
        }
        else {
            System.err.println("No id parsed in the header for GetImage");
        }
        
        
        // Return metadata JSON object
        if(meta.equals("true")) {
            Photo photo = null;
            
            try {
                photo = getPhotoMetadataWithID(id);
            }catch (ClassNotFoundException ex) {
                System.err.println(ex);
            }
            
            // Respond
            if(photo != null) {
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print("{");
                out.print("userName:\""+photo.getUserName()+"\"");
                out.print(", title:\""+photo.getTitle()+"\"");
                out.print(", date:\""+photo.getDate()+"\"");
                out.print(", contentType:\""+photo.getContentType()+"\"");
                out.print(", numberOfRatings:\""+photo.getNumberOfRatings()+"\"");
                out.print("}");
            }else {
                System.err.println("getPhotoMetadataWithID returned null photo.");
            }
        }
        // Return image blob
        else {
            byte[] blob = null;
            
            try {
                blob = getPhotoBlobWithID(id);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GetImage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.err.println(Arrays.toString(blob));
            
            if(blob != null) {
                PrintWriter out = response.getWriter();
                Blob blobb = null;
                try {
                    blobb = new javax.sql.rowset.serial.SerialBlob(blob);
                } catch (SQLException ex) {
                    Logger.getLogger(GetImage.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(blob);
            }else{
                System.err.println("Could not get blob for id: " + id);
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Returns blob of image or metadata of id image depending on the metadata flag.";
    }// </editor-fold>

}
