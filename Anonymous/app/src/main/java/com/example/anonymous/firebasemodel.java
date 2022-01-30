package com.example.anonymous;

public class firebasemodel {

    String name;
    String userType;
    String uid;
    String status;


    public firebasemodel(String name, String userType, String uid, String status) {
        this.name = name;
        this.userType = userType;
        this.uid = uid;
        this.status = status;
    }

    public firebasemodel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
