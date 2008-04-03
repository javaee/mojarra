/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * Portions Copyright Apache Software Foundation.
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


package webtiersample;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotWritableException;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Introduces a new ${Color} implicit object and resolves properties
 * on that object.
 * 
 * <p>Features include:
 * <ul>
 *   <li>Look up common colors by name, e.g. ${Color.MintCream}</li>
 *   <li>Look up colors by hex, e.g. ${Color["#f0f0f0"]}</li>   
 *   <li>Retrieve brigter and darker colors, e.g. 
 *       ${Color.AliceBlue.darker.hex}</li>
 * </ul>
 * </p>
 * 
 * @author Mark Roth
 */
public class ColorELResolver 
    extends ELResolver
{
    public Object getValue(ELContext context, Object base, Object property)
        throws ELException
    {
        if(context == null) throw new NullPointerException();
        
        Object result = null;

        if (base == null) {
            // Resolving first variable (e.g. ${Color}).  
            // We only handle "Color"
            String propertyName = (String) property;
            if ("Color".equals(propertyName)) {
                result = new ColorImplicitObject();
                context.setPropertyResolved(true);
            }
        } else if (base instanceof ColorImplicitObject) {
            // Resolving a property on ${Color}

            String colorName = property.toString();

            if (colorName.startsWith("#")) {
                // Handle ${Color['#f0f0f0']}
                result = ColorImplicitObject.fromHex(colorName);
                context.setPropertyResolved(true);
            } else {
                // Handle ${Color.MintCream}
                result = ColorImplicitObject.fromName(colorName);
                context.setPropertyResolved(true);
            }

        } 
        
        return result;
    }

    public Class getType(ELContext context, Object base, Object property)
        throws ELException
    {
        Class result = null;
        
        if(context == null) throw new NullPointerException();
        
        if(base == null) {
            // We don't handle setting top-level implicit objects.
        }
        else if(base instanceof ColorImplicitObject
                  || base instanceof ColorImplicitObject.ColorRGB) {
            // None of the properties of the ${Color} implicit object 
            // or ColorRGB are ever writable 
            context.setPropertyResolved(true);
        }
       
        // The rest is handled by BeanELResolver, etc.
        
        return result;
    }

    public void setValue(ELContext context, Object base, Object property,
                         Object value)
        throws ELException {
        if (context == null) {
            throw new NullPointerException();
        }

        if (base == null && "Color".equals(property)) {
            throw new PropertyNotWritableException();
        } else if (base instanceof ColorImplicitObject
                     || base instanceof ColorImplicitObject.ColorRGB) {
            // None of the properties of the ${Color} implicit object 
            // or ColorRGB are ever writable.
            throw new PropertyNotWritableException();
        } 
    }

    public boolean isReadOnly(ELContext context, Object base, 
        Object property)
        throws ELException
    {
        boolean result = true;

        if (context == null) {
            throw new NullPointerException();
        }

        if (base == null) {
            // We don't handle setting top-level implicit objects.
        } else if (base instanceof ColorImplicitObject
                     || base instanceof ColorImplicitObject.ColorRGB) {
            // None of the properties of the ${Color} implicit object 
            // or ColorRGB are ever writable.
            result = true;
            context.setPropertyResolved(true);
        } 

        return result;
    }

    public Iterator getFeatureDescriptors(ELContext context, Object base) {
        Iterator result = null;

        if (context == null) {
            throw new NullPointerException();
        }

        if (base == null) {
            FeatureDescriptor desc = new FeatureDescriptor();
            desc.setName("Color");
            desc.setDisplayName("Color");            
            desc.setValue(ELResolver.TYPE, ColorImplicitObject.class);
            desc.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME, false);
            result = Arrays.asList(new FeatureDescriptor[] { desc }).iterator();
        } else if (base instanceof ColorImplicitObject) {
            // Return all color names
            List<FeatureDescriptor> descList = 
                new ArrayList<FeatureDescriptor>();
            for (Iterator<String> i = ColorImplicitObject.colorNameIterator();
                 i.hasNext(); ) {
                String colorName = i.next();
                FeatureDescriptor desc = new FeatureDescriptor();
                desc.setName(colorName);
                desc.setDisplayName(colorName);
                desc.setValue(ELResolver.TYPE, String.class);
                desc.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME, false);
                descList.add(desc);
            }
            result = descList.iterator();          
        } 

        // BeanELResolver will add to this iterator with the bean properties.

        return result;
    }

    public Class getCommonPropertyType(ELContext context,
                                                Object base)
    {
        Class result = null;

        if (base == null) {
            // Resolving first variable (e.g. ${Color}).  
            // We only handle "Color"
            result = String.class;
        } else if (base instanceof ColorImplicitObject) {
            // We handle either integers or strings, so return Object
            result = Object.class;
        } 

        return result;
    }
}
