/*
 * $Id: ElTagParserImpl.java,v 1.5 2003/11/06 22:40:16 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import java.io.IOException;
import javax.faces.application.Application;
import com.sun.faces.RIConstants;
import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.ExpressionEvaluator;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.util.Util;
import org.xml.sax.Attributes;

import com.sun.faces.taglib.ValidatorInfo;
import com.sun.faces.taglib.TagParser;


/**
 *
 * <p>Parses tags and checks the expression enabled tags for valid 
 * expression syntax</p>
 *
 */
public class ElTagParserImpl implements TagParser {

    //*********************************************************************
    // Constants

    final String JSF_COMPONENTREF_QN = "componentRef";
    final String JSF_VALUEREF_QN = "valueRef";
    final String JSF_ACTIONREF_QN = "actionRef";
    final String JSF_ID_QN = "id";

    //*********************************************************************
    // Validation and configuration state (protected)

    private ValidatorInfo validatorInfo;
    private StringBuffer failureMessages;
    private boolean failed;

    //*********************************************************************
    // Constructor and lifecycle management

    /**
     * <p>ElTagParserImpl constructor</p>
     *
     */
    public ElTagParserImpl() {
        failed = false;
        failureMessages = new StringBuffer();
    }

    /**
     * <p>Set the validator info object that has the current tag 
     * information</p>
     *
     * @param ValidatorInfo object with current tag info
     *
     */
    public void setValidatorInfo(ValidatorInfo validatorInfo) {
        this.validatorInfo = validatorInfo;
    }

    /**
     * <p>Get the failure message</p>
     *
     * @return String Failure message
     */
    public String getMessage() {
	return failureMessages.toString();
    }

    /**
     * <p>Return false if validator conditions have not been met</p>
     *
     * @return boolean false if validation conditions have not been met
     */
    public boolean hasFailed() {
        return failed;
    }


    /**
     * <p>Parse the starting element. If it is an expression enabled tag
     * make sure that the expression syntax is valid</p>
     */
    public void parseStartElement() {
        String qn = validatorInfo.getQName();
        Attributes attrs = validatorInfo.getAttributes();

        for (int i = 0; i < attrs.getLength(); i++) {
            String value = attrs.getValue(i);
            String qname = attrs.getQName(i);

            //check to see if attribute has an expression
            if (Util.isElExpression(value)) {
                ExpressionEvaluator evaluator = 
                    Util.getExpressionEvaluator(RIConstants.JSP_EL_PARSER);
                ExpressionInfo exprInfo = new ExpressionInfo();
                exprInfo.setExpressionString(value);
                try {
                    evaluator.parseExpression(exprInfo);
                } catch (ElException ex) {
                    failed = true;
                    buildErrorMessage(qn, qname, value);
                }
            }
            else if (qname.equals(JSF_COMPONENTREF_QN) ||
                     qname.equals(JSF_VALUEREF_QN) ||
                     qname.equals(JSF_ACTIONREF_QN)) {
                try {
                    Application.getCurrentInstance().
                        getValueBinding(value);
                } catch (Throwable ex) {
                    failed = true;
                    buildErrorMessage(qn, qname, value);
                }
            }
        }
    }

    /**
     * <p>Parse the end element</p>
     */
    public void parseEndElement() {
       //no parsing required
    }


    //*********************************************************************
    // Private methods

    /**
     * Build the validation error message.
     *
     * @param tag Tag element name
     * @param attr Tag attribute name
     * @param value Tag value
     */
    private void buildErrorMessage(String tag, String attr, String value) {
        StringBuffer tagBuf = new StringBuffer();
        tagBuf.append("'&lt;");
        tagBuf.append(tag);
        tagBuf.append(" "); 
        tagBuf.append(attr);
        tagBuf.append("=&quot;");
        tagBuf.append(value);
        tagBuf.append("&quot;/&gt;'\n");

  	Object[] obj = new Object[1];
        obj[0] = tagBuf;
        failureMessages.append(
            Util.getExceptionMessage(Util.VALIDATION_EL_ERROR_ID, obj));
        failureMessages.append("\n");
    }
}
