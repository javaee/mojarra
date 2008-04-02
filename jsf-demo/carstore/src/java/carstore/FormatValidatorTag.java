/*
 * $Id: FormatValidatorTag.java,v 1.3 2006/03/09 01:17:30 rlubke Exp $
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

package carstore;


import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorELTag;
import javax.servlet.jsp.JspException;

/**
 * FormatValidatorTag is the tag handler class for FormatValidator tag,
 * <code>format_validator</code>.
 */

public class FormatValidatorTag extends ValidatorELTag {

    private static final String VALIDATOR_ID = "FormatValidator";
    protected ValueExpression formatPatterns = null;


    public FormatValidatorTag() {
        super();
    }


    public ValueExpression getFormatPatterns() {
        return formatPatterns;
    }


    public void setFormatPatterns(ValueExpression formatPatterns) {
        this.formatPatterns = formatPatterns;
    }

    protected Validator createValidator() throws JspException {
        FormatValidator result =
              (FormatValidator) FacesContext.getCurrentInstance()
                    .getApplication()
                    .createValidator(VALIDATOR_ID);

        result.setFormatPatterns(formatPatterns);
        return result;
    }

} // end of class FormatValidatorTag
