/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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


package com.sun.faces.facelets.tag.composite;

import com.sun.faces.facelets.tag.TagHandlerImpl;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import java.io.IOException;
import java.util.List;

/**
 * This <code>TagHandler</code> is responsible for relocating children
 * defined within a composite component to a component within the
 * composite component's <code>composite:implementation</code> section.
 */
public class InsertChildrenHandler extends TagHandlerImpl {

    private static final String REQUIRED_ATTRIBUTE = "required";

    // This attribute is not required.  If not defined, then assume the facet
    // isn't necessary.
    TagAttribute required;

    
    // ------------------------------------------------------------ Constructors


    public InsertChildrenHandler(TagConfig config) {

        super(config);
        required = getAttribute(REQUIRED_ATTRIBUTE);

    }


    // ------------------------------------------------- Methods from TagHandler


    public void apply(FaceletContext ctx, UIComponent parent)
          throws IOException {

        UIComponent compositeParent =
              UIComponent.getCurrentCompositeComponent(ctx.getFacesContext());
        if (compositeParent == null) {
            return;
        }

        boolean required =
              ((this.required != null) && this.required.getBoolean(ctx));

        if (compositeParent.getChildCount() == 0 && required) {
            throwRequiredException(ctx, compositeParent);
        }

        List<UIComponent> compositeChildren = compositeParent.getChildren();
        List<UIComponent> parentChildren = parent.getChildren();
        parentChildren.addAll(compositeChildren);

    }


    // --------------------------------------------------------- Private Methods


    private void throwRequiredException(FaceletContext ctx,
                                        UIComponent compositeParent) {

        throw new TagException(this.tag,
                               "Unable to find any children components "
                               + "nested within parent composite component with id '"
                               + compositeParent .getClientId(ctx.getFacesContext())
                               + '\'');

    }
}
