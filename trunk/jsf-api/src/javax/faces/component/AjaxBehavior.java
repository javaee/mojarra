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
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Arrays;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;


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

    private String event;
    private ValueExpression onerrorExpression;
    private ValueExpression oneventExpression;
    private ValueExpression executeExpression;
    private ValueExpression renderExpression;
    private ValueExpression disabledExpression;


    // ------------------------------------------------------------ Constructors


    public AjaxBehavior(String event,
                        ValueExpression onevent,
                        ValueExpression onerror,
                        ValueExpression execute,
                        ValueExpression render,
                        ValueExpression disabled) {

        this.onerrorExpression = onerror;
        this.oneventExpression = onevent;
        this.event = event;
        this.executeExpression = execute;
        this.renderExpression = render;
        this.disabledExpression = disabled;

    }


    // ---------------------------------------------------------- Public Methods

    
    /**
     * <p class="changed_added_2_0">Return the Faces event associated with
     * this instance.</p>
     *
     * @since 2.0
     */
    public String getEvent() {

        return event;

    }

    /**
     * <p class="changed_added_2_0">Return the <code>String</code> of
     * JavaScript function name that will be used to identify
     * the client callback function that should be run in the event of
     * an error.
     *
     * @param context the {@link FacesContext} for the current request
     *
     * @since 2.0
     */
    public String getOnError(FacesContext context) {

        return (String) eval(context, onerrorExpression);

    }

    /**
     * <p class="changed_added_2_0">Return the <code>String</code> of
     * JavaScript function name that will be used to identify the
     * client callback function that should be run on the occurance
     * of a client-side event.
     *
     * @param context the {@link FacesContext} for the current request
     *
     * @since 2.0
     */
    public String getOnEvent(FacesContext context) {

        return (String) eval(context, oneventExpression);

    }

    /**
     * <p class="changed_added_2_0">Return a
     * <code>Collection&lt;String&gt;</code> of component
     * identifiers that will be used to identify components that should be
     * processed during the <code>execute</code> phase of the request
     * processing lifecycle.</p> 
     *
     * @param context the {@link FacesContext} for the current request
     *
     * @since 2.0
     */
    public Collection<String> getExecute(FacesContext context) {

        return getCollectionValue("execute", context, executeExpression);

    }

    /**
     * <p class="changed_added_2_0">Return a
     * <code>Collection&lt;String&gt;</code> of component
     * identifiers that will be used to identify components that should be
     * processed during the <code>render</code> phase of the request
     * processing lifecycle.</p> 
     *
     * @param context the {@link FacesContext} for the current request
     *
     * @since 2.0
     */
    public Collection<String> getRender(FacesContext context) {

        return getCollectionValue("render", context, renderExpression);

    }

    /**
     * <p class="changed_added_2_0">Return the disabled status of this component.</p>
     *
     * @param context the {@link FacesContext} for the current request
     *
     * @since 2.0
     */
    public Boolean isDisabled(FacesContext context) {

        // RELEASE_PENDING why not return boolean instead of Boolean?
        Boolean result = (Boolean) eval(context, disabledExpression);
        return ((result != null) ? result : false);

    }


    // --------------------------------------------------------- Private Methods

    private static Object eval(FacesContext ctx, ValueExpression expression) {

        return ((expression != null)
                ? expression.getValue(ctx.getELContext())
                : null);

    }


    private static Collection<String> getCollectionValue(String name,
                                                         FacesContext ctx,
                                                         ValueExpression expression) {

        Collection<String> result = null;
        Object tempAttr = eval(ctx, expression);
        if (tempAttr != null) {
            if (tempAttr instanceof String) {
                // split into separate strings, add these into a new Collection
                // RELEASE_PENDING String.split() isn't cheap.  It recreates the Pattern
                // each time it's called.
                result = new LinkedHashSet<String>(Arrays.asList(((String) tempAttr).split(" ")));
            } else if (tempAttr instanceof Collection) {
                //noinspection unchecked
                result = (Collection<String>) tempAttr;
            } else {
                // RELEASE_PENDING  i18n ;
                throw new FacesException(expression.toString()
                                         + " : '"
                                         + name
                                         + "' attribute value must be either a String or a Collection");
            }
        }
        return result;

    }

}
