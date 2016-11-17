/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myservlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *
 * @author redho
 */
public class NewServlet extends HttpServlet {

    
    
    Map<String, HashMap<String, String>> users = new HashMap<>();
    Map<String, String> emails = new HashMap<>();
    /**
     * 
     */
    @Override
    public void init() {
        String a = "n0b0d1js";
        users.put(a, new HashMap());
        users.get(a).put("usern", a);
        users.get(a).put("userp", "ta@@mouta2");
        emails.put("n0b0d1@tolabaki.gr", a);
    }
    
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
            out.print("<!DOCTYPE html>");
            out.print("<html>");
            out.print("<head>");
            out.print("<title>Servlet NewServlet</title>");            
            out.print("</head>");
            out.print("<body>");
            out.print("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
            out.print("</body>");
            out.print("</html>");
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
     * For login:
     * request 
     * login: "1"
     * usern: usern
     * userp: userp
     * 
     * response
     * success: success
     * 
     * 
     * For register:
     * request
     * login:   "0"
     * usern:   usern   *
     * userp:   userp   *
     * email:   email   *
     * fname:   fname   *
     * lname:   lname   *
     * date:    date    * 
     * sex:     sex
     * country: country *
     * town:    town    *
     * extra:   extra
     * 
     * response
     * success: success
     * 
     * 
     * For username lookup:
     * request
     * login: "2"
     * usern: usern
     * 
     * response
     * success: found
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*
        response.setContentType("text/html");
        PrintWriter o = response.getWriter();
        o.print("<html><head><title>Echo Request\n</title></head><body>");
        o.print("HTTP Method:"+request.getMethod());
        o.print("<br>URL"+request.getRequestURL().toString());
        String usern = request.getParameter("usern");
        String userp = request.getParameter("userp");
        
        
        o.print("<br>Username: "+usern);
        o.print("<br>Password: "+userp);
        */
        PrintWriter o = response.getWriter();
        String success;
        
        
        // Email lookup
        if(request.getParameter("login").equals("3")) {
            response.setContentType("charset=UTF-8");
            if(emails.containsKey(request.getParameter("email"))) {
                success = "0";
            }else{
                success = "1";
            }
            o.print(success);
        // Username lookup
        }else if(request.getParameter("login").equals("2")) {
            response.setContentType("charset=UTF-8");
            if(users.containsKey(request.getParameter("usern"))) {
                success = "0";
            }else{
                success = "1";
            }
            o.print(success);
        // Registration
        }else if(request.getParameter("login").equals("0")) {
            String usern    = request.getParameter("usern");
            String userp    = request.getParameter("userp");
            String email    = request.getParameter("email");
            String fname    = request.getParameter("fname");
            String lname    = request.getParameter("lname");
            String date     = request.getParameter("date");
            String sex      = request.getParameter("sex");
            String country  = request.getParameter("country");
            String town     = request.getParameter("town");
            String extra    = request.getParameter("extra");

            users.put(usern, new HashMap());
            users.get(usern).put("usern", usern);
            users.get(usern).put("userp", userp);
            users.get(usern).put("email", email);
            users.get(usern).put("fname", fname);
            users.get(usern).put("lname", lname);
            users.get(usern).put("date", date);
            if(sex != null) users.get(usern).put("sex", sex);
            users.get(usern).put("country", country);
            users.get(usern).put("town", town);
            if(extra != null) users.get(usern).put("extra", extra);
            
            emails.put(email, usern);
            
            response.setContentType("text/html");
            o.print("<p>You have successfully registered. You may now log in.</p>");
            o.print("<p>Your info is as follows</p>");
            o.print("<p>Username: "+usern+"</p>");
            o.print("<p>E-mail: "+email+"</p>");
            o.print("<p>First name: "+fname+"</p>");
            o.print("<p>Last name: "+lname+"</p>");
            o.print("<p>Date of birth: "+date+"</p>");
            if(sex != null) o.print("<p>Sex: "+sex+"</p>");
            o.print("<p>Country: "+country+"</p>");
            o.print("<p>Town: "+town+"</p>");
            if(extra != null) o.print("<p>Extra: "+extra+"</p>");
            o.print("<br>");
            
        
        // Login usern userp
        }else if(request.getParameter("login").equals("1")){
            String usern = request.getParameter("usern");
            String userp = request.getParameter("userp");
            if(users.containsKey(usern)) {
                if(users.get(usern).get("userp").equals(userp)) {
                    success = "1";
                }else{
                    success = "2";
                }
            }else{
                success = "3";
            }
            o.print(success);
        // Show users;
        }else if(request.getParameter("login").equals("4")){
            o.print("<style>");
            o.print("</style>");
            o.print("<ul>");
            for ( String key : users.keySet() ) {
                o.print("<li><a href=\"#\">"+key+"</a></li>");
            }
            o.print("</ul>");
        // Troubleshooting
        }else{
            response.setContentType("text/html"); 
            o.print("<html><head><title>Echo Request\n</title></head><body>");
            o.print("HTTP Method:"+request.getMethod());
            o.print("<br>URL"+request.getRequestURL().toString());
        }
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Supposedly will hold the user info, check creds etc";
    }// </editor-fold>

}
