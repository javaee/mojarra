/*
 * $Id: UIComponentBodyTag.java,v 1.2 2004/02/04 23:38:36 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
