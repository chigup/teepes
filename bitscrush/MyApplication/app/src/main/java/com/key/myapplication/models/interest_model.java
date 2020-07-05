package com.key.myapplication.models;

public class    interest_model {

    private String name;
    private String image;
    private int id;
    private boolean is_selected=false;

    public interest_model(int interest_id, String name, String interest_url,boolean is_selected) {
        this.name = name;
        this.image = interest_url;
        this.id = interest_id;
        this.is_selected=is_selected;
    }


    public boolean isIs_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
