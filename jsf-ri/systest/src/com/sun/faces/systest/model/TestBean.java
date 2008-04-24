/*
 * $Id: TestBean.java,v 1.30.12.2 2008/04/21 16:56:05 edburns Exp $
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
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
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
import java.util.Collections;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.ExternalContext;
import javax.faces.event.SystemEventListenerHolder;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Test JavaBean for managed object creation facility.</p>
 */
public class TestBean implements SystemEventListenerHolder {

    public List<SystemEventListener> getListenersForEventClass(Class<? extends SystemEvent> arg0) {
        return Collections.EMPTY_LIST;
    }
    
public enum Suit { Hearts, Clubs, Diamonds, Spades }
public enum Color { Red, Blue, Green, Orange }

    private Random random;
    private ArrayList newList1= new ArrayList();
    private ArrayList newList2= new ArrayList();
    ServletContext servletContext = null;
    
    public Suit returnSpades() {
        return Suit.Spades;
    }
    
    public Suit returnDiamonds() {
        return Suit.Diamonds;
    }

    public TestBean() {
    random = new Random(4143);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = (null != context) ? context.getExternalContext() : null;
        servletContext = (null != extContext) ? (ServletContext) extContext.getContext() : null;
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
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            Object responseWriter = context.getResponseWriter();
            if (null != responseWriter) {
                responseWriterInfo = responseWriter.getClass().getName();
            }
        }
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

    /**
     * Holds value of property setPropertyTarget.
     */
    private String setPropertyTarget = "default value";

    /**
     * Getter for property setPropertyTarget.
     * @return Value of property setPropertyTarget.
     */
    public String getSetPropertyTarget() {

        return this.setPropertyTarget;
    }

    /**
     * Setter for property setPropertyTarget.
     * @param setPropertyTarget New value of property setPropertyTarget.
     */
    public void setSetPropertyTarget(String setPropertyTarget) {

        this.setPropertyTarget = setPropertyTarget;
    }

    /**
     * Holds value of property counter.
     */
    private String counter = "0";
    private int counterInt = 0;

    /**
     * Getter for property counter.
     * @return Value of property counter.
     */
    public String getCounter() {
        counter = Integer.valueOf(counterInt++).toString();
        return counter;
    }

    /**
     * Holds value of property validatorMessage.
     */
    private String validatorMessage = "Validator Message Expression";

    /**
     * Getter for property validatorMessage.
     * @return Value of property validatorMessage.
     */
    public String getValidatorMessage() {

        return this.validatorMessage;
    }

    /**
     * Holds value of property converterMessage.
     */
    private String converterMessage = "Converter Message Expression";

    /**
     * Getter for property converterMessage.
     * @return Value of property converterMessage.
     */
    public String getConverterMessage() {

        return this.converterMessage;
    }

    public ArrayList getNewList1() {
        return newList1;
    }

    public ArrayList getNewList2() {
        return newList2;
    }

   public void valueChange1(ValueChangeEvent vce) {
        String newValue = vce.getNewValue().toString();
        if (newList1.size() == 3){
            newList1.clear();
        }
        newList1.add(newValue);
   }

   public void valueChange2(ValueChangeEvent vce) {
        String newValue = vce.getNewValue().toString();
        if (newList2.size() == 3){
            newList2.clear();
        }
        newList2.add(newValue);
   }

    private Integer selectedValue = new Integer(2);

