/*
 * $Id: TestBean.java,v 1.3 2007/04/27 22:02:17 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.systest.model;

import javax.faces.FactoryFinder;
import javax.faces.component.UIInput;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ActionEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.application.Application;
import javax.faces.el.PropertyResolver;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;

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

    private boolean booleanProperty2 = false;
    public boolean getBooleanProperty2() {
	return booleanProperty2;
    }

    public void setBooleanProperty2(boolean newBooleanProperty2) {
	booleanProperty2 = newBooleanProperty2;
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

    private int[] intsProperty = {5, 6, 7};

    public int[] getIntsProperty() {
        return (this.intsProperty);
    }

    public void setIntsProperty(int[] intsProperty) {
        this.intsProperty = intsProperty;
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
    
    private String renderKitInfo = null;
    public String getRenderKitInfo() {
        renderKitInfo = FacesContext.getCurrentInstance().
            getViewRoot().getRenderKitId();
        return renderKitInfo;
    }
    public void setRenderKitInfo(String renderKitInfo) {
        this.renderKitInfo = renderKitInfo;
    }
    private String responseWriterInfo = null;
    public String getResponseWriterInfo() {
        responseWriterInfo = FacesContext.getCurrentInstance().
            getResponseWriter().getClass().getName();
        return responseWriterInfo;
    }
    public void setResponseWriterInfo(String responseWriterInfo) {
        this.responseWriterInfo = responseWriterInfo;
    }

    private Object bean = null;


    public Object getBean() {
        return (this.bean);
    }


    public void setBean(Object bean) {
        this.bean = bean;
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

    /**
     * replace the propertyResolver with one that does our bidding for
     * this test.
     */

    public void replacePropertyResolver(ActionEvent action) {
	FacesContext context = FacesContext.getCurrentInstance();
	Application app = context.getApplication();

	// see if we need to take action-
	if (null == context.getExternalContext().getSessionMap().get("systest.replacePropertyResolver")) {
	    final PropertyResolver oldProp = app.getPropertyResolver();
	    PropertyResolver
		newProp = new PropertyResolver() {
		    public Object getValue(Object base, Object property)
		    throws EvaluationException, PropertyNotFoundException {
			return oldProp.getValue(base, property);
		    }
		    
		    public Object getValue(Object base, int index)
		    throws EvaluationException, PropertyNotFoundException {
			return oldProp.getValue(base, index);
		    }
		    
		    public void setValue(Object base, Object property, Object value)
		    throws EvaluationException, PropertyNotFoundException {
			TestBean.this.setValueChangeMessage("setValue() called");
			oldProp.setValue(base, property, value);
		    }
		    
		    public void setValue(Object base, int index, Object value)
		    throws EvaluationException, PropertyNotFoundException {
			TestBean.this.setValueChangeMessage("setValue() called");
			oldProp.setValue(base, index, value);
		    }
		    
		    public boolean isReadOnly(Object base, Object property)
		    throws EvaluationException, PropertyNotFoundException {
			return oldProp.isReadOnly(base, property);
		    }
		    
		    public boolean isReadOnly(Object base, int index)
		    throws EvaluationException, PropertyNotFoundException {
			return oldProp.isReadOnly(base, index);
		    }
		    
		    public Class getType(Object base, Object property)
		    throws EvaluationException, PropertyNotFoundException {
			return oldProp.getType(base, property);
		    }
		    
		    public Class getType(Object base, int index)
		    throws EvaluationException, PropertyNotFoundException {
			return oldProp.getType(base, index);
		    }
		    
		};    
	    app.setPropertyResolver(newProp);
	    context.getExternalContext().getSessionMap().put("systest.replacePropertyResolver", oldProp);
	}
    }


    
    /**
     * restore the original PropertyResolver.
     */

    public void restorePropertyResolver(ActionEvent action) {
	FacesContext context = FacesContext.getCurrentInstance();
	Application app = context.getApplication();
	PropertyResolver oldProp = null;

	// see if we need to take action-
	if (null != (oldProp = (PropertyResolver) context.getExternalContext().getSessionMap().get("systest.replacePropertyResolver"))) {
	    app.setPropertyResolver(oldProp);
	    context.getExternalContext().getSessionMap().remove("systest.replacePropertyResolver");
	    setValueChangeMessage(null);

	}
    }

    protected HtmlCommandButton boundButton = new HtmlCommandButton();
    public HtmlCommandButton getBoundButton() {
	if (null != boundButton) {
	    boundButton.setValue("button label");
	}
	return boundButton;
    }
    
    public void setBoundButton(HtmlCommandButton newBoundButton) {
	boundButton = newBoundButton;
    }

    public String getFactoryPrintout() {
	String result = "";
	String [] factoryNames = {
	    FactoryFinder.APPLICATION_FACTORY,
	    FactoryFinder.FACES_CONTEXT_FACTORY,
	    FactoryFinder.LIFECYCLE_FACTORY,
	    FactoryFinder.RENDER_KIT_FACTORY
	};
	for (int i = 0; i < factoryNames.length; i++) {
	    result = result + 
		FactoryFinder.getFactory(factoryNames[i]).toString() + " ";
	}
	return result;
    }

	
}
