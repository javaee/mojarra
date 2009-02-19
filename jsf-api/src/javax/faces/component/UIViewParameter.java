/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.component;

import java.io.IOException;
import java.util.List;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * <p class="changed_added_2_0"><strong>UIViewParameter</strong> represents a
 * binding between a request parameter and a model property or {@link UIViewRoot}
 * property. This is a bi-directional binding.</p>
 *
 * @author Dan Allen
 *
 * @since 2.0
 */
public class UIViewParameter extends UIInput {

    
    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.ViewParameter";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.ViewParameter";

    // ------------------------------------------------------ Instance Variables

    private Boolean hasStringConverter;
    
    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIViewParameter} instance with default
     * property values.</p>
     */
    public UIViewParameter() {

        super();
        setRendererType(null);

    }


    // ------------------------------------------------------ Instance Variables


    private String name;

    /**
     * <p>The raw value is the "implicit" binding for this page parameter. This property
     * maintains the submitted value of the page parameter for the duration of the request.
     * If the page parameter does not explicitly specify a value expression, then when the
     * request ends, this value is stored with the state of this component to use as the
     * submitted value on an ensuing postback.</p>
     */
    private transient String rawValue;


    // -------------------------------------------------------------- Properties


    @Override
    public String getFamily() {

        return (COMPONENT_FAMILY);

    }

    /**
     * <p>Return the request parameter name from which the value is retrieved.</p>
     */
    public String getName() {

        if (this.name != null) {
            return this.name;
        }

        ValueExpression ve = getValueExpression("name");
        if (ve != null) {
            try {
                return ((String) ve.getValue(getFacesContext().getELContext()));
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return null;
        }

    }


    /**
     * <p>Set the request parameter name from which the value is retrieved.</p>
     *
     * @param name The new request parameter name.
     */
    public void setName(String name) {

        this.name = name;

    }

    /**
     * The immediate setting is not relevant for page parameters and must be assumed to be false.
     */
    @Override
    public boolean isImmediate() {
        return false;
    }

    /**
     * For right now, we assume that the submitted value is always a string. However, we may want
     * to support a string array as well.
     */
    @Override
    public String getSubmittedValue() {
        return (String) super.getSubmittedValue();
    }

    // ----------------------------------------------------- UIComponent Methods


    // This is the "Apply Request Phase" step
    // QUESTION should we just override processDecodes() directly?
    // ANSWER: In this case, no.  We don't want to take responsibility for 
    // traversing any children we may have in the future.
    @Override
    public void decode(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // QUESTION can we move forward and support an array? no different than UISelectMany; perhaps need to know
        // if the value expression is single or multi-valued
        // ANSWER: I'd rather not right now.
        String paramValue = context.getExternalContext().getRequestParameterMap().get(getName());

        // submitted value will stay as previous value (null on initial request) if a parameter is absent
        if (paramValue != null) {
            setSubmittedValue(paramValue);
        }

        rawValue = (String) getSubmittedValue();
        setValid(true);

    }
    
    /**
     * <p class="changed_added_2_0">Specialize superclass behavior to treat
     * <code>null</code> differently.  In this class, a <code>null</code> value
     * along with the "required" flag being set to <code>true</code> will
     * cause a validation failure. </p>
     * @param context
     */

    @Override
    public void processValidators(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        // we have to override since UIInput assumes that a null value means don't check
        if (getSubmittedValue() == null && isRequired()) {
            String requiredMessageStr = getRequiredMessage();
            FacesMessage message;
            if (null != requiredMessageStr) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                           requiredMessageStr,
                                           requiredMessageStr);
            } else {
                message =
                     MessageFactory.getMessage(context, REQUIRED_MESSAGE_ID,
                          MessageFactory.getLabel(
                               context, this));
            }
            context.addMessage(getClientId(context), message);
            setValid(false);
            context.validationFailed();
            context.renderResponse();
        }
        else {
            super.processValidators(context);
        }
    }

    // This is the "Update Model Values" step
    @Override
    public void updateModel(FacesContext context) {
        super.updateModel(context);
        if (!hasValueExpression() && isValid() && isLocalValueSet()) {
            // QUESTION should this be done even when a value expression is present?
            // ANSWER: I don't see why not.
            context.getExternalContext().getRequestMap().put(getName(), getLocalValue());
        }
    }

    // This is called during the real "Render Response" phase
    @Override
    public void encodeAll(FacesContext context) throws IOException {
        if (context == null) {
            throw new NullPointerException();
        }

        // if there is a value expression, update page parameter w/ latest value after render
        // QUESTION is it okay that a null string value may be suppressing the page parameter value?
        // ANSWER: I'm not sure.
        setSubmittedValue(getStringValue(context));
    }

    public String getStringValue(FacesContext context) {
        return hasValueExpression() ? getStringValueFromModel(context) : rawValue;
    }

    public String getStringValueFromModel(FacesContext context)
        throws ConverterException {
        ValueExpression ve = getValueExpression();
        if (ve == null) {
            return null;
        }

        Object currentValue = ve.getValue(context.getELContext());

        // If there is a converter attribute, use it to to ask application
        // instance for a converter with this identifer.
        Converter c = getConverter();

        if (c == null) {
            // if value is null and no converter attribute is specified, then
            // return null (null has meaning for a page parameters; it means remove it).
            if (currentValue == null) {
                return null;
            }
            // Do not look for "by-type" converters for Strings
            if (currentValue instanceof String) {
                return (String) currentValue;
            }

            // if converter attribute set, try to acquire a converter
            // using its class type.

            Class converterType = currentValue.getClass();
            c = context.getApplication().createConverter(converterType);

            // if there is no default converter available for this identifier,
            // assume the model type to be String.
            if (c == null) {
                return currentValue.toString();
            }
        }

        return c.getAsString(context, this, currentValue);
    }

    public String getStringValueToTransfer(FacesContext context, List<UIViewParameter> viewParams) {
        for (UIViewParameter candidate : viewParams) {
            if (candidate.getName().equals(name)) {
                // QUESTION: should this be getStringValue()? That's how it is implemented in Seam
                // ANSWER: I don't know.
                return candidate.getStringValue(context);
            }
        }

        return null;
    }

    @Override
    protected Object getConvertedValue(FacesContext context, Object submittedValue)
          throws ConverterException {

        String newValue = (String) submittedValue;
        ValueExpression valueExpression = getValueExpression();
        Converter c = null;

        // If there is a converter attribute, use it to to ask application
        // instance for a converter with this identifer.
        c = getConverter();

        if (null == c && null != valueExpression) {
            Class converterType = valueExpression.getType(context.getELContext());
            // if converterType is null, assume the modelType is "String".
            if (converterType == null ||
                converterType == Object.class) {
                return newValue;
            }

            // If the converterType is a String, and we don't have a
            // converter-for-class for java.lang.String, assume the type is
            // "String".
            if (converterType == String.class && !hasStringConverter(context)) {
                return newValue;
            }

            // if getType returns a type for which we support a default
            // conversion, acquire an appropriate converter instance.
            try {
                c = context.getApplication().createConverter(converterType);
            } catch (Exception e) {
                return null;
            }
        }
        else if (c == null) {
            return newValue;
        }

        if (c != null) {
            // QUESTION do we still need this?
            //RequestStateManager.set(context, RequestStateManager.TARGET_COMPONENT_ATTRIBUTE_NAME, this);
            return c.getAsObject(context, this, newValue);
        } else {
            // throw converter exception.
            Object[] params = {
                  newValue,
                  "null Converter"
            };

            throw new ConverterException(MessageFactory.getMessage(
                  context, "com.sun.faces.TYPECONVERSION_ERROR", params));
        }

    }

    // ----------------------------------------------------- Helper Methods

    public boolean hasValueExpression() {
        return getValueExpression() != null;
    }

    /**
     * Return the value expression for the "value" attribute (yes, sort of confusing).
     */
    protected ValueExpression getValueExpression() {
        return getValueExpression("value");
    }

    protected boolean hasStringConverter(FacesContext context) {

        if (hasStringConverter == null) {
            hasStringConverter = (context.getApplication().createConverter(String.class) != null);
        }

        return hasStringConverter;

    }

    // ----------------------------------------------------- StateHolder Methods


    private Object[] values;

    @Override
    public Object saveState(FacesContext context) {

        if (values == null) {
             values = new Object[3];
        }

        values[0] = super.saveState(context);
        values[1] = name;
        values[2] = getSubmittedValue();
        return (values);

    }


    @Override
    public void restoreState(FacesContext context, Object state) {

        values = (Object[]) state;
        super.restoreState(context, values[0]);
        name = (String) values[1];
        setSubmittedValue(values[2]);

    }
    
    public static class Reference {
        
        private StateHolderSaver saver;
        private int indexInParent;
        private String viewIdAtTimeOfConstruction;
        
        public Reference(FacesContext context, 
                UIViewParameter param, 
                int indexInParent,
                String viewIdAtTimeOfConstruction)     {
            this.saver = new StateHolderSaver(context, param);
            this.indexInParent = indexInParent;
            this.viewIdAtTimeOfConstruction = viewIdAtTimeOfConstruction;
        }
        
        public UIViewParameter getUIViewParameter(FacesContext context) {
            UIViewParameter result = null;
            UIViewRoot root = context.getViewRoot();
            // If the view root is the same as when we were constructed...
            if (this.viewIdAtTimeOfConstruction.equals(root.getViewId())) {
                // get the actual page parameter from the tree...
                UIComponent metadataFacet = root.getFacet(UIViewRoot.METADATA_FACET_NAME);
                result = (UIViewParameter) metadataFacet.getChildren().get(indexInParent);
            } else {
                // otherwise, use the saved one
                result = (UIViewParameter) this.saver.restore(context);
            }
            
            return result;
        }
        
    }

}
