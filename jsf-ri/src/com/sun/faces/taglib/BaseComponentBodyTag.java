/*
 * $Id: BaseComponentBodyTag.java,v 1.6.36.2 2007/04/27 21:27:46 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

// BaseComponentTag.java

package com.sun.faces.taglib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

/**
 * <B>BaseComponentTag</B> is a base class for most tags in the Faces Tag
 * library.  Its primary purpose is to centralize common tag functions
 * to a single base class. <P>
 */

public abstract class BaseComponentBodyTag extends BaseComponentTag
    implements BodyTag {

    //
    // Protected Constants
    //

    // Log instance for this class
    protected static Log log = LogFactory.getLog(BaseComponentBodyTag.class);

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Relationship Instance Variables

    /**
     * <p>The <code>bodyContent</code> for this tag handler.</p>
     */
    protected BodyContent bodyContent = null;


    //
    // Constructors and Initializers    
    //

    public BaseComponentBodyTag() {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    // 
    // Methods from BodyTag
    //

    public void doInitBody() throws JspException {

        ; // Default implementation does nothing

    }


    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }


    public BodyContent getBodyContent() {
        return (this.bodyContent);
    }


    public JspWriter getPreviousOut() {
        return (this.bodyContent.getEnclosingWriter());
    }


    //
    // Methods from Tag
    // 

    public void release() {
        bodyContent = null;
        super.release();
    }

    // 
    // methods from UIComponentTag
    // 

    protected int getDoStartValue() throws JspException {
        return (BodyTag.EVAL_BODY_BUFFERED);
    }


    public int doAfterBody() throws JspException {
        return (getDoAfterBodyValue());
    }


    protected int getDoEndValue() throws JspException {
        return (EVAL_PAGE);
    }


    protected int getDoAfterBodyValue() throws JspException {

        return (SKIP_BODY);

    }


} // end of class BaseComponentBodyTag
