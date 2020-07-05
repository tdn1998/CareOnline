package com.example.dell_pro.Model;

public class News {
    String title;
    String author;
    String imgUrl;
    String newsUrl;
    String date;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }


    public News(String title, String author, String imgUrl, String newsUrl, String date) {
        this.title = title;
        this.author = author;
        this.imgUrl = imgUrl;
        this.newsUrl = newsUrl;
        this.date = date;
    }

    public News()
    {

    }
}
