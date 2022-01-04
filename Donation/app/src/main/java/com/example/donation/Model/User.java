package com.example.donation.Model;

public class User {

    String name, id, idnumber, livingarea, password, email, emergencynumber, phonenumber, profilepictureurl, search, type;

    public User() {
    }

    public User(String name, String id, String idnumber, String livingarea, String password, String email, String emergencynumber, String phonenumber, String profilepictureurl, String search, String type) {
        this.name = name;
        this.id = id;
        this.idnumber = idnumber;
        this.livingarea = livingarea;
        this.password = password;
        this.email = email;
        this.emergencynumber = emergencynumber;
        this.phonenumber = phonenumber;
        this.profilepictureurl = profilepictureurl;
        this.search = search;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getLivingarea() {
        return livingarea;
    }

    public void setLivingarea(String livingarea) {
        this.livingarea = livingarea;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmergencynumber() {
        return emergencynumber;
    }

    public void setEmergencynumber(String emergencynumber) {
        this.emergencynumber = emergencynumber;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
