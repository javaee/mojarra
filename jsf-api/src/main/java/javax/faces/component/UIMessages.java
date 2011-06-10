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

package javax.faces.component;

import javax.faces.context.FacesContext;

/**
 * <p>The renderer for this component is responsible for obtaining the
 * messages from the {@link FacesContext} and displaying them to the
 * user.</p>
 *
 * <p>This component supports the <code>Messages</code> renderer-type.</p>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>javax.faces.Messages</code>".  This value can be changed by
 * calling the <code>setRendererType()</code> method.</p>
 *
 * 
 */

public class UIMessages extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Messages";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Messages";


    enum PropertyKeys {
        forValue("for"),
        globalOnly,
        showDetail,
        showSummary,
        redisplay;

        String toString;

        PropertyKeys(String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        public String toString() {
            return ((this.toString != null) ? this.toString : super.toString());
        }
    }

    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIMessages} instance with default property
     * values.</p>
     */
    public UIMessages() {

        super();
        setRendererType("javax.faces.Messages");

    }


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }

    /**
     * <p class="changed_added_2_0">Return the client identifier of the
     * component for which this component represents associated message(s)
     * (if any).</p>
     */
    public String getFor() {

        return (String) getStateHelper().eval(PropertyKeys.forValue);

    }


    /**
     * <p>Set the client identifier of the component for which this
     * component represents associated message(s) (if any).  This
     * property must be set before the message is displayed.</p>
     *
     * @param newFor The new client id
     */
    public void setFor(String newFor) {

        getStateHelper().put(PropertyKeys.forValue, newFor);

    }

    /**
     * <p>Return the flag indicating whether only global messages (that
     * is, messages with no associated client identifier) should be
     * rendered.  Mutually exclusive with the "for" property which takes
	 * precedence. Defaults to false.</p>
     */
    public boolean isGlobalOnly() {

        return (Boolean) getStateHelper().eval(PropertyKeys.globalOnly, false);

    }


    /**
     * <p>Set the flag indicating whether only global messages (that is,
     * messages with no associated client identifier) should be rendered.</p>
     *
     * @param globalOnly The new flag value
     */
    public void setGlobalOnly(boolean globalOnly) {

        getStateHelper().put(PropertyKeys.globalOnly, globalOnly);

    }

    /**
     * <p>Return the flag indicating whether the <code>detail</code>
     * property of the associated message(s) should be displayed.
     * Defaults to false.</p>
     */
    public boolean isShowDetail() {

        return (Boolean) getStateHelper().eval(PropertyKeys.showDetail, false);

    }


    /**
     * <p>Set the flag indicating whether the <code>detail</code> property
     * of the associated message(s) should be displayed.</p>
     *
     * @param showDetail The new flag
     */
    public void setShowDetail(boolean showDetail) {

        getStateHelper().put(PropertyKeys.showDetail, showDetail);
    }


    /**
     * <p>Return the flag indicating whether the <code>summary</code>
     * property of the associated message(s) should be displayed.
     * Defaults to true.</p>
     */
    public boolean isShowSummary() {

        return (Boolean) getStateHelper().eval(PropertyKeys.showSummary, true);

    }


    /**
     * <p>Set the flag indicating whether the <code>summary</code> property
     * of the associated message(s) should be displayed.</p>
     *
     * @param showSummary The new flag value
     */
    public void setShowSummary(boolean showSummary) {

        getStateHelper().put(PropertyKeys.showSummary, showSummary);

    }


    /**
     * @return <code>true</code> if this <code>UIMessage</code> instance should
     *  redisplay {@link javax.faces.application.FacesMessage}s that have already been handled,
     *  otherwise returns <code>false</code>.  By default this method will
     *  always return <code>true</code> if {@link #setRedisplay(boolean)} has
     *  not been called.
     *
     * @since 2.0
     */
    public boolean isRedisplay() {

        return (Boolean) getStateHelper().eval(PropertyKeys.redisplay, true);

    }


    /**
     * <p>Set the flag indicating whether the <code>detail</code> property
     * of the associated message(s) should be displayed.</p>
     *
     * @param redisplay flag indicating whether previously handled messages
     *  are redisplayed or not
     *
     * @since 2.0
     */
    public void setRedisplay(boolean redisplay) {

        getStateHelper().put(PropertyKeys.redisplay, redisplay);

    }

}
