/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs359db.model;

import java.io.Serializable;

/**
 * Code in exercise 5
 *
 * We might not use this for more optimized access to the database
 *
 * @author papadako
 */
public class Photo implements Serializable {

    private String userName;
    private String date;
    private String title;
    private int numberOfRatings;
    private String contentType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User Name: ").append(userName).append("\n")
                .append("date: ").append(date).append("\n")
                .append("title: ").append(title).append("\n")
                .append("number Of Ratings: ").append(numberOfRatings).append("\n")
                .append("content Type: ").append(contentType).append("\n");

        return sb.toString();
    }
}
