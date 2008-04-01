/*
 * $Id: MessageRenderer.java,v 1.3 2002/04/05 19:41:16 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// MessageRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.FacesContext;
import javax.faces.Renderer;
import javax.faces.UIOutput;
import javax.faces.UIComponent;
import javax.faces.ObjectManager;
import javax.faces.MessageList;
import javax.faces.Message;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>MessageRenderer</B> renders messages in the MessageList.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: MessageRenderer.java,v 1.3 2002/04/05 19:41:16 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class MessageRenderer extends Object implements Renderer
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public MessageRenderer()
    {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init()
    {
        // super.init();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //
    
    public Iterator getSupportedAttributeNames(String componentType) throws FacesException {
        return null;
    }

    public Iterator getSupportedAttributes(String componentType) throws FacesException {
	return null;
    }

    public PropertyDescriptor getAttributeDescriptor(String attributeName)
	throws FacesException {
	return null;
    }


    public void renderStart(FacesContext fc, UIComponent c )
        throws IOException, FacesException { 

        ParameterCheck.nonNull(fc);
        ParameterCheck.nonNull(c);

        UIOutput label = null;
        if ( supportsComponentType(c)) {
            label = (UIOutput) c;
        } else {
            throw new FacesException("Invalid component type. Expected UIOutput");
        }
        ObjectManager om = fc.getObjectManager();
        Assert.assert_it(om != null );

        MessageList ml = (MessageList)om.get(fc.getRequest(),
                 Constants.MESSAGE_LIST_ID);
        if ( ml == null ) { 
            return;
        }
        Iterator it = ml.iterator();
        boolean first = true;
        StringBuffer text = new StringBuffer(300);
        while(it.hasNext()) {
            if (first) {
                text.append("<ul>");
                first = false;
            }
            text.append("<li>");
            Message msg = (Message) it.next();
            text.append(msg.toString() + " " + msg.getSecondLevelMessage());
        }
        if (first) {
            text.append("</ul>");
        }
        OutputMethod outputMethod = fc.getOutputMethod();
        Assert.assert_it(outputMethod != null );
        outputMethod.writeText(text.toString());
        outputMethod.flush();
    }

    public void renderChildren(FacesContext fc, UIComponent c) 
            throws IOException {
        return;
    }

    public void renderComplete(FacesContext fc, UIComponent c) 
            throws IOException,FacesException {
        return;
    }

    public boolean supportsComponentType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_UIOUTPUT)) {
            supports = true;
        }
        return supports;
    }
    
    public boolean supportsComponentType(UIComponent c) {
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof UIOutput ) {
            supports = true;
        }
        return supports;
    }


} // end of class MessageRenderer
