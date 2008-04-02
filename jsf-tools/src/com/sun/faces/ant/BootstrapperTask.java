/*
 * $Id: BootstrapperTask.java,v 1.1 2006/09/19 21:13:32 jdlee Exp $
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
import org.apache.tools.ant.Task;

import com.sun.faces.generate.BootstrapperGenerator;

/**
 * <p>Task to create the Bootstrapper class.</p>
 */
public class BootstrapperTask extends Task {
    private String runtimeConfig;
    private String outputDir;
    private String copyright;
    public BootstrapperTask() {
    }

    public void execute() throws BuildException {
        BootstrapperGenerator generator = new BootstrapperGenerator();
        generator.execute(runtimeConfig, copyright, outputDir);
    }

    public String getRuntimeConfig() {
        return runtimeConfig;
    }

    public void setRuntimeConfig(String runtimeConfig) {
        this.runtimeConfig = runtimeConfig;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String generatorConfig) {
        this.copyright = generatorConfig;
    }
}