/*
 * $Id: LoginBean.java,v 1.3 2001/12/20 22:26:44 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package basic;

public class LoginBean {

    String userName = null;
    String password = null;
    String address = null;
    String validUser = null;

    public LoginBean () {
        System.out.println("Model Object Created");
    }
  
    public void setUserName(String user_name) {
        userName = user_name;
        System.out.println("Set userName " + userName);
    }

    public String getUserName() {
        System.out.println("get userName " + userName);
        return userName;
    }

    public void setPassword(String pwd) {
        password = pwd;
        System.out.println("set Password " + password);
    }

    public String getPassword() {
        System.out.println("get Password " + password);
        return password;
    }

    public void setAddress(String addr) {
        address = addr;
        System.out.println("Set Address " + address);
    }

    public String getAddress() {
        System.out.println("get Address " + address);
        return address;
    }

    public void setValidUser(String valid_user) {
        validUser = valid_user;
        System.out.println("Set validUser " + validUser);
    }

    public String getvalidUser() {
        System.out.println("get validUser " + validUser);
        return validUser;
    }

}
