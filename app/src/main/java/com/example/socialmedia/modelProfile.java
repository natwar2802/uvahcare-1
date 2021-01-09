package com.example.socialmedia;

public class modelProfile {
    String usernameP,cityP,countryP,imgUrlP,id;

    public modelProfile() {
    }

    public modelProfile(String usernameP, String cityP, String countryP, String imgUrlP,String id) {
        this.usernameP = usernameP;
        this.cityP = cityP;
        this.countryP = countryP;
        this.imgUrlP = imgUrlP;
        this.id=id;
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
