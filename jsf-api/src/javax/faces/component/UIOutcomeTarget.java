/*
 * $Id: 
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

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;


/**
 * <p class="changed_added_2_0">This component is paired with the
 * <code>javax.faces.Button</code> or <code>javax.faces.Link</code>
 * renderers and uncapsulates properties relating to the rendering of
 * outcomes directly to the response.  This enables bookmarkability in
 * JSF applications.</p>
 *
 * @since 2.0
 */
public class UIOutcomeTarget extends UIOutput {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.OutcomeTarget";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.OutcomeTarget";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIOutcomeTarget} instance with default property
     * values.</p>
     */
    public UIOutcomeTarget() {
        super();
        setRendererType("javax.faces.Link");

    }


    // ------------------------------------------------------ Instance Variables


    private Boolean includeViewParams;
    private String outcome;
    private String fragment;

    // -------------------------------------------------------------- Properties


    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    
    /**
     * <p class="changed_added_2_0">Return whether or not the view
     * parameters should be encoded into the target url.</p>
     *
     * @since 2.0
     */
    public boolean isIncludeViewParams() {

        if (this.includeViewParams != null) {
            return this.includeViewParams;
        }

        ValueExpression ve = getValueExpression("includeViewParams");
        if (ve != null) {
            try {
                return Boolean.TRUE.equals(ve.getValue(getFacesContext().getELContext()));
            } catch (ELException e) {
                throw new FacesException(e);
            }

        } else {
            return false;
        }

    }

    /**
     * <p class="changed_added_2_0">Set whether or not the page
     * parameters should be encoded into the target url.</p>
     *
     * @param includeViewParams The state of the switch for encoding
     * page parameters
     *
     * @since 2.0
     */
    public void setIncludeViewParams(boolean includeViewParams) {

        this.includeViewParams = includeViewParams;

    }


    /**
     * <p class="changed_added_2_0">Returns the <code>outcome</code>
     * property of the <code>UIOutcomeTarget</code>. This value is
     * passed to the {@link javax.faces.application.NavigationHandler}
     * when resolving the target url of this component.</p>
     *
     * @since 2.0
     */
    public String getOutcome() {

        if (this.outcome != null) {
            return this.outcome;
        }

        ValueExpression ve = getValueExpression("outcome");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }

        } else {
            return null;
        }
    }

    /**
     * <p class="changed_added_2_0">Sets the <code>outcome</code>
     * property of the <code>UIOutcomeTarget</code>.  This value is
     * passed to the NavigationHandler when resolving the target url of
     * this component.</p>
     *
     * @since 2.0
     *
     * @param outcome the navigation outcome
     */
    public void setOutcome(String outcome) {

        this.outcome = outcome;

    }

    /**
     * <p class="changed_added_2_0">For HTML pages, represents the
     * fragement of the document that should be brought into focus when
     * the view is rendered.</p>

     * PENDING(edburns): move this property to the renderkit, removing
     * getter and setter here.
     *
     * @return the target view fragment
     *
     * @since 2.0
     */
    public String getFragment() {

        if (fragment != null) {
            return fragment;
        }

        ValueExpression ve = getValueExpression("fragment");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }

        } else {
            return null;
        }

    }


    /**
     * // RELEASE_PENDING (rogerk) review docs
     * <p>Sets the fragment of the document that should be brought into focus
     * when the view is rendered.</p>
     *
     * @param fragment the view fragment
     */
    public void setFragment(String fragment) {

        this.fragment = fragment;

    }


    // ----------------------------------------------------- StateHolder Methods


    private Object[] values;

    @Override
    public Object saveState(FacesContext context) {

        if (values == null) {
             values = new Object[3];
        }

        values[0] = super.saveState(context);
        values[1] = includeViewParams;
        values[2] = outcome;
        return values;

    }


    @Override
    public void restoreState(FacesContext context, Object state) {

        values = (Object[]) state;
        super.restoreState(context, values[0]);
        includeViewParams = (Boolean) values[1];
        outcome = (String) values[2];

    }

}
