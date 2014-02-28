/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.config;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.faces.context.FacesContext;

import com.sun.faces.cactus.ServletFacesTestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;

/**
 * Test cases to validate faces-config ordering.
 */
public class TestFacesConfigOrdering extends ServletFacesTestCase {


    // ------------------------------------------------------------ Constructors


    public TestFacesConfigOrdering()  {

        this("TestFacesConfigOrdering");

    }


    public TestFacesConfigOrdering(String name) {

        super(name);

    }


    // ------------------------------------------------------------ Test Methods


    public void testDocumentOrderingWrapperInit() throws Exception {

        // this should test segment should fail since this document is
        // before and after A
        List<String> docBeforeIds = new ArrayList<String>();
        Collections.addAll(docBeforeIds, "A");
        List<String> docAfterIds = new ArrayList<String>();
        Collections.addAll(docAfterIds, "A");

        try {
            new DocumentOrderingWrapper(createDocument("MyDoc", docBeforeIds, docAfterIds));
            fail("Expected DocumentOrderingWrapper to throw an exception when the wrapped document was configured to be before and after the same document.");
        } catch (ConfigurationException ce) {
            // expected
        }


        // this test segment ensures that 'empty defaults will be used if the
        // document has no document ID.
        DocumentOrderingWrapper w = new DocumentOrderingWrapper(createDocument(null, docBeforeIds, null));
        assertEquals("Expected DocumentOrderingWrapper.getDocumentId() to return an empty string when no ID was specified.  Received: " + w.getDocumentId(), "", w.getDocumentId());
        assertTrue(Arrays.equals(new String[] { "A" }, w.getBeforeIds()));
        assertTrue(Arrays.equals(new String[] {  }, w.getAfterIds()));

        docAfterIds.clear();
        Collections.addAll(docAfterIds, "others");
        w = new DocumentOrderingWrapper(createDocument("MyDoc", docBeforeIds, docAfterIds));
        assertEquals("Expected DocumentOrderingWrapper.getDocumentId() to return MyDoc, received: " + w.getDocumentId(), "MyDoc", w.getDocumentId());
        assertTrue(Arrays.equals(new String[] { "A" }, w.getBeforeIds()));
        assertTrue(Arrays.equals(new String[] { "others" }, w.getAfterIds()));
        
    }


    public void testAfterAfterOthersBeforeBeforeOthers() throws Exception {

        List<String> docAAfterIds = new ArrayList<String>();
        Collections.addAll(docAAfterIds, "@others", "C");
        List<String> docCAfterIds = new ArrayList<String>();
        Collections.addAll(docCAfterIds, "@others");
        List<String> docBBeforeIds = new ArrayList<String>();
        Collections.addAll(docBBeforeIds, "@others");
        List<String> docFBeforeIds = new ArrayList<String>();
        Collections.addAll(docFBeforeIds, "B", "@others");
        DocumentInfo docA = createDocument("A", null, docAAfterIds);
        DocumentInfo docB = createDocument("B", docBBeforeIds, null);
        DocumentInfo docC = createDocument("C", null, docCAfterIds);
        DocumentInfo docD = createDocument("D", null, null);
        DocumentInfo docE = createDocument("E", null, null);
        DocumentInfo docF = createDocument("F", docFBeforeIds, null);

        List<DocumentOrderingWrapper> documents =
              new ArrayList<DocumentOrderingWrapper>();
        Collections.addAll(documents,
                           new DocumentOrderingWrapper(docA),
                           new DocumentOrderingWrapper(docB),
                           new DocumentOrderingWrapper(docC),
                           new DocumentOrderingWrapper(docD),
                           new DocumentOrderingWrapper(docE),
                           new DocumentOrderingWrapper(docF));

        DocumentOrderingWrapper[] wrappers =
              documents.toArray(new DocumentOrderingWrapper[documents.size()]);
        DocumentOrderingWrapper.sort(wrappers);
        String[] ids = { "F", "B", "D", "E", "C", "A" };
        validate(ids, wrappers);

    }


