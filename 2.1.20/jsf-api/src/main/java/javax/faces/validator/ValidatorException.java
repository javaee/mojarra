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

import java.util.Collection;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;


/**
 * <p>A <strong class="changed_modified_2_0">ValidatorException</strong> is an exception
 * thrown by the <code>validate()</code> method of a
 * {@link Validator} to indicate that validation failed.
 */
public class ValidatorException extends FacesException {
    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new exception with the specified message and
     * no root cause.</p>
     *
     * @param message The message for this exception
     */
    public ValidatorException(FacesMessage message) {

        super(message.getSummary());
        this.message = message;
    }
    
    /**
     * <p class="changed_added_2_0">Allow this one exception to contain
     * multiple messages.</p>
     * @param messages
     * 
     * @since 2.0
     */

    public ValidatorException(Collection<FacesMessage> messages) {
        this.messages = messages;
    }

    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause   The root cause for this exception
     */
    public ValidatorException(FacesMessage message, Throwable cause) {

        super(message.getSummary(), cause);
        this.message = message;
    }

    /**
     * <p class="changed_added_2_0">Allow this one exception to contain
     * multiple messages, while passing on the root cause to the superclass</p>
     * @param messages the detail messages for this exception
     * @param cause the root cause for this exception
     * 
     * @since 2.0
     */

    public ValidatorException(Collection<FacesMessage> messages, Throwable cause) {
        super(messages.isEmpty() ? "" : messages.iterator().next().getSummary(),
              cause);
        this.messages = messages;
    }

    /**
     * <p class="changed_modified_2_0">Returns the <code>FacesMessage</code>
     * associated with 
     * the exception.  If this instance
     * was created with a constructor that takes 
     * <code>Collection&lt;FacesMessage&gt;</code>, this method returns the first
     * message in the <code>Collection</code></p>
     */
    public FacesMessage getFacesMessage() {
        FacesMessage result = this.message;
        if (null == result && null != this.messages && !this.messages.isEmpty()) {
            result = messages.iterator().next();
        }
        return result;
    }
    
    
    /**
     * <p class="changed_modified_2_0">If this instance was created with a 
     * constructor that takes 
     * <code>Collection&lt;FacesMessage&gt;</code>, this method returns the passed
     * collection, otherwise this method returns <code>null</code>.</p>
     * 
     * @since 2.0
     */

    public Collection<FacesMessage> getFacesMessages() {
        return this.messages;
    }

    private FacesMessage message;
    private Collection<FacesMessage> messages;
}
