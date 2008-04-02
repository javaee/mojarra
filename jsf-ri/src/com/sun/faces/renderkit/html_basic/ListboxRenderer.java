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

/*
 * $Id: ListboxRenderer.java,v 1.22 2006/03/29 22:38:38 rlubke Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */


package com.sun.faces.renderkit.html_basic;

import javax.faces.context.ResponseWriter;

import java.io.IOException;

/**
 * <B>ListRenderer</B> is a class that renders the current value of
 * <code>UISelectOne<code> or <code>UISelectMany<code> component as a list of
 * options.
 */

public class ListboxRenderer extends MenuRenderer {

    // ------------------------------------------------------------ Constructors


    public ListboxRenderer() {

        super();

    }

    // ------------------------------------------------------- Protected Methods


    protected void writeDefaultSize(ResponseWriter writer, int itemCount)
          throws IOException {

        // If size not specified, default to number of items
        writer.writeAttribute("size", new Integer(itemCount), "size");

    }

} // end of class ListboxRenderer