    public void testBeforeAfterOthersSorting() throws Exception {

        List<String> docAAfterIds = new ArrayList<String>();
        Collections.addAll(docAAfterIds, "@others");

        List<String> docABeforeIds = new ArrayList<String>();
        Collections.addAll(docABeforeIds, "C");

        List<String> docBBeforeIds = new ArrayList<String>();
        Collections.addAll(docBBeforeIds, "@others");

        List<String> docDAfterIds = new ArrayList<String>();
        Collections.addAll(docDAfterIds, "@others");

        List<String> docEBeforeIds = new ArrayList<String>();
        Collections.addAll(docEBeforeIds, "@others");

        DocumentInfo docA = createDocument(null, docABeforeIds, docAAfterIds); // no ID here to ensure this works
        DocumentInfo docB = createDocument("B", docBBeforeIds, null);
        DocumentInfo docC = createDocument("C", null, null);
        DocumentInfo docD = createDocument("D", null, docDAfterIds);
        DocumentInfo docE = createDocument("E", docEBeforeIds, null);
        DocumentInfo docF = createDocument("F", null, null);

        List<DocumentOrderingWrapper> documents =
              new ArrayList<DocumentOrderingWrapper>();
        Collections.addAll(documents,
                           new DocumentOrderingWrapper(docA),
                           new DocumentOrderingWrapper(docB),
                           new DocumentOrderingWrapper(docC),
                           new DocumentOrderingWrapper(docD),
                           new DocumentOrderingWrapper(docE),
                           new DocumentOrderingWrapper(docF));
        DocumentOrderingWrapper[] wrappers =
              documents.toArray(new DocumentOrderingWrapper[documents.size()]);
        DocumentOrderingWrapper.sort(wrappers);
        String[] ids = { "B", "E", "F", "", "C", "D" };
        validate(ids, wrappers);

    }


    public void testAfterBeforeOthersSorting() throws Exception {

        List<String> docAAfterIds = new ArrayList<String>();
        Collections.addAll(docAAfterIds, "@others");

        List<String> docBBeforeIds = new ArrayList<String>();
        Collections.addAll(docBBeforeIds, "@others");

        List<String> docDAfterIds = new ArrayList<String>();
        Collections.addAll(docDAfterIds, "@others");

        List<String> docEBeforeIds = new ArrayList<String>();
        Collections.addAll(docEBeforeIds, "@others");
        List<String> docEAfterIds = new ArrayList<String>();
        Collections.addAll(docEAfterIds, "C");

        DocumentInfo docA = createDocument("A", null, docAAfterIds);
        DocumentInfo docB = createDocument("B", docBBeforeIds, null);
        DocumentInfo docC = createDocument("C", null, null);
        DocumentInfo docD = createDocument("D", null, docDAfterIds);
        DocumentInfo docE = createDocument("E", docEBeforeIds, docEAfterIds);
        DocumentInfo docF = createDocument("F", null, null);

        List<DocumentOrderingWrapper> documents =
              new ArrayList<DocumentOrderingWrapper>();
        Collections.addAll(documents,
                           new DocumentOrderingWrapper(docA),
                           new DocumentOrderingWrapper(docB),
                           new DocumentOrderingWrapper(docC),
                           new DocumentOrderingWrapper(docD),
                           new DocumentOrderingWrapper(docE),
                           new DocumentOrderingWrapper(docF));
        DocumentOrderingWrapper[] wrappers =
              documents.toArray(new DocumentOrderingWrapper[documents.size()]);
        DocumentOrderingWrapper.sort(wrappers);
        String[] ids = { "B", "C", "E", "F", "A", "D" };
        validate(ids, wrappers);

    }




