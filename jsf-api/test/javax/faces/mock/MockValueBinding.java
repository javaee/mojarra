/*
 * $Id: MockValueBinding.java,v 1.2 2003/11/07 01:24:00 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;


/**
 * <p>Mock implementation of {@link ValueBinding} that supports a limited
 * subset of expression evaluation functionality:</p>
 * <ul>
 * <li>Looking up the first name via the configured {@link VariableResolver}
 *     (which is also limited in capability).</li>
 * <li>Resolving the "." operator via the configured
 *     {@link PropertyResolver}.</li>
 * <li>Supports only <code>getValue()</code> and <code>setValue()</code>.</li>
 * </ul>
 */

public class MockValueBinding extends ValueBinding {


    // ------------------------------------------------------------ Constructors


    public MockValueBinding(Application application, String ref) {

        if ((application == null) || (ref == null)) {
            throw new NullPointerException();
        }
        this.application = application;
	if (ref.startsWith("#{") && ref.endsWith("}")) {
	    ref = ref.substring(2, ref.length() - 1);
	}
        this.ref = ref;

    }


    // ------------------------------------------------------ Instance Variables


    private Application application;
    private String ref;


    // ---------------------------------------------------- ValueBinding Methods


    public Object getValue(FacesContext context)
        throws PropertyNotFoundException {

        if (context == null) {
            throw new NullPointerException();
        }
        // System.out.println("getValue(" + ref + ")");
        List names = parse(ref);
        // for (int i = 0; i < names.size(); i++) {
        //     System.out.println("  names[" + i + "]=" + names.get(i));
        // }

        // Resolve the variable name
        VariableResolver vr = application.getVariableResolver();
        String name = (String) names.get(0);
        Object base = vr.resolveVariable(context, name);
        // System.out.println("  base=" + base);
        if (names.size() < 2) {
            return (base);
        }

        // Resolve the property names
        PropertyResolver pr = application.getPropertyResolver();
        for (int i = 1; i < names.size(); i++) {
            // System.out.println("  property=" + names.get(i));
            base = pr.getValue(base, (String) names.get(i));
        }

        // Return the resolved value
        // System.out.println("  result=" + base);
        return (base);

    }


    public void setValue(FacesContext context, Object value)
        throws PropertyNotFoundException {

        if (context == null) {
            throw new NullPointerException();
        }
        // System.out.println("setValue(" + ref + "," + value + ")");
        List names = parse(ref);
        // for (int i = 0; i < names.size(); i++) {
        //     System.out.println("  names[" + i + "]=" + names.get(i));
        // }

        // Resolve the variable name
        VariableResolver vr = application.getVariableResolver();
        String name = (String) names.get(0);
        Object base = vr.resolveVariable(context, name);
        // System.out.println("  base=" + base);
        if (names.size() < 2) {
            if ("applicationScope".equals(name) ||
                "requestScope".equals(name) ||
                "sessionScope".equals(name)) {
                throw new ReferenceSyntaxException("Cannot set '" +
                                                   name + "'");
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
        PropertyResolver pr = application.getPropertyResolver();
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


    // --------------------------------------------------------- Private Methods


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
            throw new ReferenceSyntaxException("No expression in '" +
                                               ref + "'");
        }
        for (int i = 0; i < names.size(); i++) {
            String name = (String) names.get(i);
            if (name.length() < 1) {
                throw new ReferenceSyntaxException("Invalid expression '" +
                                                   ref + "'");
            }
        }
        return (names);

    }


}
