/*
 * $Id: PartialViewContext.java,v 1.76.2.2 2008/04/09 08:59:05 edburns Exp $
 */

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

package javax.faces.context;

import java.util.Collection;

import javax.faces.event.PhaseId;

/**
 * <p><strong class="changed_added_2_0">PartialViewContext</strong>
 * contains methods and properties that pertain to partial request
 * processing and partial response rendering on a view.</p> 
 *
 * <p>The {@link PartialViewContext} instance is used to determine if
 * the current request indicates the requirement to perform 
 * <code>partial processing</code> and/or <code>partial rendering</code>.
 * Partial processing is the processing of selected components 
 * through the <code>execute</code> portion of the request processing
 * lifecycle.  Partial rendering is the rendering of specified
 * components in the <code>Render Response Phase</code> of the
 * request processing lifecycle.</p>
 * 
 */

public abstract class PartialViewContext {


    /**
     * <p class="changed_added_2_0">
     * The request parameter name whose request parameter value 
     * is a <code>Collection</code> of client identifiers identifying the
     * components that must be processed during the 
     * <em>Render Response</em> phase of the request processing 
     * lifecycle.</p>
     *
     * @since 2.0
     */
    public static final String PARTIAL_RENDER_PARAM_NAME =
          "javax.faces.partial.render";


    /**
     * <p class="changed_added_2_0">
     * The request parameter name whose request parameter value 
     * is a <code>Collection</code> of client identifiers identifying the
     * components that must be processed during the 
     * <em>Apply Request Values</em>, <em>Process Validations</em>,
     * and <em>Update Model Values</em> phases of the request 
     * processing lifecycle.</p>
     *
     * @since 2.0
     */
    public static final String PARTIAL_EXECUTE_PARAM_NAME =
          "javax.faces.partial.execute";


    /**
     * <p class="changed_added_2_0">
     * The value that when used with {@link #PARTIAL_EXECUTE_PARAM_NAME}
     * or {@link #PARTIAL_RENDER_PARAM_NAME} indicates these phases
     * must be skipped.</p>  
     *
     * @since 2.0
     */
    public static final String NO_PARTIAL_PHASE_CLIENT_IDS = "@none";

    /**
     * <p class="changed_added_2_0">
     * The value that when used with {@link #PARTIAL_EXECUTE_PARAM_NAME}
     * or {@link #PARTIAL_RENDER_PARAM_NAME} indicates these phases
     * must be skipped.</p>
     *
     * @since 2.0
     */
    public static final String ALL_PARTIAL_PHASE_CLIENT_IDS = "@all";


    // -------------------------------------------------------------- Properties
    
    /**
     * <p class="changed_added_2_0">Return a
     * <code>Collection</code> of client identifiers from the current request
     * with the request parameter name {@link #PARTIAL_EXECUTE_PARAM_NAME}.
     * If the value of the request parameter is {@link #NO_PARTIAL_PHASE_CLIENT_IDS},
     * or there is no such request parameter, return an empty <code>Collection</code>.
     * These client identifiers are used to identify components that
     * will be processed during the <code>execute</code> phase of the
     * request processing lifecycle.  The returned <code>Collection</code> is
     * mutable.</p>
     *
     * @throws IllegalStateException if this method is called after
     *  this instance has been released
     *
     * @since 2.0
     */
    public abstract Collection<String> getExecuteIds();
    
    /**
     * <p class="changed_added_2_0">Return a
     * <code>Collection</code> of client identifiers from the current request
     * with the request parameter name {@link #PARTIAL_RENDER_PARAM_NAME}.
     * If the value of the request parameter is {@link #NO_PARTIAL_PHASE_CLIENT_IDS},
     * or there is no such request parameter, return an empty <code>Collection</code>.
     * These client identifiers are used to identify components that
     * will be processed during the <code>render</code> phase of the
     * request processing lifecycle.  The returned <code>Collection</code> is 
     * mutable.</p>
     *
     * @throws IllegalStateException if this method is called after
     *  this instance has been released
     *
     * @since 2.0
     */
    public abstract Collection<String> getRenderIds();

    /**
     * <p class="changed_added_2_0">Return the {@link ResponseWriter}
     * to which components should
     * direct their output for partial view rendering.  Within a given
     * response, components can use either the ResponseStream or the
     * ResponseWriter, but not both.</p>
     *
     * @throws IllegalStateException if this method is called after
     *  this instance has been released
     *
     * @since 2.0
     */
    public abstract PartialResponseWriter getPartialResponseWriter();

    /**
     * <p class="changed_added_2_0">
     * Return <code>true</code> if the request parameter
     * <code>javax.faces.partial.ajax</code> is present in the current
     * request.  Otherwise, return <code>false</code>.</p>
     *
     * @throws IllegalStateException if this method is called after
     *  this instance has been released
     *
     * @since 2.0
     */
    public abstract boolean isAjaxRequest();

    /**
     * <p class="changed_added_2_0">
     * Return <code>true</code> if the request parameter
     * <code>javax.faces.partial</code> or
     * <code>javax.faces.partial.ajax</code> is present in the current
     * request.  Otherwise, return <code>false</code>.</p>
     *
     * @throws IllegalStateException if this method is called after
     *  this instance has been released
     *
     * @since 2.0
     */
    public abstract boolean isPartialRequest();

    /**
     * <p class="changed_added_2_0">
     * Return <code>true</code> if {@link #isAjaxRequest}
     * returns <code>true</code> and {@link #PARTIAL_EXECUTE_PARAM_NAME}
     * is present in the current request with the value
     * {@link #ALL_PARTIAL_PHASE_CLIENT_IDS}.</p>
     * Otherwise, return <code>false</code>.</p>
     *
     * @throws IllegalStateException if this method is called after
     *  this instance has been released
     *
     * @since 2.0
     */
    public abstract boolean isExecuteAll();

    /**
     * <p class="changed_added_2_0">
     * Return <code>true</code> if {@link #isAjaxRequest}
     * returns <code>true</code> and {@link #PARTIAL_RENDER_PARAM_NAME}
     * is present in the current request with the value
     * {@link #ALL_PARTIAL_PHASE_CLIENT_IDS}.</p>
     * Otherwise, return <code>false</code>.</p>
     *
     * @throws IllegalStateException if this method is called after
     *  this instance has been released
     *
     * @since 2.0
     */
    public abstract boolean isRenderAll();

    /**
     * <p class="changed_added_2_0">
     * Indicate the entire view must be rendered if
     * <code>renderAll</code> is <code>true</code>.</p>
     *
     * @param renderAll the value <code>true</code> indicates
     * the entire view must be rendered.
     *
     * @throws IllegalStateException if this method is called after
     *  this instance has been released
     *
     * @since 2.0
     */
    public abstract void setRenderAll(boolean renderAll);

    /**
     * <p><span class="changed_added_2.0">Release</span> any
     * resources associated with this <code>PartialViewContext</code>
     * instance.</p>
     *
     * @throws IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract void release();

    /**
     * <p class="changed_added_2_0">Perform lifecycle processing on 
     * components during the indicated <code>phaseId</code>.  Only 
     * those components with identifiers existing in the 
     * <code>Collection</code> returned from {@link #getExecuteIds} 
     * and {@link #getRenderIds} will be processed.</p>  
     *
     * @param phaseId the {@link javax.faces.event.PhaseId} that indicates
     * the lifecycle phase the components will be processed in. 
     */ 
    public abstract void processPartial(PhaseId phaseId);


}
