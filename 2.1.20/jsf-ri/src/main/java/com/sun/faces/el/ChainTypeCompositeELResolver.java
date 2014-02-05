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

import com.sun.faces.util.RequestStateManager;
import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;

/**
 * Maintains an ordered composite list of child <code>ELResolver for JSF</code>.
 *
 */
public final class ChainTypeCompositeELResolver extends FacesCompositeELResolver
{
  @Override
  public void addRootELResolver(ELResolver elResolver)
  {
    _wrapped.addRootELResolver(elResolver);
  }

  public void addPropertyELResolver(ELResolver elResolver)
  {
    _wrapped.addPropertyELResolver(elResolver);
  }

  public void add(ELResolver elResolver)
  {
    _wrapped.add(elResolver);    
  }

  @Override
  public Object getValue(ELContext context, Object base, Object property) throws ELException
  {      
    FacesContext ctx = getFacesContext(context);
    if (ctx == null)
    {
      return null;
    }

    Map<String,Object> stateMap = 
	RequestStateManager.getStateMap(ctx);
    
    stateMap.put(RequestStateManager.EL_RESOLVER_CHAIN_TYPE_NAME,
		 _chainType);
    Object result = null;
    try {
        result = _wrapped.getValue(context, base, property);
    } finally {
        stateMap.remove(RequestStateManager.EL_RESOLVER_CHAIN_TYPE_NAME);
    }
    
    return result;
  }
  
  @Override
  public Class<?> getType(ELContext context, Object base, Object property) throws ELException
  {

    FacesContext ctx = getFacesContext(context);
    
    if (ctx == null)
    {
      return null;
    }
    
    Map<String,Object> stateMap =
	RequestStateManager.getStateMap(ctx);

    stateMap.put(RequestStateManager.EL_RESOLVER_CHAIN_TYPE_NAME,
		 _chainType);
    Class<?> result = null;
    try {
        result = _wrapped.getType(context, base, property);
    } finally {
        stateMap.remove(RequestStateManager.EL_RESOLVER_CHAIN_TYPE_NAME);
    }

    return result;
  }
  
  @Override
  public void setValue(ELContext context, Object base, Object property, Object val)
    throws ELException
  {
    FacesContext ctx = getFacesContext(context);
    if (ctx == null)
    {
      return;
    }
    
    Map<String,Object> stateMap =
	RequestStateManager.getStateMap(ctx);

    stateMap.put(RequestStateManager.EL_RESOLVER_CHAIN_TYPE_NAME,
		 _chainType);
    try {
        _wrapped.setValue(context, base, property, val);
    } finally {
        stateMap.remove(RequestStateManager.EL_RESOLVER_CHAIN_TYPE_NAME);
    }
  }
    
  @Override
  public boolean isReadOnly(ELContext context, Object base, Object property) throws ELException
  {
    FacesContext ctx = getFacesContext(context);
    if (ctx == null)
    {
      return false;
    }
    
    Map<String,Object> stateMap =
	RequestStateManager.getStateMap(ctx);

    stateMap.put(RequestStateManager.EL_RESOLVER_CHAIN_TYPE_NAME,
		 _chainType);
    boolean result = false;
    try {
        result = _wrapped.isReadOnly(context, base, property);
    } finally {
        stateMap.remove(RequestStateManager.EL_RESOLVER_CHAIN_TYPE_NAME);
    }
    
    return result;
  }
  
  @Override
  public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
  {
    FacesContext ctx = getFacesContext(context);
    Iterator<FeatureDescriptor> result = null;
    if (ctx != null) {
        Map<String,Object> stateMap =
            RequestStateManager.getStateMap(ctx);

        stateMap.put(RequestStateManager.EL_RESOLVER_CHAIN_TYPE_NAME,
                     _chainType);
        try {
            result = _wrapped.getFeatureDescriptors(context, base);
        } finally {
            stateMap.remove(RequestStateManager.EL_RESOLVER_CHAIN_TYPE_NAME);
        }
    }
    return result;
  }
   
  @Override
  public ELResolverChainType getChainType()
  {
    return _chainType;
  }
    
  private final FacesCompositeELResolver _wrapped;
  private final ELResolverChainType _chainType;

  /**
   * <p>Guarantee that this instance knows of what chain it is a
   * member.</p>
   * @param chainType the ELResolverChainType
   */
  public ChainTypeCompositeELResolver(ELResolverChainType chainType)
  {
    _wrapped = new DemuxCompositeELResolver(chainType);
    _chainType = chainType;
  }

  public ChainTypeCompositeELResolver(FacesCompositeELResolver delegate)
  {
    _wrapped = delegate;
    _chainType = delegate.getChainType();
  }

  /**
   * @param elContext context for the current expression evaluation
   * @return the <code>FacesContext</code> associated with this expression
   *  evaluation
   */
  private FacesContext getFacesContext(ELContext elContext) {

      return (FacesContext) elContext.getContext(FacesContext.class);

  }
}

