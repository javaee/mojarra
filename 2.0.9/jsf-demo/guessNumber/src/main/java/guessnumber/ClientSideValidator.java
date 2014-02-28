/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package guessnumber;

import java.text.MessageFormat;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.validator.FacesValidator;

/**
 * <p>This validator splits the work between the client and the server.</p>
 *
 * <p>This Validator, when processed by the runtime, will cause an external
 * script reference to be generated in the head refering to <code>js/validator.js</code>
 * (by viture of the @ResourceDepedency annotation associated with this Validator).
 * Additionally, this Validator is a <code>ComponentSystemEventListener</code>,
 * and as such when it's processed by the <code>ClientSideValidatorHandler</code>,
 * this Validator will be registered as a listener to its associated input
 * component listening for <code>BeforeRenderEvents</code>.  When the event
 * is triggered and this listener is invoked, the validator will add an
 * <code>onmouseout</code> event to be rendered by the output component.</p>
 */
@FacesValidator(value="ClientSideValidator")
@ResourceDependency(name="js/validator.js")
@SuppressWarnings("unused")
public class ClientSideValidator implements Validator,
                                            Serializable {

    private static final long serialVersionUID = -5174092316834520806L;
    
    private int minimum;
    private int maximum;


    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public void validate(FacesContext context,
                         UIComponent component,
                         Object value) throws ValidatorException {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        int converted = ((Number) value).intValue();
        if ((converted > maximum) || (converted < minimum)) {
            String message = MessageFormat
                  .format("Validation Error: {0} is not within the range of {1} and {2}",
                          converted, minimum, maximum);
            FacesMessage facesMessage =
                  new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                   message,
                                   message);
            throw new ValidatorException(facesMessage);
        }

    }

}
