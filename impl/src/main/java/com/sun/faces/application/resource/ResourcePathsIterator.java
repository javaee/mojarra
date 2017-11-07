/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.application.resource;

import static com.sun.faces.util.Util.startsWithOneOf;

import java.util.ArrayDeque;
import java.util.Iterator;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;

public class ResourcePathsIterator implements Iterator<String> {
    
    private final int maxDepth;
    private final ExternalContext externalContext;
    private final String[] extensions;
    private final String[] restrictedDirectories;
    
    private final ArrayDeque<String> stack = new ArrayDeque<>();
    
    private String next;
    
    public ResourcePathsIterator(String rootPath, int maxDepth, String[] extensions, String[] restrictedDirectories, ExternalContext externalContext) {
        this.maxDepth = maxDepth;
        this.externalContext = externalContext;
        this.extensions = extensions;
        this.restrictedDirectories = restrictedDirectories;
        visit(rootPath);
    }

    @Override
    public boolean hasNext() {
        if (next != null) {
            return true;
        }
        tryTake();
        
        return next != null;
    }
    
    @Override
    public String next() {
        if (next == null) {
            tryTake();
        }
        String nextReturn = next;
        next = null;
        return nextReturn;
    }
    
    private void visit(String resourcePath) {
        stack.addAll(externalContext.getResourcePaths(resourcePath));
    }
    
    private void tryTake() {
        if (stack.isEmpty()) {
            return;
        }
        
        while (next == null && !stack.isEmpty()) {
            String nextCandidate = stack.removeFirst();
            if (isDirectory(nextCandidate)) {
                if (!startsWithOneOf(nextCandidate, restrictedDirectories) && !directoryExceedsMaxDepth(nextCandidate, maxDepth)) {
                    visit(nextCandidate);
                }
            } else if (isValidCandidate(nextCandidate, extensions)) {
                next = nextCandidate;
            }
            
        }
    }
    
    /**
     * Checks if the given resource path obtained from {@link ServletContext#getResourcePaths(String)} represents a
     * directory.
     *
     * @param resourcePath the resource path to check
     * @return true if the resource path represents a directory, false otherwise
     */
    private static boolean isDirectory(final String resourcePath) {
        return resourcePath.endsWith("/");
    }
    
    private static boolean directoryExceedsMaxDepth(final String resourcePath, final long max) {
        return resourcePath.chars().filter(i -> i == '/').count() > max;
    }
    
    private static boolean isValidCandidate(final String resourcePath, final String[] extensions) {
        if (extensions == null || extensions.length == 0) {
            return true;
        }
        
        for (String extension : extensions) {
            if (resourcePath.endsWith(extension)) {
                return true;
            }
        }
        
        return false;
    }
    
}
