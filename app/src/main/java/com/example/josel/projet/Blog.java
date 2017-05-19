package com.example.josel.projet;

/**
 * Created by josel on 16/05/2017.
 */

public class Blog {

    private String title;
    private String desc;
    private String date;
    private String location;
    private String image;


    public Blog(){

    }

    public Blog(String title, String desc, String date, String location, String image) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.location = location;
        this.image = image;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
