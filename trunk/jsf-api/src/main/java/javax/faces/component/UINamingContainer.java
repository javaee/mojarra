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

import java.util.Collection;

import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;


/**
 * <p><strong class="changed_modified_2_0">UINamingContainer</strong> is a
 * convenience base class for components that wish to implement {@link
 * NamingContainer} functionality.</p>
 */

public class UINamingContainer extends UIComponentBase
      implements NamingContainer, UniqueIdVendor, StateHolder {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.NamingContainer";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.NamingContainer";

    /**
     * <p class="changed_added_2_0">The context-param that allows the separator
     * char for clientId strings to be set on a per-web application basis.</p>
     *
     * @since 2.0
     */
    public static final String SEPARATOR_CHAR_PARAM_NAME =
          "javax.faces.SEPARATOR_CHAR";


    enum PropertyKeys {
           lastId
    }


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UINamingContainer} instance with default property
     * values.</p>
     */
    public UINamingContainer() {

        super();
        setRendererType(null);

    }
    

    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }

    /**
     * <p class="changed_added_2_0">Return the character used to separate
     * segments of a clientId.  The implementation must determine if there is a
     * &lt;<code>context-param</code>&gt; with the value given by the value of
     * the symbolic constant {@link #SEPARATOR_CHAR_PARAM_NAME}.  If there is a
     * value for this param, the first character of the value must be returned
     * from this method.  Otherwise, the value of the symbolic constant {@link
     * NamingContainer#SEPARATOR_CHAR} must be returned.</p>
     *
     * @param context the {@link FacesContext} for the current request
     * @since 2.0
     */
    public static char getSeparatorChar(FacesContext context) {


        Character separatorChar =
              (Character) context.getAttributes().get(SEPARATOR_CHAR_PARAM_NAME);
        if (separatorChar == null) {
            String initParam = context.getExternalContext().getInitParameter(SEPARATOR_CHAR_PARAM_NAME);
            separatorChar = NamingContainer.SEPARATOR_CHAR;
            if (initParam != null) {
                initParam = initParam.trim();
                if (initParam.length() != 0) {
                    separatorChar = initParam.charAt(0);
                }
            }
            context.getAttributes().put(SEPARATOR_CHAR_PARAM_NAME, separatorChar);
        }
        return separatorChar;

    }

    /**
     * @see UIComponent#visitTree
     */
    @Override
    public boolean visitTree(VisitContext context, 
                               VisitCallback callback) {

        // NamingContainers can optimize partial tree visits by taking advantage
        // of the fact that it is possible to detect whether any ids to visit
        // exist underneath the NamingContainer.  If no such ids exist, there
        // is no need to visit the subtree under the NamingContainer.
        Collection<String> idsToVisit = context.getSubtreeIdsToVisit(this);
        assert(idsToVisit != null);

        // If we have ids to visit, let the superclass implementation
        // handle the visit
        if (!idsToVisit.isEmpty()) {
            return super.visitTree(context, callback);
        }

        // If we have no child ids to visit, just visit ourselves, if
        // we are visitable.
        if (isVisitable(context)) {
            FacesContext facesContext = context.getFacesContext();
            pushComponentToEL(facesContext, null);

            try {
                VisitResult result = context.invokeVisitCallback(this, callback);
                return (result == VisitResult.COMPLETE);
            }
            finally {
                popComponentFromEL(facesContext);
            }
        }

        // Done visiting this subtree.  Return false to allow 
        // visit to continue.
        return false;
    }

     public String createUniqueId(FacesContext context, String seed) {
        Integer i = (Integer) getStateHelper().get(PropertyKeys.lastId);
        int lastId = ((i != null) ? i : 0);
        getStateHelper().put(PropertyKeys.lastId,  ++lastId);
        return UIViewRoot.UNIQUE_ID_PREFIX + (seed == null ? lastId : seed);
     }

}