    public void testSpecSimple() throws Exception {

        List<String> docAAfterIds = new ArrayList<String>();
        Collections.addAll(docAAfterIds, "B");
        List<String> docCBeforeIds = new ArrayList<String>();
        Collections.addAll(docCBeforeIds, "@others");
        DocumentInfo docA = createDocument("A", null, docAAfterIds);
        DocumentInfo docB = createDocument("B", null, null);
        DocumentInfo docC = createDocument("C", docCBeforeIds, null);
        DocumentInfo docD = createDocument("D", null, null);

        List<DocumentOrderingWrapper> documents =
              new ArrayList<DocumentOrderingWrapper>();
        Collections.addAll(documents,
                           new DocumentOrderingWrapper(docA),
                           new DocumentOrderingWrapper(docB),
                           new DocumentOrderingWrapper(docC),
                           new DocumentOrderingWrapper(docD));
        DocumentOrderingWrapper[] wrappers =
              documents.toArray(new DocumentOrderingWrapper[documents.size()]);
        DocumentOrderingWrapper.sort(wrappers);
        String[] ids = { "C", "B", "D", "A" };
        validate(ids, wrappers);

    }


    public void testBeforeIdAfterOthers() throws Exception {

        List<String> docCBeforeIds = new ArrayList<String>();
        Collections.addAll(docCBeforeIds, "B");
        List<String> docCAfterIds = new ArrayList<String>();
        Collections.addAll(docCAfterIds, "@others");
        DocumentInfo docA = createDocument("A", null, null);
        DocumentInfo docB = createDocument("B", null, null);
        DocumentInfo docC = createDocument("C", docCBeforeIds, docCAfterIds);
        DocumentInfo docD = createDocument("D", null, null);

        List<DocumentOrderingWrapper> documents =
              new ArrayList<DocumentOrderingWrapper>();
        Collections.addAll(documents,
                           new DocumentOrderingWrapper(docA),
                           new DocumentOrderingWrapper(docB),
                           new DocumentOrderingWrapper(docC),
                           new DocumentOrderingWrapper(docD));
        DocumentOrderingWrapper[] wrappers =
              documents.toArray(new DocumentOrderingWrapper[documents.size()]);
        DocumentOrderingWrapper.sort(wrappers);
                String[] ids = { "A", "D", "C", "B" };
        validate(ids, wrappers);

    }


    public void testAfterIdBeforeOthers() throws Exception {

        List<String> docCAfterIds = new ArrayList<String>();
        Collections.addAll(docCAfterIds, "D");
        List<String> docCBeforeIds = new ArrayList<String>();
        Collections.addAll(docCBeforeIds, "@others");
        DocumentInfo docA = createDocument("A", null, null);
        DocumentInfo docB = createDocument("B", null, null);
        DocumentInfo docC = createDocument("C", docCBeforeIds, docCAfterIds);
        DocumentInfo docD = createDocument("D", null, null);

        List<DocumentOrderingWrapper> documents =
              new ArrayList<DocumentOrderingWrapper>();
        Collections.addAll(documents,
                           new DocumentOrderingWrapper(docA),
                           new DocumentOrderingWrapper(docB),
                           new DocumentOrderingWrapper(docC),
                           new DocumentOrderingWrapper(docD));
        DocumentOrderingWrapper[] wrappers =
              documents.toArray(new DocumentOrderingWrapper[documents.size()]);
        DocumentOrderingWrapper.sort(wrappers);
                String[] ids = { "D", "C", "A", "B" };
        validate(ids, wrappers);

    }

    
    public void testAllAfterSpecificIds() throws Exception {

        List<String> docAAfterIds = new ArrayList<String>();
        List<String> docBAfterIds = new ArrayList<String>();
        List<String> docCAfterIds = new ArrayList<String>();
        Collections.addAll(docAAfterIds, "B");
        Collections.addAll(docBAfterIds, "C");
        Collections.addAll(docCAfterIds, "D");
        DocumentInfo docA = createDocument("A", null, docAAfterIds);
        DocumentInfo docB = createDocument("B", null, docBAfterIds);
        DocumentInfo docC = createDocument("C", null, docCAfterIds);
        DocumentInfo docD = createDocument("D", null, null);

        List<DocumentOrderingWrapper> documents =
              new ArrayList<DocumentOrderingWrapper>();
        Collections.addAll(documents,
                           new DocumentOrderingWrapper(docA),
                           new DocumentOrderingWrapper(docB),
                           new DocumentOrderingWrapper(docC),
                           new DocumentOrderingWrapper(docD));
        DocumentOrderingWrapper[] wrappers =
              documents.toArray(new DocumentOrderingWrapper[documents.size()]);
        DocumentOrderingWrapper.sort(wrappers);
                String[] ids = { "D", "C", "B", "A" };
        validate(ids, wrappers);

    }


