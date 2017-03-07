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
 */
package javax.faces.model;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Set;

import javax.faces.component.UIData;
import javax.inject.Qualifier;

/**
 * <div class="changed_added_2_3">
 * <p>The presence of this annotation on a class automatically registers the class with the runtime as a
 * {@link DataModel} that is capable of wrapping a type indicated by the {@link FacesDataModel#forClass()} attribute.
 * 
 * <p>
 * The runtime must maintain a collection of these {@link DataModel}s such that
 * {@link UIData} and other components defined by the JSF specification can query the runtime for a 
 * suitable {@link DataModel} wrapper (adapter) for the type of their <code>value</code>.
 * This has to be done after all wrappers for specific types such as {@link Set} are tried, but before 
 * the {@link ScalarDataModel} is selected as the wrapper. See {@link UIData#getValue()}.
 * 
 * <p>
 * This query must work as follows:
 * 
 * <p>
 * For an instance of type <code>Z</code> that is being bound to a <code>UIData</code> component or other component 
 * defined by the JSF specification that utilizes <code>DataModel</code>, the query for that type must return the 
 * <em>most specific</em> DataModel that can wrap <code>Z</code>. 
 * 
 * <p>
 * This <em>most specific</em> DataModel is defined as the DataModel that is obtained by first sorting the collection in 
 * which the registered <code>DataModels</code> are stored <i>(for details on this sorting see below)</i> and then 
 * iterating through the sorted collection from beginning to end and stopping this iteration at the first match where 
 * for the class <code>ZZ</code> wrapped by the DataModel (as indicated by the {@link FacesDataModel#forClass()} attribute) 
 * it holds that <code>ZZ.isAssignableFrom(Z)</code>. This match is then taken as the <em>most specific</em> DataModel.
 * 
 * <p>
 * The sorting must be done as follows:
 * 
 * <p>
 * Sort on the class wrapped by a DataModel that is stored in the above mentioned collection such that for any 2 classes 
 * <code>X</code> and <code>Y</code> from this collection, if an object of <code>X</code> is an <code>instanceof</code>
 * an object of <code>Y</code>, <code>X</code> appears in the collection <em>before</em> <code>Y</code>. 
 * The collection's sorting is otherwise arbitrary. In other words, subclasses come before their superclasses.
 * 
 * <p>
 * For example:
 *
 *<p>
 * Given <code>class B</code>, <code>class A extends B</code> and <code>class Q</code>, two possible orders are;
 * <ol>
 *   <li> <code>{A, B, Q}</code>
 *   <li> <code>{Q, A, B}</code>
 * </ol>
 *
 * <p>
 * The only requirement here is that <code>A</code> appears before <code>B</code>, since <code>A</code> is a subclass of 
 * <code>B</code>.
 *
 * <p>The specification does not define a public method to obtain an
 * instance of the "most specific DataModel for a given type".  Such an
 * instance can be obtained using code similar to the following.</p>
 * 
 * <pre>
 * <code>
 *   &#64;SuppressWarnings("unchecked")
 *   public &lt;T&gt; DataModel&lt;T&gt; createDataModel(Class&lt;T&gt; forClass, Object value) {
 *       class LocalUIData extends UIData {
 *           &#64;Override
 *           public DataModel&lt;?&gt; getDataModel() {
 *               return super.getDataModel();
 *           }
 *       }
 *       LocalUIData localUIData = new LocalUIData();
 *       localUIData.setValue(value);
 *       
 *       return (DataModel&lt;T&gt;) localUIData.getDataModel();
 *   }
 * </code>
 * </pre>
 *
 * <p>For example:</p>
 * 
 * <pre>
 * <code>
 * public class Child1 {
 *
 * }
 * </code>
 * </pre> 
 * 
 * and
 *
 * <pre>
 * <code>
 * package test.jsf23;
 * 
 * &#64;FacesDataModel(forClass = Child1.class)
 * public class Child1Model&lt;E&gt; extends DataModel&lt;E&gt; {
 * 
 *    &#64;Override
 *    public int getRowCount() {
 *        return 0;
 *    }
 * 
 *    &#64;Override
 *    public E getRowData() {
 *        return null;
 *    }
 * 
 *    &#64;Override
 *    public int getRowIndex() {
 *        return 0;
 *    }
 *
 *    &#64;Override
 *    public Object getWrappedData() {
 *        return null;
 *    } 
 *
 *    &#64;Override
 *    public boolean isRowAvailable() {
 *        return false;
 *    }
 *
 *    &#64;Override
 *    public void setRowIndex(int arg0) {
 *       
 *    }
 *
 *    &#64;Override
 *    public void setWrappedData(Object arg0) {
 *        
 *    }
 * }
 * </code>
 * </pre>
 * 
 * <p>Then the following must work:</p>
 * 
 * <pre>
 * <code>
 * DataModel&lt;Child1&gt; myModel = createDataModel(Child1.class, new Child1());
 * assert myModel instanceof Child1Model;
 * System.out.println(myModel.getClass());
 * </code>
 * </pre>
 * 
 * <p>The result printed should be e.g.: <code>"class
 * test.jsf23.Child1Model"</code></p>
 * 
 * </div>
 * 
 */
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
@Qualifier
public @interface FacesDataModel {
    
    /**
     * <p class="changed_added_2_3">The value of this annotation
     * attribute is taken to be the type that the DataModel that is
     * annotated with this annotation is able to wrap.</p>
     * 
     * @return the type that the DataModel that is annotated with this annotation is able to wrap
     */
    Class<?> forClass() default Object.class;
}
