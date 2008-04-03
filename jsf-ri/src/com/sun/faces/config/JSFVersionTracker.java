/*
 * JSFVersionTracker
 *
 * Created on February 15, 2006, 11:41 AM
 * $Id: JSFVersionTracker.java,v 1.15 2007/07/17 21:35:18 rlubke Exp $
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

package com.sun.faces.config;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.util.FacesLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.io.Serializable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * <p>Application Singleton that helps to track the version numbers of
 * JSF artifacts defined in the application configuration resources (aka
 * faces-config.xml files.</p>
 *
 * <p>This class operates in two modes:</p>
 *
 * 	<ol>

	  <li><p>Startup time mode.</p>

<p>During startup time, this class is populated with version data for
each of the artifacts specified in the application configuration
resources.  The version of the artifact is decided based on the version
of the DTD or schema in which the artifact is declared.
PENDING(edburns): Note that when we start allowing things to be declared
via annotations, versioning will be a problem.  I've filed <a
href="https://javaserverfaces-spec-public.dev.java.net/issues/show_bug.cgi?id=149">JSF-API-149</a>
on this.</p>

<p>During startup time, this classes is accessed via a
<code>ThreadLocal</code> variable exposed via the {@link #getCurrentInstance} 
 method.</p>

          </li>

	  <li><p>Run time mode
	  </p>

<p>During runtime, this class is consulted to determine the version data
of a JSF artifact.  The reference for this class is stored on the {@link
com.sun.faces.application.ApplicationAssociate}.</p>

        </li>

	</ol>

 *
 * @author edburns
 */

public class JSFVersionTracker implements Serializable {
    
    private static Version DEFAULT_VERSION = new Version(1,2);
    
    /**
     * <p>The <code>Logger</code> instance for this class.</p>
     */
    
    //------------------------------------------------------------------- Private Methods

    private Map<String,Version> grammarToVersionMap = null;
    private List<Version> versionStack;
    
    private Map<String,Version> getGrammarToVersionMap() {
        if (null == grammarToVersionMap) {
            grammarToVersionMap = new HashMap<String, Version>(6);
            grammarToVersionMap.put("web-facesconfig_1_0.dtd", new Version(1,0));
            grammarToVersionMap.put("web-facesconfig_1_1.dtd", new Version(1,1));
            grammarToVersionMap.put("web-facesconfig_1_2.xsd", new Version(1,2));
        }
        return grammarToVersionMap;
    }
    
    private List<Version> getVersionStack() {
        if (null == versionStack) {
            versionStack = new ArrayList<Version>() {
                public String toString() {
                    //noinspection StringBufferWithoutInitialCapacity
                    StringBuilder result = new StringBuilder();
                    for (Version cur : this) {
                        if (null == cur) {
                            result.append("null\n");
                        }
                        else {
                            result.append(cur.toString()).append('\n');
                        }
                    }
                    return result.toString();
                }
            };
        }
        return versionStack;
    }

    // This is package private only for the sake of unit testing.
    Version popJSFVersionNumber() {
        List<Version> stack = getVersionStack();
        assert(null != stack);
        int nonNull;
        int end;
        Version result = null;
        
        // Starting at the end of the stack, look for 
        // a value that is not null.
        end = stack.size() - 1;
        for (nonNull = end; nonNull >= 0; nonNull--) {
            if (null != (result = stack.get(nonNull))) {
                break;
            }
        }

        // Pop all the values including the first non-null one.
        if (null != result) {
            for (int j = end; j >= nonNull; j--) {
                stack.remove(stack.size() - 1);
            }
        }
        return result;
    }

    // This is package private only for the sake of unit testing.
    Version peekJSFVersionNumber() {
        List<Version> stack = getVersionStack();
        assert(null != stack);
        Version result = null;
        
        // Starting at the end of the stack, look for 
        // a value that is not null.
        for (int i = (stack.size() - 1); i >= 0; i--) {
            if (null != (result = stack.get(i))) {
                break;
            }
        }
        return result;
    }
    
    private Map<String, Version> trackedClasses;
    
    private Map<String, Version> getTrackedClassMap() {
        if (null == trackedClasses) {
            trackedClasses = new HashMap<String, Version>() {
                public String toString() {
                    //noinspection StringBufferWithoutInitialCapacity
                    StringBuilder result = new StringBuilder();
                    for (Map.Entry cur : this.entrySet()) {
                        Version curVersion = (Version)cur.getValue();
                        result.append(cur).append(": ");
                        result.append(curVersion.toString()).append('\n');
                    }
                    return result.toString();
                }
            };
        }
        return trackedClasses;
    }
    
