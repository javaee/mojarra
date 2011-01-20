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

package javax.faces.application;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.List;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0"><strong>NavigationCase</strong>
 * represents a <code>&lt;navigation-case&gt;</code> in the navigation
 * rule base, as well as the <code>&lt;from-view-id&gt;</code> inside
 * which this <code>&lt;navigation-case&gt;</code> is nested.</p>
 *
 * @since 2.0
 */
public class NavigationCase {

    private final String fromViewId;
    private final String fromAction;
    private final String fromOutcome;
    private final String condition;
    private final String toViewId;
    private final Map<String,List<String>> parameters;
    private final boolean redirect;
    private final boolean includeViewParams;

    private ValueExpression toViewIdExpr;
    private ValueExpression conditionExpr;
    private String toString;
    private int hashCode;


    // ------------------------------------------------------------ Constructors


    /**
     * <p class="changed_added_2_0"> Construct a new
     * <code>NavigationCase</code> based on the provided arguments.  See
     * section JSF.7.4.2 for how a <code>NavigationCase</code> is used
     * by the standard {@link ConfigurableNavigationHandler}</p>
     *
     * @param fromViewId return from {@link #getFromViewId}
     * @param fromAction return from {@link #getFromAction}
     * @param fromOutcome return from {@link #getFromOutcome}
     * @param condition A string to be interpreted as a
     * <code>ValueExpression</code> by a call to {@link #getCondition}
     * @param toViewId return from {@link #getToViewId}
     * @param parameters return from {@link #getParameters}
     * @param redirect return from {@link #isRedirect}
     * @param includeViewParams return {@link #isIncludeViewParams}
     */
    public NavigationCase(String fromViewId,
                          String fromAction,
                          String fromOutcome,
                          String condition,
                          String toViewId,
                          Map<String,List<String>> parameters,
                          boolean redirect,
                          boolean includeViewParams) {

        this.fromViewId = fromViewId;
        this.fromAction = fromAction;
        this.fromOutcome = fromOutcome;
        this.condition = condition;
        this.toViewId = toViewId;
        this.parameters = parameters;
        this.redirect = redirect;
        this.includeViewParams = includeViewParams;

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p class="changed_added_2_0">Construct an absolute URL to this
     * <code>NavigationCase</code> instance using {@link
     * javax.faces.application.ViewHandler#getActionURL} on the path
     * portion of the url.</p>
     *
     * @param context the {@link FacesContext} for the current request
     *
     * @throws MalformedURLException if the process of constructing the
     * URL causes this exception to be thrown.
     */
    public URL getActionURL(FacesContext context) throws MalformedURLException {

        ExternalContext extContext = context.getExternalContext();
        return new URL(extContext.getRequestScheme(),
                extContext.getRequestServerName(),
                extContext.getRequestServerPort(),
                context.getApplication().getViewHandler().getActionURL(context, getToViewId(context)));
        
    }


    /**
     * <p class="changed_added_2_0">Construct an absolute URL to this
     * <code>NavigationCase</code> instance using {@link
     * javax.faces.application.ViewHandler#getResourceURL} on the path
     * portion of the url.</p>
     *
     * @param context the {@link FacesContext} for the current request
     *
     * @throws MalformedURLException if the process of constructing the
     * URL causes this exception to be thrown.
     */
    public URL getResourceURL(FacesContext context) throws MalformedURLException {
        
        ExternalContext extContext = context.getExternalContext();

        return new URL(extContext.getRequestScheme(),
                extContext.getRequestServerName(),
                extContext.getRequestServerPort(),
                context.getApplication().getViewHandler().getResourceURL(context, getToViewId(context)));

    }


    /**
     * <p class="changed_added_2_0">Construct an absolute URL suitable for a
     * "redirect" to this <code>NavigationCase</code> instance using {@link
     * javax.faces.application.ViewHandler#getRedirectURL} on the path
     * portion of the url.</p>
     *
     * @param context the {@link FacesContext} for the current request
     *
     * @throws MalformedURLException if the process of constructing the
     * URL causes this exception to be thrown.
     */
    public URL getRedirectURL(FacesContext context) throws MalformedURLException {

        ExternalContext extContext = context.getExternalContext();

        return new URL(extContext.getRequestScheme(),
                extContext.getRequestServerName(),
                extContext.getRequestServerPort(),
                context.getApplication().getViewHandler().getRedirectURL(context,
                                                                         getToViewId(context),
                                                                         getParameters(),
                                                                         isIncludeViewParams()));

    }


    /**
     * <p class="changed_added_2_0">Construct an absolute URL suitable for a
     * bookmarkable link to this <code>NavigationCase</code> instance using {@link
     * javax.faces.application.ViewHandler#getBookmarkableURL} on the path
     * portion of the url.  This URL may include view parameters specified
     * as metadata within the view.</p>
     *
     * @param context the {@link FacesContext} for the current request
     *
     * @throws MalformedURLException if the process of constructing the
     * URL causes this exception to be thrown.
     */
    public URL getBookmarkableURL(FacesContext context) throws MalformedURLException {

        ExternalContext extContext = context.getExternalContext();
        return new URL(extContext.getRequestScheme(),
                extContext.getRequestServerName(),
                extContext.getRequestServerPort(),
                context.getApplication().getViewHandler().getBookmarkableURL(context,
                                                                             getToViewId(context),
                                                                             getParameters(),
                                                                             isIncludeViewParams()));
    }

    
    /**
     * <p class="changed_added_2_0">Return the
     * <code>&lt;from-view-id&gt;</code> of the
     * <code>&lt;navigation-rule&gt;</code> inside which this
     * <code>&lt;navigation-case&gt;</code> is nested.</p>
     */
    public String getFromViewId() {

        return fromViewId;

    }


    /**
     * <p class="changed_added_2_0">Return the <code>&lt;from-action&gt;
     * for this <code>&lt;navigation-case&gt;</code></code></p>
     */
    public String getFromAction() {

        return fromAction;

    }


    /**
     * <p class="changed_added_2_0">Return the <code>&lt;from-outcome&gt;
     * for this <code>&lt;navigation-case&gt;</code></code></p>
     */
    public String getFromOutcome() {

        return fromOutcome;

    }


    /**
     * <p class="changed_added_2_0">Evaluates the <code>&lt;to-view-id&gt;</code>
     * for this <code>&lt;navigation-case&gt;</code></p>
     *
     * @param context the {@link FacesContext} for the current request
     *
     * @return the view ID that should be navigated to
     */
    public String getToViewId(FacesContext context) {

        if (toViewIdExpr == null) {
            ExpressionFactory factory =
                  context.getApplication().getExpressionFactory();
            toViewIdExpr = factory.createValueExpression(context.getELContext(),
                                                         toViewId,
                                                         String.class);
        }
        String toViewId = (String) toViewIdExpr.getValue(context.getELContext());
        if (toViewId.charAt(0) != '/') {
            toViewId = '/' + toViewId;
        }

        return toViewId;

    }


    /**
     * <p class="changed_added_2_0">Test if this navigation case has an
     * associated <code>&lt;if&gt;</code> element.
     *
     * @return <code>true</code> if there's an <code>&lt;if&gt;</code>
     *  element associated with this <code>&lt;navigation-case&gt;</code>,
     *  otherwise <code>false</code>
     */
    public boolean hasCondition() {

        return (condition != null);

    }


    /**
     * <p class="changed_added_2_0">Evaluates the
     * <code>&lt;if&gt;</code> for this
     * <code>&lt;navigation-case&gt;</code>, if any.  The expression to
     * be evaluated is passed into the constructor as a string.  When
     * the expression is evaluated, its value must be coerced into a
     * <code>boolean</code> per the normail EL coercion rules.</p>
     *
     * @param context the {@link FacesContext} for the current request
     *
     * @return <code>null</code> if there is no <code>&lt;if&gt;</code> element
     *  associated with this <code>&lt;navigation-case&gt;</code>, otherwise
     *  return the evaluation result of the condition
     *
     * @throws any exceptions encountered during the process of
     * evaluating the expression or obtaining its value.
     */
    public Boolean getCondition(FacesContext context) {

        if (conditionExpr == null && condition != null) {
            ExpressionFactory factory =
                  context.getApplication().getExpressionFactory();
            conditionExpr = factory.createValueExpression(context.getELContext(),
                                                          condition,
                                                          Boolean.class);
        }

        return ((conditionExpr != null)
                ? (Boolean) conditionExpr.getValue(context.getELContext())
                : null);

    }


    /**
     * <p class="changed_added_2_0">Return the parameters to be included
     * for navigation cases requiring a redirect.  If no parameters are
     * defined, <code>null</code> will be returned.  The keys in the
     * <code>Map</code> are parameter names.  For each key, the
     * corresponding value is a <code>List</code> of unconverted
     * values.</p>
     */
    public Map<String, List<String>> getParameters() {

        return parameters;

    }


    /**
     * <p class="changed_added_2_0">Return the
     * <code>&lt;redirect&gt;</code> value for this
     * <code>&lt;navigation-case&gt;</code>.  This will be
     * <code>true</code> if the new view should be navigated to via a
     * {@link javax.faces.context.ExternalContext#redirect(String)}</p>
     */
    public boolean isRedirect() {

        return redirect;

    }


    /**
     * <p class="changed_added_2_0">Return the
     * <code>&lt;redirect&gt;</code> value for this
     * <code>&lt;navigation-case&gt;</code>.  This will be
     * <code>true</code> if the view parametets should be encoded into
     * the redirect URL (only applies to redirect case)</p>
     */
    public boolean isIncludeViewParams() {

        return includeViewParams;

    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NavigationCase that = (NavigationCase) o;

        return (redirect == that.redirect
               && !(fromAction != null
                    ? !fromAction.equals(that.fromAction)
                    : that.fromAction != null)
               && !(fromOutcome != null
                    ? !fromOutcome.equals(that.fromOutcome)
                    : that.fromOutcome != null)
               && !(condition != null
                    ? !condition.equals(that.condition)
                    : that.condition != null)
               && !(fromViewId != null
                    ? !fromViewId.equals(that.fromViewId)
                    : that.fromViewId != null)
               && !(toViewId != null
                    ? !toViewId.equals(that.toViewId)
                    : that.toViewId != null)
               && !(parameters != null
                    ? !parameters.equals(that.parameters)
                    : that.parameters != null));

    }

    
    @Override
    public int hashCode() {

        if (hashCode == 0) {
            int result = fromViewId != null ? fromViewId.hashCode() : 0;
            result = 31 * result + (fromAction != null
                                    ? fromAction.hashCode()
                                    : 0);
            result = 31 * result + (fromOutcome != null
                                    ? fromOutcome.hashCode()
                                    : 0);
            result = 31 * result + (condition != null
                                    ? condition.hashCode()
                                    : 0);
            result = 31 * result + (toViewId != null ? toViewId.hashCode() : 0);
            result = 31 * result + (redirect ? 1 : 0);
            result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
            hashCode = result;
        }
        return hashCode;

    }


    @Override
    public String toString() {

        if (toString == null) {
            StringBuilder sb = new StringBuilder(64);
            sb.append("NavigationCase{");
            sb.append("fromViewId='").append(fromViewId).append('\'');
            sb.append(", fromAction='").append(fromAction).append('\'');
            sb.append(", fromOutcome='").append(fromOutcome).append('\'');
            sb.append(", if='").append(condition).append('\'');
            sb.append(", toViewId='").append(toViewId).append('\'');
            sb.append(", faces-redirect=").append(redirect);
            sb.append(", includeViewParams=").append(includeViewParams).append('\'');
            sb.append(", parameters=").append(((parameters != null) ? parameters.toString() : ""));
            sb.append('}');
            toString = sb.toString();
        }
        return toString;

    }
 
}
