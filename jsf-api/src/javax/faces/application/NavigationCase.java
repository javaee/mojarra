/*
 * $Id: NavigationCase.java,v 1.13 2007/07/17 21:18:12 rlubke Exp $
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

package javax.faces.application;

/**
 * <p class="changed_added_2_0"><strong>NavigationCase</strong>
 * represents a <code>&lt;navigation-case&gt;</code> in the navigation
 * rule base, as well as the <code>&lt;from-view-id&gt;</code> inside
 * which this <code>&lt;navigation-case&gt;</code> is nested.</p>
 *
 * @since 2.0
 *
 */
public class NavigationCase {

    private final String fromViewId;
    private final String fromAction;
    private final String fromOutcome;
    private final String toViewId;
    private final boolean redirect;

    private String toString;
    private int hashCode;


    // ------------------------------------------------------------ Constructors


    /**
     * RELEASE_PENDING (edburns,rogerk) docs review
     * <p>
     * Construct a new <code>NavigationCase<code> based on the provided
     * arguments.
     * </p>
     *
     * @param fromViewId the view id being navigated from
     * @param fromAction the expression string of the invoked action that
     *  triggered the navigation
     * @param fromOutcome the outcome of action (if any)
     * @param toViewId the view id to be navigated to
     * @param redirect <code>true</code> if the new view should be navigated
     *  to via a {@link javax.faces.context.ExternalContext#redirect(String)}
     */
    public NavigationCase(String fromViewId,
                          String fromAction,
                          String fromOutcome,
                          String toViewId,
                          boolean redirect) {

        this.fromViewId = fromViewId;
        this.fromAction = fromAction;
        this.fromOutcome = fromOutcome;
        this.toViewId = toViewId;
        this.redirect = redirect;

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p class="changed_added_2_0">Return the
     * <code>&lt;from-view-id&gt;</code> of the
     * <code>&lt;navigation-rule&gt;</code> inside which this
     * <code>&lt;navigation-case&gt;</code> is nested.</p>
     *
     * @since 2.0
     */
    public String getFromViewId() {

        return fromViewId;

    }


    /**
     * <p class="changed_added_2_0">Return the <code>&lt;from-action&gt;
     * for this <code>&lt;navigation-case&gt;</code></code></p>
     *
     * @since 2.0
     */
    public String getFromAction() {

        return fromAction;

    }


    /**
     * <p class="changed_added_2_0">Return the <code>&lt;from-outcome&gt;
     * for this <code>&lt;navigation-case&gt;</code></code></p>
     *
     * @since 2.0
     */
    public String getFromOutcome() {

        return fromOutcome;

    }


    /**
     * <p class="changed_added_2_0">Return the <code>&lt;to-view-id&gt;
     * for this <code>&lt;navigation-case&gt;</code></code></p>
     *
     * @since 2.0
     */
    public String getToViewId() {

        return toViewId;

    }


    /**
     * <p class="changed_added_2_0">Return the <code>&lt;redirect&gt;
     * value for this <code>&lt;navigation-case&gt;</code></code></p>
     *
     * @since 2.0
     */

    public boolean isRedirect() {

        return redirect;

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
               && !(fromViewId != null
                    ? !fromViewId.equals(that.fromViewId)
                    : that.fromViewId != null)
               && !(toViewId != null
                    ? !toViewId.equals(that.toViewId)
                    : that.toViewId != null));

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
            result = 31 * result + (toViewId != null ? toViewId.hashCode() : 0);
            result = 31 * result + (redirect ? 1 : 0);
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
            sb.append(", toViewId='").append(toViewId).append('\'');
            sb.append(", redirect=").append(redirect);
            sb.append('}');
            toString = sb.toString();
        }
        return toString;

    }
    
}
