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

<%-- $Id: verbatim_test.jsp,v 1.15 2007/04/27 22:01:23 ofung Exp $ --%>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>

  <html>

    <head>
      <title>Test of the Verbatim Tag</title>
    </head>

    <body>

      <h1>Test of the Verbatim Tag</h1>

      <p>
        <f:verbatim>
          [DEFAULT]
          This text <b>has angle brackets</b>.
          The angle brackets MUST NOT be escaped.
        </f:verbatim>
      </p>

      <p>
        <f:verbatim escape="false">
          [FALSE]
          This text <b>has angle brackets</b>.
          The angle brackets MUST NOT be escaped.
        </f:verbatim>
      </p>

      <p>
        <f:verbatim escape="true">
          [TRUE]
          This text <b>has angle brackets</b>.
          The angle brackets MUST be escaped.
        </f:verbatim>
      </p>

     <p><f:verbatim rendered="false">This text must not be rendered.
        </f:verbatim>  This text is rendered.
     </p>

    </body>

  </html>

</f:view>

