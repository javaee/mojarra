/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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

package javax.faces;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.faces.config.manager.documents.DocumentInfo;
import com.sun.faces.config.manager.documents.DocumentOrderingWrapper;

public class FacesConfigOrderingTestCase extends TestCase {

	// ------------------------------------------------------------ Constructors
	/**
	 * Construct a new instance of this test case.
	 *
	 * @param name
	 *            Name of the test case
	 */
	public FacesConfigOrderingTestCase(String name) {
		super(name);
	}

	// ---------------------------------------------------- Overall Test Methods
	// Set up instance variables required by this test case.
	@Override
	public void setUp() throws Exception {
		super.setUp();

		Method method = FacesContext.class.getDeclaredMethod(
				"setCurrentInstance", FacesContext.class);
		method.setAccessible(true);
		method.invoke(null, new Object[] { null });

	}

	// Return the tests included in this test case.
	public static Test suite() {
		return (new TestSuite(FacesConfigOrderingTestCase.class));
	}

	// Tear down instance variables required by ths test case
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	// ------------------------------------------------- Individual Test Methods
	/**
	 * <p>
	 * verify that the overrides specified in the faces-config.xml in the user's
	 * webapp take precedence.
	 * </p>
	 *
	 * @throws java.lang.Exception
	 */
	public void testNoOrderingStartWithCab() throws Exception {
		DocumentInfo docC = createDocument("C", null, null);
		DocumentInfo doca = createDocument("a", null, null);
		DocumentInfo docb = createDocument(null, null, null);

		List<DocumentOrderingWrapper> documents = new ArrayList<DocumentOrderingWrapper>();
		// J-
		Collections.addAll(documents, new DocumentOrderingWrapper(docC),
				new DocumentOrderingWrapper(doca), new DocumentOrderingWrapper(
						docb));
		// J+

		DocumentOrderingWrapper[] wrappers = documents
				.toArray(new DocumentOrderingWrapper[documents.size()]);
		String[] originalOrder = extractNames(wrappers);
		DocumentOrderingWrapper.sort(wrappers);

		String[] orderedNames = extractNames(wrappers);

		// a solution:
		// ['C', 'a', '']
		List<String> original = Arrays.asList(originalOrder);
		List<String> actually = Arrays.asList(orderedNames);

		List<String> possibility1 = Arrays.asList("C", "a", "");

		boolean assertion = (actually.equals(possibility1));
		String message = "\n original: " + original + "\n expected: "
				+ possibility1 + "\n actually: " + actually + "\n";
		assertTrue(message, assertion);
		System.out.println("testNoOrderingStartWithCab: Passed" + message);
	}
	
	public void testCafteraStartWithCab() throws Exception {
		List<String> docCAfterIds = new ArrayList<String>();
		Collections.addAll(docCAfterIds, "a");

		DocumentInfo docC = createDocument("C", null, docCAfterIds);
		DocumentInfo doca = createDocument("a", null, null);
		DocumentInfo docb = createDocument(null, null, null);

		List<DocumentOrderingWrapper> documents = new ArrayList<DocumentOrderingWrapper>();
		//J-
		Collections.addAll(documents,
			new DocumentOrderingWrapper(docC),
			new DocumentOrderingWrapper(doca),
			new DocumentOrderingWrapper(docb)
		);
		//J+

		DocumentOrderingWrapper[] wrappers = documents.toArray(new DocumentOrderingWrapper[documents.size()]);
		String[] originalOrder = extractNames(wrappers);
		DocumentOrderingWrapper.sort(wrappers);

		String[] orderedNames = extractNames(wrappers);

		// a solution:
		// ['a', '', 'C']
		List<String> original = Arrays.asList(originalOrder);
		List<String> actually = Arrays.asList(orderedNames);

		List<String> possibility1 = Arrays.asList("a", "", "C");

		boolean assertion = (actually.equals(possibility1));
		String message = "\n original: " + original + "\n expected: " + possibility1 +
			"\n actually: " + actually + "\n";
		assertTrue(message, assertion);
		System.out.println("testCafteraStartWithCab: Passed" + message);
	}
	
