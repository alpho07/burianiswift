package com.buriani.poxy.burianiswift;

/**
 * Created by Poxy on 12/22/2016.
 */
public class MyData {
    int id;
    Constant constant;
    String dob, dod, region, date_posted,description, image_path,title,obtitle;




    public MyData(int id, String description, String image_path, String dob, String dod, String region, String date_posted, String title, String obtitle) {
        this.id = id;
        this.description = description;
        this.image_path = image_path;
        this.dob = dob;
        this.dod = dod;
        this.region = region;
        this.title=title;
        this.obtitle= obtitle;

        this.date_posted = date_posted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_path() {
        return constant.BASE_URL+image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }


    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDate_posted() {
        return date_posted;
    }

    public void setDate_posted(String date_posted) {
        this.date_posted = date_posted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObtitle() {
        return obtitle;
    }

    public void setObtitle(String obtitle) {
        this.obtitle = obtitle;
    }
}
