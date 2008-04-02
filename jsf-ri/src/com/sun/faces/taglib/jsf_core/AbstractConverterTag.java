package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;

import javax.faces.webapp.ConverterELTag;
import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.el.ValueExpression;
import javax.el.ELContext;
import javax.servlet.jsp.JspException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Ryan
 * Date: Feb 28, 2007
 * Time: 2:09:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractConverterTag extends ConverterELTag {

    private static final Logger LOGGER =
         Util.getLogger(Util.FACES_LOGGER + Util.TAGLIB_LOGGER);

    /**
     * <p>The {@link javax.el.ValueExpression} that evaluates to an object that
     * implements {@link javax.faces.convert.Converter}.</p>
     */
    protected ValueExpression binding = null;


    /**
     * <p>The identifier of the {@link javax.faces.convert.Converter}
     * instance to be created.</p>
     */
    protected ValueExpression converterId = null;

    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Set the expression that will be used to create a
     * {@link javax.el.ValueExpression} that references a backing bean property
     * of the {@link javax.faces.convert.Converter} instance to be created.</p>
     *
     * @param binding The new expression
     */
    public void setBinding(ValueExpression binding) {

        this.binding = binding;

    }


    /**
     * <p>Set the identifer of the {@link javax.faces.convert.Converter}
     * instance to be created.
     *
     * @param converterId The identifier of the converter instance to be
     *                    created.
     */
    public void setConverterId(ValueExpression converterId) {

        this.converterId = converterId;

    }

    // --------------------------------------------- Methods from ConverterELTag


    protected Converter createConverter() throws JspException {

        try {
            return createConverter(converterId,
                 binding,
                 FacesContext.getCurrentInstance());
        } catch (FacesException fe) {
            throw new JspException(fe.getCause());
        }

    }


    protected static Converter createConverter(ValueExpression converterId,
                                               ValueExpression binding,
                                               FacesContext facesContext) {

        ELContext elContext = facesContext.getELContext();
        Converter converter = null;

        // If "binding" is set, use it to create a converter instance.
        if (binding != null) {
            try {
                converter = (Converter) binding.getValue(elContext);
                if (converter != null) {
                    return converter;
                }
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }

        // If "converterId" is set, use it to create the converter
        // instance.  If "converterId" and "binding" are both set, store the
        // converter instance in the value of the property represented by
        // the ValueExpression 'binding'.
        if (converterId != null) {
            try {
                String converterIdVal = (String)
                     converterId.getValue(elContext);
                converter = facesContext.getApplication()
                     .createConverter(converterIdVal);
                if (converter != null && binding != null) {
                    binding.setValue(elContext, converter);
                }
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }

        if (converter == null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                     MessageUtils.getExceptionMessageString(
                          MessageUtils.CANNOT_CONVERT_ID,
                          converterId != null ? converterId.getExpressionString() : "",
                          binding != null ? binding.getExpressionString() : ""));
            }
        }

        return converter;

    }

}
