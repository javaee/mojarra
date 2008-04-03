package com.sun.faces.config;

/*
 * TestJSFVersionTracker_local.java
 *
 * Created on February 18, 2006, 6:50 AM
 * $Id: TestJSFVersionTracker_local.java,v 1.4 2007/04/27 22:02:04 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

import junit.framework.TestCase;

import com.sun.faces.config.JSFVersionTracker.Version;

/**
 *
 * @author edburns
 */
public class TestJSFVersionTracker_local extends TestCase {
    
    /** Creates a new instance of TestJSFVersionTracker_local */
    
    public TestJSFVersionTracker_local() {
        super("TestJSFVersionTracker_local.java");
    }


    public TestJSFVersionTracker_local(String name) {
        super(name);
    }
    
    public void testPopWithNulls() throws Exception {
        
        JSFVersionTracker tracker = new JSFVersionTracker();
        assertNotNull(tracker);
        
        // Push 1.2
        tracker.pushJSFVersionNumberFromGrammar("web-facesconfig_1_2.xsd");
        // Push bogus
        tracker.pushJSFVersionNumberFromGrammar("yoyodyne.xsd");
        // Push bogus
        tracker.pushJSFVersionNumberFromGrammar("buckaroo.dtd");
        // Push bogus
        tracker.pushJSFVersionNumberFromGrammar("reno.dtd");
        
        Version version = tracker.peekJSFVersionNumber();
        assertNotNull(version);
        assertEquals(1, version.getMajorVersion());
        assertEquals(2, version.getMinorVersion());
        
        version = tracker.popJSFVersionNumber();
        assertNotNull(version);
        assertEquals(1, version.getMajorVersion());
        assertEquals(2, version.getMinorVersion());
        
        version = tracker.popJSFVersionNumber();
        assertNull(version);
        
        tracker.pushJSFVersionNumberFromGrammar("web-facesconfig_1_0.dtd");
        tracker.pushJSFVersionNumberFromGrammar("web-facesconfig_1_1.dtd");
        tracker.pushJSFVersionNumberFromGrammar("web-facesconfig_1_2.xsd");
        tracker.pushJSFVersionNumberFromGrammar("nada.dtd");
        tracker.pushJSFVersionNumberFromGrammar("web-facesconfig_1_0.dtd");
        tracker.pushJSFVersionNumberFromGrammar("bogus");
        tracker.pushJSFVersionNumberFromGrammar("web-facesconfig_1_1.dtd");
        tracker.pushJSFVersionNumberFromGrammar("smallberries");
        
        version = tracker.popJSFVersionNumber();
        assertNotNull(version);
        assertEquals(1, version.getMajorVersion());
        assertEquals(1, version.getMinorVersion());
        
        version = tracker.popJSFVersionNumber();
        assertNotNull(version);
        assertEquals(1, version.getMajorVersion());
        assertEquals(0, version.getMinorVersion());
        
        version = tracker.popJSFVersionNumber();
        assertNotNull(version);
        assertEquals(1, version.getMajorVersion());
        assertEquals(2, version.getMinorVersion());
        
        version = tracker.popJSFVersionNumber();
        assertNotNull(version);
        assertEquals(1, version.getMajorVersion());
        assertEquals(1, version.getMinorVersion());
        
        version = tracker.popJSFVersionNumber();
        assertNotNull(version);
        assertEquals(1, version.getMajorVersion());
        assertEquals(0, version.getMinorVersion());
        
        version = tracker.popJSFVersionNumber();
        assertNull(version);

    }
    
    
}