    public void testAllBeforeSpecificIds() throws Exception {

        List<String> docBBeforeIds = new ArrayList<String>();
        List<String> docCBeforeIds = new ArrayList<String>();
        List<String> docDBeforeIds = new ArrayList<String>();
        Collections.addAll(docBBeforeIds, "A");
        Collections.addAll(docCBeforeIds, "B");
        Collections.addAll(docDBeforeIds, "C");
        DocumentInfo docA = createDocument("A", null, null);
        DocumentInfo docB = createDocument("B", docBBeforeIds, null);
        DocumentInfo docC = createDocument("C", docCBeforeIds, null);
        DocumentInfo docD = createDocument("D", docDBeforeIds, null);

        List<DocumentOrderingWrapper> documents =
              new ArrayList<DocumentOrderingWrapper>();
        Collections.addAll(documents,
                           new DocumentOrderingWrapper(docA),
                           new DocumentOrderingWrapper(docB),
                           new DocumentOrderingWrapper(docC),
                           new DocumentOrderingWrapper(docD));
        DocumentOrderingWrapper[] wrappers =
              documents.toArray(new DocumentOrderingWrapper[documents.size()]);
        DocumentOrderingWrapper.sort(wrappers);
                String[] ids = { "D", "C", "B", "A" };
        validate(ids, wrappers);

    }


    public void testMixed1() throws Exception {

        List<String> docBAfterIds = new ArrayList<String>();
        List<String> docCBeforeIds = new ArrayList<String>();
        Collections.addAll(docBAfterIds, "C");
        Collections.addAll(docCBeforeIds, "B");
        DocumentInfo docA = createDocument("A", null, null);
        DocumentInfo docB = createDocument("B", null, docBAfterIds);
        DocumentInfo docC = createDocument("C", docCBeforeIds, null);
        DocumentInfo docD = createDocument("D", null, null);

        List<DocumentOrderingWrapper> documents =
              new ArrayList<DocumentOrderingWrapper>();
        Collections.addAll(documents,
                           new DocumentOrderingWrapper(docA),
                           new DocumentOrderingWrapper(docB),
                           new DocumentOrderingWrapper(docC),
                           new DocumentOrderingWrapper(docD));
        DocumentOrderingWrapper[] wrappers =
              documents.toArray(new DocumentOrderingWrapper[documents.size()]);
        DocumentOrderingWrapper.sort(wrappers);
                String[] ids = { "A", "C", "D", "B" };
        validate(ids, wrappers);

    }


    public void testCyclic1() throws Exception {

        List<String> docABeforeIds = new ArrayList<String>();
        List<String> docBBeforeIds = new ArrayList<String>();
        List<String> docCBeforeIds = new ArrayList<String>();
        Collections.addAll(docABeforeIds, "C");
        Collections.addAll(docBBeforeIds, "A");
        Collections.addAll(docCBeforeIds, "B");
        DocumentInfo docA = createDocument("A", docABeforeIds, null);
        DocumentInfo docB = createDocument("B", docBBeforeIds, null);
        DocumentInfo docC = createDocument("C", docCBeforeIds, null);
        DocumentInfo docD = createDocument("D", null, null);

        List<DocumentOrderingWrapper> documents =
              new ArrayList<DocumentOrderingWrapper>();
        Collections.addAll(documents,
                           new DocumentOrderingWrapper(docA),
                           new DocumentOrderingWrapper(docB),
                           new DocumentOrderingWrapper(docC),
                           new DocumentOrderingWrapper(docD));
        DocumentOrderingWrapper[] wrappers =
              documents.toArray(new DocumentOrderingWrapper[documents.size()]);

        try {
            DocumentOrderingWrapper.sort(wrappers);
            fail("No exception thrown when circular document dependency is present");
        } catch (ConfigurationException ce) {
            // expected
        }

    }


