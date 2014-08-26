/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
package javax.faces.component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.application.ProjectStage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PostConstructViewMapEvent;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.servlet.http.HttpSession;
import org.easymock.EasyMock;
import org.junit.Test;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class UIViewRootTest {

    @Test
    public void testViewMapPostConstructViewMapEvent() {
        FacesContext facesContext = EasyMock.createMock(FacesContext.class);
        Application application = EasyMock.createMock(Application.class);
        ExternalContext externalContext = EasyMock.createMock(ExternalContext.class);
        HttpSession httpSession = EasyMock.createMock(HttpSession.class);
        
        setFacesContext(facesContext);

        expect(facesContext.getExternalContext()).andReturn(externalContext).anyTimes();
        expect(externalContext.getApplicationMap()).andReturn(null).anyTimes();
        replay(facesContext, externalContext);
        UIViewRoot viewRoot = new UIViewRoot();
        verify(facesContext, externalContext);
        
        reset(facesContext, externalContext);
        expect(facesContext.getApplication()).andReturn(application).anyTimes();
        expect(application.getProjectStage()).andReturn(ProjectStage.UnitTest);
        application.publishEvent(facesContext, PostConstructViewMapEvent.class, UIViewRoot.class, viewRoot);
        replay(facesContext, application, externalContext, httpSession);
        Map<String, Object> viewMap = viewRoot.getViewMap();
        assertNotNull(viewMap);
        verify(facesContext, application, externalContext, httpSession);

        setFacesContext(null);
    }

    @Test
    public void testViewMapPreDestroyViewMapEvent() {
        FacesContext facesContext = EasyMock.createMock(FacesContext.class);
        Application application = EasyMock.createMock(Application.class);
        ExternalContext externalContext = EasyMock.createMock(ExternalContext.class);
        HttpSession httpSession = EasyMock.createMock(HttpSession.class);
        
        setFacesContext(facesContext);
        
        expect(facesContext.getExternalContext()).andReturn(externalContext).anyTimes();
        expect(externalContext.getApplicationMap()).andReturn(null).anyTimes();
        replay(facesContext, externalContext);
        UIViewRoot viewRoot = new UIViewRoot();
        verify(facesContext, externalContext);
        
        reset(facesContext, externalContext);
        expect(facesContext.getApplication()).andReturn(application).anyTimes();
        expect(application.getProjectStage()).andReturn(ProjectStage.UnitTest);
        application.publishEvent(facesContext, PostConstructViewMapEvent.class, UIViewRoot.class, viewRoot);
        expect(facesContext.getViewRoot()).andReturn(viewRoot);
        application.publishEvent(facesContext, PreDestroyViewMapEvent.class, UIViewRoot.class,  viewRoot);
        expect(facesContext.getViewRoot()).andReturn(viewRoot);
        application.publishEvent(facesContext, PreDestroyViewMapEvent.class, UIViewRoot.class,  viewRoot);

        replay(facesContext, application, externalContext, httpSession);
        
        Map<String, Object> viewMap = viewRoot.getViewMap();
        assertNotNull(viewMap);
        viewRoot.getViewMap().clear();
        viewRoot.getViewMap().clear();
        
        verify(facesContext, application, externalContext, httpSession);
        
        setFacesContext(null);
    }
    
    @Test
    public void testViewMapSaveAndRestoreState() {
        FacesContext facesContext = EasyMock.createMock(FacesContext.class);
        Application application = EasyMock.createMock(Application.class);
        ExternalContext externalContext = EasyMock.createMock(ExternalContext.class);
        HttpSession httpSession = EasyMock.createMock(HttpSession.class);
        HashMap<Object, Object> attributes = new HashMap<Object, Object>();
        HashMap<String, Object> sessionMap = new HashMap<String, Object>();

        setFacesContext(facesContext);
                
        expect(facesContext.getExternalContext()).andReturn(externalContext).anyTimes();
        expect(externalContext.getApplicationMap()).andReturn(null).anyTimes();
        replay(facesContext, externalContext);
        UIViewRoot viewRoot1 = new UIViewRoot();
        verify(facesContext, externalContext);        
        reset(facesContext, externalContext);
        
        expect(facesContext.getExternalContext()).andReturn(externalContext).anyTimes();
        expect(externalContext.getApplicationMap()).andReturn(null).anyTimes();
        replay(facesContext, externalContext);
        UIViewRoot viewRoot2 = new UIViewRoot();
        verify(facesContext, externalContext);        
        reset(facesContext, externalContext);
                
        expect(facesContext.getAttributes()).andReturn(attributes).anyTimes();
        expect(facesContext.getApplication()).andReturn(application).anyTimes();
        expect(application.getProjectStage()).andReturn(ProjectStage.UnitTest).anyTimes();
        expect(facesContext.getExternalContext()).andReturn(externalContext).anyTimes();
        expect(externalContext.getSessionMap()).andReturn(sessionMap).anyTimes();
        application.publishEvent(facesContext, PostConstructViewMapEvent.class, UIViewRoot.class,  viewRoot1);
        replay(facesContext, application, externalContext, httpSession);
        Map<String, Object> viewMap = viewRoot1.getViewMap();
        viewMap.put("one", "one");
        Object saved = viewRoot1.saveState(facesContext);
        
        /*
         * Simulate our ViewMapListener.
         */
        Map<String, Object> viewMaps = new HashMap<String, Object>();
        viewMaps.put((String) viewRoot1.getTransientStateHelper().getTransient("com.sun.faces.application.view.viewMapId"), viewMap);
        sessionMap.put("com.sun.faces.application.view.activeViewMaps", viewMaps);
        
        viewRoot2.restoreState(facesContext, saved);
        viewMap = viewRoot2.getViewMap();
        assertEquals("one", viewMap.get("one"));
        verify(facesContext, application, externalContext, httpSession);
        
        setFacesContext(null);
    }
    
    private void setFacesContext(FacesContext facesContext) {
        try {
            Method method = FacesContext.class.getDeclaredMethod("setCurrentInstance", FacesContext.class);
            method.setAccessible(true);
            method.invoke(null, facesContext);
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
        }
    }
}
