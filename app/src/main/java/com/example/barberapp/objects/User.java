package com.example.barberapp.objects;

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String imageUri;
    private static User instance;


    private User() {

    }

    public static User getInstance() {
        return instance;
    }

    public static void init() {
        if (instance == null) {
            instance = new User();
        }
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
