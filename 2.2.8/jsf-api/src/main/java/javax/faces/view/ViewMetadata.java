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

package javax.faces.view;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIViewParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewAction;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0"/> <code>ViewMetadata</code> is
 * reponsible for extracting and providing view parameter metadata from
 * VDL views.  Because {@link ViewDeclarationLanguage#getViewMetadata}
 * is required to return <code>null</code> for JSP views and
 * non-<code>null</code> for views authored in Facelets for JSF 2, this
 * specification only applys to Facelets for JSF 2.  </p>
 *
 * @since 2.0
 */
public abstract class ViewMetadata {


    /**
     * <p class="changed_added_2_0"/>
     * @return the view ID for which this <code>ViewMetadata</code> instance
     *  was created
     */
    public abstract String getViewId();


    /**
     * <p class="changed_added_2_0"> Creates a new {@link UIViewRoot}
     * containing only view parameter metadata.  The processing of
     * building this <code>UIViewRoot</code> with metadata should not
     * cause any events to be published to the application.  The
     * implementation must call {@link FacesContext#setProcessingEvents}
     * passing <code>false</code> as the argument, at the beginning of
     * the method, and pass <code>true</code> to the same method at the
     * end.  The implementation must ensure that this happens regardless
     * of ant exceptions that may be thrown.</p>
     *
     * @param context the {@link FacesContext} for the current request
     * @return a <code>UIViewRoot</code> containing only view parameter metadata
     *  (if any)
     */
    public abstract UIViewRoot createMetadataView(FacesContext context);


    /**
     * <p class="changed_added_2_0"> Utility method to extract view
     * metadata from the provided {@link UIViewRoot}.  </p>
     *
     * @param root the {@link UIViewRoot} from which the metadata will
     * be extracted.
     *
     * @return a <code>Collection</code> of {@link UIViewParameter}
     * instances.  If the view has no metadata, the collection will be
     * empty.
     */
    public static Collection<UIViewParameter> getViewParameters(UIViewRoot root) {

        Collection<UIViewParameter> params;
        UIComponent metadataFacet = root.getFacet(UIViewRoot.METADATA_FACET_NAME);

        if (metadataFacet == null) {
            params = Collections.emptyList();
        } else {
            params = new ArrayList<UIViewParameter>();
            List<UIComponent> children = metadataFacet.getChildren();
            int len = children.size();
            for (int i = 0; i < len; i++) {
                UIComponent c = children.get(i);
                if (c instanceof UIViewParameter) {
                    params.add((UIViewParameter) c);
                }
            }
        }

        return params;

    }
    
    /**
     * <p class="changed_added_2_2"> Utility method to extract view
     * metadata from the provided {@link UIViewRoot}.  </p>
     *
     * @param root the {@link UIViewRoot} from which the metadata will
     * be extracted.
     *
     * @return a <code>Collection</code> of {@link UIViewAction}
     * instances.  If the view has no metadata, the collection will be
     * empty.
     */
    public static Collection<UIViewAction> getViewActions(UIViewRoot root) {
        Collection<UIViewAction> actions;
        UIComponent metadataFacet = root.getFacet(UIViewRoot.METADATA_FACET_NAME);

        if (metadataFacet == null) {
            actions = Collections.emptyList();
        } else {
            actions = new ArrayList<UIViewAction>();
            List<UIComponent> children = metadataFacet.getChildren();
            int len = children.size();
            for (int i = 0; i < len; i++) {
                UIComponent c = children.get(i);
                if (c instanceof UIViewAction) {
                    actions.add((UIViewAction) c);
                }
            }
        }
        
        return actions;
    }
    
    /**
     * <p class="changed_added_2_2">Utility method to determine if the 
     * the provided {@link UIViewRoot} has metadata.  The default implementation will 
     * return true if the provided {@code UIViewRoot} has a facet 
     * named {@link UIViewRoot#METADATA_FACET_NAME} and that facet has children.
     * It will return  false otherwise.</p>
     *
     * @param root the {@link UIViewRoot} from which the metadata will
     * be extracted from
     *
     * @return true if the view has metadata, false otherwise.
     */
    public static boolean hasMetadata(UIViewRoot root) {
        boolean result = false;
        
        UIComponent metadataFacet = root.getFacet(UIViewRoot.METADATA_FACET_NAME);
        if (null != metadataFacet) {
            result = 0 < metadataFacet.getChildCount();
        }
        
        return result;
    }


}
