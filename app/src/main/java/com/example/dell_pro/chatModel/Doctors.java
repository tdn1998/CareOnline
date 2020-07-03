package com.example.dell_pro.chatModel;

public class Doctors {

    public String docname;
    public String imgurl;
    public String status;
    public String id;

    public Doctors() {

    }

    public Doctors(String docname, String imgurl, String status, String id) {
        this.docname = docname;
        this.imgurl = imgurl;
        this.status = status;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
