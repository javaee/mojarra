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


package com.sun.faces.glassfishuc;

import com.sun.appserv.addons.ConfigurationContext;
import com.sun.appserv.addons.AddonVersion;
import com.sun.appserv.addons.Configurator;
import com.sun.appserv.addons.AddonException;
import com.sun.appserv.addons.ConfigurationContext.ConfigurationType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Configurator configurator required by Glassfish Update Center
 * Performs the configuration of the addon.
 * Typically it involves the creation of instance/domain specific artifacts
 * and configuring the domain.xml.
 *
 */
public class GFConfigurator implements Configurator {

//    private static final String PREFIX_VALUE = "${com.sun.aas.installRoot}/lib/jsf-api.jar${path.separator}"+
//            "${com.sun.aas.installRoot}/lib/jsf-impl.jar";
    private static final String PREFIX_VALUE = "${com.sun.aas.installRoot}/lib/jsf-api.jar${path.separator}"+
            "${com.sun.aas.installRoot}/lib/jsf-impl.jar";
    private static final String SEPARATOR = "${path.separator}";

    
    public void configure(ConfigurationContext cc) throws AddonException {
        System.out.println("JSF2.0: In Configurator:configure ...");

        String domainRoot = cc.getDomainDirectory().getAbsolutePath();
        String domainFilePath = domainRoot 
                + File.separator + "config" 
                + File.separator + "domain.xml";

        File domain = new File(domainFilePath);

	try {
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder = factory.newDocumentBuilder();
          Document doc = builder.parse(domain);
         
          XPathFactory xpFactory = XPathFactory.newInstance();
          XPath path = xpFactory.newXPath();
        
          Node attnode = (Node) path.evaluate("/domain/configs/config/java-config/@classpath-prefix", doc, XPathConstants.NODE);
          Node node = (Node) path.evaluate("/domain/configs/config/java-config", doc, XPathConstants.NODE);
        
          System.out.println("Current Classpath prefix is "+((attnode != null)?attnode.getNodeValue():"null"));
        
          if (attnode != null) {
              String currentValue = attnode.getNodeValue();
              String newValue = PREFIX_VALUE.concat(SEPARATOR).concat(currentValue);
              System.out.println("Classpath prefix is now "+newValue);
              attnode.setNodeValue(newValue);
          } else {
              Element element = (Element) node;
              element.setAttribute("classpath-prefix", PREFIX_VALUE);
              System.out.println("Classpath Prefix is now "+PREFIX_VALUE);
          }

          System.out.println("Saving new domain.xml file");
          Transformer t = TransformerFactory.newInstance().newTransformer();
          t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
                  doc.getDoctype().getPublicId());
          t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                  doc.getDoctype().getSystemId());
          t.setOutputProperty(OutputKeys.INDENT, "yes");
          t.setOutputProperty(OutputKeys.METHOD, "xml");
          t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"no");
        
          t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(domain)));
	} catch (Exception e) {
          throw new AddonException(e);
        }

    }
    
    public void unconfigure(ConfigurationContext cc) throws AddonException {
        System.out.println("JSF2.0: In Configurator:unconfigure ...");

        // We'll unconfigure domain.xml here


        String domainRoot = cc.getDomainDirectory().getAbsolutePath();
        String domainFilePath = domainRoot 
                + File.separator + "config" 
                + File.separator + "domain.xml";

        File domain = new File(domainFilePath);

	try {
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder = factory.newDocumentBuilder();
          Document doc = builder.parse(domain);
         
          XPathFactory xpFactory = XPathFactory.newInstance();
          XPath path = xpFactory.newXPath();
        
          Node attnode = (Node) path.evaluate("/domain/configs/config/java-config/@classpath-prefix", doc, XPathConstants.NODE);
          Node node = (Node) path.evaluate("/domain/configs/config/java-config", doc, XPathConstants.NODE);
        
          System.out.println("Current Classpath prefix is "+((attnode != null)?attnode.getNodeValue():"null"));
        
          if (attnode != null) {
              String currentValue = attnode.getNodeValue();
              String newValue = null;
              if (PREFIX_VALUE.equals(currentValue)) {
                  newValue = "";
              } else if (currentValue.indexOf(PREFIX_VALUE+SEPARATOR) != -1) {
                  newValue = currentValue.replace(PREFIX_VALUE+SEPARATOR, "");
              } else if (currentValue.indexOf(PREFIX_VALUE) != -1) {
                  newValue = currentValue.replace(PREFIX_VALUE, "");
              } else {
                  throw new AddonException("Warning:  Classpath Prefix already unconfigured");
              }
              System.out.println("Classpath prefix is now "+newValue);
              attnode.setNodeValue(newValue);
          } else {
              throw new AddonException("Warning:  Classpath Prefix already unconfigured");
          }

          System.out.println("Saving new domain.xml file");
          Transformer t = TransformerFactory.newInstance().newTransformer();
          t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
                  doc.getDoctype().getPublicId());
          t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                  doc.getDoctype().getSystemId());
          t.setOutputProperty(OutputKeys.INDENT, "yes");
          t.setOutputProperty(OutputKeys.METHOD, "xml");
          t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"no");
        
          t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(domain)));
	} catch (Exception e) {
          throw new AddonException(e);
        }



    }
    
    public void enable(ConfigurationContext cc) {
        System.out.println("JSF2.0: In Configurator:enable ...");
    }
    
    public void disable(ConfigurationContext cc) throws AddonException {
        System.out.println("JSF2.0: In Configurator:disable ...");

        throw new AddonException("It's not possible to disable this module - uninstall it instead");
    }
    
    public void upgrade(ConfigurationContext cc, AddonVersion av) throws AddonException {
        System.out.println("JSF2.0: In Configurator:upgrade ...");
        // not applicable for first release
        throw new AddonException("First release - no upgrade possible");
    }
   
}
