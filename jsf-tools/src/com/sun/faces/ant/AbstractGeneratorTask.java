/*
 * $Id: AbstractGeneratorTask.java,v 1.2 2005/05/05 20:51:34 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.ant;

import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.BuildException;

/**
 * <p>Base task for generators.</p>
 */
public abstract class AbstractGeneratorTask extends Java {

    /**
     * <p>The fully qualified path to the properties file to drive the
     * Generator.</p>
     */
    protected String generatorConfig;

    /**
     * <p>The fully qualified <code>Generator</code class.</p>
     */
    private String generatorClass;

    /**
     * <p>The fully qualified path to the faces-config.xml to serve
     * as the model for the <code>Generator</code>.</p>
     */
    private String facesConfig;


    // ---------------------------------------------------------- Public Methods


    public void setGeneratorConfig(String generatorConfig) {

        this.generatorConfig = generatorConfig;

    } // END setGeneratorConfig


    public void setFacesConfig(String facesConfig) {

        this.facesConfig = facesConfig;

    } // END setFacesConfig


    public void setGeneratorClass(String generatorClass) {

        this.generatorClass = generatorClass;

    } // END setGeneratorClass


    public void execute() throws BuildException {

        super.createArg().setValue(generatorConfig);
        super.createArg().setValue(facesConfig);

        super.setClassname(generatorClass);

        super.execute();

    } // END execute


}