    public void testCyclic2() throws Exception {

        List<String> docAAfterIds = new ArrayList<String>();
        List<String> docBAfterIds = new ArrayList<String>();
        List<String> docCAfterIds = new ArrayList<String>();
        Collections.addAll(docAAfterIds, "B");
        Collections.addAll(docBAfterIds, "C");
        Collections.addAll(docCAfterIds, "A");
        DocumentInfo docA = createDocument("A", null, docAAfterIds);
        DocumentInfo docB = createDocument("B", null, docBAfterIds);
        DocumentInfo docC = createDocument("C", null, docCAfterIds);
        DocumentInfo docD = createDocument("D", null, null);

        List<DocumentOrderingWrapper> documents =
              new ArrayList<DocumentOrderingWrapper>();
        Collections.addAll(documents,
                           new DocumentOrderingWrapper(docA),
                           new DocumentOrderingWrapper(docB),
                           new DocumentOrderingWrapper(docC),
                           new DocumentOrderingWrapper(docD));
        DocumentOrderingWrapper[] wrappers =
              documents.toArray(new DocumentOrderingWrapper[documents.size()]);
        
        try {
            DocumentOrderingWrapper.sort(wrappers);
            fail("No exception thrown when circular document dependency is present");
        } catch (ConfigurationException ce) {
            // expected
        }

    }


    public void testAbsoluteDocumentOrderingAPI() throws Exception {

        Document d = parseDocumentAsWebInfFacesConfig(getFacesContext(), "/WEB-INF/webinfAbsolute1.xml");
        FacesConfigInfo info = new FacesConfigInfo(new DocumentInfo(d, null));
        assertTrue(info.isWebInfFacesConfig());
        assertTrue(info.isVersionGreaterOrEqual(2.0));
        assertFalse(info.isMetadataComplete());
        List<String> ordering = info.getAbsoluteOrdering();
        assertNotNull(ordering);
        assertEquals(3, ordering.size());
        assertEquals("a", ordering.get(0));
        assertEquals("b", ordering.get(1));
        assertEquals("c", ordering.get(2));

        d = parseDocumentAsWebInfFacesConfig(getFacesContext(), "/WEB-INF/webinfAbsolute2.xml");
        info = new FacesConfigInfo(new DocumentInfo(d, null));
        assertTrue(info.isWebInfFacesConfig());
        assertTrue(info.isVersionGreaterOrEqual(2.0));
        assertTrue(info.isMetadataComplete());
        ordering = info.getAbsoluteOrdering();
        assertNotNull(ordering);
        assertEquals(4, ordering.size());
        assertEquals("a", ordering.get(0));
        assertEquals("b", ordering.get(1));
        assertEquals("others", ordering.get(2));
        assertEquals("c", ordering.get(3));

        d = parseDocumentAsWebInfFacesConfig(getFacesContext(), "/WEB-INF/webinfAbsolute3.xml");
        info = new FacesConfigInfo(new DocumentInfo(d, null));
        assertTrue(info.isWebInfFacesConfig());
        assertFalse(info.isVersionGreaterOrEqual(2.0));
        assertTrue(info.isMetadataComplete());
        ordering = info.getAbsoluteOrdering();
        assertNull(ordering);

        d = parseDocument(getFacesContext(), "/WEB-INF/webinfAbsolute1.xml");
        info = new FacesConfigInfo(new DocumentInfo(d, null));
        assertFalse(info.isWebInfFacesConfig());

    }


