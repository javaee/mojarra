/*
 * $Id: LoginBean.java,v 1.10 2002/08/29 00:28:06 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package basic;

import java.util.ArrayList;
import java.util.Collection;

import javax.faces.component.SelectItem;

public class LoginBean {

    String userName = null;
    String password = null;
    String address = null;
    Boolean validUser = null;
    Integer pin = null;

    String defaultOptions[] = {
	"pinto",
	"black",
	"garbanzo",
	"green",
	"string",
	"coffee",
	"baked"
    };

    public LoginBean () {
        System.out.println("Model Object Created");
	options = new ArrayList(defaultOptions.length);
	int i = 0;
	for (i = 0; i < defaultOptions.length; i++) {
	    options.add(new SelectItem(defaultOptions[i], defaultOptions[i], 
				       defaultOptions[i]));
	}
    }
  
    public void setUserName(String user_name) {
        userName = user_name;
        System.out.println("Set userName " + userName);
    }

    public String getUserName() {
        System.out.println("get userName " + userName);
        return userName;
    }

    public void setPin(Integer _pin) {
        this.pin = _pin;
        System.out.println("Set pin " + pin);
    }

    public Integer getPin() {
        System.out.println("get pin " + pin);
        return pin;
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

    public void setValidUser(Boolean valid_user) {
        validUser = valid_user;
        System.out.println("Set validUser " + validUser);
    }

    public Boolean getvalidUser() {
        System.out.println("get validUser " + validUser);
        return validUser;
    }

    protected double doubleVal;

    public void setDouble(double newDoubleVal) {
	doubleVal = newDoubleVal;
    }

    public double getDouble() {
	return doubleVal;
    }

    protected int intVal;

    public int getInt()
    {
	return intVal;
    }

    public void setInt(int newIntVal)
    {
	intVal = newIntVal;
    }

    protected float floatVal;

    public float getFloat()
    {
        return floatVal;
    }

    public void setFloat(float newFloatVal)
    {
        floatVal = newFloatVal;
    }

    protected short shortVal;

    public short getShort()
    {
        return shortVal;
    }

    public void setShort(short newShortVal)
    {
        shortVal = newShortVal;
    }

    protected long longVal;

    public long getLong()
    {
        return longVal;
    }

    public void setLong(long newLongVal)
    {
        longVal = newLongVal;
    }

    protected char charVal;

    public char getChar()
    {
        return charVal;
    }

    public void setChar(char newCharVal)
    {
        charVal = newCharVal;
    }

    protected byte byteVal;

    public byte getByte()
    {
        return byteVal;
    }

    public void setByte(byte newByteVal)
    {
        byteVal = newByteVal;
    }

    protected String stringVal;

    public String getString()
    {
	return stringVal;
    }
    
    public void setString(String newStringVal)
    {
	stringVal = newStringVal;
    }

    protected ArrayList options = null;
    protected Object currentOption = defaultOptions[4];
    
    public Collection getOptions() {
	return options;
    }

    public void setOptions(Collection newOptions) {
	options = new ArrayList(newOptions);
    }

    public Object getCurrentOption() {
	return currentOption;
    }

    public void setCurrentOption(Object newCurrentOption)
    {
	currentOption = newCurrentOption;
    }



}
