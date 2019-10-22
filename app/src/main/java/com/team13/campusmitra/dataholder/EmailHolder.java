package com.team13.campusmitra.dataholder;

public class EmailHolder {
    private String email;
    private String emailID;
    private String password;

    public EmailHolder() {
    }

    public EmailHolder(String email, String emailID, String password) {
        this.email = email;
        this.emailID = emailID;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public EmailHolder(String email, String emailID) {
        this.email = email;
        this.emailID = emailID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EmailHolder(String email) {
        this.email = email;
    }
}
