/*
 * $Id: AbstractConfigProcessor.java,v 1.1 2007/04/22 21:41:42 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2007 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.config.processor;

import com.sun.faces.application.ApplicationResourceBundle;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.List;


/**
 * <p>
 *   This is the base <code>ConfigProcessor</code> that all concrete
 *   <code>ConfigProcessor</code> implementations should extend.
 * </p> 
 */
public abstract class AbstractConfigProcessor implements ConfigProcessor {


    private ConfigProcessor nextProcessor;  


    // -------------------------------------------- Methods from ConfigProcessor


    /**
     * @see ConfigProcessor#setNext(ConfigProcessor)
     */
    public ConfigProcessor setNext(ConfigProcessor nextProcessor) {

        this.nextProcessor = nextProcessor;
        return this;
        
    }
    

    /**
     * @see ConfigProcessor#invokeNext(org.w3c.dom.Document[])
     */
    public void invokeNext(Document[] documents)
    throws Exception {

        if (nextProcessor != null) {
            nextProcessor.process(documents);
        }
        
    }


    // ------------------------------------------------------- Protected Methods


    /**
     * @return return the Application instance for this context.
     */
    protected Application getApplication() {

        ApplicationFactory afactory = (ApplicationFactory)
             FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        return afactory.getApplication();

    }


    /**
     * <p>Return the text of the specified <code>Node</code>,
     * if any.
     * @param node the <code>Node</code>
     * @return the text of the <code>Node</code>  If the length
     *  of the text is zero, this method will return <code>null</code>
     */
    protected String getNodeText(Node node) {

        String res = null;
        if (node != null) {
            res = node.getTextContent();
            if (res != null) {
                res = res.trim();
            }
        }

        return ((res != null && res.length() != 0) ? res : null);

    }


    /**
     * @return a <code>Map</code> of of textual values keyed off the values
     * of any lang or xml:lang attributes specified on an attribute.  If no
     * such attribute exists, then the key {@link ApplicationResourceBundle.DEFAULT_KEY}
     * will be used (i.e. this represents the default Locale).
     * @param list a list of nodes representing textual elements such as
     *  description or display-name     
     */
    protected Map<String, String> getTextMap(List<Node> list) {

        if (list != null && !list.isEmpty()) {
            int len = list.size();
            HashMap<String, String> names =
                    new HashMap<String, String>(len, 1.0f);
            for (int i = 0; i < len; i++) {
                Node node = list.get(i);
                String textValue = getNodeText(node);
                if (textValue != null) {
                    if (node.hasAttributes()) {
                        NamedNodeMap attributes = node
                                .getAttributes();
                        String lang
                                = getNodeText(attributes.getNamedItem(
                                     "lang"));
                        if (lang == null) {
                            lang =
                                    getNodeText(attributes.getNamedItem(
                                         "xml:lang"));
                        }
                        if (lang != null) {
                            names.put(lang, textValue);
                        } else {                                                     
                            names.put(ApplicationResourceBundle.DEFAULT_KEY,
                                      textValue);
                        }
                    } else {
                        names.put(ApplicationResourceBundle.DEFAULT_KEY,
                                  textValue);
                    }
                }
            }

            return names;
        }

        return null;
    }


}
