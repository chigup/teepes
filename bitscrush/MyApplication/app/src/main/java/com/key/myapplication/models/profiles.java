package com.key.myapplication.models;

import java.util.ArrayList;

public class profiles {
    boolean allow_gifts;
    private int age;
    private String[] images;
    private String pet_name;
    private String profile_id;
    private String name;
    private String college;
    private desc_ques_model description;
    private ArrayList<interest_model> interests;
    private ArrayList<desc_ques_model> questions;
    private int match;
    //TODO: need gender of user too


    public profiles(int age,boolean allow_gifts ,String[] images, String pet_name, String profile_id, String name, String college, desc_ques_model description, ArrayList<interest_model> interests, ArrayList<desc_ques_model> questions, int match) {
        this.age = age;
        this.images = images;
        this.pet_name = pet_name;
        this.profile_id = profile_id;
        this.name = name;
        this.college = college;
        this.description = description;
        this.interests = interests;
        this.questions = questions;
        this.match = match;
        this.allow_gifts=allow_gifts;
    }

    public boolean isAllow_gifts() {
        return allow_gifts;
    }

    public void setAllow_gifts(boolean allow_gifts) {
        this.allow_gifts = allow_gifts;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public desc_ques_model getDescription() {
        return description;
    }

    public void setDescription(desc_ques_model description) {
        this.description = description;
    }

    public ArrayList<interest_model> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<interest_model> interests) {
        this.interests = interests;
    }

    public ArrayList<desc_ques_model> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<desc_ques_model> questions) {
        this.questions = questions;
    }

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }
}