    public void testAbsoluteOrderingProcessing() throws Exception {

        DocumentInfo docA = createDocument("a", null, null);
        DocumentInfo docB = createDocument("b", null, null);
        DocumentInfo docC = createDocument("c", null, null);
        DocumentInfo docD = createDocument("d", null, null);
        DocumentInfo docE = createDocument("e", null, null);
        DocumentInfo docF = createDocument("f", null, null);
        List<DocumentOrderingWrapper> wrappers = new ArrayList<DocumentOrderingWrapper>();
        Collections.addAll(wrappers,
                           new DocumentOrderingWrapper(docF),
                           new DocumentOrderingWrapper(docE),
                           new DocumentOrderingWrapper(docD),
                           new DocumentOrderingWrapper(docC),
                           new DocumentOrderingWrapper(docB),
                           new DocumentOrderingWrapper(docA));
        DocumentOrderingWrapper[] documentWrappers =
              wrappers.toArray(new DocumentOrderingWrapper[wrappers.size()]);

        Document d = parseDocumentAsWebInfFacesConfig(getFacesContext(), "/WEB-INF/webinfAbsolute1.xml");
        FacesConfigInfo info = new FacesConfigInfo(new DocumentInfo(d, null));
        List<String> ordering = info.getAbsoluteOrdering();
        DocumentOrderingWrapper[] result =
              DocumentOrderingWrapper.sort(documentWrappers, ordering);
        assertEquals(3, result.length);
        assertEquals("a", result[0].getDocumentId());
        assertEquals("b", result[1].getDocumentId());
        assertEquals("c", result[2].getDocumentId());

        d = parseDocumentAsWebInfFacesConfig(getFacesContext(), "/WEB-INF/webinfAbsolute2.xml");
        info = new FacesConfigInfo(new DocumentInfo(d, null));
        ordering = info.getAbsoluteOrdering();
        result =
              DocumentOrderingWrapper.sort(documentWrappers, ordering);
        assertEquals(6, result.length);
        assertEquals("a", result[0].getDocumentId());
        assertEquals("b", result[1].getDocumentId());
        assertEquals("d", result[2].getDocumentId());
        assertEquals("e", result[3].getDocumentId());
        assertEquals("f", result[4].getDocumentId());
        assertEquals("c", result[5].getDocumentId());
    }


    // ---------------------------------------------------------- Helper Methods


    private void validate(String[] ids, DocumentOrderingWrapper[] wrappers) {

        for (int i = 0; i < wrappers.length; i++) {
            assertEquals("Expected ID " + ids[i] + " at index " + i + ", but received " + wrappers[i].getDocumentId(), ids[i], wrappers[i].getDocumentId());
        }

    }


    private Document parseDocument(FacesContext ctx, String path) throws Exception {

        DocumentBuilderFactory factory = DbfFactory.getFactory();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(ctx.getExternalContext().getResourceAsStream(path));

    }


    private Document parseDocumentAsWebInfFacesConfig(FacesContext ctx, String path) throws Exception {

        Document d = parseDocument(ctx, path);
        Attr webInf = d.createAttribute(ConfigManager.WEB_INF_MARKER);
        webInf.setValue("true");
        d.getDocumentElement().getAttributes().setNamedItem(webInf);
        return d;

    }
    

    private DocumentInfo createDocument(String documentId,
                                    List<String> beforeIds,
                                    List<String> afterIds)
          throws Exception {

        String ns = "http://java.sun.com/xml/ns/javaee";
        Document document = newDocument();
        Element root = document.createElementNS(ns, "faces-config");
        if (documentId != null) {
            Element nameElement = document.createElementNS(ns, "name");
            nameElement.setTextContent(documentId);
            root.appendChild(nameElement);
        }
        document.appendChild(root);
        boolean hasBefore = (beforeIds != null && !beforeIds.isEmpty());
        boolean hasAfter = (afterIds != null && !afterIds.isEmpty());
        boolean createOrdering = (hasBefore || hasAfter);
        if (createOrdering) {
            Element ordering = document.createElementNS(ns, "ordering");
            root.appendChild(ordering);
            if (hasBefore) {
                populateIds("before", beforeIds, ns, document, ordering);
            }
            if (hasAfter) {
                populateIds("after", afterIds, ns, document, ordering);
            }
        }

        return new DocumentInfo(document, null);

    }


    private void populateIds(String elementName,
                             List<String> ids,
                             String ns,
                             Document document,
                             Element ordering) {

        Element element = document.createElementNS(ns, elementName);
        ordering.appendChild(element);
        for (String id : ids) {
            Element append;
            if ("@others".equals(id)) {
                append = document.createElementNS(ns, "others");
            } else {
                append = document.createElementNS(ns, "name");
                append.setTextContent(id);
            }
            element.appendChild(append);
        }

    }


    private Document newDocument() throws ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder().newDocument();

    }

}
