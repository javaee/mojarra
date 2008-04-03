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
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * RELEASE_PENDING (eburns,rogerk) - review docs
 *
 * @since 2.0
 */
public abstract class Resource {


    private String contentType;

    private String libraryName;

    private String resourceName;

    // ---------------------------------------------------------- Public Methods


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Return the content-type for this resource.
     * @return
     */
    public String getContentType() {

        return contentType;

    }


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * @param contentType
     */
    public void setContentType(String contentType) {

        this.contentType = contentType;

    }


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Return the libraryName for this resource.  May be null.
     * @return
     */
    public String getLibraryName() {

        return libraryName;

    }


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * @param libraryName
     */
    public void setLibraryName(String libraryName) {

        this.libraryName = libraryName;

    }


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Return the resourceName for this resource.  Will never be null.
     * @return
     */
    public String getResourceName() {

        return resourceName;

    }


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * @param resourceName
     */
    public void setResourceName(String resourceName) {

        this.resourceName = resourceName;

    }


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * If the current request is a resource request, return an InputStream
     * containing the bytes of the resource.  Otherwise, throw an IOException.
     * @return
     */
    public abstract InputStream getInputStream() throws IOException;


    /**
     * RELEASE_PENDING (edburns,roger) - review docs
     * @return
     */
    public abstract URL getURL();


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Returns a Map of response headers to be sent with this resource, or an
     * empty map if no response headers are to be sent.
     * @return
     */
    public abstract Map<String, String> getResponseHeaders();


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Return the relative URI for this resource following the algorithm specified
     * in ResourceHandler.  This URI, when resolved against the URI of the view
     * request will yield an absolute URI which will return the actual bytes of
     * the resource.
     * @return
     */
    public abstract String getURI();


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Call through to getURI() and return the result.
     * @return
     */
    public String toString() {
        return getURI();
    }


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Return true if the user agent requesting this resource needs an update.
     * Returns false otherwise.  If false, the caller should send a 304 not
     * modified to the client.
     * @return
     */
    public abstract boolean userAgentNeedsUpdate(FacesContext context);

}
