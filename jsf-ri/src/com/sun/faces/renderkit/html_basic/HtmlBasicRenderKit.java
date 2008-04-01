/*
 * $Id: HtmlBasicRenderKit.java,v 1.23 2002/03/11 23:25:34 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicRenderKit.java

package com.sun.faces.renderkit.html_basic;

import java.util.Iterator;
import java.util.Properties;
import java.io.InputStream;
import java.util.Vector;
import java.util.Enumeration;
import javax.servlet.ServletRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.RenderKit;
import javax.faces.Renderer;
import javax.faces.FacesException;
import javax.faces.ClientCapabilities;
import javax.faces.EventQueue;
import javax.faces.UIComponent;
import javax.faces.UICommand;
import javax.faces.UITextEntry;
import javax.faces.CommandEvent;
import javax.faces.ValueChangeEvent;
import javax.faces.RenderContext;
import javax.faces.Constants;
import javax.faces.UISelectBoolean;
import javax.faces.UISelectOne;
import javax.faces.EventContext;
import javax.faces.ObjectManager;

/**
 *
 *  <B>HtmlBasicRenderKit</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicRenderKit.java,v 1.23 2002/03/11 23:25:34 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HtmlBasicRenderKit extends RenderKit
{
//
// Protected Constants
//

//
// Class Variables
//

private static String KIT_NAME = "HTML_3_2_RENDERKIT";

/**

 * used to ensure thread safety.

 */

private static Object lock = KIT_NAME;

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

private Properties props;

//
// Constructors and Initializers    
//

public HtmlBasicRenderKit()
{
    super();
    loadProperties();
}



//
// Class methods
//

//
// General Methods
//

private void loadProperties() {
    if (null == props) {
	props = new Properties();
    }
	
    if (null == props) {
	return;
    }

    String propsName = HtmlBasicRenderKit.class.getName();
    propsName = propsName.replace('.', '/');
    propsName += ".properties";
    InputStream is = null;

    if (null == propsName) {
	return;
    }

    // stolen from PropertyMessageResources.java in Struts
    try {
	is = this.getClass().getClassLoader().getResourceAsStream(propsName);
	if (is != null) {
	    props.load(is);
	    is.close();
	}
    } catch (Throwable t) {
	System.out.println("Can't open properties: " + t.toString());
	if (is != null) {
	    try {
		is.close();
	    } catch (Throwable u) {
		;
	    }
	}
    }	
}
//
// Methods From RenderKit
//

public String getName() {
    return KIT_NAME;
}

public Iterator getSupportedComponentTypes() {
    Assert.assert_it(null != props);

    Iterator t = new PropertyNameIterator(props, "supportedComponentTypes");
    return t;
}

public Iterator getRendererTypesForComponent(String componentType) {
    ParameterCheck.nonNull(componentType);
    Assert.assert_it(null != props);
    
    Iterator t = new PropertyNameIterator(props, componentType );
    return t;
}

public Renderer getRenderer(String name) throws FacesException {
//    ParameterCheck.nonNull(name);
    
    Class rendererClass;
    Renderer result;

    String className = props.getProperty(name);
    Assert.assert_it(null != className);

    try {
	rendererClass = Class.forName(className);
	result = (Renderer) rendererClass.newInstance();
    }
    catch (IllegalAccessException e) {
	throw new FacesException("Can't create instance for " + 
				 name + ": " + e.getMessage());
    }
    catch (InstantiationException e) {
	throw new FacesException("Can't create instance for " + 
				 name + ": " + e.getMessage());
    }
    catch (ClassNotFoundException e) {
	throw new FacesException("Can't find class for " + 
				 name + ": " + e.getMessage());
    }
    return result;
}

