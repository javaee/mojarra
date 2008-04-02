package com.sun.faces;

import org.mozilla.util.Assert;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class TestBean extends Object
{

public static final String PROP = "oneSet";

public static final String TRUE = "true";
public static final String FALSE = "false";


protected String one = null;

public void setOne(String newOne)
{
    one = newOne;
    Assert.assert_it(newOne.equals("one"));
    System.setProperty(PROP, TRUE);
}

public String getOne() 
{
    return one;
}

protected String prop = null;

public void setProp(String newProp)
{
    prop = newProp;
}

public String getProp()
{
    return prop;
}

protected boolean boolProp = false;

public void setBoolProp(boolean newBoolProp)
{
    boolProp = newBoolProp;
}

public boolean getBoolProp()
{
    return boolProp;
}


protected InnerBean inner = null;

public void setInner(InnerBean newInner)
{
    inner = newInner;
    System.setProperty(PROP, TRUE);
}

public InnerBean getInner() 
{
    return inner;
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

public static class InnerBean extends Object
{

protected String two = null;
protected Integer pin = null;
protected Boolean result = null; 
protected ArrayList customers = null;

public void setTwo(String newTwo)
{
    two = newTwo;
    Assert.assert_it(newTwo.equals("two"));
    System.setProperty(PROP, TRUE);
}

public String getTwo() 
{
    return two;
}

public void setPin(Integer newPin)
{
    pin = newPin;
}

public Integer getPin() 
{
    return pin;
}

public Collection getCustomers() {
    if (null == customers) {
	customers = new ArrayList();
	customers.add("Mickey");
	customers.add("Jerry");
	customers.add("Phil");
	customers.add("Bill");
	customers.add("Bob");
    }
    return customers;
}

public void setResult(Boolean newResult)
{
    result = newResult;
}

public Boolean getResult() 
{
    return result;
}

protected Inner2Bean inner2 = null;

public void setInner2(Inner2Bean newInner2)
{
    inner2 = newInner2;
    System.setProperty(PROP, TRUE);
}

public Inner2Bean getInner2() 
{
    return inner2;
}

}

public static class Inner2Bean extends Object
{

protected String three = null;

public void setThree(String newThree)
{
    three = newThree;
    Assert.assert_it(newThree.equals("three"));
    System.setProperty(PROP, TRUE);
}

public String getThree() 
{
    return three;
}

protected Map nicknames = new HashMap();

public Map getNicknames() {
    return nicknames;
}

public void setNicknames(Map newNicknames) {
    nicknames = newNicknames;
}

}

}
