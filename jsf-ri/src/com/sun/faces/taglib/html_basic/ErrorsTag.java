package com.sun.faces.taglib.html_basic;

import java.util.Iterator;

import javax.faces.Constants;
import javax.faces.MessageList;
import javax.faces.UIOutput;
import javax.faces.Message;
import javax.faces.UIComponent;

import com.sun.faces.taglib.FacesTag;

/**
 *
 *  <B>Output_TextTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ErrorsTag.java,v 1.2 2002/04/05 19:41:18 jvisvanathan Exp $
 * 
 *
 */

public class ErrorsTag extends FacesTag
{
    private String value = null;

    //  Output_TextTag()
    public ErrorsTag()
    {
        super();
    }

    //
    // Class methods
    //

//
// Methods from FacesTag
//

    /**
     * Creates a Form component and sets renderer specific
     * properties.
     *
     * @param rc facesContext
     */

    public UIComponent newComponentInstance() 
    {
        UIOutput o = new UIOutput();
        MessageList ml = (MessageList)facesContext.getObjectManager()
        	.get(facesContext.getRequest(), Constants.MESSAGE_LIST_ID);
        if (ml == null)
        {
        	System.err.println("No message list objet in the request");
        	return o;
        }
        	
        
        Iterator it = ml.iterator();
        boolean first = true;
        StringBuffer b = new StringBuffer(300);
        while(it.hasNext())
        {
        	
        	if (first)
        	{
        		b.append("<ul>");
        		first = false;
        	}
        	b.append("<li>");
                Message msg = (Message) it.next();
        	b.append(msg.toString() + " " + 
                        msg.getSecondLevelMessage());
        }
        if (first)b.append("</ul>");
        o.setValue(b.toString());
        return o;
    }

    public String getRendererType() {
	return "MessageRenderer";
    }

    /**
     * Returns the value of the "value" attribute
     *
     * @return String value of "value" attribute
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the "value" attribute
     * @param value value of "value" attribute
     */
    public void setValue(String value) {
        this.value = value;
    }

} // end of class ErrorsTag
