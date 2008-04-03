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

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * RELEASE_PENDING (edburns, rogerk) review and update
 * <p/>
 * <p>Provides a simple implementation of {@link Resource} that can be
 * subclassed by developers wishing to provide specialized behavior to an
 * existing {@link Resource} instance.  The default implementation of all
 * methods is to call through to the wrapped {@link Resource}.</p>
 * <p/>
 * <p>Usage: extend this class and override {@link #getWrapped} to return the
 * instance we are wrapping.</p>
 *
 * @since 2.0
 */
public abstract class ResourceWrapper extends Resource {

    /**
     * RELEASE_PENDING (edburns,roger) document
     *
     * @return
     */
    protected abstract Resource getWrapped();


    // --------------------------------------------------- Methods from Resource


    /**
     * RELEASE_PENDING (edburns,roger) document
     * @return
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException {

        return getWrapped().getInputStream();

    }


    /**
     * RELEASE_PENDING (edburns,roger) document
     * @return
     */
    public URL getURL() {

        return getWrapped().getURL();

    }


    /**
     * RELEASE_PENDING (edburns,roger) document
     * @return
     */
    public Map<String, String> getResponseHeaders() {

        return getWrapped().getResponseHeaders();

    }


    /**
     * RELEASE_PENDING (edburns,roger) document
     * @return
     */
    public String getRequestPath() {

        return getWrapped().getRequestPath();

    }


    /**
     * RELEASE_PENDING (edburns,roger) document
     * @param context
     * @return
     */
    public boolean userAgentNeedsUpdate(FacesContext context) {

        return getWrapped().userAgentNeedsUpdate(context);
        
    }

}
