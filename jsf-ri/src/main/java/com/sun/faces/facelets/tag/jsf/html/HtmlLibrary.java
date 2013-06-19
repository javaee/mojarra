/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.tag.jsf.html;

/**
 * @author Jacob Hookom
 */
public final class HtmlLibrary extends AbstractHtmlLibrary {

    public final static String Namespace = "http://java.sun.com/jsf/html";
    public final static String XMLNSNamespace = "http://xmlns.jcp.org/jsf/html";

    public final static HtmlLibrary Instance = new HtmlLibrary();

    public HtmlLibrary() {
        this(Namespace);
    }
    
    public HtmlLibrary(String namespace) {
        super(namespace);
        
        this.addHtmlComponent("body", "javax.faces.OutputBody",
                "javax.faces.Body");

        this.addHtmlComponent("button", "javax.faces.HtmlOutcomeTargetButton",
                "javax.faces.Button");

        this.addHtmlComponent("column", "javax.faces.Column", null);

        this.addHtmlComponent("commandButton", "javax.faces.HtmlCommandButton",
                "javax.faces.Button");

        this.addHtmlComponent("commandLink", "javax.faces.HtmlCommandLink",
                "javax.faces.Link");

        this.addHtmlComponent("dataTable", "javax.faces.HtmlDataTable",
                "javax.faces.Table");

        this.addHtmlComponent("form", "javax.faces.HtmlForm",
                "javax.faces.Form");

        this.addHtmlComponent("graphicImage", "javax.faces.HtmlGraphicImage",
                "javax.faces.Image");

        this.addHtmlComponent("head", "javax.faces.Output",
                "javax.faces.Head");

        this.addHtmlComponent("html", "javax.faces.Output",
                "javax.faces.Html");

        this.addHtmlComponent("doctype", "javax.faces.Output",
                "javax.faces.Doctype");

        this.addHtmlComponent("inputFile", "javax.faces.HtmlInputFile",
                "javax.faces.File");

        this.addHtmlComponent("inputHidden", "javax.faces.HtmlInputHidden",
                "javax.faces.Hidden");

        this.addHtmlComponent("inputSecret", "javax.faces.HtmlInputSecret",
                "javax.faces.Secret");

        this.addHtmlComponent("inputText", "javax.faces.HtmlInputText",
                "javax.faces.Text");

        this.addHtmlComponent("inputTextarea", "javax.faces.HtmlInputTextarea",
                "javax.faces.Textarea");

        this.addHtmlComponent("link", "javax.faces.HtmlOutcomeTargetLink",
                "javax.faces.Link");

        this.addHtmlComponent("message", "javax.faces.HtmlMessage",
                "javax.faces.Message");

        this.addHtmlComponent("messages", "javax.faces.HtmlMessages",
                "javax.faces.Messages");

        this.addHtmlComponent("outputFormat", "javax.faces.HtmlOutputFormat",
                "javax.faces.Format");

        this.addHtmlComponent("outputLabel", "javax.faces.HtmlOutputLabel",
                "javax.faces.Label");

        this.addHtmlComponent("outputLink", "javax.faces.HtmlOutputLink",
                "javax.faces.Link");

        this.addHtmlComponent("outputText", "javax.faces.HtmlOutputText",
                "javax.faces.Text");
        
        this.addComponent("outputScript",
                          "javax.faces.Output",
                          "javax.faces.resource.Script",
                          ScriptResourceHandler.class);

        this.addComponent("outputStylesheet",
                          "javax.faces.Output",
                          "javax.faces.resource.Stylesheet",
                          StylesheetResourceHandler.class);

        this.addHtmlComponent("panelGrid", "javax.faces.HtmlPanelGrid",
                "javax.faces.Grid");

        this.addHtmlComponent("panelGroup", "javax.faces.HtmlPanelGroup",
                "javax.faces.Group");

        this.addHtmlComponent("selectBooleanCheckbox",
                              "javax.faces.HtmlSelectBooleanCheckbox",
                              "javax.faces.Checkbox");

        this.addHtmlComponent("selectManyCheckbox",
                "javax.faces.HtmlSelectManyCheckbox", "javax.faces.Checkbox");

        this.addHtmlComponent("selectManyListbox",
                "javax.faces.HtmlSelectManyListbox", "javax.faces.Listbox");

        this.addHtmlComponent("selectManyMenu",
                "javax.faces.HtmlSelectManyMenu", "javax.faces.Menu");

        this.addHtmlComponent("selectOneListbox",
                "javax.faces.HtmlSelectOneListbox", "javax.faces.Listbox");

        this.addHtmlComponent("selectOneMenu", "javax.faces.HtmlSelectOneMenu",
                "javax.faces.Menu");

        this.addHtmlComponent("selectOneRadio",
                "javax.faces.HtmlSelectOneRadio", "javax.faces.Radio");

        this.addHtmlComponent("title", "javax.faces.Output",
                "javax.faces.Title");
    }

}
