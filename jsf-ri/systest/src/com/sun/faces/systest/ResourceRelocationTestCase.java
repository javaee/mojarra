/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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


package com.sun.faces.systest;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHead;
import com.gargoylesoftware.htmlunit.html.HtmlTitle;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Validate resource re-location of scripts and stylesheets
 */
public class ResourceRelocationTestCase extends AbstractTestCase {


    public ResourceRelocationTestCase() {
        this("ResourceRelocationTestCase");
    }


    public ResourceRelocationTestCase(String name) {
        super(name);
    }

    public static Test suite() {
        return (new TestSuite(ResourceRelocationTestCase.class));
    }


    // ------------------------------------------------------------ Test Methods


    public void testResourceRelocation() throws Exception {

        // for this request, the script and stylesheet will be in the head
        HtmlPage page = getPage("/faces/resourcerelocation.xhtml?location=head");
        List<HtmlHead> headList = new ArrayList<HtmlHead>(1);
        getAllElementsOfGivenClass(page, headList, HtmlHead.class);
        assertTrue(headList.size() == 1);
        HtmlHead head = headList.get(0);
        List<HtmlElement> headChildren = getChildren(head);
        assertTrue(headChildren.size() == 3);
        assertTrue(headChildren.get(0) instanceof HtmlTitle);
        assertTrue(headChildren.get(1) instanceof HtmlScript);
        assertTrue(headChildren.get(2) instanceof HtmlLink);
        List<HtmlBody> bodyList = new ArrayList<HtmlBody>(1);
        getAllElementsOfGivenClass(page, bodyList, HtmlBody.class);
        assertTrue(bodyList.size() == 1);
        HtmlBody body = bodyList.get(0);
        List<HtmlElement> bodyChildren = getChildren(body);
        assertTrue(bodyChildren.size() == 1);
        assertTrue(bodyChildren.get(0) instanceof HtmlForm);
        List<HtmlForm> formList = new ArrayList<HtmlForm>(1);
        getAllElementsOfGivenClass(page, formList, HtmlForm.class);
        assertTrue(formList.size() == 1);
        HtmlForm form = formList.get(0);
        List<HtmlElement> formChildren = getChildren(form);
        assertTrue(formChildren.size() == 2);
        assertTrue(formChildren.get(0) instanceof HtmlInput);
        assertTrue(formChildren.get(1) instanceof HtmlInput);

        // for this request, the stylesheet will be in the head, and the script
        // will be the last child of body
        page = getPage("/faces/resourcerelocation.xhtml?location=body");
        headList.clear();
        getAllElementsOfGivenClass(page, headList, HtmlHead.class);
        assertTrue(headList.size() == 1);
        head = headList.get(0);
        headChildren = getChildren(head);
        assertTrue(headChildren.size() == 2);
        assertTrue(headChildren.get(0) instanceof HtmlTitle);
        assertTrue(headChildren.get(1) instanceof HtmlLink);
        bodyList.clear();
        getAllElementsOfGivenClass(page, bodyList, HtmlBody.class);
        assertTrue(bodyList.size() == 1);
        body = bodyList.get(0);
        bodyChildren = getChildren(body);
        assertTrue(bodyChildren.size() == 2);
        assertTrue(bodyChildren.get(0) instanceof HtmlForm);
        assertTrue(bodyChildren.get(1) instanceof HtmlScript);
        formList.clear();
        getAllElementsOfGivenClass(page, formList, HtmlForm.class);
        assertTrue(formList.size() == 1);
        form = formList.get(0);
        formChildren = getChildren(form);
        assertTrue(formChildren.size() == 2);
        assertTrue(formChildren.get(0) instanceof HtmlInput);
        assertTrue(formChildren.get(1) instanceof HtmlInput);

        // for this request, the stylesheet will be in the head, and the
        // script will be the last child of the form
        page = getPage("/faces/resourcerelocation.xhtml?location=form");
        headList.clear();
        getAllElementsOfGivenClass(page, headList, HtmlHead.class);
        assertTrue(headList.size() == 1);
        head = headList.get(0);
        headChildren = getChildren(head);
        assertTrue(headChildren.size() == 2);
        assertTrue(headChildren.get(0) instanceof HtmlTitle);
        assertTrue(headChildren.get(1) instanceof HtmlLink);
        bodyList.clear();
        getAllElementsOfGivenClass(page, bodyList, HtmlBody.class);
        assertTrue(bodyList.size() == 1);
        body = bodyList.get(0);
        bodyChildren = getChildren(body);
        assertTrue(bodyChildren.size() == 1);
        assertTrue(bodyChildren.get(0) instanceof HtmlForm);
        formList.clear();
        getAllElementsOfGivenClass(page, formList, HtmlForm.class);
        assertTrue(formList.size() == 1);
        form = formList.get(0);
        formChildren = getChildren(form);
        assertTrue(formChildren.size() == 3);
        assertTrue(formChildren.get(0) instanceof HtmlInput);
        assertTrue(formChildren.get(1) instanceof HtmlInput);
        assertTrue(formChildren.get(2) instanceof HtmlScript);
        
    }


    // --------------------------------------------------------- Private Methods


    private List<HtmlElement> getChildren(HtmlElement parent) {
        List<HtmlElement> list = new ArrayList<HtmlElement>();
        for (Iterator i = parent.getChildIterator(); i.hasNext();) {
            Object o = i.next();
            if (o instanceof HtmlElement) {
                list.add((HtmlElement) o);
            }
        }
        return list;
    }
}
