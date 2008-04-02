/*
 * $Id: MockPropertyResolver.java,v 1.1 2003/10/21 23:58:22 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import org.apache.commons.beanutils.PropertyUtils;


/**
 * <p>Mock implementation of {@link PropertyResolver} that supports a limited
 * subset of expression evaluation functionality:</p>
 * <ul>
 * <li>Supports <code>getValue()</code> and <code>setValue()</code> methods
 *     that take a String second argument.</li>
 * <li>Supports property getting and setting as provided by
 *     <code>PropertyUtils.getSimpleProperty()</code> and
 *     <code>PropertyUtils.setSimpleProperty()</code>.</li>
 * </ul>
 */

public class MockPropertyResolver extends PropertyResolver {


    // ------------------------------------------------------------ Constructors


    // ------------------------------------------------ PropertyResolver Methods


    public Object getValue(Object base, String name)
        throws PropertyNotFoundException {

        if (base == null) {
            throw new NullPointerException();
        }
        try {
            return (PropertyUtils.getSimpleProperty(base, name));
        } catch (Exception e) {
            throw new PropertyNotFoundException(e);
        }

    }


    public Object getValue(Object base, int index)
        throws PropertyNotFoundException {

        throw new UnsupportedOperationException();

    }


    public void setValue(Object base, String name, Object value)
        throws PropertyNotFoundException {

        if (base == null) {
            throw new NullPointerException();
        }
        try {
            PropertyUtils.setSimpleProperty(base, name, value);
        } catch (Exception e) {
            throw new PropertyNotFoundException(e);
        }

    }


    public void setValue(Object base, int index, Object value)
        throws PropertyNotFoundException {

        throw new UnsupportedOperationException();

    }


    public boolean isReadOnly(Object base, String name)
        throws PropertyNotFoundException {

        throw new UnsupportedOperationException();

    }


    public boolean isReadOnly(Object base, int index)
        throws PropertyNotFoundException {

        throw new UnsupportedOperationException();

    }


    public Class getType(Object base, String name)
        throws PropertyNotFoundException {

        throw new UnsupportedOperationException();

    }


    public Class getType(Object base, int index)
        throws PropertyNotFoundException {

        throw new UnsupportedOperationException();

    }




}
