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
 *   <li>Specify r, g, and b values numerically, e.g. 
 *       ${Color[240][255][255]}</li>
 *   <li>Retrieve red, green, or blue individually, e.g. ${myColor.red}</li>
 *   <li>Retrieve HTML-style hex value for a color, e.g. ${myColor.hex}</li>
 *   <li>Retrieve java.awt.Color object for a color, e.g. ${myColor.color}</li>
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
        
        if(base == null) {
            // Resolving first variable (e.g. ${Color}).  
            // We only handle "Color"
            String propertyName = (String)property;
            if(propertyName.equals("Color")) {
                result = new ColorImplicitObject();
                context.setPropertyResolved(true);
            }
        }
        else if(base instanceof ColorImplicitObject) {
            // Resolving a property on ${Color}
          
            if(property instanceof Long) {
                // Handle ${Color[100]}
                int red = ((Long)property).intValue();
                result = new ColorImplicitObject.ColorR(red);
                context.setPropertyResolved(true);
            }
            else {
                String colorName = property.toString();
                
                if(colorName.startsWith("#")) {
                    // Handle ${Color['#f0f0f0']}
                    result = ColorImplicitObject.fromHex(colorName);
                    context.setPropertyResolved(true);
                }
                else {
                    // Handle ${Color.MintCream}
                    result = ColorImplicitObject.fromName(colorName);
                    context.setPropertyResolved(true);
                }
            }
        }
        else if(base instanceof ColorImplicitObject.ColorRGB) {           
            // The rest is handled by the BeanPropertyResolver.
        }
        else if(base instanceof ColorImplicitObject.ColorRG) {
            ColorImplicitObject.ColorRG rg = (ColorImplicitObject.ColorRG)base;
            if(property instanceof Long) {
                // Handle ${Color[100][150][200]}
                int blue = ((Long)property).intValue();
                result = new ColorImplicitObject.ColorRGB(rg.getRed(), 
                    rg.getGreen(), blue);
                context.setPropertyResolved(true);
            }
        }
        else if(base instanceof ColorImplicitObject.ColorR) {
            ColorImplicitObject.ColorR r = (ColorImplicitObject.ColorR)base;
            if(property instanceof Long) {
                // Handle ${Color[100][150]}
                int green = ((Long)property).intValue();
                result = new ColorImplicitObject.ColorRG(r.getRed(), green);
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
        else if(base instanceof ColorImplicitObject) {
            // None of the properties of the ${Color} implicit object are 
            // ever writable.
            context.setPropertyResolved(true);
        }
        else if(
                (base instanceof ColorImplicitObject.ColorR) 
            ||  (base instanceof ColorImplicitObject.ColorRG))
        {
            // Don't allow setting of
            // ${Color[100][150]} or ${Color[100][150][200]}
            if(property instanceof Long) {
                context.setPropertyResolved(true);
            }
        }
        // The rest is handled by BeanELResolver, etc.
        
        return result;
    }

    public void setValue(ELContext context, Object base, Object property, 
        Object value)
        throws ELException
    {
        if(context == null) throw new NullPointerException();
        
        if(base == null) {
            // We don't handle setting top-level implicit objects.
        }
        else if(base instanceof ColorImplicitObject) {
            // None of the properties of the ${Color} implicit object are 
            // ever writable.
            throw new PropertyNotWritableException();
        }
        else if(
                (base instanceof ColorImplicitObject.ColorR) 
            ||  (base instanceof ColorImplicitObject.ColorRG))
        {
            // Don't allow setting of
            // ${Color[100][150]} or ${Color[100][150][200]}
            if(property instanceof Long) {
                throw new PropertyNotWritableException();
            }
        }
    }

    public boolean isReadOnly(ELContext context, Object base, 
        Object property)
        throws ELException
    {
        boolean result = false;
        
        if(context == null) throw new NullPointerException();
        
        if(base == null) {
            // We don't handle setting top-level implicit objects.
        }
        else if(base instanceof ColorImplicitObject) {
            // None of the properties of the ${Color} implicit object are 
            // ever writable.
            result = false;
            context.setPropertyResolved(true);
        }
        else if(
                (base instanceof ColorImplicitObject.ColorR) 
            ||  (base instanceof ColorImplicitObject.ColorRG))
        {
            // Don't allow setting of
            // ${Color[100][150]} or ${Color[100][150][200]}
            if(property instanceof Long) {
                result = false;
                context.setPropertyResolved(true);
            }
        }
        
        return result;
    }

    public Iterator getFeatureDescriptors(ELContext context, Object base) {
        Iterator result = null;

        if(context == null) throw new NullPointerException();
        
        if(base == null) {
            result = Arrays.asList(new String[] {"Color"}).iterator();
        }
        else if(base instanceof ColorImplicitObject) {
            // Return all color names
            result = ColorImplicitObject.colorNameIterator();
            
            // XXX - There's no way to say we also accept 0-255
        }
        else if(
                (base instanceof ColorImplicitObject.ColorR)
            ||  (base instanceof ColorImplicitObject.ColorRG)
            ||  (base instanceof ColorImplicitObject.ColorRGB))
        {
            // We accept integers 0-255, but don't enumerate them.
            // The tool will call getCommonPropertyType() instead.
            result = null;
        }
        
        // BeanELResolver will add to this iterator with the bean properties.
        
        return result;
    }

    public Class getCommonPropertyType(ELContext context,
                                                Object base)
    {
        Class result = null;
        
        if(base == null) {
            // Resolving first variable (e.g. ${Color}).  
            // We only handle "Color"
            result = String.class;
        }
        else if(base instanceof ColorImplicitObject) {
            // We handle either integers or strings, so return Object
            result = Object.class;
        }
        else if(base instanceof ColorImplicitObject.ColorR) {
            // We handle only integers in this case.
            result = Long.class;
        }
        else if(base instanceof ColorImplicitObject.ColorRG) {
            // We handle only integers in this case.
            result = Long.class;
        }
        else if(base instanceof ColorImplicitObject.ColorRGB) {
            // We don't do anything with these - the BeanELResolver 
            // takes it from here.
        }
        
        return result;
    }
}
