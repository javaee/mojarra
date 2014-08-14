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
package javax.faces.flow.builder;

import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.flow.Parameter;

/**
 * <p class="changed_added_2_2">Create a method call node in the current {@link javax.faces.flow.Flow}.</p>
 * @since 2.2
 */
public abstract class MethodCallBuilder implements NodeBuilder {
    
    /**
     * <p class="changed_added_2_2">Set the method expression of this method call node.  The method
     * signature of the argument {@code methodExpression} must match the number and
     * type of the parameters passed in the {@link #parameters} method.</p>
     * 
     * @param methodExpression The {@code MethodExpression} to invoke.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract MethodCallBuilder expression(MethodExpression methodExpression);
    
    /**
     * <p class="changed_added_2_2">Set the method expression of this method call node.  The method
     * signature of the argument {@code methodExpression} must match the number and
     * type of the parameters passed in the {@link #parameters} method.</p>
     * 
     * @param methodExpression The {@code MethodExpression} String to invoke.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract MethodCallBuilder expression(String methodExpression);
    
    /**
     * <p class="changed_added_2_2">Set the method expression of this method call node.  The method
     * signature of the argument {@code methodExpression} must match the number and
     * type of the parameters passed in the {@link #parameters} method.</p>
     * 
     * @param methodExpression The {@code MethodExpression} to invoke.
     * @param paramTypes the types of the parameters to the method.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract MethodCallBuilder expression(String methodExpression, Class [] paramTypes);
    
    /**
     * <p class="changed_added_2_2">Set the parameters of the method call node.</p>
     * 
     * @param parameters the parameters to pass to the method when it is invoked.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract MethodCallBuilder parameters(List<Parameter> parameters);
    
    /**
     * <p class="changed_added_2_2">If the method is a void method, or the
     * method returns {@code null}, this can be used to specify what value 
     * should be passed to runtime when the method returns.</p>
     * 
     * @param outcome A {@code ValueExpression} String representing 
     * the default outcome, only used if the method is a void
     * method or returns {@code null}.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract MethodCallBuilder defaultOutcome(String outcome);
    
    /**
     * <p class="changed_added_2_2">If the method is a void method, or the
     * method returns {@code null}, this can be used to specify what value 
     * should be passed to runtime when the method returns.</p>
     * 
     * @param outcome A {@code ValueExpression} representing 
     * the default outcome, only used if the method is a void
     * method or returns {@code null}.

     * @throws NullPointerException if any of the parameters are {@code null}
     * 
     * @since 2.2
     */
    public abstract MethodCallBuilder defaultOutcome(ValueExpression outcome);

    @Override
    public abstract MethodCallBuilder markAsStartNode();
    
}
