/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.application.annotation;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.NamedEventManager;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.NamedEvent;
import javax.faces.event.SystemEvent;

/**
 * This class handles the processing the NamedEvent annotation.  For each class
 * with this annotation, the following logic is applied:
 * <ol>
 *      <li>Get the unqualified class name (e.g., UserLoginEvent)</li>
 *      <li>Strip off the trailing "Event", if present (e.g., UserLogin)</li>
 *      <li>Convert the first character to lower-case (e.g., userLogin)</li>
 *      <li>Prepend the package name to the lower-cased name</li>
 *      <li>If the <code>shortName</code> attribute is specified, register the
 *          event by that name as well.</li>
 * </ol>
 */
public class NamedEventConfigHandler implements ConfigAnnotationHandler {

    private Map<Class<?>, Annotation> namedEvents;
    private static final Collection<Class<? extends Annotation>> HANDLES;


    static {
        Collection<Class<? extends Annotation>> handles =
                new ArrayList<Class<? extends Annotation>>(2);
        handles.add(NamedEvent.class);
        HANDLES = Collections.unmodifiableCollection(handles);
    }

    public Collection<Class<? extends Annotation>> getHandledAnnotations() {
        return HANDLES;
    }

    public void collect(Class<?> target, Annotation annotation) {
        if (namedEvents == null) {
            namedEvents = new HashMap<Class<?>, Annotation>();
        }
        namedEvents.put(target, annotation);
    }

    public void push(FacesContext ctx) {
        if (namedEvents != null) {
            ApplicationAssociate associate =
                    ApplicationAssociate.getInstance(ctx.getExternalContext());
            if (associate != null) {
                NamedEventManager nem = associate.getNamedEventManager();
                for (Map.Entry<Class<?>, Annotation> entry : namedEvents.entrySet()) {
                    process(nem, entry.getKey(), entry.getValue());
                }
            }
        }
    }

    // --------------------------------------------------------- Private Methods
    /*
     */
    private void process(NamedEventManager nem,
            Class<?> annotatedClass,
            Annotation annotation) {
        String name = annotatedClass.getSimpleName();
        int index = name.lastIndexOf("Event");
        if (index > -1) {
            name = name.substring(0, index);
        }

        name = annotatedClass.getPackage().getName() + ("." + name.charAt(0)).toLowerCase() + name.substring(1);
        nem.addNamedEvent(name, (Class<? extends SystemEvent>) annotatedClass);

        String shortName = ((NamedEvent) annotation).shortName();

        if (!"".equals(shortName)) {
            if (nem.isDuplicateNamedEvent(shortName)) {
                nem.addDuplicateName(shortName, (Class<? extends SystemEvent>) annotatedClass);
            } else {
                nem.addNamedEvent(shortName, (Class<? extends SystemEvent>) annotatedClass);
            }
        }
    }
}