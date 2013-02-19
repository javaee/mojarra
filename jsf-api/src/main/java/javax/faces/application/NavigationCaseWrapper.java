/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
import java.util.List;
import java.util.Map;
import javax.faces.FacesWrapper;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_2">Provides a simple implementation of 
 * {@link NavigationCase} that can be subclassed by developers wishing to
 * provide specialized behavior to an existing {@link NavigationCase}
 * instance.  The default implementation of all methods is to call
 * through to the wrapped {@link NavigationCase} instance.</p>
 *
 * <p class="changed_added_2_2">Usage: extend this class and override 
 * {@link #getWrapped} to
 * return the instance being wrapping.</p>
 *
 * @since 2.2
 */
public abstract class NavigationCaseWrapper extends NavigationCase implements FacesWrapper<NavigationCase> {

    public NavigationCaseWrapper() {
		super((String) null, (String) null, (String) null, (String) null, (String) null,
                        (Map<String, List<String>>) null, false, false);
    }
    
    @Override
    public boolean equals(Object o) {
        return getWrapped().equals(o);
    }

    @Override
    public int hashCode() {
        return getWrapped().hashCode();
    }

    @Override
    public String toString() {
        return getWrapped().toString();
    }

    @Override
    public abstract NavigationCase getWrapped();

    @Override
    public URL getActionURL(FacesContext context) throws MalformedURLException {
        return getWrapped().getActionURL(context);
    }

    @Override
    public URL getBookmarkableURL(FacesContext context) throws MalformedURLException {
        return getWrapped().getBookmarkableURL(context);
    }

    @Override
    public Boolean getCondition(FacesContext context) {
        return getWrapped().getCondition(context);
    }

    @Override
    public String getFromAction() {
        return getWrapped().getFromAction();
    }

    @Override
    public String getFromOutcome() {
        return getWrapped().getFromOutcome();
    }

    @Override
    public String getFromViewId() {
        return getWrapped().getFromViewId();
    }

    @Override
    public Map<String, List<String>> getParameters() {
        return getWrapped().getParameters();
    }

    @Override
    public URL getRedirectURL(FacesContext context) throws MalformedURLException {
        return getWrapped().getRedirectURL(context);
    }

    @Override
    public URL getResourceURL(FacesContext context) throws MalformedURLException {
        return getWrapped().getResourceURL(context);
    }

    @Override
    public String getToViewId(FacesContext context) {
        return getWrapped().getToViewId(context);
    }

    @Override
    public String getToFlowDocumentId() {
        return getWrapped().getToFlowDocumentId();
    }

    @Override
    public boolean hasCondition() {
        return getWrapped().hasCondition();
    }

    @Override
    public boolean isIncludeViewParams() {
        return getWrapped().isIncludeViewParams();
    }

    @Override
    public boolean isRedirect() {
        return getWrapped().isRedirect();
    }

}
