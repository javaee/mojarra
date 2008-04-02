/*
 * $Id: FormatValidatorTag.java,v 1.2 2003/05/20 23:08:49 jvisvanathan Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
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

package cardemo;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.webapp.ValidatorTag;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;

/**
 * FormatValidatorTag is the tag handler class for FormatValidator tag,
 * <code>format_validator</code>.
 *
 */

public class FormatValidatorTag extends ValidatorTag {
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
    protected String formatPatterns = null;
 
  
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public FormatValidatorTag() {
        super();
        super.setId("FormatValidator");
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public String getFormatPatterns() {
        return formatPatterns;
    }

    public void setFormatPatterns(String fmtPatterns){
       formatPatterns = fmtPatterns;
    }

    // 
    // Methods from ValidatorTag
    // 

    protected Validator createValidator() throws JspException
    {
        FormatValidator result = null;
        result = (FormatValidator) super.createValidator();
        Assert.assert_it(null != result);
        
        result.setFormatPatterns(formatPatterns);
        return result;
    }

} // end of class FormatValidatorTag
