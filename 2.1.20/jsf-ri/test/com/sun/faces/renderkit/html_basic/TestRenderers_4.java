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

/**
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// TestRenderers_4.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.cactus.JspFacesTestCase;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import org.apache.cactus.WebRequest;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContextFactory;

import java.io.IOException;
import java.util.Locale;

/**
 * Test encode and decode methods in Renderer classes.
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 */

public class TestRenderers_4 extends JspFacesTestCase {

    //
    // Protected Constants
    //

    public boolean sendWriterToFile() {
        return true;
    }


    public String getExpectedOutputFilename() {
        return "CorrectRenderersResponse_4";
    }

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private FacesContextFactory facesContextFactory = null;

    // Attribute Instance Variables
    // Relationship Instance Variables
    //
    // Constructors and Initializers    
    //

    public TestRenderers_4() {
        super("TestRenderers_4");
    }


    public TestRenderers_4(String name) {
        super(name);
    }

    //
    // Class methods
    //

    //
    // Methods from TestCase
    //
    public void setUp() {
        super.setUp();
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setViewId("viewId");
        page.setLocale(Locale.US);
        getFacesContext().setViewRoot(page);
        Object view = 
	    Util.getStateManager(getFacesContext()).saveSerializedView(getFacesContext());
	getFacesContext().getExternalContext().getRequestMap().put(RIConstants.SAVED_STATE, view);
        assertTrue(null != getFacesContext().getResponseWriter());
    }


    public void beginRenderers(WebRequest theRequest) {

    }


    public void testRenderers() {

        try {
            // create a dummy root for the tree.
            UIViewRoot root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
            root.setLocale(Locale.US);
            root.setId("root");

            testGridRenderer(root);

            root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
            root.setId("root");
            root.setLocale(Locale.US);
            testGridRendererWithNonRenderedChildren(root);

            getFacesContext().getResponseWriter().close();
            assertTrue(verifyExpectedOutput());
        } catch (Throwable t) {
            t.printStackTrace();
            assertTrue(false);
            return;
        }
    }


    public void testGridRenderer(UIComponent root)
        throws IOException {
        System.out.println("Testing GridRenderer");
        GridRenderer gridRenderer = null;
        UIPanel
            panel = null,
            headerGroup = null,
            footerGroup = null;
        UIOutput
            header1 = null,
            header2 = null,
            footer1 = null,
            footer2 = null,
            body1 = null,
            body2 = null;

        panel = new UIPanel();
        root.getChildren().add(panel);

        headerGroup = new UIPanel();
        headerGroup.setId("header");
        headerGroup.setRendererType("javax.faces.Group");
        header1 = new UIOutput();
        header1.setValue("header1 ");
        headerGroup.getChildren().add(header1);
        header2 = new UIOutput();
        header2.setValue("header2 ");
        headerGroup.getChildren().add(header2);
        panel.getFacets().put("header", headerGroup);

        footerGroup = new UIPanel();
        footerGroup.setId("footer");
        footerGroup.setRendererType("javax.faces.Group");
        footer1 = new UIOutput();
        footer1.setValue("footer1 ");
        footerGroup.getChildren().add(footer1);
        footer2 = new UIOutput();
        footer2.setValue("footer2 ");
        footerGroup.getChildren().add(footer2);
        panel.getFacets().put("footer", footerGroup);

        body1 = new UIOutput();
        body1.setValue("body1");
        panel.getChildren().add(body1);

        body2 = new UIOutput();
        body2.setValue("body2");
        panel.getChildren().add(body2);

        gridRenderer = new GridRenderer();

        System.out.println("    Testing encodeBegin method... ");
        gridRenderer.encodeBegin(getFacesContext(), panel);
        gridRenderer.encodeChildren(getFacesContext(), panel);
        gridRenderer.encodeEnd(getFacesContext(), panel);

    }


    public void testGridRendererWithNonRenderedChildren(UIComponent root)
        throws IOException {
        System.out.println("Testing GridRenderer");
        GridRenderer gridRenderer = null;
        UIPanel
            panel = null,
            headerGroup = null,
            footerGroup = null;
        UIOutput
            header1 = null,
            header2 = null,
            footer1 = null,
            footer2 = null,
            body1 = null,
            body2 = null;

        panel = new UIPanel();
        root.getChildren().add(panel);

        // the header should not be rendered
        headerGroup = new UIPanel();
        headerGroup.setRendered(false);
        headerGroup.setId("header");
        headerGroup.setRendererType("javax.faces.Group");
        header1 = new UIOutput();
        header1.setValue("header1 ");
        headerGroup.getChildren().add(header1);
        header2 = new UIOutput();
        header2.setValue("header2 ");
        headerGroup.getChildren().add(header2);
        panel.getFacets().put("header", headerGroup);

        footerGroup = new UIPanel();
        footerGroup.setId("footer");
        footerGroup.setRendererType("javax.faces.Group");
        footer1 = new UIOutput();
        footer1.setValue("footer1 ");
        footerGroup.getChildren().add(footer1);
        footer2 = new UIOutput();
        footer2.setValue("footer2 ");
        footerGroup.getChildren().add(footer2);
        panel.getFacets().put("footer", footerGroup);

        // this child should not be rendered
        body1 = new UIOutput();
        body1.setRendered(false);
        body1.setValue("body1");
        panel.getChildren().add(body1);

        body2 = new UIOutput();
        body2.setValue("body2");
        panel.getChildren().add(body2);

        gridRenderer = new GridRenderer();

        System.out.println("    Testing encodeBegin method... ");
        gridRenderer.encodeBegin(getFacesContext(), panel);
        gridRenderer.encodeChildren(getFacesContext(), panel);
        gridRenderer.encodeEnd(getFacesContext(), panel);

    }


} // end of class TestRenderers_4
