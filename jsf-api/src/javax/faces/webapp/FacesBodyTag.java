/*
 * $Id: FacesBodyTag.java,v 1.3 2003/01/21 20:37:17 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;


/**
 * <p><strong>FacesTag</strong> is a base class for all JSP custom actions
 * that correspond to user interface components in a page that is rendered by
 * JavaServer Faces.  Tags that need to process their tag bodies should
 * subclass {@link FacesBodyTag} instead.</p>
 */

public abstract class FacesBodyTag extends FacesTag implements BodyTag {


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The <code>bodyContent</code> for this tag handler.</p>
     */
    protected BodyContent bodyContent = null;


    // -------------------------------------------------------- BodyTag Methods


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


    protected int getDoStartValue() throws JspException {

        return (EVAL_BODY_BUFFERED);

    }


}
