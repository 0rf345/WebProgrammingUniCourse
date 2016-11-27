/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs359db.db;

import cs359db.model.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author papadako
 */
public class UserDB {

    /**
     * Get user
     *
     * @param userName
     * @return
     * @throws ClassNotFoundException
     */
    public static List<User> getUsers() throws ClassNotFoundException {
        List<User> users = new ArrayList<>();

        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("SELECT * FROM user;");

                stmt.execute(insQuery.toString());

                ResultSet res = stmt.getResultSet();

                while (res.next() == true) {
                    User user = new User();
                    user.setUserName(res.getString("userName"));
                    user.setEmail(res.getString("email"));
                    user.setPassword(res.getString("password"));
                    user.setFirstName(res.getString("firstName"));
                    user.setLastName(res.getString("lastName"));
                    user.setGender(res.getString("gender"));
                    user.setBirthDate(res.getString("birthDate"));
                    user.setCountry(res.getString("country"));
                    user.setTown(res.getString("town"));
                    user.setInfo(res.getString("additional"));
                    users.add(user);
                }

                // Close connection
                stmt.close();
                con.close();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return users;
    }

    /**
     * Get user
     *
     * @param userName
     * @return
     * @throws ClassNotFoundException
     */
    public static User getUser(String userName) throws ClassNotFoundException {
        User user = new User();
        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("SELECT * FROM user ")
                        .append(" WHERE ")
                        .append(" USERNAME = ").append("'").append(userName).append("';");

                stmt.execute(insQuery.toString());

                ResultSet res = stmt.getResultSet();

                if (res.next() == true) {
                    user.setUserName(res.getString("userName"));
                    user.setEmail(res.getString("email"));
                    user.setPassword(res.getString("password"));
                    user.setFirstName(res.getString("firstName"));
                    user.setLastName(res.getString("lastName"));
                    user.setGender(res.getString("gender"));
                    user.setBirthDate(res.getString("birthDate"));
                    user.setCountry(res.getString("country"));
                    user.setTown(res.getString("town"));
                    user.setInfo(res.getString("additional"));
                }

                // Close connection
                stmt.close();
                con.close();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return user;
    }

    /**
     * Establish a database connection and add the member in the database.
     *
     * @param user
     * @throws ClassNotFoundException
     */
    public static void addUser(User user) throws ClassNotFoundException {
        // Check that we have all we need
        try {
            user.checkFields();

        } catch (Exception ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("INSERT INTO ")
                        .append(" user (USERNAME, EMAIL, PASSWORD, FIRSTNAME, LASTNAME, BIRTHDATE, GENDER, COUNTRY, TOWN, ADDITIONAL) ")
                        .append(" VALUES (")
                        .append("'").append(user.getUserName()).append("',")
                        .append("'").append(user.getEmail()).append("',")
                        .append("'").append(user.getPassword()).append("',")
                        .append("'").append(user.getFirstName()).append("',")
                        .append("'").append(user.getLastName()).append("',")
                        .append("'").append(user.getBirthDate()).append("',")
                        .append("'").append(user.getGender()).append("',")
                        .append("'").append(user.getCountry()).append("',")
                        .append("'").append(user.getTown()).append("',")
                        .append("'").append(user.getInfo()).append("');");

                stmt.executeUpdate(insQuery.toString());
                System.out.println("#DB: The member was successfully added in the database.");

                // Close connection
                stmt.close();
                con.close();

            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Updates information for specific user
     *
     * @param user
     * @throws ClassNotFoundException
     */
    public static void updateUser(User user) throws ClassNotFoundException {
        // Check that we have all we need
        try {
            user.checkFields();

        } catch (Exception ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("UPDATE user ")
                        .append(" SET ")
                        .append(" EMAIL = ").append("'").append(user.getEmail()).append("',")
                        .append(" PASSWORD = ").append("'").append(user.getPassword()).append("',")
                        .append(" FIRSTNAME = ").append("'").append(user.getFirstName()).append("',")
                        .append(" LASTNAME = ").append("'").append(user.getLastName()).append("',")
                        .append(" BIRTHDATE = ").append("'").append(user.getBirthDate()).append("',")
                        .append(" GENDER = ").append("'").append(user.getGender()).append("',")
                        .append(" COUNTRY = ").append("'").append(user.getCountry()).append("',")
                        .append(" TOWN = ").append("'").append(user.getTown()).append("',")
                        .append(" ADDITIONAL = ").append("'").append(user.getInfo()).append("'")
                        .append(" WHERE USERNAME = ").append("'").append(user.getUserName()).append("';");

                stmt.executeUpdate(insQuery.toString());
                System.out.println("#DB: The member was successfully updated in the database.");

                // Close connection
                stmt.close();
                con.close();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Delete information for specific user
     *
     * @param user
     * @throws ClassNotFoundException
     */
    public static void deleteUser(User user) throws ClassNotFoundException {

        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("DELETE FROM user ")
                        .append(" WHERE ")
                        .append(" USERNAME = ").append("'").append(user.getUserName()).append("';");

                stmt.executeUpdate(insQuery.toString());
                System.out.println("#DB: The member was successfully deleted from the database.");

                // Close connection
                stmt.close();
                con.close();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Delete information for specific user
     *
     * @param userName
     * @param email
     * @throws ClassNotFoundException
     */
    public static void deleteUser(String userName) throws ClassNotFoundException {

        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("DELETE FROM user ")
                        .append(" WHERE ")
                        .append(" USERNAME = ").append("'").append(userName).append("';");

                stmt.executeUpdate(insQuery.toString());
                System.out.println("#DB: The member was successfully deleted from the database.");

                // Close connection
                stmt.close();
                con.close();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean checkValidUserName(String userName) throws ClassNotFoundException {
        boolean valid = true;
        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("SELECT * FROM user ")
                        .append(" WHERE ")
                        .append(" USERNAME = ").append("'").append(userName).append("';");

                System.out.println(insQuery.toString());

                stmt.execute(insQuery.toString());

                if (stmt.getResultSet().next() == true) {
                    System.out.println("#DB: The member already exists");
                    valid = false;
                }

                // Close connection
                stmt.close();
                con.close();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return valid;
    }

    public static boolean checkValidEmail(String email) throws ClassNotFoundException {
        boolean valid = true;
        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("SELECT * FROM user ")
                        .append(" WHERE ")
                        .append(" EMAIL = ").append("'").append(email).append("';");

                stmt.execute(insQuery.toString());
                if (stmt.getResultSet().next() == true) {
                    System.out.println("#DB: The member alreadyExists");
                    valid = false;
                }

                // Close connection
                stmt.close();
                con.close();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return valid;
    }
}
