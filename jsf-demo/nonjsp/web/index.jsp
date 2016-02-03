<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
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

<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
   <title>Non-JSP Examples</title>
</head>
<body>
<h1>Non-JSP Examples</h1>

<p>

<h2>Overview:</h2>

<p>
This is a collection of examples written in XUL demonstrating 
the usage of different Java Server Faces components. These components
are rendered by the Java Server Faces framework and are not dependent
on a JSP container<br><br>
These examples are important because they are a technology demonstrator
showing that the Java Server Faces framework can be used and rendered 
independently of a JSP container.
</p>

<h2>How this Non-JSP Web Application works:</h2>
<p>
<ol>
  <li>
    This web application works by first creating a 
    <code>XulServletContextListener</code>.<br>
    This context listener creates a new <code>XulViewHandler</code> 
    when the context is initialized and makes it the default 
    View Handler.
  </li>
  <li>
    The <code>XulViewHandler</code> handles how the XUL view is rendered. It goes through the following steps in order to render the page:
    <ul>
      <li>
        <b>Parsing Xml Rule Set</b><br>
        A Rule Set is created which maps the XUL elements to Java Server
        Faces objects. The XUL page is then parsed using this Rule Set
        and a tree of Java Server Faces objects is created.
      </li>
      <li>
        <b>Setting the Tree</b><br>
        The object tree that is created is set within the FacesContext
        along with the appropriate ResponseWriter and ContentType.
      </li>
      <li>
        <b>Call component encode methods</b><br>
        The response is rendered by traversing the tree and calling the
        individual object's encode methods.
      </li>
      <li>
        <b>Saving the state</b><br>
        The tree is saved in the session to allow for navigation between
        different XUL pages. This allows ActionEvents such as the
        demo's <code>ButtonAction</code> to be written.<br>
        During the parsing phase an ActionRule was written that creates an
        ActionListener on an object. The ActionListener implementation
        class then handles the event. In the <code>ButtonAction</code>
        case the event handler redirects to another XUL page.
      </li>
    </ul>
  </li>
</ol>

<p>
<hr>

<table BORDER=0 CELLSPACING=5 WIDTH="85%" >
  <tr VALIGN=TOP>
    <td>Simple Label Example&nbsp;</td>

    <td VALIGN=TOP WIDTH="30%">
      <a href="faces/label.xul">
        <img SRC="images/execute.gif" HSPACE=4 BORDER=0 align=TOP>
      </a>
      <a href="faces/label.xul">Execute</a>
    </td>

    <td WIDTH="30%">
      <a href="ShowSource.jsp?filename=/label.xul">
        <img SRC="images/code.gif" HSPACE=4 BORDER=0 height=24 width=24 align=TOP>
      </a>
      <a href="ShowSource.jsp?filename=/label.xul">Source</a>
    </td>
  </tr>

  <tr VALIGN=TOP>
    <td>Text Field Example&nbsp;</td>

    <td VALIGN=TOP WIDTH="30%">
      <a href="faces/textField.xul">
        <img SRC="images/execute.gif" HSPACE=4 BORDER=0 align=TOP>
      </a>
      <a href="faces/textField.xul">Execute</a>
    </td>

    <td WIDTH="30%">
      <a href="ShowSource.jsp?filename=/textField.xul">
        <img SRC="images/code.gif" HSPACE=4 BORDER=0 height=24 width=24 align=TOP>
      </a>
      <a href="ShowSource.jsp?filename=/textField.xul">Source</a>
    </td>
  </tr>

  <tr VALIGN=TOP>
    <td>Hello Duke Example&nbsp;</td>

    <td VALIGN=TOP WIDTH="30%">
      <a href="faces/helloDuke.xul">
        <img SRC="images/execute.gif" HSPACE=4 BORDER=0 align=TOP>
      </a>
      <a href="faces/helloDuke.xul">Execute</a>
    </td>

    <td WIDTH="30%">
      <a href="ShowSource.jsp?filename=/helloDuke.xul">
        <img SRC="images/code.gif" HSPACE=4 BORDER=0 height=24 width=24 align=TOP>
      </a>
      <a href="ShowSource.jsp?filename=/helloDuke.xul">Source</a>
    </td>
  </tr>

</table>

</body>
</html>
