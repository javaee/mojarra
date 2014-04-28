package com.sun.faces.test.agnostic.factory;

import javax.faces.view.facelets.FaceletCache;
import javax.faces.view.facelets.FaceletCacheFactory;

public class MDSFaceletCacheFactory
  extends FaceletCacheFactory
{
    private boolean oneArgCtorCalled = false;
    private FaceletCache _cache;
    private FaceletCacheFactory _wrapped;

    public boolean isOneArgCtorCalled() {
        return oneArgCtorCalled;
    }

    @Override
    public FaceletCacheFactory getWrapped() {
        return _wrapped;
    }
    
  public MDSFaceletCacheFactory()
  {
    super();
    System.out.println("ADFTEST: DEFAULT CONSTRUCTOR CALLED ***************************");
    oneArgCtorCalled = false;
  }
  
  public MDSFaceletCacheFactory(FaceletCacheFactory wrapped)
  {
    System.out.println("ADFTEST: ONE ARG CONSTRUCTOR CALLED ***************************");
    oneArgCtorCalled = true;
    _wrapped = wrapped;
  }

  @Override
  public FaceletCache getFaceletCache()
  {
    FaceletCacheFactory wrapped = getWrapped();

    if (wrapped != null)
    {
      System.out.println("ADFTEST: WRAPPED FACELETCACHEFACTORY IS " + wrapped);
    }
    
    if (_cache == null)
    {
      _cache = wrapped.getFaceletCache();
    }
    
    return _cache;
  }
  
}
