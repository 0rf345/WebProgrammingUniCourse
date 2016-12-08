/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs359db.servlets;

import cs359db.db.PhotosDB;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author papadako
 */
@WebServlet("/UploadImage")
@MultipartConfig(maxFileSize = 1011074)    // upload file's size up to 1MB
public class UploadImage extends HttpServlet {

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // gets values of text fields
        String userName = request.getParameter("userName");
        String contentType = request.getParameter("contentType");

        InputStream inputStream = null; // input stream of the upload file

        // obtains the upload file part in this multipart request
        Part filePart = request.getPart("photo");
        if (filePart != null) {
            // prints out some information for debugging
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());

            // obtains input stream of the upload file
            inputStream = filePart.getInputStream();
        }
        try {
            // uploadPhoto returns the id of the photo
            PhotosDB.uploadPhoto(inputStream, userName, contentType);
        } catch (Exception ex) {
            Logger.getLogger(UploadImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
