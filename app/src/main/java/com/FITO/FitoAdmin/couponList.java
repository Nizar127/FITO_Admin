package com.FITO.FitoAdmin;

public class couponList {
    private String name;
    private String image;
    private String points;
    private String sponsoredName;
    private String sponsoredTerm;
    private String sponsoredContact;
    private String sponsoredHighlight;
    private String coupon_validity;
    private String date_added;
    //private int couponImg;


    public couponList(String name, String image, String points, String sponsoredName, String sponsoredTerm, String sponsoredContact, String sponsoredHighlight, String coupon_validity, String date_added) {
        this.name = name;
        this.image = image;
        this.points = points;
        this.sponsoredName = sponsoredName;
        this.sponsoredTerm = sponsoredTerm;
        this.sponsoredContact = sponsoredContact;
        this.sponsoredHighlight = sponsoredHighlight;
        this.coupon_validity = coupon_validity;
        this.date_added = date_added;
    }

    public couponList(){

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

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getSponsoredName() {
        return sponsoredName;
    }

    public void setSponsoredName(String sponsoredName) {
        this.sponsoredName = sponsoredName;
    }

    public String getSponsoredTerm() {
        return sponsoredTerm;
    }

    public void setSponsoredTerm(String sponsoredTerm) {
        this.sponsoredTerm = sponsoredTerm;
    }

    public String getSponsoredContact() {
        return sponsoredContact;
    }

    public void setSponsoredContact(String sponsoredContact) {
        this.sponsoredContact = sponsoredContact;
    }

    public String getSponsoredHighlight() {
        return sponsoredHighlight;
    }

    public void setSponsoredHighlight(String sponsoredHighlight) {
        this.sponsoredHighlight = sponsoredHighlight;
    }

    public String getCoupon_validity() {
        return coupon_validity;
    }

    public void setCoupon_validity(String coupon_validity) {
        this.coupon_validity = coupon_validity;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }
}
