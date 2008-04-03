/*
 * $Id: RenderKitConfigProcessor.java,v 1.1 2007/04/22 21:41:42 rlubke Exp $
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

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.faces.FactoryFinder;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.xml.xpath.XPathExpressionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.text.MessageFormat;

/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined under
 *  <code>/faces-config/render-kit</code>.
 * </p>
 */
public class RenderKitConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER =
         Logger.getLogger(FacesLogger.CONFIG.getLoggerName());

    private static final String DEFAULT_RENDER_KIT_CLASS =
         "com.sun.faces.renderkit.RenderKitImpl";

    /**
     * <p>/faces-config/render-kit</p>
     */
    private static final String RENDERKIT = "render-kit";

    /**
     * <p>/faces-config/render-kit/render-kit-id</p>
     */
    private static final String RENDERKIT_ID = "render-kit-id";

    /**
     * <p>/faces-config/render-kit/render-kit-class</p>
     */
    private static final String RENDERKIT_CLASS = "render-kit-class";

    /**
     * <p>/faces-config/render-kit/renderer</p>
     */
    private static final String RENDERER = "renderer";

    /**
     * <p>/faces-config/render-kit/renderer/component-family</p>
     */
    private static final String RENDERER_FAMILY = "component-family";

    /**
     * <p>/faces-config/render-kit/renderer/renderer-type</p>
     */
    private static final String RENDERER_TYPE = "renderer-type";

    /**
     * <p>/faces-config/render-kit/renderer/renderer-class</p>
     */
    private static final String RENDERER_CLASS = "renderer-class";


    // -------------------------------------------- Methods from ConfigProcessor


    /**
     * @see ConfigProcessor#process(org.w3c.dom.Document[])
     */
    public void process(Document[] documents)
    throws Exception {

        for (int i = 0; i < documents.length; i++) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Processing render-kit elements for document: ''{0}''",
                                documents[i].getDocumentURI()));
            }
            String namespace = documents[i].getDocumentElement()
                 .getNamespaceURI();
            NodeList renderkits = documents[i].getDocumentElement()
                 .getElementsByTagNameNS(namespace, RENDERKIT);
            if (renderkits != null && renderkits.getLength() > 0) {
                addRenderKits(renderkits, namespace);
            }
        }
        invokeNext(documents);

    }


    // --------------------------------------------------------- Private Methods


    private void addRenderKits(NodeList renderKits, String namespace)
    throws XPathExpressionException {

        RenderKitFactory rkf = (RenderKitFactory)
             FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        for (int i = 0, size = renderKits.getLength(); i < size; i++) {
            Node renderKit = renderKits.item(i);
            NodeList children = ((Element) renderKit)
                 .getElementsByTagNameNS(namespace, "*");
            String rkId = null;
            String rkClass = null;
            List<Node> renderers =
                 new ArrayList(children.getLength());
            for (int c = 0, csize = children.getLength(); c < csize; c++) {
                Node n = children.item(c);
                if (RENDERKIT_ID.equals(n.getNodeName())) {
                    rkId = getNodeText(n);
                } else if (RENDERKIT_CLASS.equals(n.getNodeName())) {
                    rkClass = getNodeText(n);
                } else if (RENDERER.equals(n.getNodeName())) {
                    renderers.add(n);
                }
            }

            rkId = ((rkId == null)
                    ? RenderKitFactory.HTML_BASIC_RENDER_KIT
                    : rkId);
            rkClass = ((rkClass == null)
                       ? DEFAULT_RENDER_KIT_CLASS
                       : rkClass);
            RenderKit rk = rkf.getRenderKit(null, rkId);
            if (rk == null) {
                rk = (RenderKit) Util.createInstance(rkClass);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               MessageFormat.format(
                                    "Calling RenderKitFactory.addRenderKit({0}, {1})",
                                    rkId,
                                    rkClass));
                }
                rkf.addRenderKit(rkId, rk);
            }
            if (rk != null) {
                addRenderers(rk, renderers, namespace);
            }
        }

    }


    private void addRenderers(RenderKit renderKit,
                              List<Node> renderers,
                              String namespace)
    throws XPathExpressionException {

        for (Node renderer : renderers) {
            NodeList children = ((Element) renderer)
                 .getElementsByTagNameNS(namespace, "*");
            String rendererFamily = null;
            String rendererType = null;
            String rendererClass = null;
            for (int i = 0, size = children.getLength(); i < size; i++) {
                Node n = children.item(i);
                if (RENDERER_FAMILY.equals(n.getNodeName())) {
                    rendererFamily = getNodeText(n);
                } else if (RENDERER_TYPE.equals(n.getNodeName())) {
                    rendererType = getNodeText(n);
                } else if (RENDERER_CLASS.equals(n.getNodeName())) {
                    rendererClass = getNodeText(n);
                }
            }

            if ((rendererFamily != null)
                  && (rendererType != null)
                  && (rendererClass != null)) {
                Renderer r = (Renderer) Util.createInstance(rendererClass);                
                if (r != null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   MessageFormat.format(
                                        "Calling RenderKit.addRenderer({0},{1}, {2}) for RenderKit ''{3}''",
                                        rendererFamily,
                                        rendererType,
                                        rendererClass,
                                        renderKit.getClass()));
                    }
                    renderKit.addRenderer(rendererFamily, rendererType, r);
                }
            }
        }

    }

}
