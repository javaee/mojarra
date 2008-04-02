/*
 * $Id: CustomerBean.java,v 1.1 2002/11/02 01:34:59 jball Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package cardemo;

import java.util.*;

public class CustomerBean extends Object {
    
    
    String title = null;
    String firstName = null;
    String middleInitial = null;
    String lastName = null;
    String mailingAddress = null;
    String city = null;
    String state = null;
    int zip;
    String month = null;
    String year = null;
    
    public CustomerBean() {
        super();
        System.out.println("CustomerBean created");
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setFirstName(String first) {
        firstName = first;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setMiddleInitial(String mI) {
        middleInitial = mI;
    }
    
    public String getMiddleInitial() {
        return middleInitial;
    }
    
    public void setLastName(String last) {
        lastName = last;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setMailingAddress(String mA) {
        mailingAddress = mA;
    }
    
    public String getMailingAddress() {
        return mailingAddress;
    }
    
    public void setCity(String cty) {
        city = cty;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setState(String sT) {
        state = sT;
    }
    
    public String getState() {
        return state;
    }
    
    public void setZip(int zipCode) {
        zip = zipCode;
    }
    
    public int getZip() {
        return zip;
    }
    
    public void setMonth(String mth) {
        month = mth;
    }
    
    public String getMonth() {
        return month;
    }
    
    public void setYear(String yr) {
        year = yr;
    }

    public String getYear() {
        return year;
    }
}
