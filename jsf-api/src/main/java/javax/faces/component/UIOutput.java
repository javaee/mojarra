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

package javax.faces.component;


import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;



/**
 * <p><strong class="changed_modified_2_0_rev_a">UIOutput</strong> is a
 * {@link UIComponent} that has a value, optionally retrieved from a
 * model tier bean via a value expression, that is displayed to the
 * user.  The user cannot directly modify the rendered value; it is for
 * display purposes only.</p>
 *
 * <p>During the <em>Render Response</em> phase of the request processing
 * lifecycle, the current value of this component must be
 * converted to a String (if it is not already), according to the following
 * rules:</p>
 * <ul>
 * <li>If the current value is not <code>null</code>, and is not already
 *     a <code>String</code>, locate a {@link Converter} (if any) to use
 *     for the conversion, as follows:
 *     <ul>
 *     <li>If <code>getConverter()</code> returns a non-null {@link Converter},
 *         use that one, otherwise</li>
 *     <li>If <code>Application.createConverter(Class)</code>, passing the
 *         current value's class, returns a non-null {@link Converter},
 *         use that one.</li>
 *     </ul></li>
 * <li>If the current value is not <code>null</code> and a {@link Converter}
 *     was located, call its <code>getAsString()</code> method to perform
 *     the conversion.</li>
 * <li>If the current value is not <code>null</code> but no {@link Converter}
 *     was located, call <code>toString()</code> on the current value to perform
 *     the conversion.</li>
 * </ul>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>javax.faces.Text</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIOutput extends UIComponentBase
    implements ValueHolder {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Output";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Output";


    enum PropertyKeys {
        value,
        converter
    }

    private Converter converter;


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIOutput} instance with default property
     * values.</p>
     */
    public UIOutput() {

        super();
        setRendererType("javax.faces.Text");

    }


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    // --------------------------------------- EditableValueHolder Properties


    public Converter getConverter() {

        if (this.converter != null) {
            return (this.converter);
        }
        return (Converter) getStateHelper().eval(PropertyKeys.converter);

    }


    public void setConverter(Converter converter) {

        clearInitialState();
        // we don't push the converter to the StateHelper
        // if it's been explicitly set (i.e. it's not a ValueExpression
        // reference
        this.converter = converter;

    }


    public Object getLocalValue() {

        return getStateHelper().get(PropertyKeys.value);

    }


    public Object getValue() {

        return getStateHelper().eval(PropertyKeys.value);

    }


    public void setValue(Object value) {

        getStateHelper().put(PropertyKeys.value, value);

    }


    /**
     * <p>
     * In addition to the actions taken in {@link UIComponentBase}
     * when {@link PartialStateHolder#markInitialState()} is called,
     * check if the installed {@link Converter} is a PartialStateHolder and
     * if it is, call {@link javax.faces.component.PartialStateHolder#markInitialState()}
     * on it.
     * </p>
     */
    @Override
    public void markInitialState() {

        super.markInitialState();
        Converter c = getConverter();
        if (c != null && c instanceof PartialStateHolder) {
            ((PartialStateHolder) c).markInitialState();
        }

    }


    @Override
    public void clearInitialState() {

        if (initialStateMarked()) {
            super.clearInitialState();
            Converter c = getConverter();
            if (c != null && c instanceof PartialStateHolder) {
                ((PartialStateHolder) c).clearInitialState();
            }
        }

    }


    @Override
    public Object saveState(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        Object[] values = (Object[]) super.saveState(context);
        Object converterState = null;
        if (converter != null) {
            if (!initialStateMarked()) {
                // we saving the full state
                converterState = saveAttachedState(context, converter);
            } else {
                if (converter instanceof StateHolder) {
                    StateHolder sh = (StateHolder) converter;
                    if (!sh.isTransient()) {
                        converterState = ((StateHolder) converter).saveState(context);
                    }
                }
            }
        }
        if (converterState != null || values != null) {
            return new Object[] { values, converterState };
        }
        return values;

    }

    
    @Override
    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (state == null) {
            return;
        }
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        Object converterState = values[1];
        if (converterState instanceof StateHolderSaver) {
            // this means full state was saved and as such
            // overwrite any existing converter with the saved
            // converter
            converter = (Converter) restoreAttachedState(context,
                                                         converterState);
        } else {
            // apply any saved state to the existing converter
            if (converterState != null && converter instanceof StateHolder) {
                ((StateHolder) converter)
                      .restoreState(context, converterState);
            }
        }

    }
}
