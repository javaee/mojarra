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

package javax.faces.convert;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

/**
 * The JUnit tests for the LongConverter class.
 */
public class LongConverterTest {

    /**
     * Test getAsObject method.
     */
    @Test(expected = NullPointerException.class)
    public void testGetAsObject() {
        LongConverter converter = new LongConverter();
        converter.getAsObject(null, null, null);
    }

    /**
     * Test getAsObject method.
     */
    @Test
    public void testGetAsObject2() {
        LongConverter converter = new LongConverter();
        FacesContext facesContext = PowerMock.createMock(FacesContext.class);
        replay(facesContext);
        assertNull(converter.getAsObject(facesContext, new UIPanel(), null));
        verify(facesContext);
    }

    /**
     * Test getAsObject method.
     */
    @Test
    public void testGetAsObject3() {
        LongConverter converter = new LongConverter();
        FacesContext facesContext = PowerMock.createMock(FacesContext.class);
        replay(facesContext);
        assertNull(converter.getAsObject(facesContext, new UIPanel(), "     "));
        verify(facesContext);
    }

    /**
     * Test getAsObject method.
     */
    @Test
    public void testGetAsObject4() {
        LongConverter converter = new LongConverter();
        FacesContext facesContext = PowerMock.createMock(FacesContext.class);
        replay(facesContext);
        assertEquals(Long.valueOf("123"), converter.getAsObject(facesContext, new UIPanel(), "123"));
        verify(facesContext);
    }

    /**
     * Test getAsString method.
     */
    @Test(expected = NullPointerException.class)
    public void testGetAsString() {
        LongConverter converter = new LongConverter();
        converter.getAsString(null, null, null);
    }

    /**
     * Test getAsString method.
     */
    @Test
    public void testGetAsString2() {
        LongConverter converter = new LongConverter();
        FacesContext facesContext = PowerMock.createMock(FacesContext.class);
        replay(facesContext);
        assertEquals("", converter.getAsString(facesContext, new UIPanel(), null));
        verify(facesContext);
    }

    /**
     * Test getAsString method.
     */
    @Test
    public void testGetAsString3() {
        LongConverter converter = new LongConverter();
        FacesContext facesContext = PowerMock.createMock(FacesContext.class);
        replay(facesContext);
        assertEquals("123", converter.getAsString(facesContext, new UIPanel(), "123"));
        verify(facesContext);
    }

    /**
     * Test getAsString method.
     */
    @Test
    public void testGetAsString4() {
        LongConverter converter = new LongConverter();
        FacesContext facesContext = PowerMock.createMock(FacesContext.class);
        replay(facesContext);
        assertEquals("123", converter.getAsString(facesContext, new UIPanel(), Long.valueOf("123")));
        verify(facesContext);
    }
}
