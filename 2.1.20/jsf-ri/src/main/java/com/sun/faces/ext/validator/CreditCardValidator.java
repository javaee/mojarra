/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package com.sun.faces.ext.validator;

import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;
import java.util.Locale;
import java.io.Serializable;

/**
 * A Validator that checks against a Regular Expression (which is the pattern 
 * property).  The pattern must resolve to a String that follows the java.util.regex
 * standards.  
 * @author driscoll
 */
public class CreditCardValidator implements Validator, Serializable {

    /**
     * Validate a String against a regular expression pattern...  The full regex
     * pattern must be matched in order to pass the validation.
     * @param context Context of this request
     * @param component The component wrapping this validator 
     * @param obj A string which will be compared to the pattern property of this validator.  Must be a string.
     */
    public void validate(FacesContext context, UIComponent component, Object obj) {

        FacesMessage fmsg;

        Locale locale = context.getViewRoot().getLocale();

        if (obj == null) {
            return;
        }
        if (!(obj instanceof String)) {
            fmsg = MojarraMessageFactory.getMessage(locale,
                    "com.sun.faces.ext.validator.creditcardValidator.NOT_STRING",
                    (Object) null);
            throw new ValidatorException(fmsg);
        }

        String input = (String) obj;

        if (!input.matches("^[0-9\\ \\-]*$")) {
            fmsg = MojarraMessageFactory.getMessage(locale,
                    "com.sun.faces.ext.validator.creditcardValidator.INVALID_CHARS",
                    (Object) null);
            throw new ValidatorException(fmsg);
        }

        if (!luhnCheck(stripNonDigit(input))) {
            fmsg = MojarraMessageFactory.getMessage(locale,
                    "com.sun.faces.ext.validator.creditcardValidator.INVALID_NUMBER",
                    (Object) null);
            throw new ValidatorException(fmsg);            
        }
    }

    private String stripNonDigit(String s) {
        return s.replaceAll(" ", "").replaceAll("-", "");
    }

    private boolean luhnCheck(String number) {
        int sum = 0;

        boolean timestwo = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (timestwo) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            timestwo = !timestwo;
        }
        return sum % 10 == 0;
    }


}
