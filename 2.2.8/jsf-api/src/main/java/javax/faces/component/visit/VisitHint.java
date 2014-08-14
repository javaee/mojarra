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

package javax.faces.component.visit;

/**
 * <p class="changed_added_2_0"><span class="changed_modified_2_1">An</span>
 * enum that specifies hints that impact
 * the behavior of a component tree visit.</p>

 * @since 2.0
 */
public enum VisitHint {

  /** 
   * <p class="changed_added_2_0">Hint that indicates that only the
   * rendered subtrees should be visited.</p>
   * @since 2.0
   */
  SKIP_UNRENDERED,

  /** 
   * <p class="changed_added_2_0">Hint that indicates that only
   * non-transient subtrees should be visited.</p>
   * @since 2.0
   */
  SKIP_TRANSIENT,

  /** 
   * <p class="changed_added_2_1">Hint that indicates that components
   * that normally visit children multiple times (eg. <code>UIData</code>)
   * in an iterative fashion should instead visit each child only one time.</p>
   * @since 2.1
   */
  SKIP_ITERATION,

  /**
   * <p class="changed_added_2_0">Hint that indicates that the visit is
   * being performed as part of lifecycle phase execution and as such
   * phase-specific actions (initialization) may be taken.</p>
   * @since 2.0
   */
  EXECUTE_LIFECYCLE,

}
