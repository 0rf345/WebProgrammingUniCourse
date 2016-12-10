/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myservlets;

import cs359db.db.PhotosDB;
import static cs359db.db.PhotosDB.getPhotoBlobWithID;
import static cs359db.db.PhotosDB.getPhotoMetadataWithID;
import cs359db.db.RatingDB;
import cs359db.model.Photo;
import cs359db.model.Rating;
import java.io.IOException;
import java.io.OutputStream;
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
import java.util.List;

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
                int numOfR = 0;
                int userR  = -1;
                int total  = 0;
                List<Rating> ratings = null;
                try {
                    ratings = RatingDB.getRatings(id);
                    numOfR = ratings.size();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GetImage.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(ratings != null) {
                    for(int i = 0; i < ratings.size(); i++) {
                        if(ratings.get(i).getUserName().equals(photo.getUserName())) {
                            userR = ratings.get(i).getRate();
                        }
                        total += ratings.get(i).getRate();
                    }
                }
                
                out.print(", numberOfRatings:\""+numOfR+"\"");
                if(userR == -1) {
                    out.print(", userRated:\"unrated\"");
                }else{
                    out.print(", userRated:\""+userR+"\"");
                }
                
                if(numOfR != 0) {
                    out.print(", averageScore:\""+(total/numOfR)+"\"");
                }
                
                out.print("}");
            }else {
                System.err.println("getPhotoMetadataWithID returned null photo.");
            }
        }
        // Return image blob
        else {
            byte[] imgData = null;
            
            try {
                imgData = PhotosDB.getPhotoBlobWithID(id);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GetImage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.err.println(Arrays.toString(imgData));
            
            if(imgData != null) {
                /*
                PrintWriter out = response.getWriter();
                Blob blobb = null;
                try {
                    blobb = new javax.sql.rowset.serial.SerialBlob(blob);
                } catch (SQLException ex) {
                    Logger.getLogger(GetImage.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(blob);
                */
                response.setContentType("image/jpg");
                try (OutputStream os = response.getOutputStream()) {
                    os.write(imgData);
                    os.flush();
                }catch (Exception ex) {
                    System.err.println("Could not get outputstream.");
                }
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
