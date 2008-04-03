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
<f:view> 
    <h3>TabView - Border Style Demo</h3>
    This page shows a normal, three tab tabView with "dynamic" <code>maxHeight</code>.
    <br /><br />
    <risb:tabView tabStyle="border">
        <risb:tab label="Label 1" active="true">
            <h:outputText value="Some text!"/>
        </risb:tab>
        <risb:tab>
            <f:facet name="label"><h:outputText value="Label 2"/></f:facet>
            <h:outputText>
                Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Morbi 
                gravida. Etiam nibh metus, tincidunt eget, egestas eu, dictum at, 
                purus. Integer vehicula eros sit amet magna. Nulla dignissim. 
                Donec lobortis libero ac lacus. Nulla fermentum enim ac turpis 
                suscipit aliquet. Maecenas posuere erat nec justo. Cum sociis 
                natoque penatibus et magnis dis parturient montes, nascetur 
                ridiculus mus. In suscipit. Nullam ornare velit non felis. 
                Suspendisse potenti. Mauris orci dui, facilisis fringilla, 
                facilisis eget, molestie ut, enim. Nullam quam. Quisque aliquet. 
                Mauris arcu dui, vestibulum eget, commodo et, suscipit in, nunc. 
                Ut blandit felis ullamcorper magna. Sed nunc neque, tincidunt 
                ultricies, vehicula ut, aliquam quis, ipsum. Vivamus lorem urna, 
                volutpat quis, interdum nec, faucibus in, est. Etiam lobortis mi 
                ac libero.
            </h:outputText>
        </risb:tab>
        <risb:tab>
            <f:facet name="label"><h:outputText value="Label 3"/></f:facet>
            <risb:download mimeType="image/png" fileName="sample.png" data="#{testBean.image}" urlVar="foo">
                <h:graphicImage url="#{foo}" width="250px" />
            </risb:download>
        </risb:tab>
    </risb:tabView>
</f:view>
<%@ include file="footer.inc" %>