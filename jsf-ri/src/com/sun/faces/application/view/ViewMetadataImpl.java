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

package com.sun.faces.application.view;

import com.sun.faces.facelets.Facelet;
import com.sun.faces.facelets.FaceletFactory;
import com.sun.faces.application.ApplicationAssociate;

import java.util.Collection;

import java.io.IOException;
import javax.faces.webapp.pdl.ViewMetadata;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIViewParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.webapp.pdl.ViewDeclarationLanguage;
import javax.faces.FacesException;


/**
 * @see javax.faces.webapp.pdl.ViewMetadata
 */
public class ViewMetadataImpl extends ViewMetadata {

    private String viewId;
    private ViewDeclarationLanguage pdl;
    private FaceletFactory faceletFactory;
    

    // ------------------------------------------------------------ Constructors


    public ViewMetadataImpl(ViewDeclarationLanguage pdl, String viewId) {

        this.pdl = pdl;
        this.viewId = viewId;

    }


    // ----------------------------------------------- Methods from ViewMetadata


    /**
     * @see javax.faces.webapp.pdl.ViewMetadata#getViewId()
     */
    @Override
    public String getViewId() {

        return viewId;

    }

    /**
     * @see javax.faces.webapp.pdl.ViewMetadata#createMetadataView(javax.faces.context.FacesContext)
     */
    @Override
    public UIViewRoot createMetadataView(FacesContext context) {

        UIViewRoot result = null;

        try {
            context.setProcessingEvents(false);
            if (faceletFactory == null) {
                ApplicationAssociate associate = ApplicationAssociate
                      .getInstance(context.getExternalContext());
                faceletFactory = associate.getFaceletFactory();
                assert (faceletFactory != null);
            }
            Facelet f = faceletFactory.getMetadataFacelet(viewId);
            result = pdl.createView(context, viewId);
            f.apply(context, result);
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        } finally {
            context.setProcessingEvents(true);
        }


        return result;
        
    }

}
