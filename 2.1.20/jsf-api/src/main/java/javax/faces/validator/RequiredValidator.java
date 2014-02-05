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

package javax.faces.validator;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;

/**
 * <p class="changed_added_2_0">A Validator that checks for an empty
 * value in the same way that UIInput checks for a value. In fact, this validator
 * is equivalent to setting the required attribute on the input component to true.</p>
 *
 * @since 2.0
 */
public class RequiredValidator implements Validator {

    /**
     * <p>The standard converter id for this converter.</p>
     */
    public static final String VALIDATOR_ID = "javax.faces.Required";

    /**

     * </p>Verify that the converted object value is not null.</p>

     * @param context {@inheritDoc}
     * @param component {@inheritDoc}
     * @param value {@inheritDoc}

     * @throws ValidatorException   {@inheritDoc}

     */
    public void validate(FacesContext context,
                         UIComponent component,
                         Object value) {

        if (UIInput.isEmpty(value)) {
            FacesMessage msg;
            String requiredMessageStr = null;
            if (component instanceof UIInput) {
                requiredMessageStr = ((UIInput) component).getRequiredMessage();
            }

            // respect the message string override on the component to emulate required="true" behavior
            if (requiredMessageStr != null) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, requiredMessageStr, requiredMessageStr);
            }
            else {
                msg = MessageFactory.getMessage(context, UIInput.REQUIRED_MESSAGE_ID,
                    MessageFactory.getLabel(context, component));
            }

            throw new ValidatorException(msg);
        }
    }

}
