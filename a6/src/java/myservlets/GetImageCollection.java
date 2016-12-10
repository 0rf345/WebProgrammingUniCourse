/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myservlets;

import cs359db.db.PhotosDB;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;

/**
 *
 * @author redho
 */
public class GetImageCollection extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * 
     * Returns the latest images for a specific user or from all the users
     * depending on the header
     * 
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
            out.println("<title>Servlet GetImageCollection</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetImageCollection at " + request.getContextPath() + "</h1>");
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
        String JSONobj = "";
        List<Integer> ids = new ArrayList();
        String user = request.getParameter("user");
        String maxC = request.getParameter("number");
        HttpSession session = request.getSession();
        
        int number = 10;
        if(maxC != null) {
            number = Integer.parseInt(maxC);
        }else if(session.getAttribute("number") != null) {
            number = Integer.parseInt(session.getAttribute("number").toString());
        }
        
        // Get the IDs of latest user photos, or overall photos
        try{
            if(user != null) {
                ids = PhotosDB.getPhotoIDs(number, user);
            }else{
                ids = PhotosDB.getPhotoIDs(number);
            }
        }catch(Exception ex) {
            System.err.println("Couldn't get photos at GetImageCollection.");
        }
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("[");
        out.print("\""+ids.get(0)+"\"");
        for(int i = 1; i < ids.size(); i++) {
            out.print(", \""+ids.get(i)+"\"");
        }
        out.print("]");
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
