/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
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
package javax.faces.view.facelets;

/**
 * <p class="changed_added_2_0">An Exception caused by a {@link
 * TagAttribute}</p>
 *
 * @since 2.0
 */
public final class TagAttributeException extends FaceletException {

    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param attr the {@link TagAttribute}.
     */
    public TagAttributeException(TagAttribute attr) {
        super(attr.toString());
    }

    /**
     * Constructor.
     * 
     * @param attr the {@link TagAttribute}.
     * @param message the message.
     */
    public TagAttributeException(TagAttribute attr, String message) {
        super(attr + " " + message);
    }

    /**
     * Constructor.
     * 
     * @param attr the {@link TagAttribute}.
     * @param cause the cause.
     */
    public TagAttributeException(TagAttribute attr, Throwable cause) {
        super(attr + " " + cause.getMessage(), cause);
    }

    /**
     * Constructor.
     * 
     * @param attr the {@link TagAttribute}.
     * @param message the message.
     * @param cause the cause.
     */
    public TagAttributeException(TagAttribute attr, String message,
            Throwable cause) {
        super(attr + " " + message, cause);
    }

    /**
     * Constructor.
     *
     * @param tag the {@link Tag}.
     * @param attr the {@link TagAttribute}.
     */
    public TagAttributeException(Tag tag, TagAttribute attr) {
        super(print(tag, attr));
    }

    private static String print(Tag tag, TagAttribute attr) {
        return tag.getLocation() + " <" + tag.getQName() + " " + attr.getQName()
                + "=\"" + attr.getValue() + "\">";
    }

    /**
     * Constructor.
     * 
     * @param tag the {@link Tag}.
     * @param attr the {@link TagAttribute}.
     * @param message the message.
     */
    public TagAttributeException(Tag tag, TagAttribute attr, String message) {
        super(print(tag, attr) + " " + message);
    }

    /**
     * Constructor.
     * 
     * @param tag the {@link Tag}.
     * @param attr the {@link TagAttribute}.
     * @param cause the cause.
     */
    public TagAttributeException(Tag tag, TagAttribute attr, Throwable cause) {
        super(print(tag, attr) + " " + cause.getMessage(), cause);
    }

    /**
     * Constructor.
     *
     * @param tag the {@link Tag}.
     * @param attr the {@link TagAttribute}.
     * @param message the message.
     * @param cause the cause.
     */
    public TagAttributeException(Tag tag, TagAttribute attr, String message,
            Throwable cause) {
        super(print(tag, attr) + " " + message, cause);
    }
}
