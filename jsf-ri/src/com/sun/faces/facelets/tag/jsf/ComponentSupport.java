/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.tag.jsf;

import com.sun.faces.facelets.tag.jsf.core.FacetHandler;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.webapp.pdl.facelets.tag.TagAttributeException;

/**
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public final class ComponentSupport {

    private final static String MARK_DELETED = "com.sun.faces.facelets.MARK_DELETED";
    public final static String MARK_CREATED = "com.sun.faces.facelets.MARK_ID";
    private final static String IMPLICIT_PANEL = "com.sun.faces.facelets.IMPLICIT_PANEL";
    
    /**
     * Used in conjunction with markForDeletion where any UIComponent marked
     * will be removed.
     * 
     * @param c
     *            UIComponent to finalize
     */
    public static final void finalizeForDeletion(UIComponent c) {
        // remove any existing marks of deletion
        c.getAttributes().remove(MARK_DELETED);

        // finally remove any children marked as deleted
        int sz = c.getChildCount();
        if (sz > 0) {
            UIComponent cc = null;
            List cl = c.getChildren();
            while (--sz >= 0) {
                cc = (UIComponent) cl.get(sz);
                if (cc.getAttributes().containsKey(MARK_DELETED)) {
                    cl.remove(sz);
                }
            }
        }

        // remove any facets marked as deleted
        if (c.getFacets().size() > 0) {
            Collection col = c.getFacets().values();
            UIComponent fc;
            for (Iterator itr = col.iterator(); itr.hasNext();) {
                fc = (UIComponent) itr.next();
                if (fc.getAttributes().containsKey(MARK_DELETED)) {
                    itr.remove();
                }
            }
        }
    }
    

    /**
     * A lighter-weight version of UIComponent's findChild.
     * 
     * @param parent
     *            parent to start searching from
     * @param id
     *            to match to
     * @return UIComponent found or null
     */
    public static final UIComponent findChild(UIComponent parent, String id) {
        int sz = parent.getChildCount();
        if (sz > 0) {
            UIComponent c = null;
            List cl = parent.getChildren();
            while (--sz >= 0) {
                c = (UIComponent) cl.get(sz);
                if (id.equals(c.getId())) {
                    return c;
                }
            }
        }
        return null;
    }
    
    /**
     * By TagId, find Child
     * @param parent
     * @param id
     * @return
     */
    public static final UIComponent findChildByTagId(UIComponent parent, String id) {
        Iterator itr = parent.getFacetsAndChildren();
        UIComponent c = null;
        String cid = null;
        while (itr.hasNext()) {
            c = (UIComponent) itr.next();
            cid = (String) c.getAttributes().get(MARK_CREATED);
            if (id.equals(cid)) {
                return c;
            }
            if (c instanceof UIPanel && c.getAttributes().containsKey(IMPLICIT_PANEL)) {
                for (UIComponent c2 : c.getChildren()) {
                    cid = (String) c2.getAttributes().get(MARK_CREATED);
                    if (id.equals(cid)) {
                        return c2;
                    }
                }
            }
        }
//        int sz = parent.getChildCount();
//        if (sz > 0) {
//            UIComponent c = null;
//            List cl = parent.getChildren();
//            String cid = null;
//            while (--sz >= 0) {
//                c = (UIComponent) cl.get(sz);
//                cid = (String) c.getAttributes().get(MARK_CREATED);
//                if (id.equals(cid)) {
//                    return c;
//                }
//            }
//        }
        return null;
    }

    /**
     * According to JSF 1.2 tag specs, this helper method will use the
     * TagAttribute passed in determining the Locale intended.
     * 
     * @param ctx
     *            FaceletContext to evaluate from
     * @param attr
     *            TagAttribute representing a Locale
     * @return Locale found
     * @throws TagAttributeException
     *             if the Locale cannot be determined
     */
    public static final Locale getLocale(FaceletContext ctx, TagAttribute attr)
            throws TagAttributeException {
        Object obj = attr.getObject(ctx);
        if (obj instanceof Locale) {
            return (Locale) obj;
        }
        if (obj instanceof String) {
            String s = (String) obj;
            if (s.length() == 2) {
                return new Locale(s);
            }
            if (s.length() == 5) {
                return new Locale(s.substring(0, 2), s.substring(3, 5)
                        .toUpperCase());
            }
            if (s.length() >= 7) {
                return new Locale(s.substring(0, 2), s.substring(3, 5)
                        .toUpperCase(), s.substring(6, s.length()));
            }
            throw new TagAttributeException(attr, "Invalid Locale Specified: "
                    + s);
        } else {
            throw new TagAttributeException(attr,
                    "Attribute did not evaluate to a String or Locale: " + obj);
        }
    }

    /**
     * Tries to walk up the parent to find the UIViewRoot, if not found, then go
     * to FaceletContext's FacesContext for the view root.
     * 
     * @param ctx
     *            FaceletContext
     * @param parent
     *            UIComponent to search from
     * @return UIViewRoot instance for this evaluation
     */
    public static final UIViewRoot getViewRoot(FaceletContext ctx,
            UIComponent parent) {
        UIComponent c = parent;
        do {
            if (c instanceof UIViewRoot) {
                return (UIViewRoot) c;
            } else {
                c = c.getParent();
            }
        } while (c != null);
        return ctx.getFacesContext().getViewRoot();
    }

    /**
     * Marks all direct children and Facets with an attribute for deletion.
     * 
     * @see #finalizeForDeletion(UIComponent)
     * @param c
     *            UIComponent to mark
     */
    public static final void markForDeletion(UIComponent c) {
        // flag this component as deleted
        c.getAttributes().put(MARK_DELETED, Boolean.TRUE);

        // mark all children to be deleted
        int sz = c.getChildCount();
        if (sz > 0) {
            UIComponent cc = null;
            List cl = c.getChildren();
            while (--sz >= 0) {
                cc = (UIComponent) cl.get(sz);
                if (cc.getAttributes().containsKey(MARK_CREATED)) {
                    cc.getAttributes().put(MARK_DELETED, Boolean.TRUE);
                }
            }
        }

        // mark all facets to be deleted
        if (c.getFacets().size() > 0) {
            Collection col = c.getFacets().values();
            UIComponent fc;
            for (Iterator itr = col.iterator(); itr.hasNext();) {
                fc = (UIComponent) itr.next();
                if (fc.getAttributes().containsKey(MARK_CREATED)) {
                    fc.getAttributes().put(MARK_DELETED, Boolean.TRUE);
                }
            }
        }
    }
    
    public final static void encodeRecursive(FacesContext context,
            UIComponent viewToRender) throws IOException, FacesException {
        if (viewToRender.isRendered()) {
            viewToRender.encodeBegin(context);
            if (viewToRender.getRendersChildren()) {
                viewToRender.encodeChildren(context);
            } else if (viewToRender.getChildCount() > 0) {
                Iterator kids = viewToRender.getChildren().iterator();
                while (kids.hasNext()) {
                    UIComponent kid = (UIComponent) kids.next();
                    encodeRecursive(context, kid);
                }
            }
            viewToRender.encodeEnd(context);
        }
    }
    
    public static void removeTransient(UIComponent c) {
        UIComponent d, e;
        if (c.getChildCount() > 0) {
            for (Iterator itr = c.getChildren().iterator(); itr.hasNext();) {
                d = (UIComponent) itr.next();
                if (d.getFacets().size() > 0) {
                    for (Iterator jtr = d.getFacets().values().iterator(); jtr
                            .hasNext();) {
                        e = (UIComponent) jtr.next();
                        if (e.isTransient()) {
                            jtr.remove();
                        } else {
                            removeTransient(e);
                        }
                    }
                }
                if (d.isTransient()) {
                    itr.remove();
                } else {
                    removeTransient(d);
                }
            }
        }
        if (c.getFacets().size() > 0) {
            for (Iterator itr = c.getFacets().values().iterator(); itr
                    .hasNext();) {
                d = (UIComponent) itr.next();
                if (d.isTransient()) {
                    itr.remove();
                } else {
                    removeTransient(d);
                }
            }
        }
    }
    
    /**
     * <p class="changed_added_2_0">Add the child component to the parent. If the parent is a facet,
     * check to see whether the facet is already defined. If it is, wrap the existing component
     * in a panel group, if it's not already, then add the child to the panel group. If the facet
     * does not yet exist, make the child the facet.</p>
     */
    public final static void addComponent(FaceletContext ctx, UIComponent parent, UIComponent child) {
        String facetName = getFacetName(parent);
        if (facetName == null) {
            parent.getChildren().add(child);
        } else {
            UIComponent existing = parent.getFacets().get(facetName);
            if (existing != null) {
                if (!(existing instanceof UIPanel)) {
                    // move existing component under a panel group
                    UIComponent panelGroup = ctx.getFacesContext().getApplication().createComponent(UIPanel.COMPONENT_TYPE);
                    panelGroup.setId(getViewRoot(ctx.getFacesContext(), parent).createUniqueId());
                    panelGroup.getAttributes().put(ComponentSupport.IMPLICIT_PANEL, true);
                    panelGroup.getChildren().add(existing);
                    // the panel group becomes the facet
                    parent.getFacets().put(facetName, panelGroup);
                    existing = panelGroup;
                }
                if (existing.getAttributes().get(ComponentSupport.IMPLICIT_PANEL) != null) {
                    // we have a panel group, so add the new component to it
                    existing.getChildren().add(child);
                } else {
                    parent.getFacets().put(facetName, child);
                }
            } else {
                parent.getFacets().put(facetName, child);
            }
        }
    }

    public final static String getFacetName(UIComponent parent) {
        return (String) parent.getAttributes().get(FacetHandler.KEY);
    }


    // --------------------------------------------------------- private classes


    private static UIViewRoot getViewRoot(FacesContext ctx, UIComponent parent) {

        if (parent instanceof UIViewRoot) {
            return (UIViewRoot) parent;
        }
        UIViewRoot root = ctx.getViewRoot();
        if (root != null) {
            return root;
        }
        UIComponent c = parent.getParent();
        while (c != null) {
            if (c instanceof UIViewRoot) {
                root = (UIViewRoot) c;
                break;
            } else {
                c = c.getParent();
            }
        }

        return root;

    }
}
