/*
 * $Id: HtmlBasicRenderKit.java,v 1.12 2001/12/14 00:52:15 rogerk Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
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
import javax.faces.WComponent;
import javax.faces.WCommand;
import javax.faces.WTextEntry;
import javax.faces.CommandEvent;
import javax.faces.ValueChangeEvent;
import javax.faces.RenderContext;
import javax.faces.Constants;
import javax.faces.WSelectBoolean;
import javax.faces.WSelectOne;

import javax.faces.ObjectTable;

/**
 *
 *  <B>HtmlBasicRenderKit</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicRenderKit.java,v 1.12 2001/12/14 00:52:15 rogerk Exp $
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

public Iterator getRendererNamesForComponent(String componentType) {
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

public void queueEvents(ServletRequest request, EventQueue queue) {

    String param_name = null;
    String param_value = null;
    String cmdName = null;
    WComponent c = null;
    Vector cmd_events = new Vector();
    ObjectTable ot = null;
    RenderContext rc = null;
 
    Enumeration param_names = request.getParameterNames();

    if ( param_names.hasMoreElements() ) {
        // get ObjectTable
        ot = ObjectTable.getInstance();
        Assert.assert_it(ot != null );

        // get renderContext
        rc = (RenderContext)ot.get(request,
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

        // PENDING ( visvan ) type of the component and model should be
        // encoded as a hidden field so that it need not be obtained
        // from the ObjectTable since the component may not exist in
        // the pool. Also value should also be encoded as a hidden
        // field so that change in values can be detected without WComponent.

        c = (WComponent)(ot.get(request,param_name));
        // if the component is not found then it might not have been a
        // faces component or it was not stored in OT because of the scope.
        if ( c == null ) {
            continue;
        }

        // add value change events first. Because they should
        // be processed before command events.
        if ( c instanceof WTextEntry || c instanceof WSelectBoolean ||
	     c instanceof WSelectOne) {
             String old_value = null;
             String model_str = null;
             // check in for Component types will be removed
             // once we support encoding of types using 
             /// hidden fields.	
            if ( c instanceof WTextEntry ) {
                WTextEntry te = (WTextEntry) c;
                old_value = te.getText(rc);
                model_str = (String) te.getModel();
            } else if (c instanceof WSelectBoolean) {
                WSelectBoolean sb = (WSelectBoolean) c;
                boolean old_state = sb.isSelected(rc);
                old_value = String.valueOf(old_state);
                model_str = (String) sb.getModel();
            } else if (c instanceof WSelectOne) {
		WSelectOne so = (WSelectOne) c;
		old_value = (String) so.getSelectedValue(rc);
		model_str = (String) so.getSelectedValueModel();
	    }

            // construct value changed event objects and put in the queue.
            ValueChangeEvent e =  new ValueChangeEvent(request, param_name,
                        model_str, param_value);
            if ( old_value != null && old_value.compareTo(param_value) != 0 ) {
                queue.add(e);
            } else if ( old_value == null ) {
                queue.add(e);
            }
	} else if ( c instanceof WCommand) {
            CommandEvent e =  new CommandEvent(request, param_name, 
					       param_value);
            cmd_events.add(e);
        }

    }
    // add command events to the end of the queue
    for ( int i = 0; i < cmd_events.size(); ++i ) {
        CommandEvent e = (CommandEvent) cmd_events.elementAt(i);
        queue.add(e);
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
