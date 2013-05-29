/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU General
 * Public License Version 2 only ("GPL") or the Common Development and
 * Distribution License("CDDL") (collectively, the "License"). You may not use
 * this file except in compliance with the License. You can obtain a copy of the
 * License at https://glassfish.dev.java.net/public/CDDLGPL_1_1.html or
 * packager/legal/LICENSE.txt. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception: Oracle designates this particular file as subject to
 * the "Classpath" exception as provided by Oracle in the GPL Version 2 section
 * of the License file that accompanied this code.
 *
 * Modifications: If applicable, add the following below the License Header,
 * with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s): If you wish your version of this file to be governed by only
 * the CDDL or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution under the
 * [CDDL or GPL Version 2] license." If you don't indicate a single choice of
 * license, a recipient has the option to distribute your version of this file
 * under either the CDDL, the GPL Version 2 or to extend the choice of license
 * to its licensees as provided above. However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright holder.
 */

package com.sun.faces.test.agnostic.dynamic;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

@FacesComponent( value = "com.sun.faces.test.agnostic.dynamic.MoveComponent" )
public class MoveComponent extends UIComponentBase implements SystemEventListener {

    //
    // Constructor - subscribes to PostAddToViewEvent(s)
    //

    public MoveComponent() {
        setRendererType( "component" );
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        root.subscribeToViewEvent( PreRenderViewEvent.class, this );
    }

    //
    // Public methods
    //

    @Override
    public String getFamily() {
        return "com.sun.faces.test.agnostic.dynamic";
    }

    public boolean isListenerForSource( Object source ) {
        return ( source instanceof UIViewRoot );
    }

    // This event method will do the following:
    //   PostAddToViewEvent processing: 
    //     Dynamically modifies component tree by moving "Foo" from outside
    //     the panel to inside the panel.

    @Override
    public void processEvent( SystemEvent event )
        throws AbortProcessingException {
        // Do not re-modify the component tree

        if (FacesContext.getCurrentInstance().isPostback()) {
                return;
        }

        // Dynamically modify the component tree. Move Foo from outside to inside the panel

        HtmlOutputText  outputText = (HtmlOutputText) getChildren().get( 0 );
        HtmlPanelGroup panel = (HtmlPanelGroup) getChildren().get( 1 );
        boolean removed = getChildren().remove( outputText );
        panel.getChildren().add( 0, outputText );
    }
}
