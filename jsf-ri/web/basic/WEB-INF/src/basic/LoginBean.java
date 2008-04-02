/*
 * $Id: LoginBean.java,v 1.16 2002/10/11 00:58:42 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.faces.component.SelectItem;

public class LoginBean {

    String userName = "joebob";
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
	currentOptions = new ArrayList(2);
	currentOptions.add(defaultOptions[3]);
	currentOptions.add(defaultOptions[4]);
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

    protected char charVal = 'e';

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
    protected ArrayList currentOptions = null;
    
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

    public Object[] getCurrentOptions()
    {
        System.out.println("get current options");
	return currentOptions.toArray();
    }
    
    public void setCurrentOptions(Object []newCurrentOptions)
    {
	int len = 0;
        if (null == newCurrentOptions ||
            (len = newCurrentOptions.length) == 0) {
            return;
        }
	currentOptions.clear();
	currentOptions = new ArrayList(len);
	for (int i = 0; i < len; i++) {
	    currentOptions.add(newCurrentOptions[i]);
	}
        System.out.println("set current options");
    }


    protected Date date = new Date(System.currentTimeMillis());

    public Date getDate()
    {
	return date;
    }
    
    public void setDate(Date newDate)
    {
	date = newDate;
    }

    protected Date time;
    public Date getTime() {
        return time;
    }
    public void setTime(Date newTime) {
        time = newTime;
    }

    protected Date dateTime;
    public Date getDateTime() {
        return dateTime;
    }
    public void setDateTime(Date newDateTime) {
        dateTime = newDateTime;
    }

    protected Float floater = new Float(3.141592);

    public Float getFloater()
    {
	return floater;
    }
    
    public void setFloater(Float newFloater)
    {
	floater = newFloater;
    }

    protected String imagePath = null;

    public String getImagePath()
    {
	return imagePath;
    }
    
    public void setImagePath(String newImagePath)
    {
	imagePath = newImagePath;
    }

}
