/*
 * ResourceHandler.java
 *
 * Created on October 16, 2007, 12:29 PM
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

import java.io.IOException;

import javax.faces.context.FacesContext;


/**
 * RELEASE_PENDING (eburns,rogerk) - review docs
 * @since 2.0
 */
public abstract class ResourceHandler {


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Create a Resource instance that is capable of rendering a reference
     * to this resource that will ultimately be sent to the client.
     * Typically, this method will be called by Renderers and UIComponents
     * that want to reference resources during the "render response" phase
     * of the lifecycle for a view request.
     *
     * The returned Resource will use the above "Encoding Resource Identifiers"
     * algorithm in its implementation of getURI().
     *
     * @param resourceName the name of the resource.
     * @return
     */
    public abstract Resource createResource(String resourceName);


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * @param resourceName
     * @param libraryName
     * @return
     */
    public abstract Resource createResource(String resourceName,
                                            String libraryName);


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * @param resourceName
     * @param libraryName
     * @param contentType
     * @return
     */
    public abstract Resource createResource(String resourceName,
                                            String libraryName,
                                            String contentType);


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     *
     * @param context
     * @return
     */
    public abstract void handleResourceRequest(FacesContext context)
    throws IOException;


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Return true if the current request is a resource request.  This
     * method is called by ViewHandler.renderView() to determine if this
     * request is a view request or a resource request.  This must return
     * false if the viewId ends in the string ".class".  This satisfies
     * request [CompRes.R01.E].
     * edburns note: Again, likely Ajax spec interaction
     * This method could be made to do Ken's "resource prefix" idea.
     *
     * @param context
     * @return
     */
    public abstract boolean isResourceRequest(FacesContext context);

}
