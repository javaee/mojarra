/*
 * $Id: ValueHolder.java,v 1.20 2007/04/27 22:00:05 ofung Exp $
 */

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


import javax.faces.convert.Converter;
import javax.el.ValueExpression;


/**
 * <p><strong>ValueHolder</strong> is an interface that may be implemented
 * by any concrete {@link UIComponent} that wishes to support a local
 * value, as well as access data in the model tier via a <em>value
 * expression</em>, and support conversion 
 * between String and the model tier data's native data type.
 */

public interface ValueHolder {


    // -------------------------------------------------------------- Properties

    /**
     * <p>Return the local value of this {@link UIComponent} (if any),
     * without evaluating any associated {@link ValueExpression}.</p>
     */
    public Object getLocalValue();


    /**
     * <p>Gets the value of this {@link UIComponent}.  First, consult
     * the local value property of this component.  If
     * non-<code>null</code> return it.  If <code>null</code>, see if we have a
     * {@link ValueExpression} for the <code>value</code> property.  If
     * so, return the result of evaluating the property, otherwise
     * return <code>null</code>.  Note that because the specification for {@link
     * UIComponent#setValueBinding} requires a
     * call through to {@link UIComponent#setValueExpression}, legacy
     * tags will continue to work.</p>
     */
    public Object getValue();


    /**
     * <p>Set the value of this {@link UIComponent} (if any).</p>
     *
     * @param value The new local value
     */
    public void setValue(Object value);



    /**
     * <p>Return the {@link Converter} (if any)
     * that is registered for this {@link UIComponent}.</p>
     */
    public Converter getConverter();


    /**
     * <p>Set the {@link Converter} (if any)
     * that is registered for this {@link UIComponent}.</p>
     *
     * @param converter New {@link Converter} (or <code>null</code>)
     */
    public void setConverter(Converter converter);
}
