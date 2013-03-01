/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package javax.faces.flow;

import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * <p class="changed_added_2_2">Represents a method call node in the flow graph.
 * When control passes to a method call node, its {@code MethodExpression} is
 * invoked, passing any parameters.  Let <em>outcome</em> be the value determined
 * by the following algorithm.  If there is a {@code null} return from the invocation, 
 * {@link #getOutcome} is called.  If the result is non-{@code null}, its {@code getValue()} method 
 * is called and the value is considered to be <em>outcome</em>. If there is a non-{@code null} return,
 * let it be <em>outcome</em>.  Convert <em>outcome</em> to a String by calling 
 * its {@code toString} method.  Use <em>outcome</em> to determine the next
 * node in the flow graph.</p>
 * 
 * @since 2.2
 */

public abstract class MethodCallNode extends FlowNode {
    
    /**
     * <p class="changed_added_2_2">Return the {@code MethodExpression} to be
     * invoked to when control passes to this node.</p>
     * 
     * @since 2.2
     */
    public abstract MethodExpression getMethodExpression();

    /**
     * <p class="changed_added_2_2">Return the {@code outcome} to be
     * used in the event of a {@code null} return from the method.</p>
     * 
     * @since 2.2
     */
    public abstract ValueExpression getOutcome();
    
    /**
     * <p class="changed_added_2_2">Return the parameters to be passed
     * to the method.</p>
     * 
     * @since 2.2
     */
    public abstract List<Parameter> getParameters();

}
