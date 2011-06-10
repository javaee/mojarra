/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.el;

import java.beans.FeatureDescriptor;

import java.util.Iterator;

import java.util.NoSuchElementException;

import javax.el.ELResolver;
import javax.el.ELContext;
import javax.el.ELException;

/**
 * Maintains an ordered composite list of child <code>ELResolver for JSF</code>.
 *
 */
public class DemuxCompositeELResolver extends FacesCompositeELResolver
{
  private final ELResolverChainType _chainType;
  
  private ELResolver[] _rootELResolvers = new ELResolver[2];
  private ELResolver[] _propertyELResolvers = new ELResolver[2];
  private ELResolver[] _allELResolvers = new ELResolver[2];

  private int _rootELResolverCount = 0;
  private int _propertyELResolverCount = 0;
  private int _allELResolverCount = 0;

  public DemuxCompositeELResolver(ELResolverChainType chainType)
  {
    if (chainType == null)
      throw new NullPointerException();
    
    _chainType = chainType;
  }
  
  public ELResolverChainType getChainType()
  {
    return _chainType;
  }
  
  private void _addAllELResolver(ELResolver elResolver)
  {
    if (elResolver == null)
      throw new NullPointerException();
    
    // grow array, if necessary
    if (_allELResolverCount == _allELResolvers.length)
    {
      ELResolver[] biggerResolvers = new ELResolver[_allELResolverCount * 2];
      System.arraycopy(_allELResolvers, 0, biggerResolvers, 0, _allELResolverCount);
      _allELResolvers = biggerResolvers;
    }
    
    // assign new resolver to end
    _allELResolvers[_allELResolverCount] = elResolver;
    _allELResolverCount++;    
  }

  private void _addRootELResolver(ELResolver elResolver)
  {
    if (elResolver == null)
      throw new NullPointerException();
    
    // grow array, if necessary
    if (_rootELResolverCount == _rootELResolvers.length)
    {
      ELResolver[] biggerResolvers = new ELResolver[_rootELResolverCount * 2];
      System.arraycopy(_rootELResolvers, 0, biggerResolvers, 0, _rootELResolverCount);
      _rootELResolvers = biggerResolvers;
    }
    
    // assign new resolver to end
    _rootELResolvers[_rootELResolverCount] = elResolver;
    _rootELResolverCount++;    
  }
  
  public void _addPropertyELResolver(ELResolver elResolver)
  {
    if (elResolver == null)
      throw new NullPointerException();
    
    // grow array, if necessary
    if (_propertyELResolverCount == _propertyELResolvers.length)
    {
      ELResolver[] biggerResolvers = new ELResolver[_propertyELResolverCount * 2];
      System.arraycopy(_propertyELResolvers, 0, biggerResolvers, 0, _propertyELResolverCount);
      _propertyELResolvers = biggerResolvers;
    }
    
    // assign new resolver to end
    _propertyELResolvers[_propertyELResolverCount] = elResolver;
    _propertyELResolverCount++;
  }

  public void addRootELResolver(ELResolver elResolver)
  {
    // pass ELResolver to CompositeELResolver so that J2EE6 invoke() method works.  Once we can
    // have a compile dependency on J2EE6, we can override invoke() ourselves and remove this.
    super.add(elResolver);
    
    _addRootELResolver(elResolver);
    _addAllELResolver(elResolver);
  }

  public void addPropertyELResolver(ELResolver elResolver)
  {
    // pass ELResolver to CompositeELResolver so that J2EE6 invoke() method works.  Once we can
    // have a compile dependency on J2EE6, we can override invoke() ourselves and remove this.
    super.add(elResolver);
    
    _addPropertyELResolver(elResolver);
    _addAllELResolver(elResolver);
  }
  
  public void add(ELResolver elResolver)
  {
    // pass ELResolver to CompositeELResolver so that J2EE6 invoke() method works.  Once we can
    // have a compile dependency on J2EE6, we can override invoke() ourselves and remove this.
    super.add(elResolver);

    _addRootELResolver(elResolver);
    _addPropertyELResolver(elResolver);
    _addAllELResolver(elResolver);
  }

  private Object _getValue(
    int resolverCount,
    ELResolver[] resolvers,
    ELContext context,
    Object base,
    Object property) throws ELException
  {
    for (int i = 0; i < resolverCount; i++)
    {
      Object result = resolvers[i].getValue(context, base, property);
      
      if (context.isPropertyResolved())
        return result;
    }
    
    return null;
  }
  
  public Object getValue(ELContext context, Object base, Object property) throws ELException
  {
    context.setPropertyResolved(false);
    
    int resolverCount;
    ELResolver[] resolvers;
    
    if (base == null)
    {
      resolverCount = _rootELResolverCount;
      resolvers     = _rootELResolvers;
    }
    else
    {
      resolverCount = _propertyELResolverCount;
      resolvers     = _propertyELResolvers;
    }
    
    return _getValue(resolverCount, resolvers, context, base, property);      
  }

