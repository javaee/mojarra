/*
 * $Id: TaglibGenTask.java,v 1.6 2006/03/14 01:09:04 rlubke Exp $
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

package com.sun.faces.ant;

import org.apache.tools.ant.BuildException;

import com.sun.faces.generate.PropertyManager;

/**
 * <p>Task to create a JSP tags.</p>
 */
public class TaglibGenTask extends AbstractGeneratorTask {

    private static final String GENERATOR_12_CLASS =
        "com.sun.faces.generate.HtmlTaglib12Generator";

    private static final String GENERATOR_21_CLASS =
        "com.sun.faces.generate.HtmlTaglib21Generator";


    // ---------------------------------------------------------- Public Methods


    public void execute() throws BuildException {

        PropertyManager manager = PropertyManager.newInstance(generatorConfig);
        String jspVersion =
              manager.getProperty(PropertyManager.JSP_VERSION_PROPERTY);
        System.out.println("Generating taglibs for JSP version " + jspVersion);
        if ("2.1".equals(jspVersion)) {
            setGeneratorClass(GENERATOR_21_CLASS);
        } else if ("1.2".equals(jspVersion)) {
            setGeneratorClass(GENERATOR_12_CLASS);
        } else {
            throw new BuildException("Unsupported JSP version '"
                                     + jspVersion
                                     + '\'');
        }

        super.execute();

    } // END execute

}
