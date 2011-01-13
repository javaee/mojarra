/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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

// TestRestoreViewFromPage.java

package com.sun.faces.lifecycle;

import com.sun.faces.cactus.CompareFiles;
import com.sun.faces.cactus.FileOutputResponseWriter;
import com.sun.faces.cactus.ServletFacesTestCase;
import org.apache.cactus.WebRequest;

import javax.faces.render.RenderKitFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <B>TestRestoreViewFromPage</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 */

public class TestRestoreViewFromPage extends ServletFacesTestCase {

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    public static final String TEST_URI = "/greeting.jsp";
    public static final String RESTORE_VIEW_OUTPUT_FILE = FileOutputResponseWriter.FACES_RESPONSE_ROOT +
        "RestoreView_output";
    public static final String RESTORE_VIEW_CORRECT_FILE = FileOutputResponseWriter.FACES_RESPONSE_ROOT +
        "RestoreView_correct";

    public static final String ignore[] = {
        "value=[Ljava.lang.Object;@14a18d"
    };
// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestRestoreViewFromPage() {
        super("TestRestoreViewFromPage");
    }


    public TestRestoreViewFromPage(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//

    public void beginRestoreViewFromPage(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
        theRequest.addParameter("javax.faces.ViewState",
                                "rO0ABXNyACBjb20uc3VuLmZhY2VzLnV0aWwuVHJlZVN0cnVjdHVyZRRmG0QclWAgAgAETAAIY2hpbGRyZW50ABVMamF2YS91dGlsL0FycmF5TGlzdDtMAAljbGFzc05hbWV0ABJMamF2YS9sYW5nL1N0cmluZztMAAZmYWNldHN0ABNMamF2YS91dGlsL0hhc2hNYXA7TAACaWRxAH4AAnhwc3IAE2phdmEudXRpbC5BcnJheUxpc3R4gdIdmcdhnQMAAUkABHNpemV4cAAAAAF3BAAAAApzcQB+AABzcQB+AAUAAAACdwQAAAAKc3EAfgAAcHQAJmphdmF4LmZhY2VzLmNvbXBvbmVudC5iYXNlLlVJSW5wdXRCYXNlcHQABnVzZXJOb3NxAH4AAHB0AChqYXZheC5mYWNlcy5jb21wb25lbnQuYmFzZS5VSUNvbW1hbmRCYXNlcHQABnN1Ym1pdHh0ACVqYXZheC5mYWNlcy5jb21wb25lbnQuYmFzZS5VSUZvcm1CYXNlcHQACWhlbGxvRm9ybXh0AClqYXZheC5mYWNlcy5jb21wb25lbnQuYmFzZS5VSVZpZXdSb290QmFzZXB0AARyb290dXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAAAnVxAH4AEwAAAAJ0ABlERUZBVUxUW3NlcF0vZ3JlZXRpbmcuanNwdXEAfgATAAAABnEAfgAScHEAfgAXcHBwdXEAfgATAAAAAXVxAH4AEwAAAAJ1cQB+ABMAAAAGcQB+ABBwcQB+ABp0AARGb3JtcHNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAABB3CAAAABAAAAACdAAYY29tLnN1bi5mYWNlcy5Gb3JtTnVtYmVyc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAAAAB0AARuYW1ldAAJaGVsbG9Gb3JteHVxAH4AEwAAAAJ1cQB+ABMAAAACdXEAfgATAAAAAnVxAH4AEwAAAAR0AA5mYWxzZVtzZXBddHJ1ZXBwcHVxAH4AEwAAAAJ1cQB+ABMAAAADcHQABk5VTUJFUnB1cQB+ABMAAAAGdAAQaGVsbG9Gb3JtX3VzZXJOb3BxAH4ALHQABFRleHRwcHVxAH4AEwAAAAB1cQB+ABMAAAACdXEAfgATAAAAAnVxAH4AEwAAAAJ0AA1udWxsW3NlcF1udWxsdXEAfgATAAAAB3BwcHBwdXIAK1tMamF2YXguZmFjZXMuYXBwbGljYXRpb24uU3RhdGVIb2xkZXJTYXZlcjus7jOyrNsurwIAAHhwAAAAAHB1cQB+ABMAAAACdXEAfgATAAAAA3B0AAZTdWJtaXRwdXEAfgATAAAABnQAEGhlbGxvRm9ybV9zdWJtaXRwcQB+ADp0AAZCdXR0b25wc3EAfgAcP0AAAAAAABB3CAAAABAAAAABdAAEdHlwZXQABnN1Ym1pdHh1cQB+ABMAAAAAc3IAEGphdmEudXRpbC5Mb2NhbGV++BFgnDD57AMABEkACGhhc2hjb2RlTAAHY291bnRyeXEAfgACTAAIbGFuZ3VhZ2VxAH4AAkwAB3ZhcmlhbnRxAH4AAnhw/////3QAAlVTdAACZW50AAB4");
    }


    public void testRestoreViewFromPage() {
        Phase restoreView = new RestoreViewPhase();

        try {
            restoreView.execute(getFacesContext());
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        assertTrue(null != getFacesContext().getViewRoot());
        assertTrue(RenderKitFactory.HTML_BASIC_RENDER_KIT.equals(
            getFacesContext().getViewRoot().getRenderKitId()));
        assertTrue(
            getFacesContext().getViewRoot().getLocale().equals(Locale.US));
        CompareFiles cf = new CompareFiles();
        try {
            FileOutputStream os = new FileOutputStream(
                RESTORE_VIEW_OUTPUT_FILE);
            PrintStream ps = new PrintStream(os);
            com.sun.faces.util.DebugUtil.printTree(
                getFacesContext().getViewRoot(), ps);

            List ignoreList = new ArrayList();
            for (int i = 0; i < ignore.length; i++) {
                ignoreList.add(ignore[i]);
            }
            boolean status = cf.filesIdentical(RESTORE_VIEW_OUTPUT_FILE,
                                               RESTORE_VIEW_CORRECT_FILE,
                                               ignoreList);
            assertTrue(status);
            // PENDING (visvan) test case to verify nothing is persisted if the root
            // is marked transient for both client tand server case.
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

} // end of class TestRestoreViewFromPage
