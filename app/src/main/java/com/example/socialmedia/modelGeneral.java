package com.example.socialmedia;

public class modelGeneral {
    String title,brief,urlimage,description,pid,blogerid;

    private int claps=0;
    public modelGeneral() {
    }
    public modelGeneral(String title, String brief, String urlimage, String description,String pid,String blogerid) {
        this.pid=pid;
        this.title = title;
        this.brief = brief;
        this.urlimage = urlimage;
        this.description = description;
        this.blogerid=blogerid;
    }

    public String getBlogerid() {
        return blogerid;
    }

    public void setBlogerid(String blogerid) {
        this.blogerid = blogerid;
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

    public int getClaps() {
        return claps;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setClaps(int claps) {
        this.claps = claps;
    }

    public int incclaps(){
        this.claps=this.claps+1;
        return this.claps;
    }
}
