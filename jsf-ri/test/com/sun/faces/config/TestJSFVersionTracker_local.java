package com.sun.faces.config;

/*
 * TestJSFVersionTracker_local.java
 *
 * Created on February 18, 2006, 6:50 AM
 * $Id: TestJSFVersionTracker_local.java,v 1.2 2006/03/29 22:39:40 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

import junit.framework.TestCase;

import com.sun.faces.config.JSFVersionTracker.Version;

/**
 *
 * @author edburns
 */
public class TestJSFVersionTracker_local extends TestCase {


    // ------------------------------------------------------------ Constructors


    /** Creates a new instance of TestJSFVersionTracker_local */
    
    public TestJSFVersionTracker_local() {

        super("TestJSFVersionTracker_local.java");

    }


    public TestJSFVersionTracker_local(String name) {

        super(name);

    }


    // ---------------------------------------------------------- Public Methods


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
