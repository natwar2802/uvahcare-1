package com.example.socialmedia;

import java.util.ArrayList;

public class modelGeneral {
    String title,brief,urlimage,description,pid,blogerid;
    String preference;
    long datetime,ratesum;
    float rating;
    double postscore;
    private int claps=0;
    public modelGeneral() {
    }

    public modelGeneral(String title, String brief, String urlimage, String description, String pid, String blogerid,String preference, long datetime, long ratesum, float rating, int claps,double postscore) {
        this.title = title;
        this.brief = brief;
        this.urlimage = urlimage;
        this.description = description;
        this.pid = pid;
        this.blogerid = blogerid;
        this.preference = preference;
        this.datetime = datetime;
        this.ratesum = ratesum;
        this.rating = rating;
        this.claps = claps;
        this.postscore=postscore;
    }

    public double getPostscore() {
        return postscore;
    }

    public void setPostscore(double postscore) {
        this.postscore = postscore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBlogerid() {
        return blogerid;
    }

    public void setBlogerid(String blogerid) {
        this.blogerid = blogerid;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public long getRatesum() {
        return ratesum;
    }

    public void setRatesum(long ratesum) {
        this.ratesum = ratesum;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getClaps() {
        return claps;
    }

    public void setClaps(int claps) {
        this.claps = claps;
    }

    public int incclaps(){
        this.claps=this.claps+1;
        return this.claps;
    }
}
