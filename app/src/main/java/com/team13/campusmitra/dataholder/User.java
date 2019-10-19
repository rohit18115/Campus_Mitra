package com.team13.campusmitra.dataholder;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String userName;
    private String userFirstName;
    private String userLastName;
    private String gender;
    private String dob;
    private int userType;
    private String userEmail;
    private String userPersonalMail;
    private int profileCompleteCount;
    private long lastLoginTimeStamp;
    private int activeStatus;
    private String imageUrl;

    public User(){

    }
    public User(String userId, String userName, String userFirstName, String userLastName, String gender, String dob, int userType, String userEmail, String userPersonalMail, int profileCompleteCount, long lastLoginTimeStamp, int activeStatus, String imageUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.gender = gender;
        this.dob = dob;
        this.userType = userType;
        this.userEmail = userEmail;
        this.userPersonalMail = userPersonalMail;
        this.profileCompleteCount = profileCompleteCount;
        this.lastLoginTimeStamp = lastLoginTimeStamp;
        this.activeStatus = activeStatus;
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPersonalMail() {
        return userPersonalMail;
    }

    public void setUserPersonalMail(String userPersonalMail) {
        this.userPersonalMail = userPersonalMail;
    }

    public int getProfileCompleteCount() {
        return profileCompleteCount;
    }

    public void setProfileCompleteCount(int profileCompleteCount) {
        this.profileCompleteCount = profileCompleteCount;
    }

    public long getLastLoginTimeStamp() {
        return lastLoginTimeStamp;
    }

    public void setLastLoginTimeStamp(long lastLoginTimeStamp) {
        this.lastLoginTimeStamp = lastLoginTimeStamp;
    }

    public int getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(int activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
