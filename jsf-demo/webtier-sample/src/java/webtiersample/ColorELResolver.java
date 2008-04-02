/*
* Copyright 2004 The Apache Software Foundation
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
package webtiersample;

import java.util.Arrays;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotWritableException;

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
            result = Arrays.asList(new String[] {"Color"}).iterator();
        } else if (base instanceof ColorImplicitObject) {
            // Return all color names
            result = ColorImplicitObject.colorNameIterator();          
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
