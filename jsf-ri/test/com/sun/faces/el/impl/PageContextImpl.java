/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */ 

package com.sun.faces.el.impl;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.PageContext;
import java.util.Enumeration;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.Servlet;

/**
 *
 * <p>This is a "dummy" implementation of PageContext whose only
 * purpose is to serve the attribute getter/setter API's.
 * 
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: rlubke $
 **/

public class PageContextImpl
  extends PageContext
{
  //-------------------------------------
  // Properties
  //-------------------------------------

  //-------------------------------------
  // Member variables
  //-------------------------------------

  Map mPage = Collections.synchronizedMap (new HashMap ());
  Map mRequest = Collections.synchronizedMap (new HashMap ());
  Map mSession = Collections.synchronizedMap (new HashMap ());
  Map mApp = Collections.synchronizedMap (new HashMap ());

  //-------------------------------------
  /**
   *
   * Constructor
   **/
  public PageContextImpl ()
  {
  }

  //-------------------------------------
  // PageContext methods
  //-------------------------------------
  public void initialize (Servlet servlet,
			  ServletRequest request,
			  ServletResponse response,
			  String errorPageURL,
			  boolean needSession,
			  int bufferSize,
			  boolean autoFlush)
  {
  }

  //-------------------------------------
  public void release ()
  {
  }

  //-------------------------------------
  public void setAttribute (String name,
			    Object attribute)
  {
    mPage.put (name, attribute);
  }

  //-------------------------------------
  public void setAttribute (String name,
			    Object attribute,
			    int scope)
  {
    switch (scope) {
    case PAGE_SCOPE:
      mPage.put (name, attribute);
      break;
    case REQUEST_SCOPE:
      mRequest.put (name, attribute);
      break;
    case SESSION_SCOPE:
      mSession.put (name, attribute);
      break;
    case APPLICATION_SCOPE:
      mApp.put (name, attribute);
      break;
    default:
      throw new IllegalArgumentException  ("Bad scope " + scope);
    }
  }

  //-------------------------------------
  public Object getAttribute (String name)
  {
    return mPage.get (name);
  }

  //-------------------------------------
  public Object getAttribute (String name,
			      int scope)
  {
    switch (scope) {
    case PAGE_SCOPE:
      return mPage.get (name);
    case REQUEST_SCOPE:
      return mRequest.get (name);
    case SESSION_SCOPE:
      return mSession.get (name);
    case APPLICATION_SCOPE:
      return mApp.get (name);
    default:
      throw new IllegalArgumentException  ("Bad scope " + scope);
    }
  }

  //-------------------------------------
  public Object findAttribute (String name)
  {
    if (mPage.containsKey (name)) {
      return mPage.get (name);
    }
    else if (mRequest.containsKey (name)) {
      return mRequest.get (name);
    }
    else if (mSession.containsKey (name)) {
      return mSession.get (name);
    }
    else if (mApp.containsKey (name)) {
      return mApp.get (name);
    }
    else {
      return null;
    }
  }

  //-------------------------------------
  public void removeAttribute (String name)
  {
    if (mPage.containsKey (name)) {
      mPage.remove (name);
    }
    else if (mRequest.containsKey (name)) {
      mRequest.remove (name);
    }
    else if (mSession.containsKey (name)) {
      mSession.remove (name);
    }
    else if (mApp.containsKey (name)) {
      mApp.remove (name);
    }
  }

  //-------------------------------------
  public void removeAttribute (String name,
			       int scope)
  {
    switch (scope) {
    case PAGE_SCOPE:
      mPage.remove (name);
      break;
    case REQUEST_SCOPE:
      mRequest.remove (name);
      break;
    case SESSION_SCOPE:
      mSession.remove (name);
      break;
    case APPLICATION_SCOPE:
      mApp.remove (name);
      break;
    default:
      throw new IllegalArgumentException  ("Bad scope " + scope);
    }
  }

  //-------------------------------------
  public int getAttributesScope (String name)
  {
    if (mPage.containsKey (name)) {
      return PAGE_SCOPE;
    }
    else if (mRequest.containsKey (name)) {
      return REQUEST_SCOPE;
    }
    else if (mSession.containsKey (name)) {
      return SESSION_SCOPE;
    }
    else if (mApp.containsKey (name)) {
      return APPLICATION_SCOPE;
    }
    else {
      return 0;
    }
  }

  //-------------------------------------
  public Enumeration getAttributeNamesInScope (int scope)
  {
    return null;
  }

  //-------------------------------------
  public JspWriter getOut ()
  {
    return null;
  }

  //-------------------------------------
  public HttpSession getSession ()
  {
    return null;
  }

  //-------------------------------------
  public Object getPage ()
  {
    return null;
  }

  //-------------------------------------
  public ServletRequest getRequest ()
  {
    return null;
  }

  //-------------------------------------
  public ServletResponse getResponse ()
  {
    return null;
  }

  //-------------------------------------
  public Exception getException ()
  {
    return null;
  }

  //-------------------------------------
  public ServletConfig getServletConfig ()
  {
    return null;
  }

  //-------------------------------------
  public ServletContext getServletContext ()
  {
    return null;
  }

  //-------------------------------------
  public void forward (String path)
  {
  }

  //-------------------------------------
  public void include (String path)
  {
  }

  //-------------------------------------
  public void handlePageException (Exception exc)
  {
  }

  //-------------------------------------
  public void handlePageException (Throwable exc)
  {
  }

  //-------------------------------------
  
}
