package com.example.dell_doc.chatModel;

public class Users {

    private String username;
    private String bloodgrp;
    private String desp;
    private String dob;
    private String gender;
    private String id;
    private String imgurl;
    private String phno;

    public Users(String username, String bloodgrp, String desp, String dob, String gender, String id, String imgurl, String phno) {
        this.username = username;
        this.bloodgrp = bloodgrp;
        this.desp = desp;
        this.dob = dob;
        this.gender = gender;
        this.id = id;
        this.imgurl = imgurl;
        this.phno = phno;
    }

    public Users() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBloodgrp(String bloodgrp) {
        this.bloodgrp = bloodgrp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getUsername() {
        return username;
    }

    public String getBloodgrp() {
        return bloodgrp;
    }

    public String getDesp() {
        return desp;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getId() {
        return id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getPhno() {
        return phno;
    }
}
