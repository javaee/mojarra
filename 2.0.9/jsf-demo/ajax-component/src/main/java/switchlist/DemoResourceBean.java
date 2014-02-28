/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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
 */

package switchlist;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * This bean is responsible for building the metadata used in generating the
 * demo index page.
 */
@ManagedBean(name = "demoBean", eager = true)
@ApplicationScoped
public class DemoResourceBean {

    /**
     * The path and filename to the demo metadata descriptor.
     */
    private static final String DEMO_DESCRIPTOR = "/WEB-INF/demo.xml";


    /**
     * List of <code>DemoBean</code>s for this application instance.
     */
    private List<DemoBean> demoBeans;


    // ---------------------------------------------------------- Public Methods


    /**
     * @return a <code>Collection</code> of <code>DemoBeans</code> for this
     *  application.
     */
    public Collection<DemoBean> getDemoBeans() {

        return demoBeans;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * @return a non-validating, non-namespace aware <code>DocumentBuilder</code>
     * @throws Exception if the DocumentBuilder cannot be obtained
     */
    private DocumentBuilder getBuilder() throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(false);
        return dbf.newDocumentBuilder();

    }


    /**
     * @return the <code>Document</code> based off the content found within
     *  {@link #DEMO_DESCRIPTOR}
     * @throws Exception if an error occurs parsing {@link #DEMO_DESCRIPTOR}
     */
    private Document getDemoMetadata() throws Exception {

        FacesContext ctx = FacesContext.getCurrentInstance();
        DocumentBuilder builder = getBuilder();
        return builder.parse(ctx.getExternalContext().getResourceAsStream(DEMO_DESCRIPTOR));

    }


    /**
     * <p>
     * This method is responsible for constructing the <code>DemoBean</code>
     * instances based on the parsed content of {@link #DEMO_DESCRIPTOR}.
     * </p>
     * <p>
     * This method will be called by the JSF managed bean facility before the
     * bean is automatically pushed to the application scope when the
     * application starts.
     * </p>
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @PostConstruct
    private void buildDemoMetaData()  {

        try {
            Document d = getDemoMetadata();
            ArrayList<DemoBean> beans = new ArrayList<DemoBean>();
            NodeList demoElements = d.getElementsByTagName("demo");
            for (int i = 0, len = demoElements.getLength(); i < len; i++) {
                DemoBean b = processDemo(demoElements.item(i));
                if (b != null) {
                    beans.add(b);
                }
            }
            beans.trimToSize();
            demoBeans = beans;
        } catch (Exception e) {
            throw new FacesException("Unable to initialize demo", e);
        }

    }


    /**
     * @param demoNode node representing the &lt;demo&gt; element.
     * @return a new <code>DemoBean</code> instance based off the provided
     *  <code>Node</code> and it's children.
     */
    private DemoBean processDemo(Node demoNode) {

        NodeList children = demoNode.getChildNodes();
        String name = null;
        String page = null;
        ArrayList<DemoSourceInfo> sourceInfo = new ArrayList<DemoSourceInfo>();
        for (int i = 0, len = children.getLength(); i < len; i++) {
            Node n = children.item(i);
            if ("name".equals(n.getNodeName())) {
                name = getNodeText(n);
            } else if ("page".equals(n.getNodeName())) {
                page = getNodeText(n);
            } else if ("sources".equals(n.getNodeName())) {
                NodeList sources = n.getChildNodes();
                for (int j = 0, jlen = sources.getLength(); j < jlen; j++) {
                    Node s = sources.item(j);
                    DemoSourceInfo info = createDemoSourceInfo(s);
                    if (info != null) {
                        sourceInfo.add(createDemoSourceInfo(s));
                    }
                }
            }
        }

        sourceInfo.trimToSize();

        if (name != null && page != null && !sourceInfo.isEmpty()) {
            return new DemoBean(name, page, sourceInfo);
        }
        return null;

    }


    /**
     * @param sourceInfo node representing the &lt;sources&gt; element.
     * @return a new <code>DemoSourceInfo</code> instance based off the provided
     *  <code>Node</code> and it's children.
     */
    private DemoSourceInfo createDemoSourceInfo(Node sourceInfo) {

        NodeList children = sourceInfo.getChildNodes();
        String label = null;
        String path = null;
        for (int i = 0, len = children.getLength(); i < len; i++) {
            Node n = children.item(i);
            if ("label".equals(n.getNodeName())) {
                label = getNodeText(n);
            } else if ("path".equals(n.getNodeName())) {
                path = getNodeText(n);
            }
        }

        if (label != null && path != null) {
            return new DemoSourceInfo(label, path);
        }
        return null;

    }


    /**
     * @param node target <code>Node</code>
     * @return the textual value of the <code>Node</code>
     */
    private String getNodeText(Node node) {

        String res = null;
        if (node != null) {
            res = node.getTextContent();
            if (res != null) {
                res = res.trim();
            }
        }

        return ((res != null && res.length() != 0) ? res : null);

    }


    // ---------------------------------------------------------- Nested Classes


    public static final class DemoBean {

        private List<DemoSourceInfo> sourceInfo;
        private String demoName;
        private String page;


        DemoBean(String demoName, String page, List<DemoSourceInfo> sourceInfo) {

            this.demoName = demoName;
            this.page = page;
            this.sourceInfo = sourceInfo;

        }


        // ------------------------------------------------------ Public Methods


        public String getDemoName() {

            return demoName;

        }


        public String getPage() {

            return page;

        }


        public List<DemoSourceInfo> getSourceInfo() {

            return sourceInfo;

        }


    } // END DemoBean


    public static final class DemoSourceInfo  {


        private String label;
        private String sourceFilePath;


        // -------------------------------------------------------- Constructors


        DemoSourceInfo(String label, String sourceFilePath) {

            this.label = label;
            this.sourceFilePath = sourceFilePath;

        }


        // ------------------------------------------------------ Public Methods


        public String getLabel() {

            return label;

        }


        public String getSourceFilePath() {

            return sourceFilePath;

        }


    } // END DemoSourceInfo

}
