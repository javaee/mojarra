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

package com.sun.faces.context;

import javax.faces.FactoryFinder;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;

import com.sun.faces.cactus.ServletFacesTestCase;

/**
 * <B>TestPartialResponseWriter.java</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 */

public class TestPartialResponseWriter extends ServletFacesTestCase // ServletTestCase
{

//
// Protected Constants
//

// Class Variables
//

//
// Instance Variables
//
    private ResponseWriter writer = null;
    private PartialResponseWriter pWriter = null;
    private RenderKit renderKit = null;
    private StringWriter sw = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestPartialResponseWriter() {
        super("TestPartialResponseWriter.java");
    }


    public TestPartialResponseWriter(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//
    public void setUp() {
        super.setUp();
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = renderKitFactory.getRenderKit(getFacesContext(),
                                                  RenderKitFactory.HTML_BASIC_RENDER_KIT);
        sw = new StringWriter();
        writer = renderKit.createResponseWriter(sw, "text/html", "ISO-8859-1");
        pWriter = new PartialResponseWriter(writer);
    }

    // Tests that the <extension> element is placed correctly in the partial response output

    public void testExtension() {
        try {
            pWriter.startDocument();
            pWriter.startUpdate(PartialResponseWriter.VIEW_STATE_MARKER);
            pWriter.write("foo");
            pWriter.endUpdate();
            pWriter.startExtension(Collections.<String, String>emptyMap());
            pWriter.startElement("data", null);
            pWriter.endElement("data");
            pWriter.endExtension();
            pWriter.endDocument();

            assertTrue(sw.toString().indexOf("</update><extension><data></data></extension></changes></partial-response>") >= 0);
        } catch (IOException e) {
            assertTrue(false);
        }
            

    }
}
