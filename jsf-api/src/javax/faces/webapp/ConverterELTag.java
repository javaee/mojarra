/*
 * $Id: ConverterELTag.java,v 1.5 2006/08/25 19:36:24 rogerk Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.webapp;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <p><strong>ConverterELTag</strong> is a base class for all JSP custom
 * actions that create and register a <code>Converter</code> instance on
 * the {@link ValueHolder} associated with our most immediate
 * surrounding instance of a tag whose implementation class is a
 * subclass of {@link UIComponentClassicTagBase}.  To avoid creating duplicate
 * instances when a page is redisplayed, creation and registration of a
 * {@link Converter} occurs <strong>only</strong> if the corresponding
 * {@link UIComponent} was created (by the owning {@link
 * UIComponentTag}) during the execution of the current page.</p>
 *
 * <p>This class may be used as a base class for tag instances that
 * support specific {@link Converter} subclasses.</p>
 *
 * <p>Subclasses of this class must implement the
 * <code>createConverter()</code> method, which creates and returns a
 * {@link Converter} instance.  Any configuration properties that specify
 * behavior of this {@link Converter} must have been set by the
 * <code>createConverter()</code> method.  Generally, this occurs
 * by copying corresponding attribute values on the tag instance.</p>
 *
 * <p>This tag creates no output to the page currently being created.  It
 * is used solely for the side effect of {@link Converter} creation.</p>
 *
 */

public abstract class ConverterELTag extends TagSupport {

    private static final Logger LOGGER =
          Logger.getLogger("javax.faces.webapp", "javax.faces.LogStrings");

    // ---------------------------------------------------------- Public Methods

    /**
     * <p>Create a new instance of the specified {@link Converter}
     * class, and register it with the {@link UIComponent} instance associated
     * with our most immediately surrounding {@link UIComponentClassicTagBase} instance, if
     * the {@link UIComponent} instance was created by this execution of the
     * containing JSP page.  If the <code>localValue</code> of the 
     * {@link UIComponent} is a String, attempt to convert it.
     * If the conversion fails and the component is an input component,
     * enqueue an appropriate error message by calling the <code>addMessage()</code> 
     * method on the <code>FacesContext</code>.  In all cases, log an 
     * appropriate error message.</p> 
     *
     * @throws JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        Converter converter = null;

        // Locate our parent UIComponentTag
        UIComponentClassicTagBase tag =
            UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
        if (tag == null) { // PENDING - i18n
            throw new JspException("Not nested in a UIComponentTag Error for tag with handler class:"+
                    this.getClass().getName());
        }

        // Nothing to do unless this tag created a component
        if (!tag.getCreated()) {
            return (SKIP_BODY);
        }

        UIComponent component = tag.getComponentInstance();
        if (component == null) {            
            //PENDING i18n
            throw new JspException("Can't create Component from tag.");
        }
        if (!(component instanceof ValueHolder)) {
            //PENDING i18n
            throw new JspException("Not nested in a tag of proper type. Error for tag with handler class:"+
                    this.getClass().getName());
        }
        
        converter = createConverter();
        
        if (converter == null) {
            throw new JspException("Can't create class of type:"+
                    " javax.faces.convert.Converter, converter is null");
        }
        
        ValueHolder vh = (ValueHolder)component;
        FacesContext context = FacesContext.getCurrentInstance();
        
        // Register an instance with the appropriate component
        vh.setConverter(converter);
        
        // Once the converter has been set, attempt to convert the
        // incoming "value"
        Object localValue = vh.getLocalValue();
        if (localValue instanceof String) {
            try {
                localValue = converter.getAsObject(context, (UIComponent)vh, (String) localValue);
                vh.setValue(localValue);
            }
            catch (ConverterException ce) {
                if (component instanceof UIInput) {
                    addConversionErrorMessage(context, component, ce);
                } 
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, "error.webapp.conversion_error_thrown",
                        new Object[]{ce.getMessage(), 
                        (String) localValue, 
                        component.getId()});
                } 
            }
        }        
  
        return (SKIP_BODY);

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Create and return a new {@link Converter} to be registered
     * on our surrounding {@link UIComponent}.</p>
     *
     * @throws JspException if a new instance cannot be created
     */
    protected abstract Converter createConverter()
        throws JspException;

    private void addConversionErrorMessage(FacesContext context,
            UIComponent component, ConverterException ce) {
        FacesMessage message = null;
        String converterMessageString = null;
        converterMessageString = ((UIInput)component).getConverterMessage();
        if (null != converterMessageString) {
            message = new FacesMessage(converterMessageString, converterMessageString);
        }
        if (message == null) {
            message = ce.getFacesMessage();
            if (message == null) {
                message = MessageFactory.getMessage(context,
                    UIInput.CONVERSION_MESSAGE_ID);
                if (message.getDetail() == null) {
                    message.setDetail(ce.getMessage());
                }
            }
        }
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage(component.getClientId(context), message);
    }

}
