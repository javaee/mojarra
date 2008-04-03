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

<%@ include file="header.inc" %>
        <ul>
            <li><a href="calendar.jsf">Calendar Demo</a></li>
            <li><a href="download.jsf">Download Demo</a></li>
            <li>HtmlEditor Demo
				<ul>
					<li><a href="htmlEditor.jsf?mode=normal">Normal Mode</a></li>
					<li><a href="htmlEditor.jsf?mode=simplified">Simplified Mode</a></li>
					<li><a href="htmlEditor.jsf?mode=full">Full Mode</a></li>
				</ul>
			</li>
			<li>Menu Demo
				<ul>
		            <li><a href="menu.jsf">Menu Demo</a></li>
		            <li><a href="contextMenu.jsf">Context Menu Demo</a></li>
		            <li><a href="menuBar.jsf">Menu Bar Demo</a></li>
	            </ul>
            </li>
            <li><a href="multiFileUpload.jsf">Multi-file Upload Demo</a></li>
			<li>Pretty URL Demo
				<ul>
            		<li><a href="example1/This+is+pretty+URL+%231">Pretty URL PhaseListener Demo #1</a></li>
            		<li><a href="example/2/This+is+pretty+URL+%232">Pretty URL PhaseListener Demo #2</a></li>
            	</ul>
            </li>
            <li>TabView Demo
            	<ul>
            		<li><a href="tabView.jsf">Default Style</a></li>
            		<li><a href="tabView.jsf?tabStyle=border">Border Style</a></li>
            		<li><a href="tabView.jsf?tabStyle=module">Module Style</a></li>
            		<li><a href="tabView.jsf?tabStyle=round">Round Style</a></li>
            	</ul>
            </li>
            <li><a href="tree.jsf">Tree Demo</a></li>
        </ul>
<%@ include file="footer.inc" %>