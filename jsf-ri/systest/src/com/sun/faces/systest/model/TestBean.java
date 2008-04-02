/*
 * $Id: TestBean.java,v 1.8 2004/04/07 20:08:01 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import javax.faces.component.UIInput;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ActionEvent;
import javax.faces.event.AbortProcessingException;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * <p>Test JavaBean for managed object creation facility.</p>
 */

public class TestBean {

    private Random random;

    public TestBean() {
	random = new Random(4143);
    }	


    private boolean booleanProperty = true;


    public boolean getBooleanProperty() {
        return (this.booleanProperty);
    }


    public void setBooleanProperty(boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }


    private byte byteProperty = 12;


    public byte getByteProperty() {
        return (this.byteProperty);
    }


    public void setByteProperty(byte byteProperty) {
        this.byteProperty = byteProperty;
    }


    private double doubleProperty = 123.45;


    public double getDoubleProperty() {
        return (this.doubleProperty);
    }


    public void setDoubleProperty(double doubleProperty) {
        this.doubleProperty = doubleProperty;
    }


    private float floatProperty = (float) 12.34;


    public float getFloatProperty() {
        return (this.floatProperty);
    }


    public void setFloatProperty(float floatProperty) {
        this.floatProperty = floatProperty;
    }


    private int intProperty = 123;


    public int getIntProperty() {
        return (this.intProperty);
    }


    public void setIntProperty(int intProperty) {
        this.intProperty = intProperty;
    }


    private long longProperty = 12345;


    public long getLongProperty() {
        return (this.longProperty);
    }


    public void setLongProperty(long longProperty) {
        this.longProperty = longProperty;
    }


    private short shortProperty = 1234;


    public short getShortProperty() {
        return (this.shortProperty);
    }


    public void setShortProperty(short shortProperty) {
        this.shortProperty = shortProperty;
    }


    private String stringProperty = "This is a String property";


    public String getStringProperty() {
        return (this.stringProperty);
    }


    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }


    private UIInput userName = null;


    public UIInput getUserName() {
        return (this.userName);
    }


    public void setUserName(UIInput userName) {
        this.userName = userName;
    }

    public List selectList = null;

    public List getSelectList() {
	if (null == selectList) {
	    selectList = new ArrayList();
	    selectList.add(new SelectItem("one", "one", "one"));
	    selectList.add(new SelectItem("two", "two", "two"));
	    selectList.add(new SelectItem("three", "three", "three"));
	}
	return selectList;
    }

    public void setSelectList(List newSelectList) {
	selectList = newSelectList;
    } 

    protected String selection = null;

    public String getSelection() {
	return selection;
    }

    public void setSelection(String newSelection) {
	selection = newSelection;
    }

    protected String [] multiSelection;
    public String [] getMultiSelection() {
	return multiSelection;
    }

    public void setMultiSelection(String [] newMultiSelection) {
	multiSelection = newMultiSelection;
    }

    public void valueChanged(ValueChangeEvent event)
        throws AbortProcessingException {
	String [] values = (String []) event.getNewValue();
	if (null == values) {
	    valueChangeMessage = "";
	}
	else {
	    valueChangeMessage = "value changed, new values: ";
	    for (int i = 0; i < values.length; i++) {
		valueChangeMessage = valueChangeMessage + " " + values[i];
	    }
	}
    }

    protected String valueChangeMessage;
    public String getValueChangeMessage() {
	return valueChangeMessage;
    }

    public void setValueChangeMessage(String newValueChangeMessage) {
	valueChangeMessage = newValueChangeMessage;
    }

    public List getNondeterministicSelectList() {
	ArrayList list = new ArrayList(3);
	String str = new String((new Float(random.nextFloat())).toString());
	list.add(new SelectItem(str, str, str));
	str = new String((new Float(random.nextFloat())).toString());
	list.add(new SelectItem(str, str, str));
	str = new String((new Float(random.nextFloat())).toString());
	list.add(new SelectItem(str, str, str));
	return list;
    }

    public void setNondeterministicSelectList(List newNondeterministicSelectList) {
    }

    public void addComponentToTree(ActionEvent action) {
	HtmlOutputText output = new HtmlOutputText();
	output.setValue("<p>==new output==</p>");
	output.setEscape(false);
	
	UIComponent group = FacesContext.getCurrentInstance().getViewRoot().findComponent("form" + NamingContainer.SEPARATOR_CHAR +  "addHere");
	group.getChildren().add(output);
	
    }
	
}
