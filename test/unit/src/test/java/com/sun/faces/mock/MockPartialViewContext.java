/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
package com.sun.faces.mock;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.event.PhaseId;

/**
 * MockPartialViewContext implementation.
 */
public class MockPartialViewContext extends PartialViewContext {

    private Map<Object, Object> attributes;

    // ------------------------------------------------------------ Constructors
    public MockPartialViewContext() {

        attributes = new HashMap<Object, Object>();

    }

    // ----------------------------------------- Methods from PartialViewContext
    public Map<Object, Object> getAttributes() {
        return attributes;
    }

    public List<String> getExecutePhaseClientIds() {
        throw new UnsupportedOperationException();
    }

    public void setExecutePhaseClientIds(List<String> executePhaseClientIds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> getExecuteIds() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<String> getRenderIds() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setRenderPhaseClientIds(List<String> renderPhaseClientIds) {
        throw new UnsupportedOperationException();
    }

    public PartialResponseWriter getPartialResponseWriter() {
        throw new UnsupportedOperationException();
    }

    public boolean isAjaxRequest() {
        return false;
    }

    public boolean isPartialRequest() {
        return false;
    }

    public boolean isExecuteNone() {
        throw new UnsupportedOperationException();
    }

    public boolean isExecuteAll() {
        throw new UnsupportedOperationException();
    }

    public boolean isRenderAll() {
        throw new UnsupportedOperationException();
    }

    public void setRenderAll(boolean renderAll) {
        throw new UnsupportedOperationException();
    }

    public boolean isRenderNone() {
        throw new UnsupportedOperationException();
    }

    public void enableResponseWriting(boolean enable) {
        throw new UnsupportedOperationException();
    }

    public void processPartial(PhaseId phaseId) {
        throw new UnsupportedOperationException();
    }

    public void release() {
        // no-op
    }

    @Override
    public void setPartialRequest(boolean arg0) {
    }

}
