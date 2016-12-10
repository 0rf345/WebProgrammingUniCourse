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
import cs359db.model.User.Gender;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author redho
 */
public class NewServlet extends HttpServlet {

    String epochString = "1081157732";
    long epoch = Long.parseLong( epochString );
    Map<String,String> loggedIn;
    Map<String,Date>   track;
    
    public NewServlet() throws ClassNotFoundException {
        this.loggedIn = new HashMap<>();
        this.track = new HashMap<>();
        List<User> users = UserDB.getUsers();
        for(int i = 0; i < users.size(); i++) {
            loggedIn.put(users.get(i).getUserName(), "0");
            track.put(users.get(i).getUserName(), new Date(epoch * 1000));
        }
        
    }

    
    /**
     * Returns a String which is the MD5 hash of a
     * @param a
     * @return 
     */
    public String hashMD5(String a) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(a.getBytes());
            byte byteData[] = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }catch(NoSuchAlgorithmException e) {
            System.err.println("For whatever reason MD5 failed: " + e.toString());
            return null;
        }
        
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
     * 
     * For email lookup:
     * request
     * login: "3"
     * email: email
     * 
     * response
     * success: found
     * 
     * 
     * For show users:
     * request
     * login: "4"
     * 
     * response
     * A page with a list of all registered users
     * 
     * 
     * For show user info
     * request
     * login: "5"
     * 
     * response
     * A page with all the logged in user's info and the ability to change them
     * 
     * 
     * For save user info
     * request
     * login: "6"
     * usern:   usern   
     * userp:   userp   
     * email:   email   
     * fname:   fname   
     * lname:   lname   
     * date:    date     
     * sex:     sex
     * country: country 
     * town:    town    
     * extra:   extra
     * 
     * response
     * Saves the non "" data in the User Database
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
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        // Registration
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
            try {
                User usr = UserDB.getUser(session.getAttribute("usern").toString());
                String l = "<label>";
                String p = "<p>";
                String el = "</label>";
                String ep = "</p>";
                o.print(p+"<h1>Anything you don't wish to change, simply change nothing on it.</h1>"+ep);
                o.print(p+l+"Username"+el+"<input id='usern' placeholder='"+
                        usr.getUserName()+"' pattern='.{8,}' title='8 or more latin characters'>"+ep);
                o.print(p+l+"Email"+el+
                        "<input id='email' type='email' placeholder='"+usr.getEmail()+
                        "'pattern='([a-zA-Z0-9]{1,}(\\.*[a-zA-Z0-9]){0,}@[a-zA-Z0-9]{1,}(\\.{1}[a-zA-Z0-9]{1,}){1,})' "
                                + "title='text(@)text(.)text Can have more (.)'>"+ep);
                o.print(p+l+"New Password"+el+"<input type='password' id='userp' placeholder='New password'"+
                        "pattern='(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?\\x26])[A-Za-z\\d$@$!%*#?\\x26]{6,10}$' "
                        + "title='6-10 characters, must contain 1 latin character, a number and a special symbol'>"+
                        "<button id='toggler' onclick='togglePassword();'>Show</button>" +ep);
                o.print(p+l+"First Name"+el+"<input id='fname' value='"+usr.getFirstName()+
                        "' pattern='[a-zA-Z]{3,20}' title='3-20 latin characters'>"+ep);
                o.print(p+l+"Last Name"+el+"<input id='lname' value='"+usr.getLastName()+
                        "' pattern='[a-zA-Z]{4,20}' title='4-20 latin characters'>"+ep);
                o.print(p+l+"Birthday"+el+"<input type='date' id='bdate' value='"+
                        usr.getBirthDate()+"' max='2001-01-01'>"+ep);
                String tmpN = "";
                String tmpF = "";
                String tmpM = "";
                if(usr.getGender().equals(Gender.FEMALE)) tmpF = " checked='checked' ";
                else if(usr.getGender().equals(Gender.MALE)) tmpM = " checked='checked' ";
                else tmpN =  "checked='checked'";
                o.print(p+"Not applicable<input type='radio' name='sex' value='NotApplicable'"+tmpN+"/>"+
                        "\tMale<input type='radio' name='sex' value='Male'" +tmpM+">"+
                        "\tFemale<input type='radio' name='sex' value='Female' "+tmpF+">"+ep);
                o.print("<script>populateCountries('#logged');"
                        + "populateButton();</script>");
                o.print(ep);
                o.print(p+l+"Town"+el+"<input id='town' placeholder='"+usr.getTown()+
                        "' pattern='.{2,50}' title='Must be 2-50 characters long' />"+ep);
                o.print(p+l+"Extra Info"+el+"<textarea rows='10' cols='50' id='extraInfo' "+
                        "placeholder='"+usr.getInfo()+"' maxlength=500 title='Up to 500 characters'></textarea>"+ep);
                
                o.print("<script>$(\"#usern\").on(\"change\", validateUsername);\n" +
"               $(\"#email\").on(\"change\", validateEmail);\n" +
"               $(\"#town\").on(\"change\", validateTown);\n" +
"               $(\"#extraInfo\").on(\"change\", validateExtra);</script>");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
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
                    this.loggedIn.put(usern, "1");
                    this.track.put(usern, new Date(1000 * session.getLastAccessedTime()));
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
                String tmp = "";
                
                for ( User a : userList ) {
                    if(this.loggedIn.get((a.getUserName())).equals("1")) {
                        tmp = "Logged in right now.";
                    }else{
                        tmp = "Last logged in: " + this.track.get(a.getUserName());
                    }
                    String mpla = a.getUserName();
                    String thisSucks = "";
                    thisSucks += "<div onclick=\"showThesePhotos('";
                    thisSucks += a.getUserName();
                    thisSucks += "');\" >";
                    o.print(thisSucks);
                    //o.print("<div onclick=\"showThesePhotos(\""+"\");\" >");
                    o.print("<li><a href=\"#\">"+a.getUserName()+"</a>+"+tmp+"</li>");
                    o.print("</div>");
                }
                o.print("</ul>");
            }
        // Save User Info Changes
        }else if(request.getParameter("login").equals("6")){
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
            
            try {
                User tmp = UserDB.getUser(session.getAttribute("usern").toString());
                  
                if(usern != null) tmp.setUserName(usern);
                if(userp != null) tmp.setPassword(hashMD5(userp));
                if(email != null) tmp.setEmail(email);
                if(fname != null) tmp.setFirstName(fname);
                if(lname != null) tmp.setLastName(lname);
                if(date != null) tmp.setBirthDate(date);
                if(sex != null) tmp.setGender(sex);
                if(country != null) tmp.setCountry(country);
                if(town != null) tmp.setTown(town);
                if(extra != null) tmp.setInfo(extra);
                
                UserDB.updateUser(tmp);
                o.print("<p><h1>Your Info has been updated successfully</h1></p>");
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        // Troubleshooting
        }else if(request.getParameter("login").equals("7")){
            this.loggedIn.put(session.getAttribute("usern").toString(), "0");
            this.track.put(session.getAttribute("usern").toString(), new Date(1000 * session.getLastAccessedTime()));
            session.invalidate();
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
