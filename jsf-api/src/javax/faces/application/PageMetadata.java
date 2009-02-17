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

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIPageParameter;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">A metamodel that represents a page in a JSF application. A page maps one-to-one
 * with a viewId and stores information that is retrieved from the metadata facet within the UIViewRoot. This
 * metadata can be retrieved eagerly, when the application starts, or as needed. When the metadata is retrieved,
 * it should be stored in the Application object.</p>
 *
 * @author Dan Allen
 */
public class PageMetadata {
    private String viewId;
    private List<PageParameterMetadata> parameters;

    public PageMetadata(FacesContext context, String viewId, List<UIPageParameter> parameterComponents) {
        this.viewId = viewId;
        parameters = new ArrayList<PageParameterMetadata>();
        for (UIPageParameter c : parameterComponents) {
            parameters.add(new PageParameterMetadata(context, c));
        }
    }

    public String getViewId() {
        return this.viewId;
    }

    public List<UIPageParameter> restorePageParameters(FacesContext context, String viewId) {
        List<UIPageParameter> params = new ArrayList<UIPageParameter>();
        for (PageParameterMetadata metadata : parameters) {
            params.add(metadata.restoreState(context));
        }

        return params;
    }
}
