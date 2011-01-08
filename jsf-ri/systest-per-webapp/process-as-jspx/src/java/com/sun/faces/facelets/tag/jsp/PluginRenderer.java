
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.facelets.tag.jsp;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;


@FacesRenderer(componentFamily="javax.faces.Output", rendererType="jsp.Plugin")
public class PluginRenderer extends Renderer {

    final String [] passthruAttrs = {
        "name",
        "width",
        "height",
        "hspace",
        "vspace",
        "align"
    };
    final String [] pluginSkipAttrs = {
        "code",
        "codebase",
        "com.sun.faces.facelets.MARK_ID",
        "com.sun.faces.facelets.APPLIED",
        "jreversion",
        "type"
    };

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        Map<String, Object> attrs = component.getAttributes();
        ResponseWriter out = context.getResponseWriter();
        out.startElement("OBJECT", component);
        // write out the classid
        out.writeAttribute("classid", "clsid:8AD9C840-044E-11D1-B3E9-00805F499D93", "classid");
        // write out the passthru attrs
        for (String attrName : passthruAttrs) {
            if (attrs.containsKey(attrName)) {
                out.writeAttribute(attrName, attrs.get(attrName), attrName);
            }
        }
        // write out the codebase
        out.writeAttribute("codebase",
                "http://java.sun.com/products/plugin/1.2.2/jinstall-1_2_2-win.cab#Version=1,2,2,0",
                "codebase");
        // write out <PARAM> elements for the attrs that need to be prefixed by
        // the string "java_"
        writeRequiredParamFromAttrs(context, component, attrs, out, "code", "java_");
        writeRequiredParamFromAttrs(context, component, attrs, out, "codebase", "java_");
        // write out the type attribute
        String jreversion = attrs.containsKey("jreversion") ? attrs.get("jreversion").toString() : "1.2";
        jreversion = "application/x-java-applet;version=" + jreversion;
        writeParam(context, component, out, "type", jreversion);
        // write out the remaining attributes to the plugin element as params
        Set<String> attrKeys = attrs.keySet();
        for (String cur : attrKeys) {
            // skip if it's a passthru attr
            boolean skip = false;
            for (int i = 0; i < passthruAttrs.length; i++) {
                if (passthruAttrs[i].equals(cur)) {
                    skip = true;
                    break;
                }
            }
            for (int i = 0; i < pluginSkipAttrs.length; i++) {
                if (pluginSkipAttrs[i].equals(cur)) {
                    skip = true;
                    break;
                }
            }
            if (skip) {
                continue;
            }
            writeParamFromAttrs(context, component, attrs, out, cur, "");
        }
        // write out the nested <jsp:params>
        Map<String, ValueExpression> params = ParamHandler.getParams(context, component);
        Set<String> paramKeys = params.keySet();
        ELContext elc = context.getELContext();
        for (String cur : paramKeys) {
            writeParam(context, component, out, cur, params.get(cur).getValue(elc).toString());
        }

        // now do the whole thing again, just a little bit differently,
        // and in a <COMMENT> element.
        out.startElement("COMMENT", component);
        out.startElement("EMBED", component);
        out.writeAttribute("type", jreversion, "type");
        // write out the passthru attrs
        for (String attrName : passthruAttrs) {
            if (attrs.containsKey(attrName)) {
                out.writeAttribute(attrName, attrs.get(attrName), attrName);
            }
        }
        out.writeAttribute("pluginspage",
                "http://java.sun.com/products/plugin/",
                "pluginspage");
        out.writeAttribute("java_code", attrs.get("code"), "java_code");
        out.writeAttribute("java_codebase", attrs.get("codebase"), "java_codebase");
        // write out the remaining attributes to the plugin element as attributes on emebed
        for (String cur : attrKeys) {
            // skip if it's a passthru attr
            boolean skip = false;
            for (int i = 0; i < passthruAttrs.length; i++) {
                if (passthruAttrs[i].equals(cur)) {
                    skip = true;
                    break;
                }
            }
            for (int i = 0; i < pluginSkipAttrs.length; i++) {
                if (pluginSkipAttrs[i].equals(cur)) {
                    skip = true;
                    break;
                }
            }
            if (skip) {
                continue;
            }
            out.writeAttribute(cur, attrs.get(cur), cur);
        }
        // write out the nested params as attributes this time
        for (String cur : paramKeys) {
            out.writeAttribute(cur, params.get(cur).getValue(elc).toString(), cur);
        }

        out.endElement("EMBED");
        out.startElement("NOEMBED", component);
        if (component.getChildCount() > 0) {
        	Iterator<UIComponent> kids = component.getChildren().iterator();
        	while (kids.hasNext()) {
        	    UIComponent kid = kids.next();
        	    kid.encodeAll(context);
        	}
        }
        out.endElement("NOEMBED");
        out.endElement("COMMENT");
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter out = context.getResponseWriter();
        out.endElement("OBJECT");

    }

    private void writeRequiredParamFromAttrs(FacesContext context, UIComponent component,
            Map<String, Object> attrs, ResponseWriter out, String attrName,
            String prefix) throws IOException {
        if (!attrs.containsKey(attrName)) {
            throw new IOException("plugin must have a " + attrName + " attribute");
        }
        this.writeParamFromAttrs(context, component, attrs, out, attrName, prefix);
    }

    private void writeParamFromAttrs(FacesContext context, UIComponent component,
            Map<String, Object> attrs, ResponseWriter out, String attrName,
            String prefix) throws IOException {
        out.startElement("PARAM", component);
        out.writeAttribute("name", prefix + attrName,
                "name");
        out.writeAttribute("value", attrs.get(attrName).toString(),
                "value");
        out.endElement("PARAM");
    }

    private void writeParam(FacesContext context, UIComponent component,
            ResponseWriter out, String attrName,
            String attrValue) throws IOException {
        out.startElement("PARAM", component);
        out.writeAttribute("name", attrName, "name");
        out.writeAttribute("value", attrValue, "value");
        out.endElement("PARAM");
    }


}
