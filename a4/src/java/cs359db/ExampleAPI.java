/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs359db;

import cs359db.db.UserDB;
import cs359db.model.User;

/**
 *
 * @author papadako
 */
public class ExampleAPI {
    /**
     * An example of adding a new member in the database. Turing is a user of
     * our system
     *
     * @param args the command line arguments
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException {

        // O Turing έσπασε τον κώδικα enigma που χρησιμοποιούσαν οι Γερμανοί
        // στον Παγκόσμιο Πόλεμο ΙΙ για να κρυπτογραφήσουν την επικοινωνία τους.
        // Άρα είναι πιθανό να χρησιμοποιούσε σαν passwd τη λέξη enigma, κάπως
        // τροποποιημένη :)
        // http://en.wikipedia.org/wiki/Enigma_machine
        // md5 της λέξης 3n!gm@ είναι e37f7cfcb0cd53734184de812b5c6175
        User turing = new User("turing", "turing@csd.uoc.gr",
                "e37f7cfcb0cd53734184de812b5c6175", "Alan", "Turing",
                "1912-07-7", "Science", "Computer Science");
        turing.setGender("Male");
        turing.setInfo("You will have a job due to my work! :)");

        if (UserDB.checkValidUserName("turing")) {
            // Add turing to database
            UserDB.addUser(turing);
        }

        // Add a wish as info
        turing.setInfo("I hope you follow my path...");
        turing.setEmail("gnirut@csd.uoc.gr");
        UserDB.updateUser(turing);

        System.out.println(UserDB.getUser("turing"));

        UserDB.deleteUser("turing");

    }
}