    // ---------- Package private methods, used from ConfigureListener and beans
    
    /**
     * <p>This is a no-op, but is included for parity with {@link #endParse}.</p>
     */
    void startParse() {

    }
    
    /**
     * <p>Conclude parsing one application configuration resource.</p>
     *
     * <p>This method takes no action if version tracking is disabled.</p>
     */ 
    
    void endParse() {
        popJSFVersionNumber();
    }
    
    /**
     * <p>Called from the point in the code when we have the grammar from 
     * an application configuration resource file.  Currently this is in
     * our custom EntityResolver for Digester.</p>
     * 
     * <p>This method takes no action if version tracking is disabled.</p>
     *
     * <p>See {@link DigesterFactory#JsfEntityResolver#resolveEntity}</p>
     *
     * @param grammar - the grammar to push.  This will be something like
     *  web_facesconfig_1_1.dtd.
     *
     */

    String pushJSFVersionNumberFromGrammar(String grammar) {
        Map<String, Version> map = getGrammarToVersionMap();
        List<Version> stack = getVersionStack();
        assert(null != map);
        assert(null != stack);
        
        // This may push null onto the stack if the grammar 
        // does not correspond to a JSF version.  
        // For example, javaee_5.xsd.
        stack.add(map.get(grammar));
        return grammar;
    }

    
    
    /**
     * <p>Associate the argument string with the JSF Spec version of the
     * application configuration resource file currently being parsed.</p>
     *
     * <p>This method takes no action if version tracking is disabled.</p>
     */
    
    
    void putTrackedClassName(String fqcn) {
        Version version = peekJSFVersionNumber();
        
        if (null == version) {
            version = DEFAULT_VERSION;
        }

        getTrackedClassMap().put(fqcn, version);
    }
    
    void publishInstanceToApplication() {
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        if (null != extContext) {
            ApplicationAssociate associate = ApplicationAssociate.getInstance(extContext);
            if (null != associate) {
                associate.setJSFVersionTracker(this);
            }
        }
    }
    
    // Public methods, used by the runtime once the instance has been published 
    // to the application.
    
    /**
     * @return the Version for the argument tracked String.
     */
    
    public Version getVersionForTrackedClassName(String fqcn) {

        return getTrackedClassMap().get(fqcn);
    }
    
    /**
     * @return the Version of the current JSF implementation
     */
    
    public Version getCurrentVersion() {
        return DEFAULT_VERSION;
    }

    public static final class Version implements Comparable {
        /**
         * Holds value of property majorVersion.
         */
        private int majorVersion;

        /**
         * Getter for property majorVersion.
         * @return Value of property majorVersion.
         */
        public int getMajorVersion() {
            return this.majorVersion;
        }

        /**
         * Setter for property majorVersion.
         * @param majorVersion New value of property majorVersion.
         */
        public void setMajorVersion(int majorVersion) {
            this.majorVersion = majorVersion;
        }

        /**
         * Holds value of property minorVersion.
         */
        private int minorVersion;

        /**
         * Getter for property minorVersion.
         * @return Value of property minorVersion.
         */
        public int getMinorVersion() {
            return this.minorVersion;
        }

        /**
         * Setter for property minorVersion.
         * @param minorVersion New value of property minorVersion.
         */
        public void setMinorVersion(int minorVersion) {
            this.minorVersion = minorVersion;
        }
        
        public int compareTo(Object obj) {
            Version other = (Version) obj;
            int result;
            int thisMajor, thisMinor, otherMajor, otherMinor;
            // Is thisMajor < thisMinor?
            if ((thisMajor = this.getMajorVersion()) < 
                 (otherMajor = other.getMajorVersion())) {
                // If so, return -1.
                result = -1;
            }
            else {
                assert(thisMajor >= otherMajor);
                // Else, thisMajor >= otherMajor.
                
                // Do the versions differ only in minorVersion?
                if (thisMajor == otherMajor) {
                    if ((thisMinor = this.getMinorVersion()) <
                        (otherMinor = other.getMinorVersion())) {
                        result = -1;
                    }
                    else {
                        assert(thisMinor >= otherMinor);
                        result = thisMinor == otherMinor ? 1 : 0;
                    }
                }
                else {
                    assert(thisMajor > otherMajor);
                    result = 1;
                }
            }
            return result;

        }

        public Version(int majorVersion, int minorVersion) {
            setMajorVersion(majorVersion);
            setMinorVersion(minorVersion);
        }
        
        public String toString() {
            return (String.valueOf(getMajorVersion()) + '.' + getMinorVersion());
        }
    }

    
}
