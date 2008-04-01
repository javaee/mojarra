package com.sun.faces;

import com.sun.faces.Address;

public class User {

    private Address address;
    private String ID;

    public User() {
        address = new Address();
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }
}

