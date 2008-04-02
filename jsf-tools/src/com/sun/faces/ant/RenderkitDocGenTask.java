/*
 * $Id: RenderkitDocGenTask.java,v 1.1 2004/12/13 19:07:48 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.ant;

import org.apache.tools.ant.BuildException;

/**
 * <p>Task to create RenderKit documentation.</p>
 */
public class RenderkitDocGenTask extends AbstractGeneratorTask {
    
    
    private static final String GENERATOR_CLASS = 
        "com.sun.faces.generate.RenderKitSpecificationGenerator";


    // ---------------------------------------------------------- Public Methods


    public void execute() throws BuildException {

        setGeneratorClass(GENERATOR_CLASS);

        super.execute();


    } // END execute

}
