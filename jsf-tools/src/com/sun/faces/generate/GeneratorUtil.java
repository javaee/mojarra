/*
 * $Id: GeneratorUtil.java,v 1.1 2004/12/13 19:07:49 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import com.sun.faces.config.beans.ComponentBean;
import com.sun.faces.config.beans.RenderKitBean;
import com.sun.faces.config.beans.RendererBean;
import com.sun.faces.config.beans.FacesConfigBean;
import com.sun.faces.config.DigesterFactory;
import com.sun.faces.config.rules.FacesConfigRuleSet;

import org.xml.sax.InputSource;
import org.apache.commons.digester.Digester;

/**
 * <p>Utility methods that may be useful to all <code>Generators</code>.</p>
 */
public class GeneratorUtil {

    private static final String PREFIX = "javax.faces.";


    // ---------------------------------------------------------- Public Methods


    /**
     * Obtain an instance of JspTldGenerator based on the JSP version
     * provided.
     */
    public static JspTLDGenerator getTldGenerator(PropertyManager propManager) {

        String version =
            propManager.getProperty(PropertyManager.JSP_VERSION_PROPERTY)[0];
        if ("1.2".equals(version)) {
            return new JspTLD12Generator(propManager);
        } else if ("2.1".equals(version)) {
            return new JspTLD21Generator(propManager);
        } else {
            throw new IllegalArgumentException("Unsupported version of JSP '" +
                version + '\'');
        }
    }

    /**
     * <p>Strip any "javax.faces." prefix from the beginning of the specified
     * identifier, and return it.</p>
     *
     * @param identifier Identifier to be stripped
     */
    public static String stripJavaxFacesPrefix(String identifier) {

        if (identifier.startsWith(PREFIX)) {
            return (identifier.substring(PREFIX.length()));
        } else {
            return (identifier);
        }

    } // END stripJavaxFacesPrefix


    /**
     * Build the tag handler class name from componentFamily and rendererType.
     *
     * @param componentFamily the component family
     * @param rendererType the renderer type
     */
    public static String makeTagClassName(String componentFamily,
                                          String rendererType) {

        if (componentFamily == null) {
            return null;
        }
        String tagClassName = componentFamily;
        if (rendererType != null) {
            if (!componentFamily.equals(rendererType)) {
                tagClassName = tagClassName + rendererType;
            }
        }
        return tagClassName + "Tag";

    } // END makeTagClassName


    /**
     * @return a SortedMap, where the keys are component-family String entries,
     *         and the values are {@link com.sun.faces.config.beans.ComponentBean}
     *         instances Only include components that do not have a base
     *         component type.
     */
    public static Map getComponentFamilyComponentMap(
        FacesConfigBean configBean) {

        TreeMap result = new TreeMap();
        ComponentBean component;
        ComponentBean[] components = configBean.getComponents();
        for (int i = 0, len = components.length; i < len; i++) {
            component = components[i];
            if (component == null) {
                throw new IllegalStateException("No Components Found");
            }
            if (component.getBaseComponentType() != null) {
                continue;
            }
            String componentFamily = component.getComponentFamily();

            result.put(componentFamily, component);
        }

        return result;

    } // END getComponentFamilyComponentMap


    public static Map getComponentFamilyRendererMap(FacesConfigBean configBean,
                                                    String renderKitId) {

        RenderKitBean renderKit = configBean.getRenderKit(renderKitId);
        if (renderKit == null) {
            throw new IllegalArgumentException("No RenderKit for id '" +
                renderKitId + '\'');
        }

        RendererBean[] renderers = renderKit.getRenderers();
        if (renderers == null) {
            throw new IllegalStateException("No Renderers for RenderKit id" +
                '"' + renderKitId + '"');
        }

        TreeMap result = new TreeMap();

        for (int i = 0, len = renderers.length; i < len; i++) {
            RendererBean renderer = renderers[i];

            if (renderer == null) {
                throw new IllegalStateException("no Renderer");
            }

            // if this is the first time we've encountered this
            // componentFamily
            String componentFamily = renderer.getComponentFamily();
            ArrayList list = (ArrayList)
                result.get(componentFamily);
            if (list == null) {
                // create a list for it
                list = new ArrayList();
                list.add(renderer);
                result.put(componentFamily, list);
            } else {
                list.add(renderer);
            }
        }

        return result;

    } // END getComponentFamilyRendererMap


    public static FacesConfigBean getConfigBean(String facesConfig)
    throws Exception {

        FacesConfigBean fcb = null;
        InputStream stream = null;
        try {
            File file = new File(facesConfig);
            stream = new BufferedInputStream(new FileInputStream(file));
            InputSource source = new InputSource(file.toURL().toString());
            source.setByteStream(stream);
            fcb = (FacesConfigBean)
                createDigester(true, false, true).parse(source);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    ;
                }
                stream = null;
            }
        }
        return (fcb);

    } // END getConfigBean


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Configure and return a <code>Digester</code> instance suitable for
     * use in the environment specified by our parameter flags.</p>
     *
     * @param design Include rules suitable for design time use in a tool
     * @param generate Include rules suitable for generating component,
     * renderer, and tag classes
     * @param runtime Include rules suitable for runtime execution
     */
    private static Digester createDigester(boolean design,
                                       boolean generate, boolean runtime) {

        Digester digester = DigesterFactory.newInstance(true).createDigester();

        // Configure parsing rules
        digester.addRuleSet(new FacesConfigRuleSet(design, generate, runtime));

        // Configure preregistered entities

        return (digester);

    } // END createDigester

}
