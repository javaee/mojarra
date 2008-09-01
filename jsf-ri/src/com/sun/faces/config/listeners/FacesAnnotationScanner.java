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
package com.sun.faces.config.listeners;

import java.util.Set;
import java.util.HashSet;

/**
 * This class scans for the following annotation types:
 * <ul>
 *  <li>javax.faces.component.FacesComponent</li>
 *  <li>javax.faces.convert.FacesConverter</li>
 *  <li>javax.faces.validator.FacesValidator</li>
 *  <li>javax.faces.render.FacesRenderkit</li>
 *  <li>javax.faces.render.FacesRenderer</li>
 * </ul>
 */
class FacesAnnotationScanner {

    private Set<String> annotations=null;


    // ------------------------------------------------------------ Constructors


    public FacesAnnotationScanner() {

        init();
        
    }


    // ------------------------------------ Methods from CustomAnnotationScanner


    /**
     * Test if the passed constant pool string is a reference to 
     * a Type.TYPE annotation of a J2EE component
     *
     * @String the constant pool info string 
     * @return true if it is a J2EE annotation reference
     */
    public boolean isAnnotation(String value) {

        return annotations.contains(value);

    }


    // --------------------------------------------------------- Private Methods


    private void init() {

        annotations = new HashSet<String>(5);
        annotations.add("Ljavax/faces/component/FacesComponent;");
        annotations.add("Ljavax/faces/convert/FacesConverter;");
        annotations.add("Ljavax/faces/validator/FacesValidator;");
        annotations.add("Ljavax/faces/render/FacesRenderKit;");
        annotations.add("Ljavax/faces/render/FacesRenderer;");

    }
}
