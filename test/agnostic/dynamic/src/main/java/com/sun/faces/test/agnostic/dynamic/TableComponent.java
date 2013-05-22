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

package com.sun.faces.test.agnostic.dynamic;

import javax.faces.application.Application;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

@FacesComponent(value = "com.sun.faces.test.agnostic.dynamic.TableComponent" )
public class TableComponent extends UIComponentBase implements SystemEventListener {

    //
    // Constructor: Subscribes to PreRenderView Event(s)
    //
    public TableComponent() {
        setRendererType( "component" );
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        root.subscribeToViewEvent( PreRenderViewEvent.class, this );
    }

    @Override
    public String getFamily() {
        return "com.sun.faces.test.agnostic.dynamic";
    }

    public boolean isListenerForSource( Object source ) {
        return ( source instanceof UIViewRoot );
    }

    //
    // Event processing: Creates a datatable and adds a component to it.
    //
    @Override
    public void processEvent( SystemEvent event ) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
           if ( !context.isPostback() ) {
              Application application = context.getApplication();

              HtmlDataTable dataTable = new HtmlDataTable();
              dataTable.setVar( "_internal" );
              dataTable.setValueExpression( "value", 
                  application.getExpressionFactory().createValueExpression( 
                     context.getELContext(), "#{addBean.list}", Object.class ));
              getChildren().add( dataTable );

              UIColumn column = new UIColumn();
              column.setId( context.getViewRoot().createUniqueId() );
              dataTable.getChildren().add( column );

              HtmlOutputText outputText = new HtmlOutputText();
              outputText.setId( context.getViewRoot().createUniqueId() );
              outputText.setValueExpression( "value", 
                  application.getExpressionFactory().createValueExpression( 
                     context.getELContext(), "#{_internal}", Object.class ));
              column.getChildren().add( outputText );
        }
    }
}
