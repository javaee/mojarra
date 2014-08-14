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

import javax.el.MethodExpression;
import javax.faces.event.ActionListener;



/**
 * <p><strong>ActionSource2</strong> extends {@link ActionSource} and
 * provides a JavaBeans property analogous to the "<code>action</code>"
 * property on <code>ActionSource</code>.  The difference is the type of
 * this property is a {@link MethodExpression} rather than a
 * <code>MethodBinding</code>.  This allows the
 * <code>ActionSource</code> concept to leverage the new Unified EL
 * API.</p>
 *
 * @since 1.2
 */

public interface ActionSource2 extends ActionSource {


    // -------------------------------------------------------------- Properties

    /**
     * <p>Return the {@link MethodExpression} pointing at the application
     * action to be invoked, if this {@link UIComponent} is activated by
     * the user, during the <em>Apply Request Values</em> or <em>Invoke
     * Application</em> phase of the request processing lifecycle,
     * depending on the value of the <code>immediate</code>
     * property.</p>
     *
     * <p>Note that it's possible that the returned
     * <code>MethodExpression</code> is just a wrapper around a
     * <code>MethodBinding</code> instance whith was set by a call to
     * {@link ActionSource#setAction}.  This makes it possible for the
     * default {@link ActionListener} to continue to work properly with
     * older components.</p>
     */
    public MethodExpression getActionExpression();

    /**
     * <p>Set the {@link MethodExpression} pointing at the appication
     * action to be invoked, if this {@link UIComponent} is activated by
     * the user, during the <em>Apply Request Values</em> or <em>Invoke
     * Application</em> phase of the request processing lifecycle,
     * depending on the value of the <code>immediate</code>
     * property.</p>
     *
     * <p>Any method referenced by such an expression must be public, with
     * a return type of <code>String</code>, and accept no parameters.</p>
     *
     * @param action The new method expression
     */
    public void setActionExpression(MethodExpression action);

    
}
