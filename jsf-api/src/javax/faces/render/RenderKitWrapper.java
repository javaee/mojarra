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

package javax.faces.render;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Iterator;
import javax.faces.FacesWrapper;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;

/**
 * <p class="changed_modified_2_0">Provides a simple implementation of 
 * {@link RenderKit} that
 * can be subclassed by developers wishing to provide specialized
 * behavior to an existing {@link RenderKit} instance.  The default
 * implementation of all methods is to call through to the wrapped
 * {@link RenderKit}.</p>
 *
 * <p class="changed_added_2_0">Usage: extend this class and override {@link #getWrapped} to
 * return the instance we are wrapping.</p>
 *
 * @since 2.0
 */
public abstract class RenderKitWrapper extends RenderKit implements FacesWrapper<RenderKit> {

    @Override
    public void addRenderer(String family, String rendererType, Renderer renderer) {
        getWrapped().addRenderer(family, rendererType, renderer);
    }

    @Override
    public ResponseStream createResponseStream(OutputStream out) {
        return getWrapped().createResponseStream(out);
    }

    @Override
    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String characterEncoding) {
        return getWrapped().createResponseWriter(writer, contentTypeList, characterEncoding);
    }

    @Override
    public Renderer getRenderer(String family, String rendererType) {
        return getWrapped().getRenderer(family, rendererType);
    }

    @Override
    public ResponseStateManager getResponseStateManager() {
        return getWrapped().getResponseStateManager();
    }

    public abstract RenderKit getWrapped();

    @Override
    public Iterator<String> getComponentFamilies() {
        return getWrapped().getComponentFamilies();
    }

    @Override
    public Iterator<String> getRendererTypes(String componentFamily) {
        return getWrapped().getRendererTypes(componentFamily);
    }


    
    
}
