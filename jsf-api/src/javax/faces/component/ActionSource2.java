/*
 * $Id: ActionSource2.java,v 1.3 2007/01/29 07:56:10 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
