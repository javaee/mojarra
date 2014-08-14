/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.component.StateHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;

/**
 * <p>
 * Mock implementation of {@link ValueBinding} that supports a limited subset of
 * expression evaluation functionality:</p>
 * <ul>
 * <li>Looking up the first name via the configured {@link VariableResolver}
 * (which is also limited in capability).</li>
 * <li>Resolving the "." operator via the configured
 * {@link PropertyResolver}.</li>
 * <li>Supports only <code>getValue()</code> and <code>setValue()</code>.</li>
 * </ul>
 */
public class MockValueBinding extends ValueBinding implements StateHolder {

    // ------------------------------------------------------------ Constructors
    public MockValueBinding() {

        this(null, null);

    }

    public MockValueBinding(Application application, String ref) {

        this.application = application;
        if (ref != null) {
            if (ref.startsWith("#{") && ref.endsWith("}")) {
                ref = ref.substring(2, ref.length() - 1);
            }
        }
        this.ref = ref;

    }

    // ------------------------------------------------------ Instance Variables
    private transient Application application; // Restored as necessary
    private String ref;

    // ---------------------------------------------------- ValueBinding Methods
    public Object getValue(FacesContext context)
            throws EvaluationException, PropertyNotFoundException {

        if (context == null) {
            throw new NullPointerException();
        }
        // System.out.println("getValue(" + ref + ")");
        List names = parse(ref);
        // for (int i = 0; i < names.size(); i++) {
        //     System.out.println("  names[" + i + "]=" + names.get(i));
        // }

        // Resolve the variable name
        VariableResolver vr = application().getVariableResolver();
        String name = (String) names.get(0);
        Object base = vr.resolveVariable(context, name);
        // System.out.println("  base=" + base);
        if (names.size() < 2) {
            return (base);
        }

        // Resolve the property names
        PropertyResolver pr = application().getPropertyResolver();
        for (int i = 1; i < names.size(); i++) {
            if (null != base) {

                // System.out.println("  property=" + names.get(i));
                base = pr.getValue(base, (String) names.get(i));
            }
        }

        // Return the resolved value
        // System.out.println("  result=" + base);
        return (base);

    }

    public void setValue(FacesContext context, Object value)
            throws EvaluationException, PropertyNotFoundException {

        if (context == null) {
            throw new NullPointerException();
        }
        // System.out.println("setValue(" + ref + "," + value + ")");
        List names = parse(ref);
        // for (int i = 0; i < names.size(); i++) {
        //     System.out.println("  names[" + i + "]=" + names.get(i));
        // }

        // Resolve the variable name
        VariableResolver vr = application().getVariableResolver();
        String name = (String) names.get(0);
        Object base = vr.resolveVariable(context, name);
        // System.out.println("  base=" + base);
        if (names.size() < 2) {
            if ("applicationScope".equals(name)
                    || "requestScope".equals(name)
                    || "sessionScope".equals(name)) {
                throw new ReferenceSyntaxException("Cannot set '"
                        + name + "'");
            }
            Map map = econtext().getRequestMap();
            if (map.containsKey(name)) {
                map.put(name, value);
                return;
            }
            map = econtext().getSessionMap();
            if ((map != null) && (map.containsKey(name))) {
                map.put(name, value);
                return;
            }
            map = econtext().getApplicationMap();
            if (map.containsKey(name)) {
                map.put(name, value);
                return;
            }
            econtext().getRequestMap().put(name, value);
            return;
        }

        // Resolve the property names
        PropertyResolver pr = application().getPropertyResolver();
        for (int i = 1; i < (names.size() - 1); i++) {
            // System.out.println("  property=" + names.get(i));
            base = pr.getValue(base, (String) names.get(i));
        }

        // Update the last property
        pr.setValue(base, (String) names.get(names.size() - 1), value);

    }

    public boolean isReadOnly(FacesContext context)
            throws PropertyNotFoundException {

        throw new UnsupportedOperationException();

    }

    public Class getType(FacesContext context)
            throws PropertyNotFoundException {

        throw new UnsupportedOperationException();

    }

    public String getExpressionString() {
        return "#{" + ref + "}";
    }

    // ----------------------------------------------------- StateHolder Methods
    public Object saveState(FacesContext context) {
        Object values[] = new Object[1];
        values[0] = ref;
        return (values);
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        ref = (String) values[0];
    }

    private boolean transientFlag = false;

    public boolean isTransient() {
        return (this.transientFlag);
    }

    public void setTransient(boolean transientFlag) {
        this.transientFlag = transientFlag;
    }

    // ---------------------------------------------------------- Public Methods
    public String ref() {
        return (this.ref);
    }

    // --------------------------------------------------------- Private Methods
    private Application application() {

        if (application == null) {
            application = FacesContext.getCurrentInstance().getApplication();
        }
        return (application);

    }

    private ExternalContext econtext() {

        return (FacesContext.getCurrentInstance().getExternalContext());

    }

    private List parse(String ref) {

        String expr = ref;
        List names = new ArrayList();
        while (expr.length() > 0) {
            int period = expr.indexOf(".");
            if (period >= 0) {
                names.add(expr.substring(0, period));
                expr = expr.substring(period + 1);
            } else {
                names.add(expr);
                expr = "";
            }
        }
        if (names.size() < 1) {
            throw new ReferenceSyntaxException("No expression in '"
                    + ref + "'");
        }
        for (int i = 0; i < names.size(); i++) {
            String name = (String) names.get(i);
            if (name.length() < 1) {
                throw new ReferenceSyntaxException("Invalid expression '"
                        + ref + "'");
            }
        }
        return (names);
    }
}
