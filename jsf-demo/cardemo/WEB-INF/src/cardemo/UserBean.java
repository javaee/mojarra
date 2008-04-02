/*
 * $Id: UserBean.java,v 1.1 2002/09/30 21:42:20 jball Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package cardemo;

// UserBean.java

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import java.util.Collection;

import java.io.Serializable;

/**
 *
 *  <B>UserBean</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: UserBean.java,v 1.1 2002/09/30 21:42:20 jball Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class UserBean extends Object implements Serializable
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

    private String userName;
    private String password;
    private String pwdConfirm;
    private String pwdHint;

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String country;
    private String phone;
    private String creditCardType;
    private String creditCardNumber;
    private String creditCardExpr;

    private String appleQuantity;
    private String bananaQuantity;
    private String cantaloupeQuantity;
    private String grapefruitQuantity;
    private String grapeQuantity;

    private String giftWrap;
    private String giftCard;
    private String scented;

// Relationship Instance Variables

    private transient Collection items;

//
// Constructors and Initializers    
//

public UserBean()
{
    super();
    // ParameterCheck.nonNull();
    this.init();
}

protected void init()
{
    // super.init();
}

//
// Class methods
//

//
// General Methods
//

public void copyFrom(UserBean from) {
    this.userName = from.userName;
    this.password = from.password;
    this.pwdConfirm = from.pwdConfirm;
    this.pwdHint = from.pwdHint;
    
    this.firstName = from.firstName;
    this.lastName = from.lastName;
    this.address = from.address;
    this.city = from.city;
    this.state = from.state;
    this.country = from.country;
    this.phone = from.phone;
    this.creditCardType = from.creditCardType;
    this.creditCardNumber = from.creditCardNumber;
    this.creditCardExpr = from.creditCardExpr;

    this.appleQuantity = from.appleQuantity;
    this.bananaQuantity = from.bananaQuantity;
    this.cantaloupeQuantity = from.cantaloupeQuantity;
    this.grapefruitQuantity = from.grapefruitQuantity;
    this.grapeQuantity = from.grapeQuantity;

    this.items = from.items;
}    

public void setUserName(String newUserName) {
    userName = newUserName;
}

public String getUserName() {
    return userName;
}

public void setPassword(String newPassword) {
    password = newPassword;
}

public String getPassword() {
    return password;
}

public void setAppleQuantity(String newAppleQuantity) {
    appleQuantity = newAppleQuantity;
}

public String getAppleQuantity() {
    return appleQuantity;
}
public void setBananaQuantity(String newBananaQuantity) {
    bananaQuantity = newBananaQuantity;
}

public String getBananaQuantity() {
    return bananaQuantity;
}
public void setCantaloupeQuantity(String newCantaloupeQuantity) {
    cantaloupeQuantity = newCantaloupeQuantity;
}

public String getCantaloupeQuantity() {
    return cantaloupeQuantity;
}
public void setGrapefruitQuantity(String newGrapefruitQuantity) {
    grapefruitQuantity = newGrapefruitQuantity;
}

public String getGrapefruitQuantity() {
    return grapefruitQuantity;
}
public void setGrapeQuantity(String newGrapeQuantity) {
    grapeQuantity = newGrapeQuantity;
}

public String getGrapeQuantity() {
    return grapeQuantity;
}

public void setItems(Object newItems) {
    items = (Collection) newItems;
}

public Object getItems() {
    return items;
}

public void setPwdConfirm(String newVal) {
    pwdConfirm = newVal;
}

public String getPwdConfirm() {
    return pwdConfirm;
}

public void setPwdHint(String newVal) {
    pwdHint = newVal;
}

public String getPwdHint() {
    return pwdHint;
}

public void setFirstName(String newVal) {
    firstName = newVal;
}

public String getFirstName() {
    return firstName;
}
public void setLastName(String newVal) {
    lastName = newVal;
}

public String getLastName() {
    return lastName;
}
public void setAddress(String newVal) {
    address = newVal;
}

public String getAddress() {
    return address;
}
public void setCity(String newVal) {
    city = newVal;
}

public String getCity() {
    return city;
}
public void setState(String newVal) {
    state = newVal;
}

public String getState() {
    return state;
}
public void setCountry(String newVal) {
    country = newVal;
}

public String getCountry() {
    return country;
}
public void setPhone(String newVal) {
    phone = newVal;
}

public String getPhone() {
    return phone;
}
public void setCreditCardType(String newVal) {
    creditCardType = newVal;
}

public String getCreditCardType() {
    return creditCardType;
}
public void setCreditCardNumber(String newVal) {
    creditCardNumber = newVal;
}

public String getCreditCardNumber() {
    return creditCardNumber;
}
public void setCreditCardExpr(String newVal) {
    creditCardExpr = newVal;
}

public String getCreditCardExpr() {
    return creditCardExpr;
}

public void setGiftWrap(String newVal) {
    giftWrap = newVal;
}

public String getGiftWrap() {
    return giftWrap;
}

public void setGiftCard(String newVal) {
    giftCard = newVal;
}

public String getGiftCard() {
    return giftCard;
}

public void setScented(String newVal) {
    scented = newVal;
}

public String getScented() {
    return scented;
}



} // end of class UserBean