  private Class<?> _getType(
    int resolverCount,
    ELResolver[] resolvers,
    ELContext context,
    Object base,
    Object property) throws ELException
  {
    for (int i = 0; i < resolverCount; i++)
    {
      Class<?> type = resolvers[i].getType(context, base, property);
      
      if (context.isPropertyResolved())
        return type;
    }
    
    return null;
  }

  public Class<?> getType(ELContext context, Object base, Object property) throws ELException
  {
    context.setPropertyResolved(false);

    int resolverCount;
    ELResolver[] resolvers;
    
    if (base == null)
    {
      resolverCount = _rootELResolverCount;
      resolvers     = _rootELResolvers;
    }
    else
    {
      resolverCount = _propertyELResolverCount;
      resolvers     = _propertyELResolvers;
    }
    
    return _getType(resolverCount, resolvers, context, base, property);      
  }

  private void _setValue(
    int resolverCount,
    ELResolver[] resolvers,
    ELContext context,
    Object base,
    Object property,
    Object val) throws ELException
  {
    for (int i = 0; i < resolverCount; i++)
    {
      resolvers[i].setValue(context, base, property, val);
      
      if (context.isPropertyResolved())
        return;
    }
  }

  public void setValue(ELContext context, Object base, Object property, Object val)
    throws ELException
  {
    context.setPropertyResolved(false);

    int resolverCount;
    ELResolver[] resolvers;
    
    if (base == null)
    {
      resolverCount = _rootELResolverCount;
      resolvers     = _rootELResolvers;
    }
    else
    {
      resolverCount = _propertyELResolverCount;
      resolvers     = _propertyELResolvers;
    }

    _setValue(resolverCount, resolvers, context, base, property, val);          
  }
  
  private boolean _isReadOnly(
    int resolverCount,
    ELResolver[] resolvers,
    ELContext context,
    Object base,
    Object property) throws ELException
  {
    for (int i = 0; i < resolverCount; i++)
    {
      boolean isReadOnly = resolvers[i].isReadOnly(context, base, property);
      
      if (context.isPropertyResolved())
        return isReadOnly;
    }
    
    return false;        
  }

  @Override
  public boolean isReadOnly(ELContext context, Object base, Object property)
    throws ELException
  {
    context.setPropertyResolved(false);

    int resolverCount;
    ELResolver[] resolvers;
    
    if (base == null)
    {
      resolverCount = _rootELResolverCount;
      resolvers     = _rootELResolvers;
    }
    else
    {
      resolverCount = _propertyELResolverCount;
      resolvers     = _propertyELResolvers;
    }

    return _isReadOnly(resolverCount, resolvers, context, base, property);      
  }
  
  public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
  {
    return new DescriptorIterator(context, base, _allELResolvers, _allELResolverCount);
  }
  
  private final static class DescriptorIterator implements Iterator<FeatureDescriptor>
  {
    // snapshot the ELResolver array to avoid using a non-static inner class that needs to
    // make function calls
    public DescriptorIterator(
      ELContext context,
      Object base,
      ELResolver[] resolvers,
      int resolverCount)
    {
      _context       = context;
      _base          = base;
      _resolvers     = resolvers;
      _resolverCount = resolverCount;
    }
   
    public boolean hasNext()
    {
      do
      {
          // A null return does *not* mean hasNext() should return false.
        Iterator<FeatureDescriptor> currIterator = _getCurrIterator();

        if (null != currIterator) {
            if (currIterator.hasNext()) {
                return true;
            } else {
                _currIterator = null;
                _currResolverIndex++;
            }
        } else {
            if (_currResolverIndex < _resolverCount) {
                continue;
            } else {
                return false;
            }
        }
      
      } while (true);
    }
    
    private Iterator<FeatureDescriptor> _getCurrIterator()
    {
      Iterator<FeatureDescriptor> currIterator = _currIterator;
      
      if (currIterator == null)
      {
        if (_currResolverIndex < _resolverCount)
        {
          currIterator = _resolvers[_currResolverIndex].getFeatureDescriptors(_context, _base);
          _currResolverIndex++;
          _currIterator = currIterator;
        }
      }
      
      return currIterator;
    }

    public FeatureDescriptor next()
    {
      if (hasNext())
        return _getCurrIterator().next();
      else
        throw new NoSuchElementException();    
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }
    
    private final ELContext _context;
    private final Object    _base;
    private final ELResolver[] _resolvers;
    private final int _resolverCount;
    private int _currResolverIndex;
    private Iterator<FeatureDescriptor> _currIterator;
  }
    
  public Class<?> getCommonPropertyType(ELContext context, Object base)
  {
    return null;
  }
}

