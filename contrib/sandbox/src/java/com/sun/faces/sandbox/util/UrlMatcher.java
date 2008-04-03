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
package com.sun.faces.sandbox.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class UrlMatcher {
    private List<String> elSnippets = new LinkedList<String>();
    private String urlPattern;

    /**
     * Create a UrlMatcher with the given pattern
     * @param pattern
     */
    public UrlMatcher(String pattern) {
        Pattern elPattern = Pattern.compile("\\#\\{[\\w\\.]+?\\}");
        Matcher elMatcher = elPattern.matcher(pattern);
        while (elMatcher.find()) {
            elSnippets.add(elMatcher.group());
        }
        urlPattern = elMatcher.replaceAll("([^/]+)");
        System.out.println(pattern + " -> " + urlPattern);
        System.out.println();
    }

    public Map<String, String> getInjections(String url) {
        System.out.println("\t" + url);
        Matcher m = Pattern.compile(urlPattern).matcher(url);
        if (m.matches()) {
            int numGroups = m.groupCount();
            if (numGroups != elSnippets.size()) {
                throw new RuntimeException("Aaaaaahhhhh!");
            }
            Map<String, String> elMap = new HashMap<String, String>();
            for (int i=0; i<elSnippets.size();i++) {
                elMap.put(elSnippets.get(i), m.group(i+1));
                System.out.println("\t\t" + elSnippets.get(i) + " -> " + m.group(i+1));
            }
            System.out.println();
            return elMap;
        }
        System.out.println("\t\t<null>\n");
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        UrlMatcher um = new UrlMatcher("/blah/#{x.name}/foo/.*"); // note additional regext stuff, ok as long as no parenthesis
        um.getInjections("/doesnotmatch/anything");
        um.getInjections("/blah/12345/foo/index.faces");
        um.getInjections("/blah/54321/foo");

        um = new UrlMatcher("/#{customer.id}/order/#{order.id}/edit");
        um.getInjections("/order/984325/edit");
        um.getInjections("/wal-mart/order/12345/edit");
        um.getInjections("/hobbylobby/order/989898/edit");
        um.getInjections("/hobbylobby/order/989898/edit/extrastuff");
    }
}