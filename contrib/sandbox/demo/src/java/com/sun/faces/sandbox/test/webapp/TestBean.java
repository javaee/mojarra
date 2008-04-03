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

/**
 * 
 */
package com.sun.faces.sandbox.test.webapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import com.sun.faces.sandbox.component.YuiTree;
import com.sun.faces.sandbox.component.YuiTreeNode;
import com.sun.faces.sandbox.model.FileHolder;
import com.sun.faces.sandbox.model.FileHolderImpl;

/**
 * @author lee
 *
 */
public class TestBean {
    protected YuiTree tree;
    protected Date date;
    protected Date date2;
    protected FileHolder fileHolder = new FileHolderImpl();
    protected String editorValue = "This <i>should</i> be editable!";
    protected String queryStringValue;
    
    public String getQueryStringValue() {
        return queryStringValue;
    }

    public void setQueryStringValue(String queryStringValue) {
        this.queryStringValue = queryStringValue;
    }

    public String getEditorValue() {
        return editorValue;
    }

    public void setEditorValue(String editorValue) {
        this.editorValue = editorValue;
    }

    public TestBean() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        tree = (YuiTree)app.createComponent(YuiTree.COMPONENT_TYPE);
        YuiTreeNode node1 = (YuiTreeNode)app.createComponent(YuiTreeNode.COMPONENT_TYPE);
        YuiTreeNode node2 = (YuiTreeNode)app.createComponent(YuiTreeNode.COMPONENT_TYPE);
        YuiTreeNode node3 = (YuiTreeNode)app.createComponent(YuiTreeNode.COMPONENT_TYPE);
        
        HtmlOutputText text1 = (HtmlOutputText)app.createComponent(HtmlOutputText.COMPONENT_TYPE); text1.setValue("Text 1");
        HtmlOutputText text2 = (HtmlOutputText)app.createComponent(HtmlOutputText.COMPONENT_TYPE); text2.setValue("Text 2");
        HtmlOutputText text3 = (HtmlOutputText)app.createComponent(HtmlOutputText.COMPONENT_TYPE); text3.setValue("Text 3");

        node1.getFacets().put("label", text1);
        node1.getChildren().add(node3);
        node2.getFacets().put("label", text3);
        node3.getFacets().put("label", text2);

        tree.getChildren().add(node1);
        tree.getChildren().add(node2);

        Calendar tempcal = Calendar.getInstance();
        tempcal.set(1974, 11, 23);
        date2 = tempcal.getTime();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date) {
        this.date2 = date;
    }

    public byte[] getPdf() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/sample.pdf");
            
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) != -1) {
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return baos.toByteArray();
    }
    
    public byte[] getImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/sample.png");
            
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) != -1) {
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return baos.toByteArray();
    }

    public String getDestination() {
        return "success";
    }

    public FileHolder getFileHolder() {
        return fileHolder;
    }

    public String[] getFileNames() {
        String[] fileNames = fileHolder.getFileNames().toArray(new String[]{});
        fileHolder.clearFiles();
        
        return fileNames;
    }
    
    public List<Person> getPersonList() {
        List<Person> list = new ArrayList();
        list.add(new Person("Jim", "Halpert"));
        list.add(new Person("Michael", "Scott"));
        
        return list;
    }

    public YuiTree getTree() {
        return tree;
    }

    public void setTree(YuiTree tree) {
        this.tree = tree;
    }
}

class Person {
    protected Integer id;
    protected String lastName;
    protected String firstName;
    
    public Person() {
    }
    
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}