package com.innovation.socialmedia;

import java.util.ArrayList;
import java.util.List;

public class modelProfile {
    String usernameP,cityP,countryP,imgUrlP,id,userdetail,prevseenpost;
    ArrayList<String> userPreference;


    public modelProfile() {
    }

    public modelProfile(String usernameP, String cityP, String countryP, String imgUrlP,String id,String userdetail,ArrayList<String> userPreference,String prevseenpost) {
        this.usernameP = usernameP;
        this.cityP = cityP;
        this.countryP = countryP;
        this.imgUrlP = imgUrlP;
        this.id=id;
        this.userdetail=userdetail;
        this.userPreference=userPreference;
        this.prevseenpost=prevseenpost;
    }

    public String getPrevseenpost() {
        return prevseenpost;
    }

    public void setPrevseenpost(String prevseenpost) {
        this.prevseenpost = prevseenpost;
    }

    public ArrayList<String> getUserPreference() {
        return userPreference;
    }

    public void setUserPreference(ArrayList<String> userPreference) {
        this.userPreference = userPreference;
    }

    public String getUserdetail() {
        return userdetail;
    }

    public void setUserdetail(String userdetail) {
        this.userdetail = userdetail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsernameP() {
        return usernameP;
    }

    public void setUsernameP(String usernameP) {
        this.usernameP = usernameP;
    }

    public String getCityP() {
        return cityP;
    }

    public void setCityP(String cityP) {
        this.cityP = cityP;
    }

    public String getCountryP() {
        return countryP;
    }

    public void setCountryP(String countryP) {
        this.countryP = countryP;
    }

    public String getImgUrlP() {
        return imgUrlP;
    }

    public void setImgUrlP(String imgUrlP) {
        this.imgUrlP = imgUrlP;
    }
}
