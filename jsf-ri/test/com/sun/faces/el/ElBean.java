/*
 * $Id: ElBean.java,v 1.8 2006/03/29 22:39:42 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.el;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// JavaBean for simple expression evaluation tests

public class ElBean {


    private ElBean nestedArray[] = null;


    private ElBean nestedProperty = null;


    private List intList = null;


    private List nestedList = null;


    private Map nestedMap = null;


    private String nullString = null;


    private String stringProperty = "This is a String";


    private boolean booleanProperty = true;


    private byte byteProperty = 123;
    
    private char characterProperty = 'c';


    private double doubleProperty = 654.321;


    private float floatProperty = (float) 123.45;


    private int intArray[] = {1, 2, 3};


    private int intProperty = 1234;


    private long longProperty = 54321;


    private short shortProperty = 321;


    // ------------------------------------------------------------ Constructors


    public ElBean() {
    }


    public ElBean(String stringProperty) {

        this.stringProperty = stringProperty;

    }


    // ---------------------------------------------------------- Public Methods


    public char getCharacterProperty()
    {

        return this.characterProperty;

    }


    public void setCharacterProperty(char c)
    {

        this.characterProperty = c;

    }


    public void setBooleanProperty(boolean booleanProperty) {

        this.booleanProperty = booleanProperty;

    }


    public void setByteProperty(byte byteProperty) {

        this.byteProperty = byteProperty;

    }


    public void setDoubleProperty(double doubleProperty) {

        this.doubleProperty = doubleProperty;

    }


    public void setFloatProperty(float floatProperty) {

        this.floatProperty = floatProperty;

    }


    public void setIntArray(int intArray[]) {

        this.intArray = intArray;

    }


    public void setIntList(List intList) {

        this.intList = intList;

    }


    public void setIntProperty(int intProperty) {

        this.intProperty = intProperty;

    }


    public void setLongProperty(long longProperty) {

        this.longProperty = longProperty;

    }


    public void setNestedArray(ElBean nestedArray[]) {

        this.nestedArray = nestedArray;

    }


    public void setNestedList(List nestedList) {

        this.nestedList = nestedList;

    }


    public void setNestedMap(Map nestedMap) {

        this.nestedMap = nestedMap;

    }


    public void setNestedProperty(ElBean nestedProperty) {

        this.nestedProperty = nestedProperty;

    }


    public void setShortProperty(short shortProperty) {

        this.shortProperty = shortProperty;

    }


    public void setStringProperty(String stringProperty) {

        this.stringProperty = stringProperty;

    }


    public boolean getBooleanProperty() {

        return (this.booleanProperty);

    }


    public byte getByteProperty() {

        return (this.byteProperty);

    }


    public double getDoubleProperty() {

        return (this.doubleProperty);

    }


    public float getFloatProperty() {

        return (this.floatProperty);

    }


    public int[] getIntArray() {

        return (this.intArray);

    }


    public List getIntList() {

        if (intList == null) {
            intList = new ArrayList();
            intList.add(new Integer(10));
            intList.add(new Integer(20));
            intList.add(new Integer(30));
            intList.add(new Integer(40));
            intList.add(new Integer(50));
        }
        return (intList);

    }


    public int getIntProperty() {

        return (this.intProperty);

    }


    public long getLongProperty() {

        return (this.longProperty);

    }


    public ElBean[] getNestedArray() {

        if (nestedArray == null) {
            nestedArray = new ElBean[2];
            nestedArray[0] = new ElBean("Nested0");
            nestedArray[1] = new ElBean("Nested1");
        }
        return (this.nestedArray);

    }


    public List getNestedList() {

        if (nestedList == null) {
            nestedList = new ArrayList();
            nestedList.add(new ElBean("list0"));
            nestedList.add(new ElBean("list1"));
            nestedList.add(new ElBean("list2"));
            nestedList.add(new ElBean("list3"));
        }
        return (this.nestedList);

    }


    public Map getNestedMap() {

        if (nestedMap == null) {
            nestedMap = new HashMap();
            nestedMap.put("map0", new ElBean("map0"));
            nestedMap.put("map1", new ElBean("map1"));
            nestedMap.put("map2", new ElBean("map2"));
        }
        return (this.nestedMap);

    }


    public ElBean getNestedProperty() {

        if (nestedProperty == null) {
            nestedProperty = new ElBean();
        }
        return (this.nestedProperty);

    }


    public String getNullStringProperty() {

        return ("String length is:" + nullString.length());

    }


    public short getShortProperty() {

        return (this.shortProperty);

    }


    public String getStringProperty() {

        return (this.stringProperty);

    }

}
