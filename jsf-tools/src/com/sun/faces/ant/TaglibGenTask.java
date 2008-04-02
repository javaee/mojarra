/*
 * $Id: TaglibGenTask.java,v 1.4 2005/05/05 20:51:34 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.ant;

import org.apache.tools.ant.BuildException;

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

        if (generatorConfig.indexOf("12") > -1) {
            setGeneratorClass(GENERATOR_12_CLASS);
        } else {
           setGeneratorClass(GENERATOR_21_CLASS);
        }

        super.execute();

    } // END execute

}
