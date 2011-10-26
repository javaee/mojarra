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

package com.sun.faces.facelets.tag.jsf.core;

import com.sun.faces.application.ApplicationAssociate;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.SystemEvent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.component.UIViewRoot;
import javax.faces.event.PreRenderViewEvent;

/**
 * This is the TagHandler for the f:event tag.
 */
public class EventHandler extends TagHandler {
    protected final TagAttribute type;
    protected final TagAttribute listener;

    public EventHandler(TagConfig config) {
        super(config);
        this.type = this.getRequiredAttribute("type");
        this.listener = this.getRequiredAttribute("listener");
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        if (ComponentHandler.isNew(parent)) {
            Class<? extends SystemEvent> eventClass = getEventClass(ctx);
            UIViewRoot viewRoot = ctx.getFacesContext().getViewRoot();
            // ensure that f:event can be used anywhere on the page for preRenderView,
            // not just as a direct child of the viewRoot
            if (null != viewRoot && PreRenderViewEvent.class == eventClass &&
                parent != viewRoot) {
                parent = viewRoot;
            }
            if (eventClass != null) {
                parent.subscribeToEvent(eventClass,
                        new DeclarativeSystemEventListener(
                            listener.getMethodExpression(ctx, Object.class, new Class[] { ComponentSystemEvent.class }),
                            listener.getMethodExpression(ctx, Object.class, new Class[] { })));
            }
        }
    }

    protected Class<? extends SystemEvent> getEventClass(FaceletContext ctx) {
        String eventType = (String) this.type.getValueExpression(ctx, String.class).getValue(ctx);
        if (eventType == null) {
            throw new FacesException("Attribute 'type' can not be null");
        }

        return ApplicationAssociate.getInstance(ctx.getFacesContext().getExternalContext())
                .getNamedEventManager().getNamedEvent(eventType);
    }

}


class DeclarativeSystemEventListener implements ComponentSystemEventListener, Serializable {

    private static final long serialVersionUID = 8945415935164238908L;

    private MethodExpression oneArgListener;
    private MethodExpression noArgListener;

    // Necessary for state saving
    public DeclarativeSystemEventListener() {}

    public DeclarativeSystemEventListener(MethodExpression oneArg, MethodExpression noArg) {
        this.oneArgListener = oneArg;
        this.noArgListener = noArg;
    }

    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        final ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        try{
            noArgListener.invoke(elContext, new Object[]{});
        } catch (MethodNotFoundException mnfe) {
            // Attempt to call public void method(ComponentSystemEvent event)
            oneArgListener.invoke(elContext, new Object[]{event});
        } catch (IllegalArgumentException iae) {
            // Attempt to call public void method(ComponentSystemEvent event)
            oneArgListener.invoke(elContext, new Object[]{event});
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeclarativeSystemEventListener that = (DeclarativeSystemEventListener) o;

        if (noArgListener != null
            ? !noArgListener.equals(that.noArgListener)
            : that.noArgListener != null) {
            return false;
        }
        if (oneArgListener != null
            ? !oneArgListener.equals(that.oneArgListener)
            : that.oneArgListener != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = oneArgListener != null ? oneArgListener.hashCode() : 0;
        result = 31 * result + (noArgListener != null
                                ? noArgListener.hashCode()
                                : 0);
        return result;
    }
}
