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
public class Rating {
    private String userName;    // (unique)
    private int ID;
    private int photoID;
    private int rate;
    private String timestamp;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ratingID) {
        this.ID = ratingID;
    }

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
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
                || (rate > 5) || (rate < 0)
                || (photoID < 0)) {
            throw new Exception("Missing fields!");  // Something went wrong with the fields
        }
    }

}
