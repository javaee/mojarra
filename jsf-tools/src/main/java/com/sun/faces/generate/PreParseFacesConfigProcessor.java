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
package com.sun.faces.generate;

import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.SourceVersion.RELEASE_8;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.faces.application.ApplicationConfigurationPopulator;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.w3c.dom.Document;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

@SupportedAnnotationTypes("com.sun.faces.generate.PreParseFacesConfig")
@SupportedSourceVersion(RELEASE_8)
public class PreParseFacesConfigProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        
        for (Element element : roundEnv.getElementsAnnotatedWith(PreParseFacesConfig.class)) {

            if (element instanceof TypeElement) {

                TypeElement typeElement = (TypeElement) element;

                PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();

                try {
                    String className = typeElement.getSimpleName() + "Generated";

                    JavaFileObject fileObject = 
                        processingEnv.getFiler()
                                     .createSourceFile(packageElement.getQualifiedName() + "." + className);
                    
                    MethodSpec.Builder methodBuilder = methodBuilder("populateApplicationConfiguration") 
                        .addAnnotation(Override.class)
                        .returns(void.class)
                        .addModifiers(PUBLIC)
                        .addParameter(Document.class, "toPopulate")
                        .addStatement("String ns = toPopulate.getDocumentElement().getNamespaceURI()")
                        .addStatement("Element faces_configElement = toPopulate.getDocumentElement()");
                    
                    FacesConfigParser parser = new FacesConfigParser();
                    
                    parser.setOnStartElement(e -> {
                        methodBuilder
                            .beginControlFlow("")
                            .addStatement(
                                "$T $L = toPopulate.createElementNS(ns, $S)", 
                                org.w3c.dom.Element.class, 
                                toJavaVar(e.getNodeName()),
                                e.getNodeName());
                    });
                    
                    parser.setOnLeafElementWithText((e, t) -> {
                        methodBuilder.addStatement(
                            "$L.appendChild(toPopulate.createTextNode($S))",
                            toJavaVar(e.getNodeName()),
                            t.getWholeText().trim());
                    });
                    
                    parser.setOnEndElement(e -> {
                        methodBuilder.addStatement(
                            "$L.appendChild($L)",
                            toJavaVar(e.getParentNode().getNodeName()),
                            toJavaVar(e.getNodeName()));
                        
                        methodBuilder.endControlFlow();
                    });
                    
                    parser.parseFromClassPath("com/sun/faces/jsf-ri-runtime.xml");

                    TypeSpec helloWorld = classBuilder(className)
                        .superclass(ApplicationConfigurationPopulator.class)
                        .addModifiers(PUBLIC, FINAL)
                        .addMethod(
                            methodBuilder
                                .build())
                        .build();

                    JavaFile javaFile = JavaFile.builder(packageElement.getQualifiedName().toString(), helloWorld)
                        .build();
                    

                    try (Writer writter = fileObject.openWriter()) {
                        writter.append(javaFile.toString());
                    }

                } catch (final IOException ex) {
                    processingEnv.getMessager().printMessage(Kind.ERROR, ex.getMessage());
                }

            }

        }

        return true;
    }
    
    private static String toJavaVar(String name) {
        return name.replace('-', '_') + "Element";
    }

}