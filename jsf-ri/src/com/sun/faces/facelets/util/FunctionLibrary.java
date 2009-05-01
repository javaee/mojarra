package com.sun.faces.facelets.util;

import com.sun.faces.facelets.tag.TagLibrary;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>This <code>TagLibrary</code> exposes the <code>public static</code>
 * methods defined on the <code>functionsClass</code> provided to the constructor
 * as EL functions.</p>
 */
public class FunctionLibrary implements TagLibrary {

    private String namespace;
    private Map<String,Method> functions;

    // ------------------------------------------------------------ Constructors


    public FunctionLibrary(Class<?> functionsClass, String namespace) {

        Util.notNull("functionsClass", functionsClass);
        Util.notNull("namespace", namespace);

        this.namespace = namespace;

        try {
            Method[] methods = functionsClass.getMethods();
            functions = new HashMap<String, Method>(methods.length, 1.0f);
            for (Method method : methods) {
                if (Modifier.isStatic(method.getModifiers())
                    && Modifier.isPublic(method.getModifiers())) {
                    functions.put(method.getName(), method);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // ------------------------------------------------- Methods from TagLibrary

    public boolean containsNamespace(String ns, Tag t) {
        return namespace.equals(ns);
    }

    public boolean containsTagHandler(String ns, String localName) {
        return false;
    }

    public TagHandler createTagHandler(String ns, String localName,
            TagConfig tag) throws FacesException {
        return null;
    }

    public boolean containsFunction(String ns, String name) {
        return namespace.equals(ns) && this.functions.containsKey(name);
    }

    public Method createFunction(String ns, String name) {
        if (namespace.equals(ns)) {
            return this.functions.get(name);
        }
        return null;
    }
    
}
