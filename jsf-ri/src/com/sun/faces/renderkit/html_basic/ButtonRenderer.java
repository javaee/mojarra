/*
 * $Id: ButtonRenderer.java,v 1.66 2003/11/10 01:08:53 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ButtonRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Map;
import java.util.MissingResourceException;

import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import java.io.IOException;

import org.mozilla.util.Assert;


/**
 *
 *  <B>ButtonRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ButtonRenderer.java,v 1.66 2003/11/10 01:08:53 jvisvanathan Exp $
 *
 */

public class ButtonRenderer extends HtmlBasicRenderer {
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

    //
    // Class methods
    //

    //
    // General Methods
    //

    /** Follow the UE Spec for Button:
     * http://javaweb.sfbay.sun.com/engineering/jsue/j2ee/WebServices/
     * JavaServerFaces/uispecs/UICommand_Button.html
     */
    protected String padLabel(String label) {
	if (label.length() == 3) {
            label = "\u00a0\u00a0" + label + "\u00a0\u00a0";
	} else if (label.length() == 2) {
	    label = "\u00a0\u00a0\u00a0" + label + "\u00a0\u00a0\u00a0";
	}
	return label;
    }

    //
    // Methods From Renderer
    //

    public void decode(FacesContext context, UIComponent component) {
	if (context == null || component == null) {
	    throw new NullPointerException(Util.getExceptionMessage(
				    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
        if (Util.componentIsDisabledOnReadonly(component)) {
            return;
        } 


        // Was our command the one that caused this submission?
        // we don' have to worry about getting the value from request parameter
        // because we just need to know if this command caused the submission. We
        // can get the command name by calling currentValue. This way we can 
        // get around the IE bug.
        String clientId = component.getClientId(context);
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String value = (String)requestParameterMap.get(clientId);
        if (value == null) {
            if (requestParameterMap.get(clientId+".x") == null &&
                requestParameterMap.get(clientId+".y") == null) {
                return;
            }
        }

        String type = (String) component.getAttributes().get("type");
        if ((type != null) && (type.toLowerCase().equals("reset")) ) {
            return;
        }

	component.queueEvent(new ActionEvent(component));

        return;
    }
    
    public void encodeBegin(FacesContext context, UIComponent component) 
             throws IOException  {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        
        // Which button type (SUBMIT, RESET, or BUTTON) should we generate?
        String type = (String) component.getAttributes().get("type");
	    String styleClass = null;
        if (type == null) {
            type = "submit";
	    // This is needed in the decode method
	    component.getAttributes().put("type", type);
        }

        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
        
        String label = "";
        Object value = ((UICommand) component).getValue();
        if (value != null) {
              label = value.toString();
        } 
        String imageSrc = (String) component.getAttributes().get("image");
        writer.startElement("input", component);
        if (imageSrc != null) {
            writer.writeAttribute("type", "image", "type");
            writer.writeURIAttribute("src", imageSrc, "image");
            writer.writeAttribute("name", component.getClientId(context), "clientId");
         } else {
            writer.writeAttribute("type", type.toLowerCase(), "type");
            writer.writeAttribute("name", component.getClientId(context), "clientId");
            writer.writeAttribute("value", padLabel(label), "value");
         }

        Util.renderPassThruAttributes(writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);

        if (null != (styleClass = (String) 
            component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
	}
        writer.endElement("input");
    }
    
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    //
    // General Methods
    //

    /**
     * Obtain and return the image path for this {@link UIComponent}.
     * The attribute <code>image</code> will be checked first, if null,
     * then check for an image path contained in a resource bundle using
     * the key specified by the <code>imageKey</code> attribute.
     * @param context current FacesContext
     * @param component UIComponent
     * @return a non-encoded the path for this image, or null if it
     * cannot be determined
     */
    protected String getImageSrc(FacesContext context,
                                 UIComponent component) {
        String result = (String) component.getAttributes().get("image");

        if (result == null) {
            try {
                result = getKeyAndLookupInBundle(context, component,
                    "imageKey");
            } catch (MissingResourceException e) {
                // Do nothing since the absence of a resource is not an
                // error.
            }
        }
        if (result == null) {
            return result;
        }

        StringBuffer sb = new StringBuffer();
        if (result.charAt(0) == '0') {
            sb.append(context.getExternalContext().getRequestContextPath());
        }
        sb.append(result);
        return(sb.toString());

    }

    /**
     * <p>Returns a label for the button using the following algorithm:
     * <ul>
     * <li>Use the value, if not null, from getValue()</li>
     * <li>If a ResourceBundle is defined (i.e. the <code>key</code> and
     * <code>bundle</code> attributes are available, use the value associated
     * with the specified key</li>
     * <li>Obtain the value from available MessageFactory</li>
     * <li>If the all of the above lookups fail, a zero-length String will
     * returned</li>
     * </ul>
     * @param context current FacesContext
     * @param component UIComponent
     * @return the label for this component based on the alogrithm defined above
     * @throws java.io.IOException if an unexpected problem occurs during I/O operations
     */
    protected String getLabel(FacesContext context,
                              UIComponent component) throws IOException {
        String result = null;
        // First call getValue()
        Object value = ((UICommand) component).getValue();
        if (value != null) {
              result = value.toString();
        }

        if (result == null) {
            // no valueRef or explicit label
            try {
                result = getKeyAndLookupInBundle(context, component, "key");
            } catch (MissingResourceException e) {
                // Do nothing since the absence of a resource is not an
                // error.
            }
        }

        //PENDING (rlubke) MessageFactory lookup
        if (result == null) {
            // all lookups have failed
            result = "";
        }
        return result;
    }


} // end of class ButtonRenderer
