/*
 * $Id: UIComponentBodyTag.java,v 1.4.32.2 2007/04/27 21:26:55 ofung Exp $
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

package javax.faces.webapp;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;


/**
 * <p><strong>UIComponentBodyTag</strong> is a base class for all JSP custom
 * actions, related to a UIComponent, that need to process their tag bodies.
 * </p>
 */

public abstract class UIComponentBodyTag extends UIComponentTag
    implements BodyTag {


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The <code>bodyContent</code> for this tag handler.</p>
     */
    protected BodyContent bodyContent = null;


    // -------------------------------------------------------- BodyTag Methods


    /**
     * <p>Handle the ending of the nested body content for this tag.  The
     * default implementation simply calls <code>getDoAfterBodyValue()</code>
     * to retrieve the flag value to be returned.</p>
     *
     * <p>It should be noted that if this method returns
     * <code>IterationTag.EVAL_BODY_AGAIN</code>, any nested
     * {@link UIComponentTag} <em>must</em> have an explicit ID set.</p>
     *
     * @exception JspException if an error is encountered
     */
    public int doAfterBody() throws JspException {

        return (getDoAfterBodyValue());

    }


    /**
     * <p>Prepare for evaluation of the body.  This method is invoked by the
     * JSP page implementation object after <code>setBodyContent()</code>
     * and before the first time the body is to be evaluated.  This method
     * will not be invoked for empty tags or for non-empty tags whose
     * <code>doStartTag()</code> method returns <code>SKIP_BODY</code>
     * or <code>EVAL_BODY_INCLUDE</code>.</p>
     *
     * @exception JspException if an error is encountered
     */
    public void doInitBody() throws JspException {

        ; // Default implementation does nothing

    }

    public void release() {

        bodyContent = null;
        super.release();

    }


    /**
     * <p>Set the <code>bodyContent</code> for this tag handler.  This method
     * is invoked by the JSP page implementation object at most once per
     * action invocation, before <code>doInitiBody()</code>.  This method
     * will not be invoked for empty tags or for non-empty tags whose
     * <code>doStartTag()</code> method returns <code>SKIP_BODY</code> or
     * <code>EVAL_BODY_INCLUDE</code>.</p>
     *
     * @param bodyContent The new <code>BodyContent</code> for this tag
     */
    public void setBodyContent(BodyContent bodyContent) {

        this.bodyContent = bodyContent;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Return the <code>BodyContent</code> for this tag handler.</p>
     */
    public BodyContent getBodyContent() {

        return (this.bodyContent);

    }


    /**
     * <p>Get the <code>JspWriter</code> from our <code>BodyContent</code>.
     * </p>
     */
    public JspWriter getPreviousOut() {

        return (this.bodyContent.getEnclosingWriter());

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Return the flag value that should be returned from the
     * <code>doAfterBody()</code> method when it is called.  Subclasses
     * may override this method to return the appropriate value.</p>
     */
    protected int getDoAfterBodyValue() throws JspException {

        return (SKIP_BODY);

    }


    protected int getDoStartValue() throws JspException {

        return (EVAL_BODY_BUFFERED);

    }


}
