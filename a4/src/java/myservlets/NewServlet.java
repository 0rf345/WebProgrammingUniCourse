/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myservlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

import cs359db.db.UserDB;
import cs359db.model.User;

import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author redho
 */
public class NewServlet extends HttpServlet {

    
    
    Map<String, HashMap<String, String>> users = new HashMap<>();
    Map<String, String> emails = new HashMap<>();
    String user;
    
    public String hashMD5(String a) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(a.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }catch(Exception e) {
            System.err.println("For whatever reason MD5 failed: " + e.toString());
            return null;
        }
        
    }
 
    /**
     * 
     */
    @Override
    public void init() {
        /*
        try {
            
            // To be populated if need be
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Default user could not be created");
        }
        */
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
        
        PrintWriter o = response.getWriter();
        String success;
        
        HttpSession session = request.getSession();
        
        
        // Email lookup
        if(request.getParameter("login").equals("3")) {
            try {
                response.setContentType("charset=UTF-8");
                if(UserDB.checkValidEmail(request.getParameter("email"))) {
                    success = "1";
                }else{
                    success = "0";
                }
                o.print(success);
                // Username lookup
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        // Username lookup
        }else if(request.getParameter("login").equals("2")) {
            try {
                response.setContentType("charset=UTF-8");
                if(UserDB.checkValidUserName(request.getParameter("usern"))) {
                    success = "1";
                }else{
                    success = "0";
                }
                o.print(success);
                // Registration
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(request.getParameter("login").equals("0")) {
            String usern    = request.getParameter("usern");
            String userp    = hashMD5(request.getParameter("userp"));
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
            
            
            User registration = new User(usern, email, userp, fname, lname, date, country, town);
            if(sex != null) registration.setGender(sex);
            if(extra != null) registration.setInfo(extra);
            
            try {
                UserDB.addUser(registration);
                System.out.println("User successfully added to DB: " + UserDB.getUser(usern));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("Does this work?");
            }
            
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
        
        // Show logged in user's info and let them edit
        }else if(request.getParameter("login").equals("5")){
            
            for(String key : users.get(session.getAttribute("usern")).keySet()) {
                String val = users.get(session.getAttribute("usern")).get(key);
                o.print("<label>"+key+"</label><input type='text' id='"+key+"' value='"+val+"'"+
                        "/><br>");
            }
            o.print("<input type='Submit' id='save' value='Save Changes' onclick='save();'>");
            
            
        // Login usern userp
        }else if(request.getParameter("login").equals("1")){
            String usern = request.getParameter("usern");
            String userp = request.getParameter("userp");
            User a;
            try{
                a = UserDB.getUser(usern);
                if(a.getUserName().equals("")) {
                    success = "3";
                } else if(a.getPassword().equals(hashMD5(userp))) {
                    success = "1";
                    session.setAttribute("usern", usern);
                }else{
                    success = "2";
                }
                o.print(success);
            }catch(ClassNotFoundException e) {
                System.err.println("Could not get user: " + e.toString());
            }
        // Show users;
        }else if(request.getParameter("login").equals("4")){
            List<User> userList = null;
            try {
                userList = UserDB.getUsers();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(userList == null) {
                System.err.println("The list is somehow not populated.");
            }else{
                
                o.print("<style>");
                o.print("</style>");
                o.print("<ul>");

                for ( User a : userList ) {
                    o.print("<li><a href=\"#\">"+a.getUserName()+"</a></li>");
                }
                o.print("</ul>");
            }
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
