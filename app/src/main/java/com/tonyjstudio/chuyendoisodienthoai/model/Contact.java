package com.tonyjstudio.chuyendoisodienthoai.model;

/**
 * Created by Xuan Thinh Phan on 9/23/2018.
 */
public class Contact {
    private String contactName;
    private String contactNumber;
    private String contactCarrier;

    public Contact() {
        contactName = "";
        contactNumber = "";
        contactCarrier = "";
    }

    public Contact(String contactName, String contactNumber, String contactCarrier) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.contactCarrier = contactCarrier;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactCarrier() {
        return contactCarrier;
    }

    public void setContactCarrier(String contactCarrier) {
        this.contactCarrier = contactCarrier;
    }
}
