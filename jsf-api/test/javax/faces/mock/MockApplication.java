/*
 * $Id: MockApplication.java,v 1.6 2003/08/03 22:54:56 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.MessageResources;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
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
        addComponent("TestNamingContainer", "javax.faces.webapp.TestNamingContainer");
        addComponent("TestComponent", "javax.faces.webapp.TestComponent");
    }


    private ActionListener actionListener = null;
    public ActionListener getActionListener() {
	if (null == actionListener) {
	    actionListener = new ActionListener() {
		    public void processAction(ActionEvent e) {
			throw new UnsupportedOperationException();
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
			boolean exceptionThrown = false;
			if (other.getPhaseId() != this.getPhaseId()) {
			    return false;
			}
			try {
			    other.processAction(null);
			}
			catch (UnsupportedOperationException e) {
			    exceptionThrown = true;
			}
			return exceptionThrown;
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
    public UIComponent getComponent(String componentType) {
        String componentClass = (String) components.get(componentType);
        try {
            Class clazz = Class.forName(componentClass);
            return ((UIComponent) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }
    public UIComponent getComponent(ValueBinding componentRef,
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
    public Converter getConverter(String converterId) {
        String converterClass = (String) converters.get(converterId);
        try {
            Class clazz = Class.forName(converterClass);
            return ((Converter) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }
    public Iterator getConverterIds() {
        return (converters.keySet().iterator());
    }


    private Map messageResourcess = new HashMap();
    public void addMessageResources(String messageResourcesId, String messageResourcesClass) {
        messageResourcess.put(messageResourcesId, messageResourcesClass);
    }
    public MessageResources getMessageResources(String messageResourcesId) {
        String messageResourcesClass = (String) messageResourcess.get(messageResourcesId);
        try {
            Class clazz = Class.forName(messageResourcesClass);
            return ((MessageResources) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }
    public Iterator getMessageResourcesIds() {
        return (messageResourcess.keySet().iterator());
    }


    private Map validators = new HashMap();
    public void addValidator(String validatorId, String validatorClass) {
        validators.put(validatorId, validatorClass);
    }
    public Validator getValidator(String validatorId) {
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






}
