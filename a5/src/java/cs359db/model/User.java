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
public class User {

    private String userName;    // (unique)
    private String email;       // (unique)
    private String password;    // (could be encrypted in md5)
    private String firstName;
    private String lastName;
    private String birthDate;   // (format: yyyy-MM-dddd)
    private Gender gender;
    private String country;
    private String town;
    private String info;

    /**
     * Default Constructor
     *
     */
    public User() {
        this.userName = "";
        this.email = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.birthDate = "";
        this.country = "";
        this.town = "";
        // Not obligatory fields
        this.info = "";
        this.gender = Gender.UNKNOWN;
    }

    /**
     * Constructor
     *
     * @param userName
     * @param email
     * @param password
     * @param firstName
     * @param lastName
     * @param birthDate
     * @param country
     * @param town
     */
    public User(String userName,
            String email,
            String password,
            String firstName,
            String lastName,
            String birthDate,
            String country,
            String town) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.country = country;
        this.town = town;
        // Not obligatory fields
        this.info = "";
        this.gender = Gender.UNKNOWN;
    }

    /**
     * Constructor
     *
     * @param userName
     * @param email
     * @param password
     * @param firstName
     * @param lastName
     * @param birthDate
     * @param country
     * @param town
     * @param info
     * @param gender
     */
    public User(String userName,
            String email,
            String password,
            String firstName,
            String lastName,
            String birthDate,
            String country,
            String town,
            String info,
            Gender gender) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.country = country;
        this.town = town;
        // Not obligatory fields
        this.info = info;
        this.gender = gender;
    }

    /**
     * Constructor
     *
     * @param userName
     * @param email
     * @param password
     * @param firstName
     * @param lastName
     * @param birthDate
     * @param country
     * @param town
     * @param info
     * @param gender
     */
    public User(String userName,
            String email,
            String password,
            String firstName,
            String lastName,
            String birthDate,
            String country,
            String town,
            String info,
            String gender) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.country = country;
        this.town = town;
        // Not obligatory fields
        this.info = info;
        setGender(gender);
    }

    /**
     * Method that checks that all mandatory fields are set
     *
     * @throws Exception
     */
    public void checkFields() throws Exception {
        // Check that everything is ok
        if ((userName == null || userName.trim().isEmpty())
                || (email == null || email.trim().isEmpty())
                || (password == null || password.trim().isEmpty())
                || (firstName == null || firstName.trim().isEmpty())
                || (lastName == null || lastName.trim().isEmpty())
                || (birthDate == null || birthDate.trim().isEmpty())
                || (gender != Gender.FEMALE && gender != Gender.MALE && gender != Gender.UNKNOWN)
                || (town == null || town.trim().isEmpty())
                || (country == null || country.trim().isEmpty())
                || (birthDate == null || birthDate.trim().isEmpty())) {
            throw new Exception("Missing fields!");  // Something went wrong with the fields
        }
    }

    /* Getters and Setters */
    /**
     * Get the user name
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the username
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get the email
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the password
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of this user
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the first name of the user
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get last name
     *
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set last name
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get BirthDate
     *
     * @return
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Set birthDate
     *
     * @param birthDate
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Get gender
     *
     * @return
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Set gender
     *
     * @param gender
     */
    public void setGender(String gender) {
        switch (gender.toLowerCase().trim()) {
            case "female":
                this.gender = Gender.FEMALE;
                break;
            case "male":
                this.gender = Gender.MALE;
                break;
            default:
                this.gender = Gender.UNKNOWN;
                break;
        }
    }

    /**
     * Get country
     *
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set country
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get town
     *
     * @return
     */
    public String getTown() {
        return town;
    }

    /**
     * Set town
     *
     * @param town
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * Get info
     *
     * @return
     */
    public String getInfo() {
        return info;
    }

    /**
     * Set info
     *
     * @param info
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Enum for supporting gender values
     */
    public enum Gender {

        MALE("Male"), FEMALE("Female"), UNKNOWN("Unknown");
        private final String value;

        private Gender(String value) {
            this.value = value;
        }

        /**
         * Returns string representation of value
         *
         * @return
         */
        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * Returns a string representation of this object
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User Name: ").append(userName).append("\n")
                .append("email: ").append(email).append("\n")
                .append("password: ").append(password).append("\n")
                .append("First Name: ").append(firstName).append("\n")
                .append("Last Name: ").append(lastName).append("\n")
                .append("Birth Date: ").append(birthDate).append("\n")
                .append("Gender: ").append(gender).append("\n")
                .append("Country: ").append(country).append("\n")
                .append("Town: ").append(town).append("\n")
                .append("Info: ").append(info).append("\n");

        return sb.toString();

    }

}
