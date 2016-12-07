/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs359db.db;

import cs359db.model.Photo;
import cs359db.model.User;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author papadako
 */
public class PhotosDB {
    /**
     * Get photo IDs
     *
     * @param number Number of photos to get
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Integer> getPhotoIDs(int number) throws ClassNotFoundException {
        List<Integer> photos = new ArrayList<>();

        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder insQuery = new StringBuilder();

                // Order by timestamp and limit to number
                insQuery.append("SELECT photoID FROM photos ")
                        .append("ORDER BY date DESC ")
                        .append("LIMIT ").append(number).append(";");

                stmt.execute(insQuery.toString());

                ResultSet res = stmt.getResultSet();

                while (res.next() == true) {
                    photos.add(res.getInt("photoID"));
                }

                // Close connection
                stmt.close();
                con.close();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return photos;
    }

    /**
     * Get photo IDs
     *
     * @param number Number of photos to get (if 0 no limit)
     * @param user
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Integer> getPhotoIDs(int number, String user)
            throws ClassNotFoundException {
        List<Integer> photos = new ArrayList<>();

        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder insQuery = new StringBuilder();

                // If we have set a limit then 
                if (number != 0) {
                // Order by timestamp and limit to number
                insQuery.append("SELECT photoID FROM photos ")
                        .append("WHERE userName='").append(user).append("' ")
                        .append("ORDER BY date DESC ")
                        .append("LIMIT ").append(number).append(";");
                } else { // Else return all images
                    // Order by timestamp and limit to number
                    insQuery.append("SELECT photoID FROM photos ")
                            .append("WHERE userName='").append(user).append("' ")
                            .append("ORDER BY date DESC;");
                }
                stmt.execute(insQuery.toString());

                ResultSet res = stmt.getResultSet();

                while (res.next() == true) {
                    photos.add(res.getInt("photoID"));
                }

                // Close connection
                stmt.close();
                con.close();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return photos;
    }

    /**
     * Get specific photo blob data
     *
     * @param id the id of the photo
     * @return
     * @throws ClassNotFoundException
     */
    public static byte[] getPhotoBlobWithID(int id)
            throws ClassNotFoundException {

        byte[] data = null;
        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                ResultSet rs = stmt.executeQuery("SELECT `blob` from photos where photoID = "
                        + id + ";");

                if (rs.next()) {
                    Blob blob = rs.getBlob("blob");

                    int blobLength = (int) blob.length();
                    data = blob.getBytes(1, blobLength);

                    // release the blob and free the memory
                    blob.free();

                    /*
                    This is what you should do for the response
                    response.setContentType("image/jpg");   // Use the appropriate type from 
                    OutputStream os = response.getOutputStream(); // output with the help of outputStream
                    os.write(imgData);
                    os.flush();
                    os.close(); */

                }

                // Close connection
                stmt.close();
                con.close();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

    /**
     * Get specific photo metadata. Does not return the bytes of the photo
     *
     * @param id the id of the photo
     * @return
     * @throws ClassNotFoundException
     */
    public static Photo getPhotoMetadataWithID(int id)
            throws ClassNotFoundException {
        Photo photo = null;

        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("SELECT userName, date, title, numberRatings, contentType FROM photos ")
                        .append("WHERE photoID = ").append(id).append(";");

                stmt.execute(insQuery.toString());

                ResultSet res = stmt.getResultSet();

                if (res.next() == true) {
                    photo = new Photo();
                    photo.setUserName(res.getString("userName"));
                    photo.setDate(res.getString("date"));
                    photo.setTitle(res.getString("title"));
                    photo.setNumberOfRatings(res.getInt("numberRatings"));
                    photo.setContentType(res.getString("contentType"));
                }

                // Close connection
                stmt.close();
                con.close();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return photo;

    }

    /**
     * Delete specific photo
     *
     * @param photoID
     * @throws ClassNotFoundException
     */
    public static void deletePhoto(int photoID) throws ClassNotFoundException {

        try {
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("DELETE FROM photos ")
                        .append(" WHERE ")
                        .append(" photoID = ").append("'").append(photoID).append("';");

                stmt.executeUpdate(insQuery.toString());
                System.out.println("#DB: The photo was successfully deleted from the database.");

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
     * Upload photo
     *
     * @param input photo to upload
     * @param userName
     * @param contentType
     * @throws ClassNotFoundException
     */
    public static int uploadPhoto(InputStream input, String userName, String contentType)
            throws ClassNotFoundException, Exception {
        return uploadPhoto(input, userName, contentType, "Untitled");
    }
    /**
     * Upload photo
     *
     * @param input photo to upload
     * @param userName
     * @param contentType
     * @param title
     * @throws ClassNotFoundException
     */
    public static int uploadPhoto(InputStream input, String userName,
            String contentType, String title)
            throws ClassNotFoundException, Exception {
        int maxID = -1;
        try {
            // If inputStream is null throw exception
            if (input == null) {
                throw new Exception("Input is null for photo!");
            }
            try (Connection con = CS359DB.getConnection();
                    Statement stmt = con.createStatement()) {

                StringBuilder maxIDQ = new StringBuilder();
                maxIDQ.append("SELECT MAX(photoID) as maxid FROM photos;");

                stmt.execute(maxIDQ.toString());
                ResultSet res = stmt.getResultSet();
                maxID = 0;
                if (res.next()) {
                    maxID = res.getInt("maxid");
                }
                maxID++;    // Increment maxID


                StringBuilder insQuery = new StringBuilder();

                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());

                // constructs SQL statement
                insQuery.append("INSERT INTO photos ")
                        //.append("photoID, userName, date, blob, title, numberRatings, contentType) ")
                        .append("values (?, ?, ?, ?, ?, ?, ?);");
                PreparedStatement statement = con.prepareStatement(insQuery.toString());
                statement.setInt(1, maxID);
                statement.setString(2, userName);
                statement.setTimestamp(3, timestamp);

                // fetches input stream of the upload file for the blob column
                statement.setBlob(4, input);
                // set title to untitle
                statement.setString(5, title);
                // set rating
                statement.setInt(6, 0);
                // set contenttype
                statement.setString(7, contentType);
                System.out.println(statement.toString());
                // sends the statement to the database server
                int row = statement.executeUpdate();
                if (row > 0) {
                    System.out.println("#DB: The photo was successfully uploaded to the database.");
                }

                // Close connection
                stmt.close();
                con.close();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maxID;
    }
}
