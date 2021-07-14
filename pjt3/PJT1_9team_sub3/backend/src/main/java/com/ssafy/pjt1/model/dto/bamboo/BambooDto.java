package com.ssafy.pjt1.model.dto.bamboo;

public class BambooDto {
    private int bamboo_id;
    private String writer_nickname;
    private String user_id;
    private String bamboo_date;
    private String bamboo_title;
    private String bamboo_description;
    private String bamboo_image;
    private String bamboo_iframe;
    private String bamboo_header;
    private int is_used;

    public int getBamboo_id() {
        return this.bamboo_id;
    }

    public void setBamboo_id(int bamboo_id) {
        this.bamboo_id = bamboo_id;
    }

    public String getWriter_nickname() {
        return this.writer_nickname;
    }

    public void setWriter_nickname(String writer_nickname) {
        this.writer_nickname = writer_nickname;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBamboo_date() {
        return this.bamboo_date;
    }

    public void setBamboo_date(String bamboo_date) {
        this.bamboo_date = bamboo_date;
    }

    public String getBamboo_title() {
        return this.bamboo_title;
    }

    public void setBamboo_title(String bamboo_title) {
        this.bamboo_title = bamboo_title;
    }

    public String getBamboo_description() {
        return this.bamboo_description;
    }

    public void setBamboo_description(String bamboo_description) {
        this.bamboo_description = bamboo_description;
    }

    public String getBamboo_image() {
        return this.bamboo_image;
    }

    public void setBamboo_image(String bamboo_image) {
        this.bamboo_image = bamboo_image;
    }

    public String getBamboo_iframe() {
        return this.bamboo_iframe;
    }

    public void setBamboo_iframe(String bamboo_iframe) {
        this.bamboo_iframe = bamboo_iframe;
    }

    public String getBamboo_header() {
        return this.bamboo_header;
    }

    public void setBamboo_header(String bamboo_header) {
        this.bamboo_header = bamboo_header;
    }

    public int getIs_used() {
        return this.is_used;
    }

    public void setIs_used(int is_used) {
        this.is_used = is_used;
    }

    public BambooDto() {
        super();
    }
}
