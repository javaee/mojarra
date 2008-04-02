/*
 * $Id: TestDataActionListener.java,v 1.5 2005/08/22 22:08:15 ofung Exp $
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

package javax.faces.component;


import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.context.FacesContext;


/**
 * <p>Test {@link ActionListener} implementation.</p>
 */

public class TestDataActionListener implements ActionListener {


    // ------------------------------------------------------------ Constructors


    public TestDataActionListener() {
    }


    // ---------------------------------------------------------- Public Methods


    public void processAction(ActionEvent event) {

        trace(event.getComponent().getClientId
              (FacesContext.getCurrentInstance()));

    }


    // ---------------------------------------------------- Static Trace Methods


    // Accumulated trace log
    private static StringBuffer trace = new StringBuffer();

    // Append to the current trace log (or clear if null)
    public static void trace(String text) {
        if (text == null) {
            trace.setLength(0);
        } else {
            trace.append('/');
            trace.append(text);
        }
    }

    // Retrieve the current trace log
    public static String trace() {
        return (trace.toString());
    }


}
