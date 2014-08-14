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
 */

package com.sun.faces.config;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;


/*
 * This read-only singleton class is vended by the WebConfiguration.
 * It is queried from any point in the program that needs to take action based
 * on configuration options pertaining to facelets.
 *
 */
public class FaceletsConfiguration {

    public static final String FACELETS_CONFIGURATION_ATTRIBUTE_NAME = "com.sun.faces.config.FaceletsConfiguration";

    private static final String ESCAPE_INLINE_TEXT_ATTRIBUTE_NAME = "com.sun.faces.config.EscapeInlineText";

//    private static final String CONSUME_COMMENTS_ATTRIBUTE_NAME = "com.sun.faces.config.ConsumeComments";

    private static Pattern EXTENSION_PATTERN = Pattern.compile("\\.[^/]+$");

    private WebConfiguration config;

    private  Map<String, String> faceletsProcessingMappings;


    public FaceletsConfiguration(WebConfiguration config) {
        this.config = config;

        faceletsProcessingMappings =
                config.getFacesConfigOptionValue(WebConfiguration.WebContextInitParameter.FaceletsProcessingFileExtensionProcessAs);

    }

    public boolean isProcessCurrentDocumentAsFaceletsXhtml(String alias) {
        // We want to write the XML declaration if and only if
        // The SuppressXmlDeclaration context-param is NOT enabled
        // and the file extension for the current file has a mapping
        // with the value of XHTML
        boolean currentModeIsXhtml = true;

        String extension = getExtension(alias);

        assert (null != faceletsProcessingMappings);
        if (faceletsProcessingMappings.containsKey(extension)) {
            String value = faceletsProcessingMappings.get(extension);
            currentModeIsXhtml = value.equals("xhtml");
        }

        return currentModeIsXhtml;
    }
    
    public boolean isOutputHtml5Doctype(String alias) {
        boolean currentModeIsHtml5 = true;
        
        String extension = getExtension(alias);

        assert (null != faceletsProcessingMappings);
        if (faceletsProcessingMappings.containsKey(extension)) {
            String value = faceletsProcessingMappings.get(extension);
            currentModeIsHtml5 = value.equals("html5");
        }
        
        
        return currentModeIsHtml5;
    }

    public boolean isConsumeComments(String alias) {
        boolean consumeComments = false;

        String extension = getExtension(alias);

        assert (null != faceletsProcessingMappings);
        if (faceletsProcessingMappings.containsKey(extension)) {
            String value = faceletsProcessingMappings.get(extension);
            consumeComments = value.equals("xml") || value.equals("jspx");
        }

        return consumeComments;

    }

    public boolean isConsumeCDATA(String alias) {
        boolean consumeCDATA = false;

        String extension = getExtension(alias);

        assert (null != faceletsProcessingMappings);
        if (faceletsProcessingMappings.containsKey(extension)) {
            String value = faceletsProcessingMappings.get(extension);
            consumeCDATA = value.equals("jspx") || value.equals("xml");
        }

        return consumeCDATA;

    }

    public boolean isEscapeInlineText(FacesContext context) {
        Boolean result = Boolean.TRUE;

        result = (Boolean) context.getAttributes().get(ESCAPE_INLINE_TEXT_ATTRIBUTE_NAME);
        if (null == result) {
        
            String extension = getExtension(context.getViewRoot().getViewId());

            assert (null != faceletsProcessingMappings);
            if (faceletsProcessingMappings.containsKey(extension)) {
                String value = faceletsProcessingMappings.get(extension);
                result = value.equals("xml") || value.equals("xhtml");
            } else {
                result = Boolean.TRUE;
            }
            context.getAttributes().put(ESCAPE_INLINE_TEXT_ATTRIBUTE_NAME,
                    result);
        }

        return result;
    }

    public static FaceletsConfiguration getInstance(FacesContext context) {
        FaceletsConfiguration result = null;
        Map<Object, Object> attrs = context.getAttributes();
        result = (FaceletsConfiguration) attrs.get(FaceletsConfiguration.FACELETS_CONFIGURATION_ATTRIBUTE_NAME);
        if (null == result) {
            WebConfiguration config = WebConfiguration.getInstance(context.getExternalContext());
            result = config.getFaceletsConfiguration();
            attrs.put(FaceletsConfiguration.FACELETS_CONFIGURATION_ATTRIBUTE_NAME, result);
        }
        return result;
    }

    public static FaceletsConfiguration getInstance() {
        FacesContext context = FacesContext.getCurrentInstance();
        return FaceletsConfiguration.getInstance(context);
    }

    private static String getExtension(String alias) {
        String ext = null;

        if (alias != null) {
            Matcher matcher = EXTENSION_PATTERN.matcher(alias);
            if (matcher.find()) {
                ext = alias.substring(matcher.start(), matcher.end());
            }
        }

        return (ext == null) ? "xhtml": ext;
    }


}
