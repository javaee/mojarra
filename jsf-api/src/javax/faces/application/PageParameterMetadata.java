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

import javax.faces.component.UIPageParameter;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">A metamodel which represents a {@link UIPageParameter}. Since a
 * {@link UIPageParameter} is a stateful component, it's not possible to store it in the Application object
 * where it would be shared by all clients. Instead, the metamodel stores the state of the component after
 * it is first built as a prototype to create instances on demand. Instances are expected to be created
 * by the {@link PageMetadata} via the Application object.</p>
 *
 * @author Dan Allen
 */
public class PageParameterMetadata {
    private String name;
    private Object state;

    public PageParameterMetadata(FacesContext context, UIPageParameter parameter) {
        this.name = parameter.getName();
        saveState(context, parameter);
    }

    public UIPageParameter restoreState(FacesContext context) {
        UIPageParameter p = new UIPageParameter();
        p.restoreState(context, state);
        return p;
    }

    protected void saveState(FacesContext context, UIPageParameter parameter) {
        state = parameter.saveState(context);
    }
}
