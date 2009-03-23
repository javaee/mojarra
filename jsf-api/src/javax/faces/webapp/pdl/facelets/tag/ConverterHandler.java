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

package javax.faces.webapp.pdl.facelets.tag;


import javax.faces.component.ValueHolder;
import javax.faces.convert.Converter;

import javax.faces.webapp.pdl.ValueHolderAttachedObjectHandler;
import javax.faces.webapp.pdl.facelets.FaceletContext;

/**
 * Handles setting a Converter instance on a ValueHolder. Will wire all
 * attributes set to the Converter instance created/fetched. Uses the "binding"
 * attribute for grabbing instances to apply attributes to. <p/> Will only
 * set/create Converter is the passed UIComponent's parent is null, signifying
 * that it wasn't restored from an existing tree.
 * 
 * @see javax.faces.webapp.ConverterELTag
 * @see javax.faces.convert.Converter
 * @see javax.faces.component.ValueHolder
 * @author Jacob Hookom
 * @version $Id: ConverterHandler.java 6118 2008-12-16 03:56:08Z edburns $
 */
public class ConverterHandler extends FaceletsAttachedObjectHandler implements ValueHolderAttachedObjectHandler {

    
    private String converterId;
    
    
    private TagHandlerDelegate helper;

    public ConverterHandler(ConverterConfig config) {
        super(config);
        this.converterId = config.getConverterId();
    }

    @Override
    protected TagHandlerDelegate getTagHandlerHelper() {
        if (null == helper) {
            helper = helperFactory.createConverterHandlerDelegate(this);
        }
        return helper;

    }

    public String getConverterId(FaceletContext ctx) {
        if (converterId == null) {
            TagAttribute idAttr = getAttribute("converterId");
            if (idAttr == null) {
                return null;
            } else {
                return idAttr.getValue(ctx);
            }
        }
        return converterId;
    }
    
}
