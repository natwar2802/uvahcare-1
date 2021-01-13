package com.example.socialmedia;

public class modelGeneral {
    String title,brief,urlimage,description,pid,blogerid,prefrence;
    long datetime,rateno;
    float rating;

    private int claps=0;
    public modelGeneral() {
    }

    public modelGeneral(String title, String brief, String urlimage, String description, String pid, String blogerid, String prefrence, long datetime, long rateno, float rating, int claps) {
        this.title = title;
        this.brief = brief;
        this.urlimage = urlimage;
        this.description = description;
        this.pid = pid;
        this.blogerid = blogerid;
        this.prefrence = prefrence;
        this.datetime = datetime;
        this.rateno = rateno;
        this.rating = rating;
        this.claps = claps;
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

    public String getPrefrence() {
        return prefrence;
    }

    public void setPrefrence(String prefrence) {
        this.prefrence = prefrence;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public long getRateno() {
        return rateno;
    }

    public void setRateno(long rateno) {
        this.rateno = rateno;
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