public void queueEvents(EventContext eventContext) {

    String param_name = null;
    String param_value = null;
    String cmdName = null;
    UIComponent c = null;
    Vector cmd_events = new Vector();
    ObjectManager objectManager = null;
    RenderContext rc = null;
    EventQueue eventQueue = null;
    ServletRequest request = null; 
 
    // getServletRequest 
    request = eventContext.getRequest();
    Assert.assert_it(request != null );

    Enumeration param_names = request.getParameterNames();

    if ( param_names.hasMoreElements() ) {
        // get eventQueue
        eventQueue = eventContext.getEventQueue(); 
        Assert.assert_it(eventQueue != null);
 
        // getObjectManager
        objectManager = eventContext.getObjectManager();
        Assert.assert_it(objectManager != null );

        // get renderContext
        rc = (RenderContext)objectManager.get(request,
                Constants.REF_RENDERCONTEXT);
        Assert.assert_it( rc != null );
    }

    while ( param_names.hasMoreElements() ) {
        param_name = (String) param_names.nextElement();
        param_value = (String) request.getParameter(param_name);
        if ( param_value != null ) {
            param_value = param_value.trim();
            if ( param_value.equals("") ) {
                continue;
            }
        } else {
            continue;
        }

        // check to see if param_name is a hidden field. We use hidden
        // fields to track the status of the checkbox because HTML
        // doesn't send the status unless it is checked. 

        // Hidden field starts with facesCheckbox. For instance,
        // if the param_name which is name of the hidden field is 
        // facesCheckboxvalidUser, name of the checkbox will be
        // decoded as validUser.
        if ( param_name.startsWith(Constants.REF_HIDDENCHECKBOX) ) {
            // substring after facesCheckbox is the name of the
            // checkbox
            int name_idx = Constants.REF_HIDDENCHECKBOX.length();
            param_name = param_name.substring(name_idx);
        }
        c = (UIComponent)(objectManager.get(request,param_name));
        // if the component is not found then it might not have been a
        // faces component or it was not stored in OBJECTMANAGER because of the scope.
        if ( c == null ) {
            continue;
        }
        // if value changed, update the local value in the component's 
        // attribute set and create valueChangeEvent to add to eventQueue
        if ( ! (c instanceof UICommand ) ) {
            String old_value = (String) c.getValue(rc);
            if ( old_value == null || !(old_value.equals(param_value))) {
                ValueChangeEvent e =  new ValueChangeEvent(eventContext,
                        c, param_value);
                eventQueue.add(e);
                c.setValue(param_value);
            }    
	} else { 
            String command_name = ((UICommand)c).getCommandName();
            CommandEvent cmd_event =  new CommandEvent(eventContext, c, 
					       command_name);
            cmd_events.add(cmd_event);
        }
    }
    // add command events to the end of the queue
    for ( int i = 0; i < cmd_events.size(); ++i ) {
        CommandEvent cmd_event = (CommandEvent) cmd_events.elementAt(i);
        eventQueue.add(cmd_event);
    }
}
									
protected void initialize() {
}

protected void destroy() {
}

    // The test for this class is in TestRenderKit.java

} // end of class HtmlBasicRenderKit

class PropertyNameIterator extends Object implements Iterator {

boolean hasNext;
Properties props;
String propName;
String propValue;
int curIndex = 0;

public PropertyNameIterator(Properties p, String name) {
    ParameterCheck.nonNull(p);
    ParameterCheck.nonNull(name);
    props = p;
    propName = name;

    propValue = props.getProperty(propName);
    hasNext = true;
}

public boolean hasNext() {
    return hasNext;
}
    
public Object next() {
    String result = null;
    int oldIndex = curIndex;
    if (null == propName || null == propValue) {
	hasNext = false;
	return null;
    }
    curIndex = propValue.indexOf(',', oldIndex + 1);
    if (-1 != curIndex) {
	if (0 != oldIndex) {
	    oldIndex++;
	}
	result = propValue.substring(oldIndex, curIndex);
    }
    else {
	if (0 == oldIndex) {
	    oldIndex--;
	}
	result = propValue.substring(oldIndex + 1);
	hasNext = false;
    }
    return result;
}

public void remove() {
}

public String toString() {
    return propValue;
}

} // end of class PropertyNameIterator
