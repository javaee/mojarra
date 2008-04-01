/*
 * $Id: FacesTag.java,v 1.1 2002/06/04 22:16:03 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p><strong>FacesTag</strong> is a base class for all JSP custom actions
 * that correspond to user interface components in a page that is rendered by
 * JavaServer Faces.  Tags that need to process their tag bodies should
 * subclass {@link FacesBodyTag} instead.</p>
 *
 * <p>The <strong>id</strong> attribute of a <code>FacesTag</code> is used
 * to identify the corresponding component in the response component tree.
 * It may contain either the absolute <em>compound identifier</em> (starting
 * with a '/' character) of the corresponding component, or a relative
 * expression that is resolved from the closest surrounding
 * <code>FacesTag</code> instance as if by a call to the
 * <code>findComponent()</code> method, passing
 * the specified <code>id</code> value as a paramter.</p>
 */

public abstract class FacesTag extends TagSupport {


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Find and return the component, from the response component tree,
     * that corresponds to the absolute or relative identifier defined in the
     * <code>id</code> attribute of this tag.</p>
     *
     * @exception JspException if the specified component cannot be located
     */
    protected UIComponent findComponent() throws JspException {

        // Acquire a reference to the response component tree
        FacesContext context = (FacesContext)
            pageContext.getAttribute(FacesContext.FACES_CONTEXT_ATTR,
                                     PageContext.REQUEST_SCOPE);
        if (context == null) { // FIXME - i18n
            throw new JspException("Cannot locate FacesContext");
        }
        Tree tree = context.getResponseTree();
        if (tree == null) { // FIXME - i18n
            throw new JspException("Cannot locate Tree");
        }

        // Process an absolute identifier
        String id = getId();
        if (id == null) { // FIXME - i18n
            throw new JspException("No id attribute specified");
        } else if (id.length() < 1) { // FIXME - i18n
            throw new JspException("Zero-length id attribute specified");
        } else if (id.startsWith("/")) {
            UIComponent root = tree.getRoot();
            if (root == null) { // FIXME - i18n
                throw new JspException("Cannot locate root node");
            }
            try {
                return (root.findComponent(id));
            } catch (IllegalArgumentException e) {
                throw new JspException(e);
            }
        }

        // Process a relative identifier
        Tag tag = getParent();
        while (true) {
            if (tag == null) { // FIXME - i18n
                throw new JspException("Cannot find parent FacesTag");
            }
            if (tag instanceof FacesTag) {
                break;
            }
            tag = tag.getParent();
        }
        FacesTag parent = (FacesTag) tag;
        try {
            UIComponent component = parent.findComponent();
            return (component.findComponent(id));
        } catch (IllegalArgumentException e) { // FIXME - i18n
            throw new JspException("Cannot find parent FacesTag component");
        }

    }


}
