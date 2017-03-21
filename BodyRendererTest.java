/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package com.sun.faces.renderkit.html_basic;

import java.io.StringWriter;
import java.util.Collections;
import javax.faces.application.Application;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlBody;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.expect;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

/**
 * The JUnit tests for the BodyRenderer class.
 */
public class BodyRendererTest {

    /**
     * Test decode method.
     */
    @Test
    public void testDecode() {
        BodyRenderer bodyRenderer = new BodyRenderer();
        bodyRenderer.decode(null, null);
    }

    /**
     * Test encodeBegin method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testEncodeBegin() throws Exception {
        StringWriter writer = new StringWriter();
        ResponseWriter testResponseWriter = new TestResponseWriter(writer);
        FacesContext facesContext = PowerMock.createPartialMock(FacesContext.class, "getResponseWriter");
        BodyRenderer bodyRenderer = new BodyRenderer();
        HtmlBody htmlBody = new HtmlBody();
        htmlBody.getAttributes().put("styleClass", "myclass");
        
        expect(facesContext.getResponseWriter()).andReturn(testResponseWriter).anyTimes();
        
        PowerMock.replay(facesContext);
        bodyRenderer.encodeBegin(facesContext, htmlBody);
        PowerMock.verify(facesContext);
        String html = writer.toString();
        assertTrue(html.contains("<body"));
        assertTrue(html.contains("class=\"myclass\""));
    }

    /**
     * Test encodeChildren method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testEncodeChildren() throws Exception {
        BodyRenderer bodyRenderer = new BodyRenderer();
        bodyRenderer.encodeChildren(null, null);
    }

    /**
     * Test encodeEnd method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testEncodeEnd() throws Exception {
        StringWriter writer = new StringWriter();
        ResponseWriter testResponseWriter = new TestResponseWriter(writer);
        FacesContext facesContext = PowerMock.createPartialMockForAllMethodsExcept(FacesContext.class, "getCurrentInstance");
        UIViewRoot viewRoot = PowerMock.createMock(UIViewRoot.class);
        Application application = PowerMock.createMock(Application.class);
        BodyRenderer bodyRenderer = new BodyRenderer();
        HtmlBody htmlBody = new HtmlBody();
        
        expect(facesContext.getApplication()).andReturn(application).anyTimes();
        expect(facesContext.getClientIdsWithMessages()).andReturn(Collections.EMPTY_LIST.iterator()).anyTimes();
        expect(facesContext.getResponseWriter()).andReturn(testResponseWriter).anyTimes();
        expect(facesContext.getViewRoot()).andReturn(viewRoot).anyTimes();
        expect(facesContext.isProjectStage(ProjectStage.Development)).andReturn(false).anyTimes();
        expect(viewRoot.getComponentResources(facesContext, "body")).andReturn(Collections.EMPTY_LIST).anyTimes();
       
        PowerMock.replay(facesContext, viewRoot, application);
        bodyRenderer.encodeEnd(facesContext, htmlBody);
        PowerMock.verify(facesContext, viewRoot, application);
        String html = writer.toString();
        assertTrue(html.contains("</body>"));
    }

    /**
     * Test getRendersChildren method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetRendersChildren() throws Exception {
        BodyRenderer bodyRenderer = new BodyRenderer();
        assertFalse(bodyRenderer.getRendersChildren());
    }
}
