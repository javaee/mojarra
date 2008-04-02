/*
 * $Id: MockApplication.java,v 1.12 2003/10/15 02:03:26 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.MessageResources;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.validator.Validator;


public class MockApplication extends Application {


    public MockApplication() {
        addComponent("TestNamingContainer",
                     "javax.faces.webapp.TestNamingContainer");
        addComponent("TestComponent", "javax.faces.webapp.TestComponent");
        MockMessageResources api = new MockMessageResources();
        api.addMessage(UISelectMany.INVALID_MESSAGE_ID,
                       "Invalid value(s) not in the option list");
        api.addMessage(UISelectOne.INVALID_MESSAGE_ID,
                       "Invalid value not in the option list");
        messageResourcess.put(MessageResources.FACES_API_MESSAGES, api);
    }


    private ActionListener actionListener = null;
    private static boolean processActionCalled = false;
    public ActionListener getActionListener() {
	if (null == actionListener) {
	    actionListener = new ActionListener() {
		    public void processAction(ActionEvent e) {
			processActionCalled = true;
		    }
		    public PhaseId getPhaseId() {
			return PhaseId.INVOKE_APPLICATION;
		    }

		    // see if the other object is the same as our
		    // anonymous inner class implementation.
		    public boolean equals(Object otherObj) {
			if (!(otherObj instanceof ActionListener)) {
			    return false;
			}
			ActionListener other = (ActionListener) otherObj;
			if (other.getPhaseId() != this.getPhaseId()) {
			    return false;
			}
			processActionCalled = false;
			other.processAction(null);
			boolean result = processActionCalled;
			processActionCalled = false;
			return result;
		    }
		};
	}
	
        return (this.actionListener);
    }
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }


    private NavigationHandler navigationHandler = null;
    public NavigationHandler getNavigationHandler() {
        return (this.navigationHandler);
    }
    public void setNavigationHandler(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }


    private PropertyResolver propertyResolver = null;
    public PropertyResolver getPropertyResolver() {
        return (this.propertyResolver);
    }
    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }


    public ValueBinding getValueBinding(String ref) {
        throw new UnsupportedOperationException();
    }


    private VariableResolver variableResolver = null;
    public VariableResolver getVariableResolver() {
        return (this.variableResolver);
    }
    public void setVariableResolver(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
    }

    private ViewHandler viewHandler = null;
    public ViewHandler getViewHandler() {
	if (null == viewHandler) {
	    viewHandler = new MockViewHandler();
	}
        return (this.viewHandler);
    }
    public void setViewHandler(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
    }

    private Map components = new HashMap();
    public void addComponent(String componentType, String componentClass) {
        components.put(componentType, componentClass);
    }
    public UIComponent createComponent(String componentType) {
        String componentClass = (String) components.get(componentType);
        try {
            Class clazz = Class.forName(componentClass);
            return ((UIComponent) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }
    public UIComponent createComponent(ValueBinding componentRef,
                                       FacesContext context,
                                       String componentType)
        throws FacesException {
	throw new FacesException(new UnsupportedOperationException());
    }
    public Iterator getComponentTypes() {
        return (components.keySet().iterator());
    }


    private Map converters = new HashMap();
    public void addConverter(String converterId, String converterClass) {
        converters.put(converterId, converterClass);
    }
    public void addConverter(Class targetClass, String converterClass) {
        throw new UnsupportedOperationException();
    }
    public Converter createConverter(String converterId) {
        String converterClass = (String) converters.get(converterId);
        try {
            Class clazz = Class.forName(converterClass);
            return ((Converter) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }
    public Converter createConverter(Class targetClass) {
        throw new UnsupportedOperationException();
    }
    public Iterator getConverterIds() {
        return (converters.keySet().iterator());
    }
    public Iterator getConverterTypes() {
        throw new UnsupportedOperationException();
    }


    private Map messageResourcess = new HashMap();
    public void addMessageResources(String messageResourcesId, String messageResourcesClass) {
        try {
            Class clazz = Class.forName(messageResourcesClass);
            messageResourcess.put(messageResourcesId, clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }
    public MessageResources getMessageResources(String messageResourcesId) {
        return ((MessageResources) messageResourcess.get(messageResourcesId));
    }
    public Iterator getMessageResourcesIds() {
        return (messageResourcess.keySet().iterator());
    }


    private Map validators = new HashMap();
    public void addValidator(String validatorId, String validatorClass) {
        validators.put(validatorId, validatorClass);
    }
    public Validator createValidator(String validatorId) {
        String validatorClass = (String) validators.get(validatorId);
        try {
            Class clazz = Class.forName(validatorClass);
            return ((Validator) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }
    public Iterator getValidatorIds() {
        return (validators.keySet().iterator());
    }

    public Iterator getSupportedLocales() {
	return Collections.EMPTY_LIST.iterator();
    }

    public void setSupportedLocales(Collection newLocales) {
    }

    public Locale getDefaultLocale(){
	return Locale.getDefault();
    }

    public void setDefaultLocale(Locale newLocale) {
    }




}
