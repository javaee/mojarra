<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

    Contributor(s):
    If you wish your version of this file to be governed by only the CDDL or
    only the GPL Version 2, indicate your decision by adding "[Contributor]
    elects to include this software in this distribution under the [CDDL or GPL
    Version 2] license."  If you don't indicate a single choice of license, a
    recipient has the option to distribute your version of this file under
    either the CDDL, the GPL Version 2 or to extend the choice of license to
    its licensees as provided above.  However, if you add GPL Version 2 code
    and therefore, elected the GPL Version 2 license, then the option applies
    only if the new code is made subject to such option by the copyright
    holder.

--%>

<%@ page contentType="text/html"
%><%@ page import="javax.faces.component.UIInput" 
%><%@ page import="javax.faces.context.FacesContext" 
%><%@ page import="javax.faces.convert.ConverterException"
%><%@ page import="javax.faces.convert.EnumConverter"
%><%@ page import="com.sun.faces.systest.model.EnumBean"
%><%

  // Test - no targetClass Exception
  EnumConverter enumConverter = new EnumConverter();
  UIInput input = new UIInput();
  input.setId("myInput");
  String msg = null;
  try {
      Object obj = enumConverter.getAsObject(FacesContext.getCurrentInstance(), input, "foo");
  } catch (ConverterException ce) {
      msg = ce.getMessage();
  }
  if (msg.equals("myInput: 'foo' must be convertible to an enum from the enum, but no enum class provided.")) {
      out.println("/enum-converter-1.jsp PASSED");
  } else {
      out.println("/enum-converter-1.jsp FAILED");
  }
      
  try {
      String str = enumConverter.getAsString(FacesContext.getCurrentInstance(), input, "bar");
  } catch (ConverterException ce) {
      msg = ce.getMessage();
  }
  if (msg.equals("myInput: 'bar' must be convertible to an enum from the enum, but no enum class provided.")) {
      out.println("/enum-converter-1.jsp PASSED");
  } else {
      out.println("/enum-converter-1.jsp FAILED");
  }

  // Test Valid Enum member
  try {
      enumConverter = new EnumConverter(EnumBean.Simple.class);
      String str = enumConverter.getAsString(FacesContext.getCurrentInstance(), input, EnumBean.Simple.Value2);  
      out.println("/enum-converter-1.jsp PASSED");
  } catch (ConverterException ce) {
      out.println("/enum-converter-1.jsp FAILED");
  }

  // Test Invalid Enum member
  try {
      enumConverter = new EnumConverter(EnumBean.Simple.class);
      String str = enumConverter.getAsString(FacesContext.getCurrentInstance(), input, "FOO");  
      out.println("/enum-converter-1.jsp FAILED");
  } catch (ConverterException ce) {
      out.println("/enum-converter-1.jsp PASSED");
  }
%>
