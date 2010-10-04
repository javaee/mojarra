/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package javax.faces.context;

import java.util.Collection;

import javax.faces.FacesWrapper;
import javax.faces.event.PhaseId;

/**
 * <p>Provides a simple implementation of {@link PartialViewContext} that can
 * be subclassed by developers wishing to provide specialized behavior
 * to an existing {@link PartialViewContext} instance.  The default
 * implementation of all methods is to call through to the wrapped
 * {@link ExternalContext} instance.</p>
 *
 * <p>Usage: extend this class and override {@link #getWrapped} to
 * return the instance being wrapping.</p>
 *
 * @since 2.0
 */
public abstract class PartialViewContextWrapper extends PartialViewContext implements FacesWrapper<PartialViewContext> {


    // ----------------------------------------------- Methods from FacesWrapper


    /**
     * @return the wrapped {@link PartialViewContext} instance
     * @see javax.faces.FacesWrapper#getWrapped()
     */
    public abstract PartialViewContext getWrapped();


    // ----------------------------------------- Methods from PartialViewContext


    /**
     * <p>The default behavior of this method is to
     * call {@link PartialViewContext#getExecuteIds()}
     * on the wrapped {@link PartialViewContext} object.</p>
     *
     * @see PartialViewContext#getExecuteIds()
     */
    public Collection<String> getExecuteIds() {
        return getWrapped().getExecuteIds();
    }

     /**
     * <p>The default behavior of this method is to
     * call {@link PartialViewContext#getRenderIds()}
     * on the wrapped {@link PartialViewContext} object.</p>
     *
     * @see PartialViewContext#getRenderIds()
     */
    public Collection<String> getRenderIds() {
        return getWrapped().getRenderIds();
    }

     /**
     * <p>The default behavior of this method is to
     * call {@link PartialViewContext#getPartialResponseWriter()}
     * on the wrapped {@link PartialViewContext} object.</p>
     *
     * @see PartialViewContext#getPartialResponseWriter()
     */
    public PartialResponseWriter getPartialResponseWriter() {
        return getWrapped().getPartialResponseWriter();
    }

     /**
     * <p>The default behavior of this method is to
     * call {@link PartialViewContext#isAjaxRequest()}
     * on the wrapped {@link PartialViewContext} object.</p>
     *
     * @see javax.faces.context.PartialViewContext#isAjaxRequest()
     */
    public boolean isAjaxRequest() {
        return getWrapped().isAjaxRequest();
    }

     /**
     * <p>The default behavior of this method is to
     * call {@link PartialViewContext#isPartialRequest()}
     * on the wrapped {@link PartialViewContext} object.</p>
     *
     * @see PartialViewContext#isPartialRequest()
     */
    public boolean isPartialRequest() {
        return getWrapped().isPartialRequest();
    }

     /**
     * <p>The default behavior of this method is to
     * call {@link PartialViewContext#isExecuteAll()}
     * on the wrapped {@link PartialViewContext} object.</p>
     *
     * @see PartialViewContext#isExecuteAll()
     */
    public boolean isExecuteAll() {
        return getWrapped().isExecuteAll();
    }

     /**
     * <p>The default behavior of this method is to
     * call {@link PartialViewContext#isRenderAll()}
     * on the wrapped {@link PartialViewContext} object.</p>
     *
     * @see PartialViewContext#isRenderAll()
     */
    public boolean isRenderAll() {
        return getWrapped().isRenderAll();
    }

     /**
     * <p>The default behavior of this method is to
     * call {@link PartialViewContext#setRenderAll(boolean)}
     * on the wrapped {@link PartialViewContext} object.</p>
     *
     * @see PartialViewContext#setRenderAll(boolean)
     */
    public void setRenderAll(boolean renderAll) {
        getWrapped().setRenderAll(renderAll);
    }

     /**
     * <p>The default behavior of this method is to
     * call {@link PartialViewContext#release()}
     * on the wrapped {@link PartialViewContext} object.</p>
     *
     * @see PartialViewContext#release()
     */
    public void release() {
        getWrapped().release();
    }

     /**
     * <p>The default behavior of this method is to
     * call {@link PartialViewContext#processPartial(PhaseId)}
     * on the wrapped {@link PartialViewContext} object.</p>
     *
     * @see PartialViewContext#processPartial(PhaseId)
     */
    public void processPartial(PhaseId phaseId) {
        getWrapped().processPartial(phaseId);
    }

}