    public Integer getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Integer selectedValue) {
        this.selectedValue = selectedValue;
    }

    public SelectItem[] getMySelectItems(){
        return new SelectItem[]{
            new SelectItem(new Integer(1),"1"),
            new SelectItem(new Integer(2),"2"),
            new SelectItem(new Integer(3),"3")
        };
    }

    private int intVal = 3;
    public int getInt() {
        return intVal;
    }


    public void setInt(int newIntVal) {
        intVal = newIntVal;
    }


    @PostConstruct
    public void postConstruct() {
        setPostConstructCalled(true);
        FacesContext context = FacesContext.getCurrentInstance();
        context.getApplication().publishEvent(TestBeanPostConstructEvent.class, this);
    }

    @PreDestroy
    public void preDestroy() {
        setPreDestroyCalled(true);
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            context.getApplication().publishEvent(TestBeanPreDestroyEvent.class, this);
        }
    }

    /**
     * Holds value of property postConstructCalled.
     */
    private boolean postConstructCalled  = false;

    /**
     * Getter for property postConstructCalled.
     * @return Value of property postConstructCalled.
     */
    public boolean isPostConstructCalled() {

        return this.postConstructCalled;
    }

    /**
     * Setter for property postConstructCalled.
     * @param postConstructCalled New value of property postConstructCalled.
     */
    public void setPostConstructCalled(boolean postConstructCalled) {

        this.postConstructCalled = postConstructCalled;
        appendStatusMessage("bean: " + getStringProperty() +
                            " postConstructCalled: " + postConstructCalled);

    }

    /**
     * Holds value of property preDestroyCalled.
     */
    private boolean preDestroyCalled  = false;

    /**
     * Getter for property preDestroyCalled.
     * @return Value of property preDestroyCalled.
     */
    public boolean isPreDestroyCalled() {

        return this.preDestroyCalled;
    }

    /**
     * Setter for property preDestroyCalled.
     * @param preDestroyCalled New value of property preDestroyCalled.
     */
    public void setPreDestroyCalled(boolean preDestroyCalled) {
        this.preDestroyCalled = preDestroyCalled;
        appendStatusMessage("bean: " + getStringProperty() +
                            " preDestroyCalled: " + preDestroyCalled);
    }

    public String invalidateSession() {
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        return null;
    }

    public String removeRequestBean() {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().remove("requestBean");
        return null;
    }

    public String removeRequestBean2() {
        HttpServletRequest request = (HttpServletRequest)
              FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.removeAttribute("requestBean");
        return null;
    }

    public String replaceRequestBean() {
        HttpServletRequest request = (HttpServletRequest)
            FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.setAttribute("requestBean", new TestBean());
        return null;
    }

    public String replaceRequestBean2() {
        HttpServletRequest request = (HttpServletRequest)
            FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Object oldValue = request.getAttribute("requestBean");
        request.setAttribute("requestBean", oldValue);
        return null;
    }

    public String removeSessionBean() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sessionBean");
        return null;
    }

    public String removeSessionBean2() {
        HttpSession request = (HttpSession)
              FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        request.removeAttribute("sessionBean");
        return null;
    }

    public String replaceSessionBean() {
        HttpSession session = (HttpSession)
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute("sessionBean", new TestBean());
        return null;
    }

    public String replaceSessionBean2() {
        HttpSession session = (HttpSession)
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Object oldValue = session.getAttribute("sessionBean");
        session.setAttribute("sessionBean", oldValue);
        return null;
    }

    public String removeApplicationBean() {
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().remove("applicationBean");
        return null;
    }

    public String removeApplicationBean2() {
        ServletContext request = (ServletContext)
              FacesContext.getCurrentInstance().getExternalContext().getContext();
        request.removeAttribute("applicationBean");
        return null;
    }

    public String replaceApplicationBean() {
        ServletContext application = (ServletContext)
            FacesContext.getCurrentInstance().getExternalContext().getContext();
        application.setAttribute("applicationBean", new TestBean());
        return null;
    }

    public String replaceApplicationBean2() {
        ServletContext application = (ServletContext)
            FacesContext.getCurrentInstance().getExternalContext().getContext();
        Object oldValue = application.getAttribute("applicationBean");
        application.setAttribute("applicationBean", oldValue);
        return null;
    }

    public String clearRequestMap() {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().clear();
        return null;
    }

    public String clearRequestMapTwice() {
        clearRequestMap();
        clearRequestMap();
        return null;
    }

    public String clearSessionMap() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return null;
    }

    public String clearSessionMapTwice() {
        clearSessionMap();
        clearSessionMap();
        return null;
    }

    public void appendStatusMessage(String message) {
        if (null == servletContext) {
            return;
        }
        String oldMessage = (String) servletContext.getAttribute("previousRequestStatus");
        oldMessage = (null != oldMessage) ? oldMessage + "\n": "";
        message = (null != message) ? message : "";
        oldMessage = oldMessage + message;
        servletContext.setAttribute("previousRequestStatus", oldMessage);

    }

    public String getAppendRequestMarker() {
        appendStatusMessage("-----------------");
        return "";
    }

    public String clearStatusMessage() {
        if (null != servletContext) {
            servletContext.removeAttribute("previousRequestStatus");
        }
        return null;
    }
    
    public Map getSelectItems() {
        Map<String,SpecialBean> map = new HashMap<String,SpecialBean>();
        map.put("key1", new SpecialBean("value1"));
        map.put("key2", new SpecialBean("value2"));
        map.put("key3", new SpecialBean("value3"));
        return map;        
    }
    
    private SpecialBean special;
    public void setSpecialModel(SpecialBean special) {
        this.special = special;        
    }
    
    public SpecialBean getSpecialModel() {
        return special;
    }

    /**
     * Holds value of property suit.
     */
    private Suit suit;

    /**
     * Getter for property suit.
     * @return Value of property suit.
     */
    public Suit getSuit() {
        return this.suit;
    }

    /**
     * Setter for property suit.
     * @param suit New value of property suit.
     */
    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    /**
     * Holds value of property referencedSuit.
     */
    private Suit referencedSuit;

    /**
     * Getter for property referencedSuit.
     * @return Value of property referencedSuit.
     */
    public Suit getReferencedSuit() {
        return this.referencedSuit;
    }

    /**
     * Setter for property referencedSuit.
     * @param referencedSuit New value of property referencedSuit.
     */
    public void setReferencedSuit(Suit referencedSuit) {
        this.referencedSuit = referencedSuit;
    }

    /**
     * Holds value of property color.
     */
    private Color color;

    /**
     * Getter for property color.
     * @return Value of property color.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Setter for property color.
     * @param color New value of property color.
     */
    public void setColor(Color color) {
        this.color = color;
    }

}
