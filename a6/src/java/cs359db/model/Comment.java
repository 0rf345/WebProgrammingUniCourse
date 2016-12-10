/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs359db.model;

/**
 *
 * @author papadako
 */
public class Comment {

    private String userName;    // (unique)
    private int photoID;
    private String comment;
    private String timestamp;
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int commentID) {
        this.ID = commentID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Method that checks that all mandatory fields are set
     *
     * @throws Exception
     */
    public void checkFields() throws Exception {
        // Check that everything is ok
        if ((userName == null || userName.trim().isEmpty())
                || (comment == null || comment.trim().isEmpty())
                || (photoID < 0)) {
            throw new Exception("Missing fields!");  // Something went wrong with the fields
        }
    }

}
