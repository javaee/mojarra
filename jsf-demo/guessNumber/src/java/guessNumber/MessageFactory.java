/*
 * $Id: MessageFactory.java,v 1.3 2005/05/13 11:41:12 rogerk Exp $
 */
 
/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package guessNumber;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.Locale;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.FacesException;
import java.text.MessageFormat;
import java.io.IOException;

/**
 * 
 * <p>supported filters: <code>package</code> and
 * <code>protection</code>.</p>
 */

public class MessageFactory extends Object
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

    private MessageFactory() {
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    
    /**

    * This version of getMessage() is used in the RI for localizing RI
    * specific messages.

    */

     public static FacesMessage getMessage(String messageId, Object params[]) {
        Locale locale = null;
        FacesContext context = FacesContext.getCurrentInstance();
        // context.getViewRoot() may not have been initialized at this point.
        if (context != null && context.getViewRoot() != null) {
            locale = context.getViewRoot().getLocale();
            if (locale == null) {
                locale = Locale.getDefault();
            }
        } else {
            locale = Locale.getDefault();
        }
        
	return getMessage(locale, messageId, params);
    }

     public static FacesMessage getMessage(Locale locale, String messageId, 
					   Object params[]) {
	FacesMessage result = null;
	String 
	    summary = null,
	    detail = null,
	    bundleName = null;
	ResourceBundle bundle = null;

	// see if we have a user-provided bundle
	if (null != (bundleName = getApplication().getMessageBundle())) {
	    if (null != 
		(bundle = 
		 ResourceBundle.getBundle(bundleName, locale,
					  getCurrentLoader(bundleName)))) {
		// see if we have a hit
		try {
		    summary = bundle.getString(messageId);
                    detail = bundle.getString(messageId + "_detail");
		}
		catch (MissingResourceException e) {
		}
	    }
	}
	
	// we couldn't find a summary in the user-provided bundle
	if (null == summary) {
	    // see if we have a summary in the app provided bundle
	    bundle = ResourceBundle.getBundle(FacesMessage.FACES_MESSAGES, 
					      locale,
					      getCurrentLoader(bundleName));
	    if (null == bundle) {
		throw new NullPointerException();
	    }
	    // see if we have a hit
	    try {
		summary = bundle.getString(messageId);
                detail = bundle.getString(messageId + "_detail");
	    }
	    catch (MissingResourceException e) {
	    }
	}
	
	// we couldn't find a summary anywhere!  Return null
	if (null == summary) {
	    return null;
	}

	if (null == summary || null == bundle) {
            throw new NullPointerException(" summary " + summary + " bundle " + 
                bundle);
	}
	// At this point, we have a summary and a bundle.
        // 
        return (new BindingFacesMessage(locale, summary, detail, params));
    }


    //
    // Methods from MessageFactory
    // 
     public static FacesMessage getMessage(FacesContext context, String messageId) {
        return getMessage(context, messageId, null);
    }    
    
     public static FacesMessage getMessage(FacesContext context, String messageId,
					   Object params[]) {
        if (context == null || messageId == null ) {
            throw new NullPointerException(" context " + context + " messageId " + 
                messageId);
        }
        Locale locale = null;
        // viewRoot may not have been initialized at this point.
        if (context != null && context.getViewRoot() != null) {
            locale = context.getViewRoot().getLocale();
        } else {
            locale = Locale.getDefault();
        }
	if (null == locale) {
	    throw new NullPointerException(" locale " + locale);
	}
        FacesMessage message = getMessage(locale, messageId, params);
        if (message != null) {
            return message;
        }
        locale = Locale.getDefault();
        return (getMessage(locale, messageId, params));
    }  
    
     public static FacesMessage getMessage(FacesContext context, String messageId,
                                       Object param0) {
        return getMessage(context, messageId, new Object[]{param0});                                       
    }                                       
    
     public static FacesMessage getMessage(FacesContext context, String messageId,
                                       Object param0, Object param1) {
         return getMessage(context, messageId, new Object[]{param0, param1});                                        
    }                                       

     public static FacesMessage getMessage(FacesContext context, String messageId,
                                       Object param0, Object param1,
                                       Object param2) {
         return getMessage(context, messageId, 
             new Object[]{param0, param1, param2});                                        
    }                                       

     public static FacesMessage getMessage(FacesContext context, String messageId,
                                       Object param0, Object param1,
                                       Object param2, Object param3) {
         return getMessage(context, messageId, 
                 new Object[]{param0, param1, param2, param3});                                        
    }                                       

    // Gets the "label" property from the component.

    public static Object getLabel(FacesContext context, 
        UIComponent component) {
        Object o = component.getAttributes().get("label");
        if (o == null) {
            o = component.getValueBinding("label");
        }
        // Use the "clientId" if there was no label specified.
        if (o == null) {
            o = component.getClientId(context);
        }
        return o;
    }

    protected static Application getApplication() {
	FacesContext context = FacesContext.getCurrentInstance();
	if (context != null) {
	    return (FacesContext.getCurrentInstance().getApplication());
	}
	ApplicationFactory afactory = (ApplicationFactory)
	    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
	return (afactory.getApplication());
    }

    protected static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader =
	    Thread.currentThread().getContextClassLoader();
	if (loader == null) {
	    loader = fallbackClass.getClass().getClassLoader();
	}
	return loader;
    }

    /**
     * This class overrides FacesMessage to provide the evaluation
     * of binding expressions in addition to Strings.
     * It is often the case, that a binding expression may reference
     * a localized property value that would be used as a 
     * substitution parameter in the message.  For example:
     *  <code>#{bundle.userLabel}</code>
     * "bundle" may not be available until the page is rendered.
     * The "late" binding evaluation in <code>getSummary</code> and 
     * <code>getDetail</code> allow the expression to be evaluated
     * when that property is available.
     */
    static class BindingFacesMessage extends FacesMessage {
        BindingFacesMessage(
            Locale locale,
            String messageFormat,
            String detailMessageFormat,
            // array of parameters, both Strings and ValueBindings
            Object[] parameters) {

            super(messageFormat, detailMessageFormat);
            this.locale = locale;
            this.parameters = parameters;
            if (parameters != null) {
                resolvedParameters = new Object[parameters.length];
            }
        }

        public String getSummary() {
            String pattern = super.getSummary();
            resolveBindings();
            return getFormattedString(pattern, resolvedParameters);
        }

        public String getDetail() {
            String pattern = super.getDetail();
            resolveBindings();
            return getFormattedString(pattern, resolvedParameters);
        }

        private void resolveBindings() {
            FacesContext context = null;
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    Object o = parameters[i];
                    if (o instanceof ValueBinding) {
                        if (context == null) {
                            context = FacesContext.getCurrentInstance();
                        }
                        o = ((ValueBinding) o).getValue(context);
                    }
                    // to avoid 'null' appearing in message
                    if (o == null) {
                        o = "";
                    }
                    resolvedParameters[i] = o;
                }
            }
        }

        private String getFormattedString(String msgtext, Object[] params) {
            String localizedStr = null;
                                                                                
            if (params == null || msgtext == null ) {
                return msgtext;
            }
            StringBuffer b = new StringBuffer(100);
            MessageFormat mf = new MessageFormat(msgtext);
            if (locale != null) {
                mf.setLocale(locale);
                b.append(mf.format(params));
                localizedStr = b.toString();
            }
            return localizedStr;
        }

        private Locale locale;
        private Object[] parameters;
        private Object[] resolvedParameters;
    }
} // end of class MessageFactory
