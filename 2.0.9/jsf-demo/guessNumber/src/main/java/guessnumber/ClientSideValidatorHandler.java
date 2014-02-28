/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package guessnumber;

import java.io.IOException;

import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.PreRenderComponentEvent;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.ValidatorConfig;
import javax.faces.view.facelets.ValidatorHandler;


/**
 * <p>A custom ValidateHandler that registers the created Validator as
 * a listener for the <code>PreRenderComponentEvent</code> so that the Validator
 * can take custom action upon the rendering of the parent component.</p>
 *
 * NOTE:  These APIs <em>WILL</em> be changing so if you write similar code,
 * expect breakage when the next EDR comes out.
 */
public class ClientSideValidatorHandler extends ValidatorHandler {

    public ClientSideValidatorHandler(ValidatorConfig config) {
        super(config);
    }


    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        super.apply(ctx, parent);

        // only process if it's been created
        if (parent.getParent() == null) {
            parent.subscribeToEvent(PreRenderComponentEvent.class,
                                    new PreRenderListener());
        }
    }
    
    
    public static class PreRenderListener implements ComponentSystemEventListener, Serializable {
        private static final long serialVersionUID = 0L;
        public void processEvent(ComponentSystemEvent event)
                throws AbortProcessingException {
            UIComponent c = (UIComponent) event.getSource();
            c.getAttributes().put("onmouseout", "validate('" + c.getClientId(FacesContext.getCurrentInstance()) + "')");
        }
    }

    
}
