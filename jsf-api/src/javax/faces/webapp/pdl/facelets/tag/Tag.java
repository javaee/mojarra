/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.faces.webapp.pdl.facelets.tag;

/**
 * Representation of a Tag in the Facelet definition
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public final class Tag {
    private final TagAttributes attributes;

    private final Location location;

    private final String namespace;

    private final String localName;

    private final String qName;

    public Tag(Location location, String namespace, String localName,
            String qName, TagAttributes attributes) {
        this.location = location;
        this.namespace = namespace;
        this.localName = localName;
        this.qName = qName;
        this.attributes = attributes;
    }

    public Tag(Tag orig, TagAttributes attributes) {
        this(orig.getLocation(), orig.getNamespace(), orig.getLocalName(), orig
                .getQName(), attributes);
    }

    /**
     * All TagAttributes specified
     * 
     * @return all TagAttributes specified
     */
    public TagAttributes getAttributes() {
        return attributes;
    }

    /**
     * Local name of the tag &lt;my:tag /> would be "tag"
     * 
     * @return local name of the tag
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * Location of the Tag in the Facelet file
     * 
     * @return location of the Tag in the Facelet file
     */
    public Location getLocation() {
        return location;
    }

    /**
     * The resolved Namespace for this tag
     * 
     * @return the resolved namespace for this tag
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Get the qualified name for this tag &lt;my:tag /> would be "my:tag"
     * 
     * @return qualified name of the tag
     */
    public String getQName() {
        return qName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.location + " <" + this.qName + ">";
    }
}
