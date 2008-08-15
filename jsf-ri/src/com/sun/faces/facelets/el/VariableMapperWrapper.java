/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.el;

import java.util.HashMap;
import java.util.Map;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

/**
 * Utility class for wrapping another VariableMapper with a new context,
 * represented by a {@link java.util.Map Map}. Modifications occur to the Map
 * instance, but resolve against the wrapped VariableMapper if the Map doesn't
 * contain the ValueExpression requested.
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public class VariableMapperWrapper extends VariableMapper {

	private final VariableMapper target;

	private Map vars;

	/**
	 * 
	 */
	public VariableMapperWrapper(VariableMapper orig) {
		super();
		this.target = orig;
	}

	/**
	 * First tries to resolve agains the inner Map, then the wrapped
	 * ValueExpression.
	 * 
	 * @see javax.el.VariableMapper#resolveVariable(java.lang.String)
	 */
	public ValueExpression resolveVariable(String variable) {
		ValueExpression ve = null;
		try {
			if (this.vars != null) {
				ve = (ValueExpression) this.vars.get(variable);
			}
			if (ve == null) {
				return this.target.resolveVariable(variable);
			}
			return ve;
		} catch (StackOverflowError e) {
			throw new ELException("Could not Resolve Variable [Overflow]: " + variable, e);
		}
	}

	/**
	 * Set the ValueExpression on the inner Map instance.
	 * 
	 * @see javax.el.VariableMapper#setVariable(java.lang.String,
	 *      javax.el.ValueExpression)
	 */
	public ValueExpression setVariable(String variable,
			ValueExpression expression) {
		if (this.vars == null) {
			this.vars = new HashMap();
		}
		return (ValueExpression) this.vars.put(variable, expression);
	}
}
