package com.sun.faces.taglib.html_basic;

import java.util.Iterator;

import javax.faces.*;

import com.sun.faces.taglib.FacesTag;

/**
 *
 *  <B>Output_TextTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ErrorsTag.java,v 1.1 2002/03/15 20:58:03 jvisvanathan Exp $
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
     * @param rc renderContext
     */

    public UIComponent newComponentInstance() 
    {
        UIOutput o = new UIOutput();
        MessageList ml = (MessageList)renderContext.getObjectManager()
        	.get(renderContext.getRequest(), MessageList.MESSAGE_LIST_ID);
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

} // end of class Output_TextTag
