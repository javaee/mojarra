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

package javax.faces.convert;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p>{@link Converter} implementation for <code>java.lang.Short</code>
 * (and short primitive) values.</p>
 */

public class ShortConverter implements Converter {

    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard converter id for this converter.</p>
     */
    public static final String CONVERTER_ID = "javax.faces.Short";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion to <code>Short</code> fails.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by an example value.</li>
     * <li><code>{2}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String SHORT_ID =
         "javax.faces.converter.ShortConverter.SHORT";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion of the <code>Short</code> value to
     * <code>String</code> fails.   The message format string for this message
     * may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> relaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String STRING_ID =
         "javax.faces.converter.STRING";

    // ------------------------------------------------------- Converter Methods

    /**
     * @throws ConverterException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) {

        if (context == null || component == null) {
            throw new NullPointerException();
        }

        // If the specified value is null or zero-length, return null
        if (value == null) {
            return (null);
        }
        value = value.trim();
        if (value.length() < 1) {
            return (null);
        }

        try {
            return (Short.valueOf(value));
        } catch (NumberFormatException nfe) {
            throw new ConverterException(MessageFactory.getMessage(
                 context, SHORT_ID, value, "32456",
                 MessageFactory.getLabel(context, component)), nfe);
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }

    /**
     * @throws ConverterException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    public String getAsString(FacesContext context, UIComponent component,
                              Object value) {

        if (context == null || component == null) {
            throw new NullPointerException();
        }

        // If the specified value is null, return a 
        // zero-length String
        if (value == null) {
            return "";
        }

        // If the incoming value is still a string, play nice
        // and return the value unmodified
        if (value instanceof String) {
            return (String) value;
        }

        try {
            return (Short.toString(((Number) value).shortValue()));
        } catch (Exception e) {
            throw new ConverterException(MessageFactory.getMessage(
                 context, STRING_ID, value,
                 MessageFactory.getLabel(context, component)), e);
        }
    }
}