	public void testAafterD_BafterCbeforeOthers_CafterDbeforeB_startWithABCD() throws Exception {
		List<String> docAAfterIds = new ArrayList<String>();
		Collections.addAll(docAAfterIds, "D");

		// C should before B, hence B needs to be after C
		List<String> docBAfterIds = new ArrayList<String>();
		Collections.addAll(docBAfterIds, "C");

		List<String> docBBeforeIds = new ArrayList<String>();
		Collections.addAll(docBBeforeIds, "@others");

		List<String> docCAfterIds = new ArrayList<String>();
		Collections.addAll(docCAfterIds, "D");

		List<String> docCBeforeIds = new ArrayList<String>();
		Collections.addAll(docCBeforeIds, "B");

		DocumentInfo docA = createDocument("A", null, docAAfterIds);
		DocumentInfo docB = createDocument("B", docBBeforeIds, docBAfterIds);
		DocumentInfo docC = createDocument("C", docCBeforeIds, docCAfterIds);
		DocumentInfo docD = createDocument("D", null, null);

		List<DocumentOrderingWrapper> documents = new ArrayList<DocumentOrderingWrapper>();
		//J-
		Collections.addAll(documents,
			new DocumentOrderingWrapper(docA),
			new DocumentOrderingWrapper(docB),
			new DocumentOrderingWrapper(docC),
			new DocumentOrderingWrapper(docD)
		);
		//J+

		DocumentOrderingWrapper[] wrappers = documents.toArray(new DocumentOrderingWrapper[documents.size()]);
		String[] originalOrder = extractNames(wrappers);
		DocumentOrderingWrapper.sort(wrappers);

		String[] orderedNames = extractNames(wrappers);

		// a solution:
		// ['D', 'C', 'B', 'A']
		List<String> original = Arrays.asList(originalOrder);
		List<String> actually = Arrays.asList(orderedNames);

		List<String> possibility1 = Arrays.asList("D", "C", "B", "A");

		boolean assertion = (actually.equals(possibility1)
				);
		String message = "\n original: " + original + "\n expected: " + possibility1 +
			"\n actually: " + actually + "\n";
		assertTrue(message, assertion);
		System.out.println("testAafterD_BafterCbeforeOthers_CafterDbeforeB_startWithABCD: Passed" + message);
	}
	
	public void testAafterD_BafterCbeforeOthers_CafterDbeforeB_startWithADBC() throws Exception {

		List<String> docAAfterIds = new ArrayList<String>();
		Collections.addAll(docAAfterIds, "D");

		// C should before B, hence B needs to be after C
		List<String> docBAfterIds = new ArrayList<String>();
		Collections.addAll(docBAfterIds, "C");

		List<String> docBBeforeIds = new ArrayList<String>();
		Collections.addAll(docBBeforeIds, "@others");

		List<String> docCAfterIds = new ArrayList<String>();
		Collections.addAll(docCAfterIds, "D");

		List<String> docCBeforeIds = new ArrayList<String>();
		Collections.addAll(docCBeforeIds, "B");

		DocumentInfo docA = createDocument("A", null, docAAfterIds);
		DocumentInfo docB = createDocument("B", docBBeforeIds, docBAfterIds);
		DocumentInfo docC = createDocument("C", docCBeforeIds, docCAfterIds);
		DocumentInfo docD = createDocument("D", null, null);

		List<DocumentOrderingWrapper> documents = new ArrayList<DocumentOrderingWrapper>();
		//J-
		Collections.addAll(documents,
			new DocumentOrderingWrapper(docA),
			new DocumentOrderingWrapper(docD),
			new DocumentOrderingWrapper(docB),
			new DocumentOrderingWrapper(docC)
		);
		//J+

		DocumentOrderingWrapper[] wrappers = documents.toArray(new DocumentOrderingWrapper[documents.size()]);
		String[] originalOrder = extractNames(wrappers);
		DocumentOrderingWrapper.sort(wrappers);

		String[] orderedNames = extractNames(wrappers);

		// a solution:
		// ['D', 'C', 'B', 'A']
		List<String> original = Arrays.asList(originalOrder);
		List<String> actually = Arrays.asList(orderedNames);

		List<String> possibility1 = Arrays.asList("D", "C", "B", "A");

		boolean assertion = (actually.equals(possibility1)
				);
		String message = "\n original: " + original + "\n expected: " + possibility1 +
			"\n actually: " + actually + "\n";
		assertTrue(message, assertion);
		System.out.println("testAafterD_BafterCbeforeOthers_CafterDbeforeB_startWithADBC: Passed" + message);

	}
	
