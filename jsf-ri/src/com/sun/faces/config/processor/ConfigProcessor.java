/*
 * $Id: ConfigProcessor.java,v 1.1 2007/04/22 21:41:42 rlubke Exp $
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
 * Copyright 2007 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.config.processor;

import org.w3c.dom.Document;

/**
 * <p>
 *  This interface provides a CoR structure for procesing JSF configuration
 *  resources.
 * </p>
 */
public interface ConfigProcessor {

    /**
     * <p>
     *   Set the next <code>ConfigProcessor</code> to be invoked once
     *   {@link ConfigProcessor#process(org.w3c.dom.Document[])}
     *   has completed.
     * </p>
     */
    public ConfigProcessor setNext(ConfigProcessor nextProcessor);


    /**
     * <p>
     *  Process the array of <code>Document</code>s.
     * </p>
     */
    public void process(Document[] documents)
    throws Exception;


    /**
     * <p>
     *  Invoke the <code>ConfigProcess</code> specified by
     *  a call to {@link ConfigProcessor#setNext(ConfigProcessor)}, if any.
     * </p>
     */
    public void invokeNext(Document[] documents)
    throws Exception;

}
