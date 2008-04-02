/*
 * $Id: AttributeTag.java,v 1.16 2005/08/22 22:08:10 ofung Exp $
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


import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p>Tag implementation that adds an attribute with a specified name
 * and String value to the component whose tag it is nested inside,
 * if the component does not already contain an attribute with the
 * same name.  This tag creates no output to the page currently
 * being created.</p>
 *
 * @deprecated The Faces implementation must now provide the
 * implementation for this class.
 */

public class AttributeTag extends TagSupport {


    // ---------------------------------------------------------- Static Members


    private static final long serialVersionUID = -7782950243436672334L;


    // ------------------------------------------------------------- Attributes


    /**
     * <p>The name of the attribute to be created, if not already present.
     */
    private String name = null;


    /**
     * <p>Set the attribute name.</p>
     *
     * @param name The new attribute name
     */
    public void setName(String name) {

        this.name = name;

    }


    /**
     * <p>The value to be associated with this attribute, if it is created.</p>
     */
    private String value = null;



    /**
     * <p>Set the attribute value.</p>
     *
     * @param value The new attribute value
     */
    public void setValue(String value) {

        this.value = value;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Register the specified attribute name and value with the
     * {@link UIComponent} instance associated with our most immediately
     * surrounding {@link UIComponentTag} instance, if this {@link UIComponent}
     * does not already have a value for the specified attribute name.</p>
     *
     * @exception JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        // Locate our parent UIComponentTag
        UIComponentClassicTagBase tag =
            UIComponentTag.getParentUIComponentClassicTagBase(pageContext);
        if (tag == null) { // PENDING - i18n
            throw new JspException("Not nested in a UIComponentTag");
        }

        // Add this attribute if it is not already defined
        UIComponent component = tag.getComponentInstance();
        if (component == null) { // PENDING - i18n
            throw new JspException("No component associated with UIComponentTag");
        }

        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory exprFactory =
            context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();

        String nameVal = (String) 
                  exprFactory.createValueExpression(elContext, name, String.class)
                      .getValue(elContext);
        Object valueVal =
                exprFactory.createValueExpression(elContext, value, Object.class)
                    .getValue(elContext);

        if (component.getAttributes().get(nameVal) == null) {
            component.getAttributes().put(nameVal, valueVal);
        }
        return (SKIP_BODY);

    }


    public int doEndTag() throws JspException {
	this.release();
	return (EVAL_PAGE);
    }

    
    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.name = null;
        this.value = null;
    }

}
