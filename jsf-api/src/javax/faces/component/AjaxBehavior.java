/*
 * $Id: AjaxBehavior.java,v 1.0 2008/11/03 18:51:29 rogerk Exp $
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
 
package javax.faces.component;

import java.io.Serializable;

/**
 * <p class="changed_added_2_0">An instance of this class is added
 * to an {@link EditableValueHolder} or {@link ActionSource} component's
 * attribute <code>Map</code> to cause the rendering of the 
 * JavaScript <code>jsf.ajax.request</code> function call
 * when the component is rendered.</p>
 *
 * @since 2.0
 */
public class AjaxBehavior implements Serializable {

    /**
     * <p class="changed_added_2_0">The key that when added to a 
     * component's attributes Map will cause the rendering of 
     * JavaScript to perform an Ajax request.</p> 
     *
     * @since 2.0
     */
    public static final String AJAX_BEHAVIOR = "javax.faces.component.AjaxBehavior";

    /**
     * <p class="changed_added_2_0">The identifier for Ajax value change events.</p> 
     *
     * @since 2.0
     */
    public static final String AJAX_VALUE_CHANGE = "valueChange";

    /**
     * <p class="changed_added_2_0">The identifier for Ajax action events.</p> 
     *
     * @since 2.0
     */
    public static final String AJAX_ACTION = "action";

    /**
     * <p class="changed_added_2_0">The identifier for both Ajax value change and 
     * action events.</p> 
     *
     * @since 2.0
     */
    public static final String AJAX_VALUE_CHANGE_ACTION = "all";

    private String onError = null;

    private String onEvent = null;

    private String facesEvent = null;

    private String execute = null;

    private String render = null;

    public AjaxBehavior(String facesEvent, String onEvent, String onError, String execute, String render) {
        this.onError = onError;
        this.onEvent = onEvent;
        this.facesEvent = facesEvent;
        this.execute = execute;
        this.render = render;
    }

    /**
     * <p class="changed_added_2_0">Return the Faces event associated with
     * this instance.</p>
     *
     * @since 2.0
     */
    public String getFacesEvent() {
        return facesEvent;
    }

    /**
     * <p class="changed_added_2_0">Return the <code>String</code> of
     * JavaScript function name that will be used to identify
     * the client callback function that should be run in the event of
     * an error.
     *
     * @since 2.0
     */
    public String getOnError() {
        return onError;
    }

    /**
     * <p class="changed_added_2_0">Return the <code>String</code> of
     * JavaScript function name that will be used to identify the
     * client callback function that should be run on the occurance
     * of a client-side event.
     *
     * @since 2.0
     */
    public String getOnEvent() {
        return onEvent;
    }

    /**
     * <p class="changed_added_2_0">Return the <code>String</code> of component
     * identifiers that will be used to identify components that should be
     * processed during the <code>execute</code> phase of the request
     * processing lifecycle.</p> 
     *
     * @since 2.0
     */
    public String getExecute() {
        return execute;
    }

    /**
     * <p class="changed_added_2_0">Return the <code>String</code> of component
     * identifiers that will be used to identify components that should be
     * processed during the <code>render</code> phase of the request
     * processing lifecycle.</p> 
     *
     * @since 2.0
     */
    public String getRender() {
        return render;
    }
}