	public void testAafterD_BafterCbeforeOthers_CafterDbeforeB_shuffle() throws Exception {

		List<String> docAAfterIds = new ArrayList<String>();
		Collections.addAll(docAAfterIds, "D");

		// C should before B, hence B needs to be after C
		List<String> docBAfterIds = new ArrayList<String>();
		Collections.addAll(docBAfterIds, "C");

		List<String> docBBeforeIds = new ArrayList<String>();
		Collections.addAll(docBBeforeIds, "@others");

		List<String> docCAfterIds = new ArrayList<String>();
		Collections.addAll(docCAfterIds, "D");

		List<String> docCBeforeIds = new ArrayList<String>();
		Collections.addAll(docCBeforeIds, "B");

		DocumentInfo docA = createDocument("A", null, docAAfterIds);
		DocumentInfo docB = createDocument("B", docBBeforeIds, docBAfterIds);
		DocumentInfo docC = createDocument("C", docCBeforeIds, docCAfterIds);
		DocumentInfo docD = createDocument("D", null, null);

		List<DocumentOrderingWrapper> documents = new ArrayList<DocumentOrderingWrapper>();
		//J-
		Collections.addAll(documents,
			new DocumentOrderingWrapper(docA),
			new DocumentOrderingWrapper(docB),
			new DocumentOrderingWrapper(docC),
			new DocumentOrderingWrapper(docD)
		);
		//J+
		
		int number = 100;
		for (int i = 0; i < number; i++) {
			
			Collections.shuffle(documents);
	
			DocumentOrderingWrapper[] wrappers = documents.toArray(new DocumentOrderingWrapper[documents.size()]);
			String[] originalOrder = extractNames(wrappers);
			DocumentOrderingWrapper.sort(wrappers);
	
			String[] orderedNames = extractNames(wrappers);
	
			// some solutions:
			// [D, C, B, A]
			// [D, A, C, B]
			List<String> original = Arrays.asList(originalOrder);
			List<String> actually = Arrays.asList(orderedNames);
	
			List<String> possibility1 = Arrays.asList("D", "C", "B", "A");
			List<String> possibility2 = Arrays.asList("D", "A", "C", "B");
	
			boolean assertion = (actually.equals(possibility1)
					|| actually.equals(possibility2)
				);
			String message = "\n original: " + original + 
				"\n expected: " + possibility1 +
				"\n       or: " + possibility2 +
				"\n actually: " + actually + "\n";
			assertTrue(message, assertion);
			
		}
		
		System.out.println("testAafterD_BafterCbeforeOthers_CafterDbeforeB_shuffle: " + number + " shuffles passed.");

	}

	private DocumentInfo createDocument(String documentId,
			List<String> beforeIds, List<String> afterIds) throws Exception {

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
	
	public static String[] extractNames(DocumentOrderingWrapper[] documents) {
		String[] extractedNames = new String[documents.length];
		int i = 0;

		for (DocumentOrderingWrapper w : documents) {
			extractedNames[i] = w.getDocumentId();
			i++;
		}

		return extractedNames;
	}

	private void populateIds(String elementName, List<String> ids, String ns,
			Document document, Element ordering) {

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
