package com.buriani.poxy.burianiswift;

/**
 * Created by Poxy on 12/22/2016.
 */
public class CData {
    int id;
    String name, date, comment;
    final String base_url ="globaltalentlens.com/oap/";

    public CData(int id, String name, String date, String comment) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
