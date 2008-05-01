/*
 * $Id: TickerRenderer.java.java,v 1.35 2007/08/30 19:29:12 rlubke Exp $
 */

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

package viewstate;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

/**
 * Arbitrary grouping "renderer" that simply renders its children
 * recursively in the <code>encodeEnd()</code> method.
 *
 * @version $Id: TickerRenderer.java.java,v 1.35 2007/08/30 19:29:12 rlubke Exp $
 */
public class TickerRenderer extends Renderer {

    // ---------------------------------------------------------- Public Methods


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", component);
        String id = component.getId();
        if (id != null && (!id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX))) {
            writer.writeAttribute("id", id = component.getClientId(context), "id");
        }
        writer.writeAttribute("style", "position: relative; height: 18px; width: 100%; overflow: hidden; border-top:2px solid black; border-bottom:2px solid black;", "style");
        writer.writeAttribute("onmouseover", "ticker.stop();", "onmouseover");
        writer.writeAttribute("onmouseout", "ticker.start();", "onmouseout");
        writer.writeText("\n", null);
        writer.startElement("div", component);
        writer.writeAttribute("id", id = "tickerID", "id");
        writer.writeAttribute("class", "ticker", "styleClass");
        writer.writeText("\n", null);
        writer.startElement("span", component);
        writer.endElement("span");
        writer.writeText("\n", null);
        writer.endElement("div");
        writer.writeText("\n", null);
        writer.endElement("div");
        writer.writeText("\n", null);

        writer.startElement("table", component);
        writer.writeAttribute("cellspacing", "20", "cellspacing");
        writer.startElement("tr", component);
        writer.startElement("td", component);
        writer.writeText("Ticker Speed ", null);
        writer.startElement("input", component);
        writer.writeAttribute("id", "speed", "id");
        writer.writeAttribute("name", "speed", "name");
        writer.writeAttribute("type", "text", "type");
        writer.writeAttribute("value", "30", "value");
        writer.writeAttribute("size", "4", "4");
        writer.endElement("input");
        writer.writeText(" ms", null);
        writer.startElement("input", component);
        writer.writeAttribute("id", "tickerSpeed", "id");
        writer.writeAttribute("name", "tickerSpeed", "name");
        writer.writeAttribute("type", "submit", "type");
        writer.writeAttribute("value", "Set Ticker Speed", "value");
        writer.writeAttribute("onclick", "setTickerSpeed(); return false;", "onclick");
        writer.endElement("input");
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
        
        writer.startElement("script", component);
        writer.writeAttribute("language", "javascript", "language");
        writer.writeAttribute("type", "text/javascript", "type");
        writer.writeText("\n", null);
        writer.writeText("<!--\n", null);
        writer.writeText("startticker();\n", null);
        writer.writeText("// -->\n", null);
        writer.endElement("script");
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {

    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

    }
}
